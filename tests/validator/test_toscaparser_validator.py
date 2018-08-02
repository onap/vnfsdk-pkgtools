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

import logging
import os

from vnfsdk_pkgtools.packager import csar
from vnfsdk_pkgtools.validator import toscaparser_validator

CSAR_PATH = 'tests/resources/test_import.csar'

def test_validate(tmpdir):
    reader = csar._CSARReader(CSAR_PATH, str(tmpdir.mkdir('validate')), logging)
    validator = toscaparser_validator.ToscaparserValidator()
    validator.validate(reader)
    assert hasattr(validator, 'tosca')
