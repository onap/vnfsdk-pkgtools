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

import pytest

from vnfsdk_pkgtools.packager import csar
from vnfsdk_pkgtools.validator import toscaparser_validator
from vnfsdk_pkgtools import util

ROOT_DIR = util.get_project_root()
RESOURCES_DIR = os.path.join(ROOT_DIR, 'tests', 'resources')

CSAR_PATH = os.path.join(RESOURCES_DIR, 'test_import.csar')
HPA_PATH = os.path.join(RESOURCES_DIR, 'hpa.csar')
BAD_HPA_PATH = os.path.join(RESOURCES_DIR, 'hpa_bad.csar')

def test_validate(tmpdir):
    reader = csar._CSARReader(CSAR_PATH, str(tmpdir.mkdir('validate')))
    validator = toscaparser_validator.ToscaparserValidator()
    validator.validate(reader)
    assert hasattr(validator, 'tosca')

def test_validate_hpa(tmpdir):
    reader = csar._CSARReader(HPA_PATH, str(tmpdir.mkdir('validate')))
    validator = toscaparser_validator.ToscaparserValidator()
    validator.validate(reader)
    assert hasattr(validator, 'tosca')

def test_validate_hpa_bad(tmpdir):
    reader = csar._CSARReader(BAD_HPA_PATH, str(tmpdir.mkdir('validate')))
    validator = toscaparser_validator.ToscaparserValidator()
    with pytest.raises(toscaparser_validator.HpaValueError):
        validator.validate(reader)
