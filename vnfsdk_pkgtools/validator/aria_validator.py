# Copyright (c) 2017 Intel Corp. All rights reserved.
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

from aria.parser.loading import LiteralLocation
from aria.parser.consumption import (
    ConsumptionContext,
    ConsumerChain,
    Read,
    Validate,
    ServiceTemplate,
    ServiceInstance
)

from vnfsdk_pkgtools import validator


class AriaValidator(validator.ValidatorBase):
    def validate(self, reader):
        context = ConsumptionContext()
        context.loading.prefixes += [os.path.join(reader.destination, 'definitions')]
        context.presentation.location = LiteralLocation(reader.entry_definitions_yaml)
        print reader.entry_definitions_yaml
        chain = ConsumerChain(context, (Read, Validate, ServiceTemplate, ServiceInstance))
        chain.consume()
        if context.validation.dump_issues():
            raise RuntimeError('Validation failed')
        dumper = chain.consumers[-1]
        dumper.dump()

