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
import textwrap

import prettytable
import six
from stevedore import driver


NS = "vnfsdk.pkgtools.vnfreq"

def get_vnfreq_tester(name):
    """Get vnf requirement tester.

    :param name: name of the vnf requirement tester
    """

    loaded_driver = driver.DriverManager(NS,
                                         name,
                                         invoke_on_load=True)
    return loaded_driver.driver


def check_and_print(test_reqs, reader, tosca):
    err = 0

    table = prettytable.PrettyTable(['Req ID', 'Status', 'Description'])
    table.align = 'l'
    for t in test_reqs:
        testor = get_vnfreq_tester(t)
        testor.check(reader, tosca)
        if testor.err:
            status = 'ERROR: ' + str(testor.err)
            err = 1
        else:
            status = 'OK'
        # wrap the testor description because it's too long
        lines = textwrap.wrap(testor.DESC, width=40)
        table.add_row([testor.ID, status, lines.pop(0)])
        for line in lines:
            table.add_row(['','',line])
    if test_reqs:
        print(table)
    return err


class VnfRequirementError(ValueError):
    pass


@six.add_metaclass(abc.ABCMeta)
class TesterBase(object):
    """Base class for vnf requirement tester."""

    ID = None
    DESC = None

    def __init__(self):
        self.err = None

    @abc.abstractmethod
    def _do_check(self, reader, tosca):
        """Check the vnf requirement meet or not.

        :param reader: instance of package.csar._CSARReader
        :param tosca: instance of validator.toscaparser_validator.ToscaparserValidator

        return: 0 for success, otherwise failure
        """

    def check(self, reader, tosca):
        try:
            self.err = self._do_check(reader, tosca)
        except Exception as e:
            self.err = e

