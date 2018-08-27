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
from ruamel import yaml # @UnresolvedImport

from vnfsdk_pkgtools.packager import manifest
from vnfsdk_pkgtools.packager import utils

LOG = logging.getLogger(__name__)

META_FILE = 'TOSCA-Metadata/TOSCA.meta'
META_FILE_VERSION_KEY = 'TOSCA-Meta-File-Version'
META_FILE_VERSION_VALUE = '1.0'
META_CSAR_VERSION_KEY = 'CSAR-Version'
META_CSAR_VERSION_VALUE = '1.1'
META_CREATED_BY_KEY = 'Created-By'
META_CREATED_BY_VALUE = 'ONAP'
META_ENTRY_DEFINITIONS_KEY = 'Entry-Definitions'
META_ENTRY_MANIFEST_FILE_KEY = 'Entry-Manifest'
META_ENTRY_HISTORY_FILE_KEY = 'Entry-Change-Log'
META_ENTRY_TESTS_DIR_KEY = 'Entry-Tests'
META_ENTRY_LICENSES_DIR_KEY = 'Entry-Licenses'
META_ENTRY_CERT_FILE_KEY = 'Entry-Certificate'

BASE_METADATA = {
    META_FILE_VERSION_KEY: META_FILE_VERSION_VALUE,
    META_CSAR_VERSION_KEY: META_CSAR_VERSION_VALUE,
    META_CREATED_BY_KEY: META_CREATED_BY_VALUE,
}


def check_file_dir(root, entry, msg, check_for_non=False, check_dir=False):
    path = os.path.join(root, entry)
    if check_for_non:
        ret = not os.path.exists(path)
        error_msg = '{0} already exists. ' + msg
    elif check_dir:
        ret = os.path.isdir(path)
        error_msg = '{0} is not an existing directory. ' + msg
    else:
        ret = os.path.isfile(path)
        error_msg = '{0} is not an existing file. ' + msg
    if not ret:
        raise ValueError(error_msg.format(path))


def write(source, entry, destination, args):
    source = os.path.expanduser(source)
    destination = os.path.expanduser(destination)
    metadata = BASE_METADATA.copy()

    check_file_dir(root=source,
                   entry='',
                   msg='Please specify the service template directory.',
                   check_dir=True)

    check_file_dir(root=source,
                   entry=entry,
                   msg='Please specify a valid entry point.',
                   check_dir=False)
    metadata[META_ENTRY_DEFINITIONS_KEY] = entry

    check_file_dir(root='',
                   entry=destination,
                   msg='Please provide a path to where the CSAR should be created.',
                   check_for_non=True)

    check_file_dir(root=source,
                   entry=META_FILE,
                   msg='This commands generates a meta file for you. Please '
                       'remove the existing metafile.',
                   check_for_non=True)

    if(args.manifest):
        check_file_dir(root=source,
                       entry=args.manifest,
                       msg='Please specify a valid manifest file.',
                       check_dir=False)
        metadata[META_ENTRY_MANIFEST_FILE_KEY] = args.manifest
        manifest_file = manifest.Manifest(source, args.manifest) 
        manifest_file_full_path = os.path.join(source, args.manifest)
    else:
        manifest_file = None
        manifest_file_full_path = None


    if(args.history):
        check_file_dir(root=source,
                       entry=args.history,
                       msg='Please specify a valid change history file.',
                       check_dir=False)
        metadata[META_ENTRY_HISTORY_FILE_KEY] = args.history

    if args.certificate:
        check_file_dir(root=source,
                       entry=args.certificate,
                       msg='Please specify a valid certificate file.',
                       check_dir=False)
        metadata[META_ENTRY_CERT_FILE_KEY] = args.certificate
        if not args.privkey:
            raise RuntimeError('Need private key file for signing')
        check_file_dir(root='',
                       entry=args.privkey,
                       msg='Please specify a valid private key file.',
                       check_dir=False)

    if(args.tests):
        check_file_dir(root=source,
                       entry=args.tests,
                       msg='Please specify a valid test directory.',
                       check_dir=True)
        metadata[META_ENTRY_TESTS_DIR_KEY] = args.tests

    if(args.licenses):
        check_file_dir(root=source,
                       entry=args.licenses,
                       msg='Please specify a valid license directory.',
                       check_dir=True)
        metadata[META_ENTRY_LICENSES_DIR_KEY] = args.licenses

    LOG.debug('Compressing root directory to ZIP')
    with zipfile.ZipFile(destination, 'w', zipfile.ZIP_DEFLATED) as f:
        for root, dirs, files in os.walk(source):
            for file in files:
                file_full_path = os.path.join(root, file)
                # skip manifest file here in case we need to generate digest
                if file_full_path!=manifest_file_full_path:
                    file_relative_path = os.path.relpath(file_full_path, source)
                    LOG.debug('Writing to archive: {0}'.format(file_relative_path))
                    f.write(file_full_path, file_relative_path)
                    if manifest_file and args.digest:
                        LOG.debug('Update file digest: {0}'.format(file_relative_path))
                        manifest_file.add_file(file_relative_path, args.digest)
            # add empty dir
            for dir in dirs:
                dir_full_path = os.path.join(root, dir)
                if len(os.listdir(dir_full_path)) == 0:
                    dir_relative_path = os.path.relpath(dir_full_path, source) + os.sep
                    LOG.debug('Writing to archive: {0}'.format(dir_relative_path))
                    f.write(dir_full_path + os.sep, dir_relative_path)

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

        LOG.debug('Writing new metadata file to {0}'.format(META_FILE))
        f.writestr(META_FILE, yaml.dump(metadata, default_flow_style=False))


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
        self.metadata = {}
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
        return self.metadata.get(META_CREATED_BY_KEY)

    @property
    def csar_version(self):
        return self.metadata.get(META_CSAR_VERSION_KEY)

    @property
    def meta_file_version(self):
        return self.metadata.get(META_FILE_VERSION_KEY)

    @property
    def entry_definitions(self):
        return self.metadata.get(META_ENTRY_DEFINITIONS_KEY)

    @property
    def entry_definitions_yaml(self):
        with open(os.path.join(self.destination, self.entry_definitions)) as f:
            return yaml.load(f)

    @property
    def entry_manifest_file(self):
        return self.metadata.get(META_ENTRY_MANIFEST_FILE_KEY)

    @property
    def entry_history_file(self):
        return self.metadata.get(META_ENTRY_HISTORY_FILE_KEY)

    @property
    def entry_tests_dir(self):
        return self.metadata.get(META_ENTRY_TESTS_DIR_KEY)

    @property
    def entry_licenses_dir(self):
        return self.metadata.get(META_ENTRY_LICENSES_DIR_KEY)

    @property
    def entry_certificate_file(self):
        return self.metadata.get(META_ENTRY_CERT_FILE_KEY)

    def _extract(self):
        LOG.debug('Extracting CSAR contents')
        if not os.path.exists(self.destination):
            os.mkdir(self.destination)
        with zipfile.ZipFile(self.source) as f:
            f.extractall(self.destination)
        LOG.debug('CSAR contents successfully extracted')

    def _read_metadata(self):
        csar_metafile = os.path.join(self.destination, META_FILE)
        if not os.path.exists(csar_metafile):
            raise ValueError('Metadata file {0} is missing from the CSAR'.format(csar_metafile))
        LOG.debug('CSAR metadata file: {0}'.format(csar_metafile))
        LOG.debug('Attempting to parse CSAR metadata YAML')
        with open(csar_metafile) as f:
            self.metadata.update(yaml.load(f))
        LOG.debug('CSAR metadata:\n{0}'.format(pprint.pformat(self.metadata)))

    def _validate(self, no_verify_cert):
        def validate_key(key, expected=None):
            if not self.metadata.get(key):
                raise ValueError('{0} is missing from the metadata file.'.format(key))
            actual = str(self.metadata[key])
            if expected and actual != expected:
                raise ValueError('{0} is expected to be {1} in the metadata file while it is in '
                                 'fact {2}.'.format(key, expected, actual))
        validate_key(META_FILE_VERSION_KEY, expected=META_FILE_VERSION_VALUE)
        validate_key(META_CSAR_VERSION_KEY, expected=META_CSAR_VERSION_VALUE)
        validate_key(META_CREATED_BY_KEY)
        validate_key(META_ENTRY_DEFINITIONS_KEY)
        LOG.debug('CSAR entry definitions: {0}'.format(self.entry_definitions))
        LOG.debug('CSAR manifest file: {0}'.format(self.entry_manifest_file))
        LOG.debug('CSAR change history file: {0}'.format(self.entry_history_file))
        LOG.debug('CSAR tests directory: {0}'.format(self.entry_tests_dir))
        LOG.debug('CSAR licenses directory: {0}'.format(self.entry_licenses_dir))
        LOG.debug('CSAR certificate file: {0}'.format(self.entry_certificate_file))

        check_file_dir(self.destination,
                       self.entry_definitions,
                       'The entry definitions {0} referenced by the metadata '
                       'file does not exist.'.format(self.entry_definitions),
                       check_dir=False)

        if(self.entry_manifest_file):
             check_file_dir(self.destination,
                            self.entry_manifest_file,
                            'The manifest file {0} referenced by the metadata '
                            'file does not exist.'.format(self.entry_manifest_file),
                            check_dir=False)
             self.manifest = manifest.Manifest(self.destination,
                                               self.entry_manifest_file)


        if(self.entry_history_file):
             check_file_dir(self.destination,
                            self.entry_history_file,
                            'The change history file {0} referenced by the metadata '
                            'file does not exist.'.format(self.entry_history_file),
                            check_dir=False)

        if(self.entry_tests_dir):
             check_file_dir(self.destination,
                            self.entry_tests_dir,
                            'The test directory {0} referenced by the metadata '
                            'file does not exist.'.format(self.entry_tests_dir),
                            check_dir=True)

        if(self.entry_licenses_dir):
             check_file_dir(self.destination,
                            self.entry_licenses_dir,
                            'The license directory {0} referenced by the metadata '
                            'file does not exist.'.format(self.entry_licenses_dir),
                            check_dir=True)

        if(self.entry_certificate_file):
            # check certificate
            check_file_dir(self.destination,
                           self.entry_certificate_file,
                           'The certificate file {0} referenced by the metadata '
                           'file does not exist.'.format(self.entry_certificate_file),
                           check_dir=False)
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
