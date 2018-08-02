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

from toscaparser.common.exception import ValidationError
from toscaparser.tosca_template import ToscaTemplate

from vnfsdk_pkgtools import validator

LOG = logging.getLogger(__name__)


class ToscaparserValidator(validator.ValidatorBase):
    def __init__(self):
        super(ToscaparserValidator, self).__init__()

    def validate(self, reader):
        entry_path = os.path.join(reader.destination,
                                       reader.entry_definitions)
        try:
            #TODO set debug_mode due to upstream bug
            #     https://jira.opnfv.org/browse/PARSER-181
            self.tosca = ToscaTemplate(path=entry_path,
                                       no_required_paras_check=True,
                                       debug_mode=True)
        except ValidationError as e:
            LOG.error(e.message)
            raise e
