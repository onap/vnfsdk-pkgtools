#
# Copyright (c) 2016-2017 GigaSpaces Technologies Ltd. All rights reserved.
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
import sys

import vnfsdk_pkgtools.cli.args_parser as arg_parser


def main():
    args = arg_parser.parse_args(sys.argv[1:])
    _init_logging(args)
    return args.func(args)


def _init_logging(args):
    verbosity = [logging.WARNING, logging.INFO, logging.DEBUG]

    logging.basicConfig()
    logger = logging.getLogger('vnfsdk_pkgtools')
    if args.verbosity >= len(verbosity):
        verbose = verbosity[-1]
    else:
        verbose = verbosity[args.verbosity]
    logger.setLevel(verbose)


if __name__ == '__main__':
    main()
