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

import sys
import logging
import argparse
import os
import shutil
import tempfile

import pkg_resources

from vnfsdk_pkgtools.packager import csar
from vnfsdk_pkgtools import validator
from vnfsdk_pkgtools import vnfreq


def csar_create_func(namespace):

    csar.write(namespace.source,
                        namespace.entry,
                        namespace.destination,
                        args=namespace)

def csar_open_func(namespace):
    csar.read(namespace.source,
              namespace.destination,
              namespace.no_verify_cert)

def csar_validate_func(namespace):
    workdir = tempfile.mkdtemp()
    try:
        err = 0
        reader = csar.read(namespace.source,
                           workdir,
                           no_verify_cert=True)

        driver = validator.get_validator(namespace.parser)
        driver.validate(reader)
        print("---Basic & HPA validation passed!---")
        if namespace.test_reqs:
            print("---Check VNF Requirements---")
            err = vnfreq.check_and_print(namespace.test_reqs,
                                         reader,
                                         driver)
        return err
    finally:
        shutil.rmtree(workdir, ignore_errors=True)


def parse_args(args_list):
    """
    CLI entry point
    """
    parser = argparse.ArgumentParser(description='VNF SDK CSAR manipulation tool')
    parser.add_argument('-v', '--verbose',
            dest='verbosity',
            action='count',
            default=0,
            help='Set verbosity level (can be passed multiple times)')

    subparsers = parser.add_subparsers(help='csar-create')
    csar_create = subparsers.add_parser('csar-create')
    csar_create.set_defaults(func=csar_create_func)
    csar_create.add_argument(
        'source',
        help='Service template directory')
    csar_create.add_argument(
        'entry',
        help='Entry definition file relative to service template directory')
    csar_create.add_argument(
        '-d', '--destination',
        help='Output CSAR zip destination',
        required=True)
    csar_create.add_argument(
        '--manifest',
        help='Manifest file relative to service template directory')
    csar_create.add_argument(
        '--history',
        help='Change history file relative to service template directory')
    csar_create.add_argument(
        '--tests',
        help='Directory containing test information, relative to service template directory')
    csar_create.add_argument(
        '--licenses',
        help='Directory containing license information, relative to service template directory')
    csar_create.add_argument(
        '--digest',
        choices=['SHA256', 'SHA512'],
        help='If present, means to check the file deigest in manifest;  compute the digest using the specified hash algorithm of all files in the csar package to be put into the manifest file')
    csar_create.add_argument(
        '--certificate',
        help='Certificate file for certification, relative to service template directory')
    csar_create.add_argument(
        '--privkey',
        help='Private key file for certification, absoluate or relative path')


    csar_open = subparsers.add_parser('csar-open')
    csar_open.set_defaults(func=csar_open_func)
    csar_open.add_argument(
        'source',
        help='CSAR file location')
    csar_open.add_argument(
        '-d', '--destination',
        help='Output directory to extract the CSAR into',
        required=True)
    csar_open.add_argument(
        '--no-verify-cert',
        action='store_true',
        help="Do NOT verify the signer's certificate")


    csar_validate = subparsers.add_parser('csar-validate')
    csar_validate.set_defaults(func=csar_validate_func)
    csar_validate.add_argument(
        'source',
        help='CSAR file location')
    csar_validate.add_argument(
        '-p', '--parser',
        default='toscaparser',
        choices=[ep.name for ep in pkg_resources.iter_entry_points(validator.NS)],
        help='use which csar parser to validate')
    csar_validate.add_argument(
        '--test-reqs',
        nargs='*',
        default=[],
        choices=[ep.name for ep in pkg_resources.iter_entry_points(vnfreq.NS)],
        help='list of the ID of VNF Requirements to check, i.e. R-66070')

    return parser.parse_args(args_list)


def init_logging(args):
    verbosity = [logging.WARNING, logging.INFO, logging.DEBUG]

    logging.basicConfig()
    logger = logging.getLogger('vnfsdk_pkgtools')
    if args.verbosity >= len(verbosity):
        verbose = verbosity[-1]
    else:
        verbose = verbosity[args.verbosity]
    logger.setLevel(verbose)


def main():
    args = parse_args(sys.argv[1:])
    init_logging(args)
    return args.func(args)


if __name__ == '__main__':
    main()
