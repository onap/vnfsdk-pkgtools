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

from packager import csar
import sys
import logging
import argparse
from aria import install_aria_extensions
import os
import shutil
import tempfile
from aria.parser.loading import LiteralLocation
from aria.parser.consumption import (
    ConsumptionContext,
    ConsumerChain,
    Read,
    Validate,
    ServiceTemplate,
    ServiceInstance
)

def csar_create_func(namespace):
    csar.write(namespace.source,
                        namespace.entry,
                        namespace.destination,
                        logging)
def csar_open_func(namespace):
    csar.read(namespace.source,
                       namespace.destination,
                       logging)
def csar_validate_func(namespace):
    workdir = tempfile.mkdtemp()
    try:
        reader = None
        reader = csar.read(namespace.source,
                           workdir,
                           logging)
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
    finally:
        shutil.rmtree(workdir, ignore_errors=True)


def parse_args(args_list):
    """
    CLI entry point
    """
    install_aria_extensions()

    parser = argparse.ArgumentParser(description='VNF SDK CSAR manipulation tool')

    subparsers = parser.add_subparsers(help='csar-create')
    csar_create = subparsers.add_parser('csar-create')
    csar_create.set_defaults(func=csar_create_func)
    csar_create.add_argument('-v', '--verbose',
            dest='verbosity',
            action='count',
            default=0,
            help='Set verbosity level (can be passed multiple times)')
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

    csar_open = subparsers.add_parser('csar-open')
    csar_open.set_defaults(func=csar_open_func)
    csar_open.add_argument(
        'source',
        help='CSAR file location')
    csar_open.add_argument(
        '-d', '--destination',
        help='Output directory to extract the CSAR into',
        required=True)

    csar_validate = subparsers.add_parser('csar-validate')
    csar_validate.set_defaults(func=csar_validate_func)
    csar_validate.add_argument(
        'source',
        help='CSAR file location')

    return parser.parse_args(args_list)

def main():
    args = parse_args(sys.argv[1:])
    args.func(args)


if __name__ == '__main__':
    main()
