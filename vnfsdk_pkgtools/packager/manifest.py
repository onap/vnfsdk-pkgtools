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

from collections import namedtuple
import os
import tempfile

import udatetime

from vnfsdk_pkgtools.packager import utils

METADATA_KEYS = [ 'vnf_provider_id',
                  'vnf_product_name',
                  'vnf_release_data_time',
                  'vnf_package_version']
DIGEST_KEYS = [ 'Source', 'Algorithm', 'Hash' ]
SUPPORTED_HASH_ALGO = ['SHA256', 'SHA512']

class ManifestException(Exception):
    pass

class Manifest(object):
    ' Manifest file in CSAR package'
    def __init__(self, root_path, manifest_path):
        self.path = manifest_path
        self.root = root_path
        self.metadata = {}
        # digest dict
        #   :key = source
        #   :value = (algorithm, hash)
        self.digests = {}
        self.signature = None
        self.blocks = [ ]
        self._split_blocks()
        self._parse_blocks()

    @staticmethod
    def __split_line(s):
        remain=s
        try:
            (key, value)=s.split(':', 1)
            value = value.strip()
            remain = None
        except ValueError:
            key = None
            value = None
        return (key, value, remain)

    def _split_blocks(self):
        '''
        Split manifest file into blocks, each block is seperated by a empty
        line or a line with only spaces and tabs.
        '''
        block_content = [ ]
        with open(os.path.join(self.root, self.path),'rU') as fp:
            for line in fp:
                line = line.strip(' \t\n')
                if line:
                    block_content.append(line)
                else:
                    if len(block_content):
                        self.blocks.append(block_content)
                    block_content = []
        if len(block_content):
            self.blocks.append(block_content)

    def _parse_blocks(self):
        for block in self.blocks:
            (key, value, remain) = self.__split_line(block.pop(0))
            if key == 'metadata':
                # metadata block
                for line in block:
                    (key, value, remain) = self.__split_line(line)
                    if key in METADATA_KEYS:
                        self.metadata[key] = value
                    else:
                        raise ManifestException("Unrecognized metadata %s:" % line)
                #validate metadata keys
                missing_keys = set(METADATA_KEYS) - set(self.metadata.keys())
                if missing_keys:
                    raise ManifestException("Missing metadata keys: %s" % ','.join(missing_keys))
                # validate vnf_release_data_time
                try:
                    udatetime.from_string(self.metadata['vnf_release_data_time'])
                except ValueError:
                    raise ManifestException("Non IETF RFC 3339 vnf_release_data_time: %s"
                                    % self.metadata['vnf_release_data_time'])
            elif key in DIGEST_KEYS:
                # file digest block
                desc = {}
                desc[key] = value
                for line in block:
                    (key, value, remain) = self.__split_line(line)
                    if key in DIGEST_KEYS:
                        desc[key] = value
                    else:
                        raise ManifestException("Unrecognized file digest line %s:" % line)
                # validate file digest keys
                missing_keys = set(DIGEST_KEYS) - set(desc.keys())
                if missing_keys:
                    raise ManifestException("Missing file digest keys: %s" % ','.join(missing_keys))
                # validate file digest algo
                desc['Algorithm'] = desc['Algorithm'].upper()
                if desc['Algorithm'] not in SUPPORTED_HASH_ALGO:
                    raise ManifestException("Unsupported hash algorithm: %s" % desc['Algorithm'])
                # validate file digest hash
                hash = utils.cal_file_hash(self.root, desc['Source'], desc['Algorithm'])
                if hash != desc['Hash']:
                    raise ManifestException("Mismatched hash for file %s" % desc['Source'])
                # nothing is wrong, let's store this
                self.digests[desc['Source']] = (desc['Algorithm'], desc['Hash'])
            elif key:
                raise ManifestException("Unknown key in line '%s:%s'" % (key, value))
            else:
                # TODO signature block
                pass

        if not self.metadata:
            raise ManifestException("No metadata")

    def add_file(self, rel_path, algo='SHA256'):
        '''Add file to the manifest and calculate the digest
        '''
        algo = algo.upper()
        if algo not in SUPPORTED_HASH_ALGO:
            raise ManifestException("Unsupported hash algorithm: %s" % algo)
        hash = utils.cal_file_hash(self.root, rel_path, algo)
        self.digests[rel_path] = (algo, hash)

    def return_as_string(self):
        '''Return the manifest file content as a string
        '''
        ret = ""
        # metadata
        ret += "metadata:\n"
        ret += "vnf_product_name: %s\n" % (self.metadata['vnf_product_name'])
        ret += "vnf_provider_id: %s\n" % (self.metadata['vnf_provider_id'])
        ret += "vnf_package_version: %s\n" % (self.metadata['vnf_package_version'])
        ret += "vnf_release_data_time: %s\n" % (self.metadata['vnf_release_data_time'])
        # degist
        for (key, digest) in self.digests.iteritems():
            ret += "\n"
            ret += "Source: %s\n" % key
            ret += "Algorithm: %s\n" % digest[0]
            ret += "Hash: %s\n" % digest[1]
        return ret

    def update_to_file(self, temporary=False):
        content = self.return_as_string()
        if temporary:
            abs_path = tempfile.mktemp()
        else:
            abs_path = os.path.abspath(os.path.join(self.root, self.path))

        with open(abs_path, 'w') as fp:
            fp.write(content)
        return abs_path
