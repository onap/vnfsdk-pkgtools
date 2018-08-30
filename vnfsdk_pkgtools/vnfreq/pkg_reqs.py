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
import os

import six
from stevedore import driver

from vnfsdk_pkgtools.packager import csar
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


class R77707(vnfreq.TesterBase):
    ID = "R-77707"
    DESC = ("The VNF provider MUST include a Manifest File that contains "
            "a list of all the components in the VNF package.")

    def _do_check(self, reader, tosca):
        for root, dirs, files in os.walk(reader.destination):
            for file in files:
                full_path = os.path.join(root, file)
                rel_path = os.path.relpath(full_path, reader.destination)
                if rel_path not in (reader.entry_manifest_file, csar.META_FILE):
                    if rel_path not in reader.manifest.digests:
                        raise vnfreq.VnfRequirementError("Package component %s not found in manifest file" % rel_path)
        return 0


class R04298(vnfreq.TesterBase):
    ID = "R-04298"
    DESC = ("The VNF provider MUST provide their testing scripts "
            "to support testing.")

    def _do_check(self, reader, tosca):
        if not reader.entry_tests_dir:
            raise vnfreq.VnfRequirementError("No test directory found")
        elif not os.listdir(os.path.join(reader.destination,
                                         reader.entry_tests_dir)):
            raise vnfreq.VnfRequirementError("No testing scripts found")
        return 0

