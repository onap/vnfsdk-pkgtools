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

from ruamel import yaml
import pytest

from vnfsdk_pkgtools.validator import utils

CONTENT = "---\n1"

BAD_CONTENT = "---\na: b\n- d"

def test_load_definition(tmpdir):
    p = tmpdir.join("test_definition.yaml")
    p.write(CONTENT)
    assert 1 == utils.load_definitions(str(p))


def test_load_bad_definition(tmpdir):
    p = tmpdir.join("test_definition.yaml")
    p.write(BAD_CONTENT)
    with pytest.raises(yaml.YAMLError):
        utils.load_definitions(str(p))


def test_load_defualt_definition(tmpdir):
    p = tmpdir.join("non_exist")
    assert 1 == utils.load_definitions(str(p),defaults=1)
