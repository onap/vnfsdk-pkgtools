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

import pytest

from vnfsdk_pkgtools import vnfreq


def check_result(reqid, reader, tosca, expected_fail_msg):
    tester = vnfreq.get_vnfreq_tester(reqid)
    tester.check(reader, tosca)
    if expected_fail_msg:
        assert isinstance(tester.err, vnfreq.VnfRequirementError)
        assert expected_fail_msg in str(tester.err)
    else:
        assert tester.err == 0


def test_R66070(mocker):
    reader = mocker.Mock()
    reader.manifest = None
    check_result('R-66070', reader, None, 'No manifest file found')


def test_R77707(mocker, tmpdir):
    # check only manifest file - success
    p1 = tmpdir.join("manifest.mf")
    p1.write("manifest")
    reader = mocker.Mock()
    reader.destination = str(tmpdir)
    reader.entry_manifest_file = "manifest.mf"
    reader.manifest.digests = {}
    check_result('R-77707', reader, None, None)

    # check additional file - fail
    p2 = tmpdir.mkdir('sub').join("non-existing")
    p2.write("non existing")
    check_result('R-77707', reader, None,
                 'Package component sub/non-existing not found in manifest file')


def test_R04298(mocker, tmpdir):
    p1 = tmpdir.mkdir('tests').join('script.sh')
    p1.write("#!/bin/sh")
    reader = mocker.Mock()
    reader.destination = str(tmpdir)
    reader.entry_tests_dir = "tests"
    check_result('R-04298', reader, None, None)

    p1.remove()
    check_result('R-04298', reader, None,
                 'No testing scripts found')

def test_R26881(mocker, tmpdir):
    p1 = tmpdir.join('entry.yaml')
    p1.write("")
    p2 = tmpdir.join('image')
    p2.write("")

    reader = mocker.Mock()
    reader.destination = str(tmpdir)
    reader.entry_definitions = 'entry.yaml'

    validator = mocker.Mock()
    node = mocker.Mock()
    validator.tosca.nodetemplates = [node]
    node.entity_tpl = {'artifacts': {'sw_image': {'file': 'image',
                                                  'type': 'tosca.artifacts.nfv.SwImage',
                                                 }
                                    }
                      }
    check_result('R-26881', reader, validator, None)

