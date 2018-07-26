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
import os
import urlparse

import requests


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
