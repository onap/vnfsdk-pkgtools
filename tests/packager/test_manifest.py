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

import pytest

from vnfsdk_pkgtools.packager import manifest

METADATA = '\n'.join(["metadata:",
                      "vnf_product_name: test",
                      "vnf_provider_id: test",
                      "vnf_package_version:1.0",
                      "vnf_release_data_time: 2017-09-15T15:00:10+08:00",
                      ])

METADATA_MISSING_KEY = '\n'.join(["metadata:",
                                   "vnf_product_name: test",
                                   "vnf_provider_id: test",
                                   "vnf_package_version:1.0",
                                ])

METADATA_MISSING = "vnf_product_name: test"

FILE_CONTENT = "needToBeHashed"
FILE_DIGEST = '\n'.join(['Source: digest',
                         'Algorithm: SHA256',
                         'Hash: 20a480339aa4371099f9503511dcc5a8051ce3884846678ced5611ec64bbfc9c',
                       ])

def test_metadata(tmpdir):
    p = tmpdir.mkdir('csar').join('test.mf')
    p.write(METADATA)

    m = manifest.Manifest(p.dirname, 'test.mf')
    assert m.metadata['vnf_product_name'] == 'test'
    assert m.metadata['vnf_provider_id'] == 'test'
    assert m.metadata['vnf_package_version'] == '1.0'
    assert m.metadata['vnf_release_data_time'] == '2017-09-15T15:00:10+08:00'


def test_metadata_missing_key(tmpdir):
    p = tmpdir.mkdir('csar').join('test.mf')
    p.write(METADATA_MISSING_KEY)

    with pytest.raises(manifest.ManifestException) as excinfo:
        manifest.Manifest(p.dirname, 'test.mf')
    excinfo.match(r"Missing metadata keys:")


def test_missing_metadata(tmpdir):
    p = tmpdir.mkdir('csar').join('test.mf')
    p.write(METADATA_MISSING)

    with pytest.raises(manifest.ManifestException) as excinfo:
        manifest.Manifest(p.dirname, 'test.mf')
    excinfo.match(r"Unknown key in line")

def test_digest(tmpdir):
    root = tmpdir.mkdir('csar')
    mf = root.join('test.mf')
    digest = root.join('digest')
    mf.write(METADATA + '\n\n' + FILE_DIGEST)
    digest.write(FILE_CONTENT)

    m = manifest.Manifest(mf.dirname, 'test.mf')
    assert m.digests['digest'][0] == "SHA256"
    assert m.digests['digest'][1] == "20a480339aa4371099f9503511dcc5a8051ce3884846678ced5611ec64bbfc9c"

def test_add_file(tmpdir):
    root = tmpdir.mkdir('csar')
    mf = root.join('test.mf')
    digest = root.join('digest')
    mf.write(METADATA)
    digest.write(FILE_CONTENT)

    m = manifest.Manifest(mf.dirname, 'test.mf')
    m.add_file('digest', 'SHA256')
    assert m.digests['digest'][0] == "SHA256"
    assert m.digests['digest'][1] == "20a480339aa4371099f9503511dcc5a8051ce3884846678ced5611ec64bbfc9c"

def test_update_to_file(tmpdir):
    root = tmpdir.mkdir('csar')
    mf = root.join('test.mf')
    digest = root.join('digest')
    mf.write(METADATA + '\n\n' + FILE_DIGEST)
    digest.write(FILE_CONTENT)
    digest2 = root.join('digest2')
    digest2.write(FILE_CONTENT)

    m1 = manifest.Manifest(mf.dirname, 'test.mf')
    m1.add_file('digest2', 'SHA256')
    m1.update_to_file()
    m2 = manifest.Manifest(mf.dirname, 'test.mf')
    assert m1.metadata['vnf_provider_id'] == m2.metadata['vnf_provider_id']
    assert m1.digests['digest'] == m2.digests['digest2']
    assert len(m2.digests.keys()) == 2

    
