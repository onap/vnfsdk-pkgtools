# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import logging
import os
import pprint
import tempfile
import zipfile

import requests

from vnfsdk_pkgtools.packager import manifest
from vnfsdk_pkgtools.packager import toscameta
from vnfsdk_pkgtools.packager import utils

LOG = logging.getLogger(__name__)


def write(source, entry, destination, args):
    source = os.path.expanduser(source)
    destination = os.path.expanduser(destination)

    utils.check_file_dir(root=source,
                         entry='',
                         msg='Please specify the service template directory.',
                         check_dir=True)

    utils.check_file_dir(root='',
                         entry=destination,
                         msg='Please provide a path to where the CSAR should be created.',
                         check_for_non=True)

    utils.check_file_dir(root=source,
                         entry=toscameta.META_FILE,
                         msg='This commands generates a meta file for you.'
                             'Please remove the existing metafile.',
                         check_for_non=True)
    if args.sol241:
        metadatacls = toscameta.ToscaMeta241
    else:
        metadatacls = toscameta.ToscaMeta261
    metadata = metadatacls(source, args.entry, args.manifest,
                           args.history, args.licenses,
                           args.tests, args.certificate)

    if args.manifest:
        manifest_file = manifest.Manifest(source, args.manifest, args.sol241)
        manifest_file_full_path = os.path.join(source, args.manifest)
    elif args.certificate or args.digest:
        raise ValueError("Must specify manifest file if certificate or digest is specified")
    else:
        manifest_file = None
        manifest_file_full_path = None

    if args.certificate:
        if not args.privkey:
            raise ValueError('Need private key file for signing')
        utils.check_file_dir(root='',
                             entry=args.privkey,
                             msg='Please specify a valid private key file.',
                             check_dir=False)

    LOG.debug('Compressing root directory to ZIP')
    with zipfile.ZipFile(destination, 'w', zipfile.ZIP_DEFLATED) as f:
        for root, dirs, files in os.walk(source):
            # add dir entries
            for dir in dirs:
                dir_full_path = os.path.join(root, dir)
                dir_relative_path = os.path.relpath(dir_full_path, source) + os.sep
                LOG.debug('Writing to archive: {0}'.format(dir_relative_path))
                f.write(dir_full_path + os.sep, dir_relative_path)

            for file in files:
                file_full_path = os.path.join(root, file)
                # skip manifest file here in case we need to generate digest
                if file_full_path != manifest_file_full_path:
                    file_relative_path = os.path.relpath(file_full_path, source)
                    LOG.debug('Writing to archive: {0}'.format(file_relative_path))
                    f.write(file_full_path, file_relative_path)
                    if manifest_file:
                        LOG.debug('Update file digest: {0}'.format(file_relative_path))
                        manifest_file.add_file(file_relative_path, args.digest)
        if manifest_file:
            LOG.debug('Update manifest file to temporary file')
            manifest_file_full_path = manifest_file.update_to_file(True)
            if args.certificate and args.privkey:
                LOG.debug('calculate signature')
                manifest_file.signature = utils.sign(msg_file=manifest_file_full_path,
                                                     cert_file=os.path.join(source, args.certificate),
                                                     key_file=args.privkey)
                # write cms into it
                manifest_file_full_path = manifest_file.update_to_file(True)
            LOG.debug('Writing to archive: {0}'.format(args.manifest))
            f.write(manifest_file_full_path, args.manifest)

        LOG.debug('Writing new metadata file to {0}'.format(toscameta.META_FILE))
        f.writestr(toscameta.META_FILE, metadata.dump_as_string())


class _CSARReader(object):

    def __init__(self, source, destination, no_verify_cert=True):
        if os.path.isdir(destination) and os.listdir(destination):
            raise ValueError('{0} already exists and is not empty. '
                             'Please specify the location where the CSAR '
                             'should be extracted.'.format(destination))
        downloaded_csar = '://' in source
        if downloaded_csar:
            file_descriptor, download_target = tempfile.mkstemp()
            os.close(file_descriptor)
            self._download(source, download_target)
            source = download_target
        self.source = os.path.expanduser(source)
        self.destination = os.path.expanduser(destination)
        self.metadata = None
        self.manifest = None
        try:
            if not os.path.exists(self.source):
                raise ValueError('{0} does not exists. Please specify a valid CSAR path.'
                                 .format(self.source))
            if not zipfile.is_zipfile(self.source):
                raise ValueError('{0} is not a valid CSAR.'.format(self.source))
            self._extract()
            self._read_metadata()
            self._validate(no_verify_cert)
        finally:
            if downloaded_csar:
                os.remove(self.source)

    @property
    def created_by(self):
        return self.metadata.created_by

    @property
    def csar_version(self):
        return self.metadata.csar_version

    @property
    def meta_file_version(self):
        return self.metadata.meta_file_version

    @property
    def entry_definitions(self):
        return self.metadata.entry_definitions

    @property
    def entry_definitions_yaml(self):
        with open(os.path.join(self.destination, self.entry_definitions)) as f:
            return yaml.safe_load(f)

    @property
    def entry_manifest_file(self):
        return self.metadata.entry_manifest_file

    @property
    def entry_history_file(self):
        return self.metadata.entry_history_file

    @property
    def entry_tests_dir(self):
        return self.metadata.entry_tests_dir

    @property
    def entry_licenses_dir(self):
        return self.metadata.entry_licenses_dir

    @property
    def entry_certificate_file(self):
        return self.metadata.entry_certificate_file

    def _extract(self):
        LOG.debug('Extracting CSAR contents')
        if not os.path.exists(self.destination):
            os.mkdir(self.destination)
        with zipfile.ZipFile(self.source) as f:
            f.extractall(self.destination)
        LOG.debug('CSAR contents successfully extracted')

    def _read_metadata(self):
        self.metadata = toscameta.create_from_file(self.destination)

    def _validate(self, no_verify_cert):
        LOG.debug('CSAR entry definitions: {0}'.format(self.entry_definitions))
        LOG.debug('CSAR manifest file: {0}'.format(self.entry_manifest_file))
        LOG.debug('CSAR change history file: {0}'.format(self.entry_history_file))
        LOG.debug('CSAR tests directory: {0}'.format(self.entry_tests_dir))
        LOG.debug('CSAR licenses directory: {0}'.format(self.entry_licenses_dir))
        LOG.debug('CSAR certificate file: {0}'.format(self.entry_certificate_file))

        if self.entry_manifest_file:
            self.manifest = manifest.Manifest(self.destination,
                                              self.entry_manifest_file)

        if(self.entry_certificate_file):
            tmp_manifest = self.manifest.save_to_temp_without_cms()
            utils.verify(tmp_manifest,
                         os.path.join(self.destination, self.entry_certificate_file),
                         self.manifest.signature,
                         no_verify_cert)
            os.unlink(tmp_manifest)

    def _download(self, url, target):
        response = requests.get(url, stream=True)
        if response.status_code != 200:
            raise ValueError('Server at {0} returned a {1} status code'
                             .format(url, response.status_code))
        LOG.info('Downloading {0} to {1}'.format(url, target))
        with open(target, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                if chunk:
                    f.write(chunk)


def read(source, destination, no_verify_cert=False):
    return _CSARReader(source=source,
                       destination=destination,
                       no_verify_cert=no_verify_cert)
