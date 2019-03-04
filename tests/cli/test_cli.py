#
# Copyright (c) 2017 GigaSpaces Technologies Ltd. All rights reserved.
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

import pytest
from assertpy import assert_that

import vnfsdk_pkgtools.cli.args_parser as arg_parser


def test_main(capsys):
    with pytest.raises(SystemExit):
        args = arg_parser.parse_args(['csar-create', '-h'])
        args.func(args)
    out, err = capsys.readouterr()
    assert out.startswith('usage:')


def test_that_default_vnf_csar_type_is_used_when_type_is_not_defined_for_csar_validate_parser():
    args = arg_parser.parse_args(['csar-validate', 'dummyVNF.csar'])

    assert_that(args.parser).is_equal_to('toscaparser')
    assert_that(args.type).is_equal_to('vnf')
    assert_that(args.source).is_equal_to('dummyVNF.csar')


def test_that_pnf_csar_type_is_used_for_csar_validate_cli_parser():
    args = arg_parser.parse_args(['csar-validate', '-t', 'pnf', 'dummyPNF.csar'])

    assert_that(args.parser).is_equal_to('toscaparser')
    assert_that(args.type).is_equal_to('pnf')
    assert_that(args.source).is_equal_to('dummyPNF.csar')
