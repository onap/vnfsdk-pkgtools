# Copyright (c) 2018 Intel Corp Inc. All rights reserved.
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

import hashlib
from io import BytesIO
import logging
import os
import os.path
import urlparse
import subprocess
import tempfile

import requests

LOG = logging.getLogger(__name__)


def _hash_value_for_file(f, hash_function, block_size=2**20):
    while True:
        data = f.read(block_size)
        if not data:
            break
        hash_function.update(data)

    return hash_function.hexdigest()


def cal_file_hash(root, path, algo):
    h = hashlib.new(algo)
    if urlparse.urlparse(path).scheme:
        r = requests.get(path)
        if r.status_code != 200:
            raise ValueError('Server at {0} returned a {1} status code'
                             .format(path, r.status_code))
        fp = BytesIO(r.content)
        return _hash_value_for_file(fp, h)
    else:
        with open(os.path.join(root, path), 'rb') as fp:
            return _hash_value_for_file(fp, h)


def _run_cmd(cmd, **kwargs):
    if isinstance(cmd, list):
        args = cmd
    elif isinstance(cmd, string):
        args = [cmd]
    else:
        raise RuntimeError("cmd must be string or list")

    for key, value in kwargs.iteritems():
        args.append(key)
        if value:
            args.append(value)
    try:
        LOG.debug("Executing %s", args)
        return subprocess.check_output(args)
    except subprocess.CalledProcessError as e:
        LOG.error("Executing %s failed with return code %d, output: %s",
                  e.cmd, e.returncode, e.output)
        raise e


def sign(msg_file, cert_file, key_file):
    args = ["openssl", "cms", "-sign", "-binary", "-nocerts"]
    kwargs = {
              '-in': os.path.abspath(msg_file),
              '-signer': os.path.abspath(cert_file),
              '-inkey': os.path.abspath(key_file),
              '-outform': 'PEM',
             }

    return _run_cmd(args, **kwargs)


def verify(msg_file, cert_file, cms, no_verify_cert=False):
    args = ["openssl", "cms", "-verify"]
    if no_verify_cert:
        args.append("-no_signer_cert_verify")

    with tempfile.NamedTemporaryFile() as f:
        f.write(cms)
        f.flush()
        kwargs = {
                  '-in': f.name,
                  '-inform': 'PEM',
                  '-content': os.path.abspath(msg_file),
                  '-certfile': os.path.abspath(cert_file),
                 }
        return _run_cmd(args, **kwargs)