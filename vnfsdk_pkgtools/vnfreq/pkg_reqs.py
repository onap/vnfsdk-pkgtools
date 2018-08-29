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

import abc

import six
from stevedore import driver

from vnfsdk_pkgtools import vnfreq


class R66070(vnfreq.TesterBase):
    ID = "R-66070"
    DESC = ("The VNF Package MUST include VNF Identification Data to "
            "uniquely identify the resource for a given VNF provider. "
            "The identification data must include: an identifier for "
            "the VNF, the name of the VNF as was given by the VNF "
            "provider, VNF description, VNF provider, and version.")

    def _do_check(self, reader, tosca):
        if not reader.manifest:
            raise vnfreq.VnfRequirementError("No manifest file found")
        # Existing reader.manifest already means a valid manifest file
        # no futher check needed.
        return 0
