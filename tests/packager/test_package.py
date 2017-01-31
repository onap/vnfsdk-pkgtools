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
import filecmp
from packager import csar
import logging
import tempfile
import shutil

def test_CSARWrite():
    CSAR_RESOURCE_DIR = 'tests/resources/csar'
    CSAR_ENTRY_FILE = 'test_entry.yaml'
    CSAR_OUTPUT_FILE = 'output.csar'

    csar_target_dir = tempfile.mkdtemp()
    csar_extract_dir = tempfile.mkdtemp()
    try:
        csar.write(CSAR_RESOURCE_DIR, CSAR_ENTRY_FILE, csar_target_dir + '/' + CSAR_OUTPUT_FILE, logging)
        csar.read(csar_target_dir + '/' + CSAR_OUTPUT_FILE, csar_extract_dir, logging)
        assert filecmp.cmp(CSAR_RESOURCE_DIR + '/' + CSAR_ENTRY_FILE, csar_extract_dir + '/' + CSAR_ENTRY_FILE )
    finally:
        shutil.rmtree(csar_target_dir, ignore_errors=True)
        shutil.rmtree(csar_extract_dir, ignore_errors=True)


