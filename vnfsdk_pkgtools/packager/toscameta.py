# Copyright (c) 2019 Intel Corp. All rights reserved.
#
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

from ruamel import yaml
import six

from vnfsdk_pkgtools.packager import utils

LOG = logging.getLogger(__name__)

META_FILE = 'TOSCA-Metadata/TOSCA.meta'

META_FILE_VERSION_KEY = 'TOSCA-Meta-File-Version'
META_FILE_VERSION_VALUE = '1.0'
META_CSAR_VERSION_KEY = 'CSAR-Version'
META_CSAR_VERSION_VALUE = '1.1'
META_CREATED_BY_KEY = 'Created-By'
META_CREATED_BY_VALUE = 'ONAP VNFSDK pkgtools'

META_ENTRY_DEFINITIONS_KEY = 'Entry-Definitions'

BASE_META = {
    META_FILE_VERSION_KEY: META_FILE_VERSION_VALUE,
    META_CSAR_VERSION_KEY: META_CSAR_VERSION_VALUE,
}


class ToscaMeta(object):
    META_ENTRY_MANIFEST_FILE_KEY = 'ETSI-Entry-Manifest'
    META_ENTRY_HISTORY_FILE_KEY = 'ETSI-Entry-Change-Log'
    META_ENTRY_TESTS_DIR_KEY = 'ETSI-Entry-Tests'
    META_ENTRY_LICENSES_DIR_KEY = 'ETSI-Entry-Licenses'
    META_ENTRY_CERT_FILE_KEY = 'ETSI-Entry-Certificate'
    REQUIRED_KEYS = [
        META_FILE_VERSION_KEY, META_CSAR_VERSION_KEY,
        META_CREATED_BY_KEY, META_ENTRY_DEFINITIONS_KEY,
        META_ENTRY_MANIFEST_FILE_KEY, META_ENTRY_HISTORY_FILE_KEY,
        META_ENTRY_LICENSES_DIR_KEY, ]
    OPTIONAL_KEYS = [META_ENTRY_TESTS_DIR_KEY, META_ENTRY_CERT_FILE_KEY]

    def __init__(self, base_dir, entry, manifest=None, changelog=None,
                 licenses=None, tests=None, certificate=None,
                 meta_file_version=META_FILE_VERSION_VALUE,
                 meta_csar_version=META_CSAR_VERSION_VALUE,
                 meta_created_by=META_CREATED_BY_VALUE):

        self.base_dir = base_dir

        metadata = {}
        metadata[META_FILE_VERSION_KEY] = str(meta_file_version)
        metadata[META_CSAR_VERSION_KEY] = str(meta_csar_version)
        metadata[META_CREATED_BY_KEY] = meta_created_by
        metadata[META_ENTRY_DEFINITIONS_KEY] = entry
        if manifest:
            metadata[self.META_ENTRY_MANIFEST_FILE_KEY] = manifest
        if changelog:
            metadata[self.META_ENTRY_HISTORY_FILE_KEY] = changelog
        if licenses:
            metadata[self.META_ENTRY_LICENSES_DIR_KEY] = licenses
        if tests:
            metadata[self.META_ENTRY_TESTS_DIR_KEY] = tests
        if certificate:
            metadata[self.META_ENTRY_CERT_FILE_KEY] = certificate

        self.metadata = self._validate(metadata)

    def _validate(self, metadata):
        for (key, value) in six.iteritems(BASE_META):
            if metadata.get(key) != value:
                raise ValueError('TOSCA.meta: {} must be {}'.format(key, value))

        utils.check_file_dir(root=self.base_dir,
                             entry=metadata.get(META_ENTRY_DEFINITIONS_KEY),
                             msg='Please specify a valid entry point.',
                             check_dir=False)
        entry_file = os.path.join(self.base_dir,
                                  metadata.get(META_ENTRY_DEFINITIONS_KEY))
        try:
            with open(entry_file) as f:
                yaml.safe_load(f)['tosca_definitions_version']
        except Exception:
            raise ValueError('Entry file {} is not a valid tosca simple yaml file'.format(entry_file))

        if metadata.get(self.META_ENTRY_MANIFEST_FILE_KEY):
            utils.check_file_dir(root=self.base_dir,
                                 entry=metadata[self.META_ENTRY_MANIFEST_FILE_KEY],
                                 msg='Please specify a valid manifest file.',
                                 check_dir=False)
        if metadata.get(self.META_ENTRY_HISTORY_FILE_KEY):
            utils.check_file_dir(root=self.base_dir,
                                 entry=metadata[self.META_ENTRY_HISTORY_FILE_KEY],
                                 msg='Please specify a valid change history file.',
                                 check_dir=False)
        if metadata.get(self.META_ENTRY_LICENSES_DIR_KEY):
            utils.check_file_dir(root=self.base_dir,
                                 entry=metadata[self.META_ENTRY_LICENSES_DIR_KEY],
                                 msg='Please specify a valid license directory.',
                                 check_dir=True)
        if metadata.get(self.META_ENTRY_TESTS_DIR_KEY):
            utils.check_file_dir(root=self.base_dir,
                                 entry=metadata[self.META_ENTRY_TESTS_DIR_KEY],
                                 msg='Please specify a valid test directory.',
                                 check_dir=True)
        if metadata.get(self.META_ENTRY_CERT_FILE_KEY):
            utils.check_file_dir(root=self.base_dir,
                                 entry=metadata[self.META_ENTRY_CERT_FILE_KEY],
                                 msg='Please specify a valid certificate file.',
                                 check_dir=False)
        missing_keys = [key for key in self.REQUIRED_KEYS if key not in metadata]
        if missing_keys:
            raise ValueError('TOSCA.meta: missing keys: {}'.format(','.join(missing_keys)))
        return metadata

    def dump_as_string(self):
        s = ""
        for key in self.REQUIRED_KEYS + self.OPTIONAL_KEYS:
            if self.metadata.get(key):
                s += "{}: {}\n".format(key, self.metadata.get(key))
        return s

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
    def entry_manifest_file(self):
        return self.metadata.get(self.META_ENTRY_MANIFEST_FILE_KEY)

    @property
    def entry_history_file(self):
        return self.metadata.get(self.META_ENTRY_HISTORY_FILE_KEY)

    @property
    def entry_tests_dir(self):
        return self.metadata.get(self.META_ENTRY_TESTS_DIR_KEY)

    @property
    def entry_licenses_dir(self):
        return self.metadata.get(self.META_ENTRY_LICENSES_DIR_KEY)

    @property
    def entry_certificate_file(self):
        return self.metadata.get(self.META_ENTRY_CERT_FILE_KEY)


class ToscaMeta241(ToscaMeta):
    # SOL004 v2.4.1
    META_ENTRY_MANIFEST_FILE_KEY = 'Entry-Manifest'
    META_ENTRY_HISTORY_FILE_KEY = 'Entry-Change-Log'
    META_ENTRY_TESTS_DIR_KEY = 'Entry-Tests'
    META_ENTRY_LICENSES_DIR_KEY = 'Entry-Licenses'
    META_ENTRY_CERT_FILE_KEY = 'Entry-Certificate'
    REQUIRED_KEYS = [
        META_FILE_VERSION_KEY, META_CSAR_VERSION_KEY,
        META_CREATED_BY_KEY, META_ENTRY_DEFINITIONS_KEY, ]
    OPTIONAL_KEYS = [
        META_ENTRY_MANIFEST_FILE_KEY, META_ENTRY_HISTORY_FILE_KEY,
        META_ENTRY_LICENSES_DIR_KEY, META_ENTRY_TESTS_DIR_KEY,
        META_ENTRY_CERT_FILE_KEY, ]


class ToscaMeta261(ToscaMeta):
    # SOL004 v2.6.1
    pass


def create_from_file(base_dir):
    csar_metafile = os.path.join(base_dir, META_FILE)
    if not os.path.exists(csar_metafile):
        raise ValueError('Metadata file {0} is missing from the CSAR'.format(csar_metafile))
    LOG.debug('CSAR metadata file: {0}'.format(csar_metafile))
    LOG.debug('Attempting to parse CSAR metadata YAML')
    with open(csar_metafile) as f:
        metadata = yaml.safe_load(f)
    LOG.debug('CSAR metadata:\n{0}'.format(pprint.pformat(metadata)))
    # By default we assume it's SOL004 2.4.1
    cls = ToscaMeta241
    for key in metadata.keys():
        if key.startswith('ETSI-'):
            cls = ToscaMeta261
            break
    return cls(base_dir,
               entry=metadata.get(META_ENTRY_DEFINITIONS_KEY),
               manifest=metadata.get(cls.META_ENTRY_MANIFEST_FILE_KEY),
               changelog=metadata.get(cls.META_ENTRY_HISTORY_FILE_KEY),
               licenses=metadata.get(cls.META_ENTRY_LICENSES_DIR_KEY),
               tests=metadata.get(cls.META_ENTRY_TESTS_DIR_KEY),
               certificate=metadata.get(cls.META_ENTRY_CERT_FILE_KEY),
               meta_file_version=metadata.get(META_FILE_VERSION_KEY),
               meta_csar_version=metadata.get(META_CSAR_VERSION_KEY),
               meta_created_by=metadata.get(META_CREATED_BY_KEY))
