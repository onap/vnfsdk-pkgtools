# Copyright (c) 2018 Intel Corp. All rights reserved.
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

import os
import subprocess

import pytest

from vnfsdk_pkgtools.packager import utils
from vnfsdk_pkgtools import util

ROOT_DIR = util.get_project_root()
RESOURCES_DIR= os.path.join(ROOT_DIR, 'tests', 'resources', 'signature')

MSG_FILE  = os.path.join(RESOURCES_DIR, 'manifest.mf')
CERT_FILE = os.path.join(RESOURCES_DIR, 'test.crt')
KEY_FILE  = os.path.join(RESOURCES_DIR, 'test.key')

CONTENT = b"needToBeHashed"
SHA256 = "20a480339aa4371099f9503511dcc5a8051ce3884846678ced5611ec64bbfc9c"
SHA512 = "dbed8672e752d51d0c7ca42050f67faf1534e58470bba96e787df5c4cf6a4f8ecf7ad45fb9307adbc5b9dec8432627d86b3eb1d3d43ee9c5e93f754ff2825320"

def test_cal_file_hash(tmpdir):
    p = tmpdir.join("file_to_hash.txt")
    p.write(CONTENT)
    assert SHA512 == utils.cal_file_hash("", str(p), 'SHA512')
    assert SHA256 == utils.cal_file_hash(p.dirname, p.basename, 'sha256')

def test_cal_file_hash_remote(mocker):
    class FakeRequest(object):
        def __init__(self, *args):
            self.status_code = 200
            self.content = CONTENT
    mocker.patch('requests.get', new=FakeRequest)
    assert SHA256 == utils.cal_file_hash("", "http://fake", 'sha256')

def test_sign_verify_pairwise():
    cms = utils.sign(MSG_FILE, CERT_FILE, KEY_FILE)
    # We can't examine the exact content of cms because it contains timestamp
    assert "---BEGIN CMS---" in cms
    assert "---END CMS---" in cms
    utils.verify(MSG_FILE, CERT_FILE, cms, no_verify_cert=True)


def test_verify_bad(tmpdir):
    cms = utils.sign(MSG_FILE, CERT_FILE, KEY_FILE)
    
    p = tmpdir.join("file_msg.txt")
    p.write("BAD")
    
    with pytest.raises(subprocess.CalledProcessError):
        utils.verify(str(p), CERT_FILE, cms, no_verify_cert=True)
    
