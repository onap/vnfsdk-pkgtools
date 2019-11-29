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
import collections
import filecmp
import os
import tempfile
import shutil
import pytest

from vnfsdk_pkgtools.packager import csar
from vnfsdk_pkgtools import util


ROOT_DIR = util.get_project_root()

CSAR_RESOURCE_DIR = os.path.join(ROOT_DIR, 'tests', 'resources', 'csar')
CSAR_ENTRY_FILE = 'test_entry.yaml'
CSAR_OUTPUT_FILE = 'output.csar'

Args = collections.namedtuple('Args',
           ['source', 'entry', 'manifest', 'history', 'tests',
            'licenses', 'digest', 'certificate', 'privkey', 'sol241'])


ARGS_MANIFEST = {
            'source': CSAR_RESOURCE_DIR,
            'entry': CSAR_ENTRY_FILE,
            'manifest': 'test_entry.mf',
            'history': 'ChangeLog.txt',
            'tests': 'Tests',
            'licenses': 'Licenses',
            'digest': None,
            'certificate': None,
            'privkey': None,
            'sol241': False,
        }

ARGS_MANIFEST_DIGEST = {
            'source': CSAR_RESOURCE_DIR,
            'entry': CSAR_ENTRY_FILE,
            'manifest': 'test_entry.mf',
            'history': 'ChangeLog.txt',
            'tests': 'Tests',
            'licenses': 'Licenses',
            'digest': 'sha-256',
            'certificate': None,
            'privkey': None,
            'sol241': False,
        }

ARGS_MANIFEST_DIGEST_CERT = {
            'source': CSAR_RESOURCE_DIR,
            'entry': CSAR_ENTRY_FILE,
            'manifest': 'test_entry.mf',
            'history': 'ChangeLog.txt',
            'tests': 'Tests',
            'licenses': 'Licenses',
            'digest': 'sha-256',
            'certificate': 'test.crt',
            'privkey': os.path.join(ROOT_DIR, 'tests', 'resources', 'signature', 'test.key'),
            'sol241': False,
        }

ARGS_NO_MANIFEST = {
            'source': CSAR_RESOURCE_DIR,
            'entry': CSAR_ENTRY_FILE,
            'manifest': None,
            'history': None,
            'tests': None,
            'licenses': None,
            'digest': None,
            'certificate': None,
            'privkey': None,
            'sol241': True,
        }

INVALID_ARGS_NO_MANIFEST = {
            'source': CSAR_RESOURCE_DIR,
            'entry': CSAR_ENTRY_FILE,
            'manifest': None,
            'history': None,
            'tests': None,
            'licenses': None,
            'digest': 'sha-256',
            'certificate': None,
            'privkey': None,
            'sol241': True,
        }

INVALID_ARGS_NO_PRIVKEY = {
            'source': CSAR_RESOURCE_DIR,
            'entry': CSAR_ENTRY_FILE,
            'manifest': 'test_entry.mf',
            'history': None,
            'tests': None,
            'licenses': None,
            'digest': None,
            'certificate': 'test.crt',
            'privkey': None,
            'sol241': True,
        }


def csar_write_test(args):
    csar_target_dir = tempfile.mkdtemp()
    csar_extract_dir = tempfile.mkdtemp()
    try:
        csar.write(args.source, args.entry, csar_target_dir + '/' + CSAR_OUTPUT_FILE, args)
        csar.read(csar_target_dir + '/' + CSAR_OUTPUT_FILE, csar_extract_dir, True)
        assert filecmp.cmp(args.source + '/' + args.entry, csar_extract_dir + '/' + args.entry)
        if(args.history):
            assert filecmp.cmp(args.source + '/' + args.history,
                               csar_extract_dir + '/' + args.history)
    finally:
        shutil.rmtree(csar_target_dir, ignore_errors=True)
        shutil.rmtree(csar_extract_dir, ignore_errors=True)


def test_CSARWrite():
    csar_write_test(Args(**ARGS_NO_MANIFEST))


def test_CSARWrite_manifest():
    # Because git can not store emptry directory, we need to create manually here
    license_path = ARGS_MANIFEST['source'] + '/' + ARGS_MANIFEST['licenses']
    if not os.path.exists(license_path):
        os.makedirs(license_path)
    csar_write_test(Args(**ARGS_MANIFEST))


def test_CSARWrite_manifest_digest():
    # Because git can not store emptry directory, we need to create manually here
    license_path = ARGS_MANIFEST['source'] + '/' + ARGS_MANIFEST['licenses']
    if not os.path.exists(license_path):
        os.makedirs(license_path)
    csar_write_test(Args(**ARGS_MANIFEST_DIGEST))


def test_CSARWrite_manifest_digest_cert():
    # Because git can not store emptry directory, we need to create manually here
    license_path = ARGS_MANIFEST['source'] + '/' + ARGS_MANIFEST['licenses']
    if not os.path.exists(license_path):
        os.makedirs(license_path)
    csar_write_test(Args(**ARGS_MANIFEST_DIGEST_CERT))


def test_CSARWrite_invalid_arg():
    with pytest.raises(ValueError) as excinfo:
        csar_write_test(Args(**INVALID_ARGS_NO_MANIFEST))
    excinfo.match(r"Must specify manifest file")

    with pytest.raises(ValueError) as excinfo:
        csar_write_test(Args(**INVALID_ARGS_NO_PRIVKEY))
    excinfo.match(r"Need private key file")
