import argparse
import shutil
import tempfile

import pkg_resources

from vnfsdk_pkgtools import validator
from vnfsdk_pkgtools import vnfreq
from vnfsdk_pkgtools.packager import csar, csar_type


def parse_args(args_list):
    """
    CLI entry point
    """
    parser = _create_cli_arg_parser()
    return parser.parse_args(args_list)


def _create_cli_arg_parser():
    parser = argparse.ArgumentParser(description='VNF SDK CSAR manipulation tool')
    parser.add_argument('-v', '--verbose',
                        dest='verbosity',
                        action='count',
                        default=0,
                        help='Set verbosity level (can be passed multiple times)')
    subparsers = parser.add_subparsers(help='csar-create')
    _add_csar_create_parser(subparsers)
    _add_csar_open_parser(subparsers)
    _add_csar_validate_parser(subparsers)
    return parser


def _add_csar_create_parser(subparsers):
    csar_create = subparsers.add_parser('csar-create')
    csar_create.set_defaults(func=_csar_create_func)
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


def _add_csar_open_parser(subparsers):
    csar_open = subparsers.add_parser('csar-open')
    csar_open.set_defaults(func=_csar_open_func)
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


def _add_csar_validate_parser(subparsers):
    csar_validate = subparsers.add_parser('csar-validate')
    csar_validate.set_defaults(func=_csar_validate_func)
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
    csar_validate.add_argument(
        '-t', '--type',
        default=csar_type.VNF_CSAR_TYPE,
        choices=[csar_type.VNF_CSAR_TYPE, csar_type.PNF_CSAR_TYPE],
        help='select type of CSAR file')


def _csar_create_func(namespace):
    csar.write(namespace.source,
               namespace.entry,
               namespace.destination,
               args=namespace)


def _csar_open_func(namespace):
    csar.read(namespace.source,
              namespace.destination,
              namespace.no_verify_cert)


def _csar_validate_func(namespace):
    workdir = tempfile.mkdtemp()
    try:
        err = 0
        reader = csar.read(
            source=namespace.source,
            destination=workdir,
            csar_type=namespace.type,
            no_verify_cert=True
        )

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
