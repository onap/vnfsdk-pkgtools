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

import abc

import six
from stevedore import driver


NS = "vnfsdk.pkgtools.validator"

def get_validator(params):
    """Get validate driver and load it.

    :param params: parameters to decide which validator to load
    """

    loaded_driver = driver.DriverManager(NS,
                                         params,
                                         invoke_on_load=True)
    return loaded_driver.driver


@six.add_metaclass(abc.ABCMeta)
class ValidatorBase(object):
    """Base class for validators."""

    def __init__(self):
        self.tosca = None


    @abc.abstractmethod
    def validate(self, reader):
        """Validate the csar package.

        :param reader: instance of package.csar._CSARReader
        """
