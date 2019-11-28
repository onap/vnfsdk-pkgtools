#
# Copyright (c) 2017 GigaSpaces Technologies Ltd. All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may
# not use this file except in compliance with the License. You may obtain
# a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations
# under the License.
#
import copy
import os
import pytest
import shutil
import tempfile

from vnfsdk_pkgtools.packager import toscameta
from vnfsdk_pkgtools import util


ROOT_DIR = util.get_project_root()

CSAR_RESOURCE_DIR = os.path.join(ROOT_DIR, 'tests', 'resources', 'csar')
CSAR_ENTRY_FILE = 'test_entry.yaml'
CSAR_OUTPUT_FILE = 'output.csar'

ARGS_MANIFEST = {
            'base_dir': CSAR_RESOURCE_DIR,
            'entry': CSAR_ENTRY_FILE,
            'manifest': 'test_entry.mf',
            'changelog': 'ChangeLog.txt',
            'licenses': 'Licenses',
            'tests': 'Tests',
            'certificate': None,
        }

ARGS_MANIFEST_CERTIFICATE = {
            'base_dir': CSAR_RESOURCE_DIR,
            'entry': CSAR_ENTRY_FILE,
            'manifest': 'test_entry.mf',
            'changelog': 'ChangeLog.txt',
            'licenses': 'Licenses',
            'tests': 'Tests',
            'certificate': 'test.crt',
        }

ARGS_NO_MANIFEST = {
            'base_dir': CSAR_RESOURCE_DIR,
            'entry': CSAR_ENTRY_FILE,
            'manifest': None,
            'changelog': None,
            'licenses': None,
            'tests': None,
            'certificate': None,
        }


def _validate_metadata(cls, expected):
    metadata = cls(**expected)
    assert toscameta.META_CREATED_BY_VALUE == metadata.created_by
    assert toscameta.META_CSAR_VERSION_VALUE == metadata.csar_version
    assert toscameta.META_FILE_VERSION_VALUE == metadata.meta_file_version
    assert expected['entry'] == metadata.entry_definitions
    assert expected['manifest'] == metadata.entry_manifest_file
    assert expected['changelog'] == metadata.entry_history_file
    assert expected['licenses'] == metadata.entry_licenses_dir
    assert expected['tests'] == metadata.entry_tests_dir
    assert expected['certificate'] == metadata.entry_certificate_file
    return metadata


def test_261():
    metadata = _validate_metadata(toscameta.ToscaMeta261, ARGS_MANIFEST)
    assert "ETSI-Entry-Change-Log: ChangeLog.txt\n" in metadata.dump_as_string()


def test_241():
    metadata = _validate_metadata(toscameta.ToscaMeta241, ARGS_MANIFEST_CERTIFICATE)
    assert "Entry-Certificate: test.crt\n" in metadata.dump_as_string()


def test_261_no_manifest():
    with pytest.raises(ValueError):
        toscameta.ToscaMeta261(**ARGS_NO_MANIFEST)


def test_241_no_manifest():
    metadata = _validate_metadata(toscameta.ToscaMeta241, ARGS_NO_MANIFEST)
    assert "Entry-Definitions: test_entry.yaml\n" in metadata.dump_as_string()


def test_invalid_entry():
    args = copy.copy(ARGS_MANIFEST)
    args['entry'] = 'test_entry.mf'
    with pytest.raises(ValueError):
        toscameta.ToscaMeta261(**args)


def test_invalid_csar_version():
    args = copy.copy(ARGS_MANIFEST)
    args['meta_csar_version'] = '1.2'
    with pytest.raises(ValueError):
        toscameta.ToscaMeta241(**args)


FROM_FILE_CASES = ['TOSCA.meta.sol261', 'TOSCA.meta.sol241']

def _prepare(target, metafile_path):
    shutil.copytree(CSAR_RESOURCE_DIR, target)
    os.mkdir(os.path.join(target, 'TOSCA-Metadata'))
    shutil.copy(metafile_path, os.path.join(target,
                                            'TOSCA-Metadata',
                                            'TOSCA.meta'))

def test_create_from_file():
    for case in FROM_FILE_CASES:
        target = tempfile.mkdtemp()
        base_dir = os.path.join(target, 'mytest')
        _prepare(base_dir,
                 os.path.join(ROOT_DIR,
                              'tests',
                              'resources',
                              case))
        try:
            toscameta.create_from_file(base_dir)
        finally:
            shutil.rmtree(target, ignore_errors=True)
