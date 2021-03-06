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

from vnfsdk_pkgtools import vnfreq


class FakeTester(vnfreq.TesterBase):
    ID = 'fake'
    DESC = 'fake'

    def __init__(self, *args, **kwargs):
        super(FakeTester, self).__init__()

    def _do_check(self, reader, tosca):
        return 'error'


def test_check_and_print(mocker):
    mocker.patch('vnfsdk_pkgtools.vnfreq.get_vnfreq_tester', new=FakeTester)
    ret = vnfreq.check_and_print(['fake'], mocker.Mock(), mocker.Mock())
    assert ret == 1
