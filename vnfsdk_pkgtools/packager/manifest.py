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

import os
import re
import tempfile

import six
import udatetime

from vnfsdk_pkgtools.packager import utils

METADATA_KEYS = [ 'vnf_provider_id',
                  'vnf_product_name',
                  'vnf_release_data_time',
                  'vnf_package_version']
DIGEST_KEYS = [ 'Source', 'Algorithm', 'Hash' ]
SUPPORTED_HASH_ALGO = ['SHA-256', 'SHA-512']

NON_MANO_ARTIFACT_RE = re.compile(r'^[0-9a-z_-]+(\.[0-9a-z_-]+)*:$')

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
        # signature string, in CMS format
        self.signature = None
        # non_mano_artifact dict
        #   :key = set identifier
        #   :value = list of files
        self.non_mano_artifacts = {}
        self.blocks = [ ]
        self._split_blocks()
        self._parse_all_blocks()

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
        with open(os.path.join(self.root, self.path), 'rU') as fp:
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

    def _parse_all_blocks(self):
        for block in self.blocks:
            if block[0] == 'metadata:':
                self.parse_metadata(block)
            elif block[0] == 'non_mano_artifact_sets:':
                self.parse_non_mano_artifacts(block)
            elif '--BEGIN CMS--' in block[0]:
                self.parse_cms(block)
            else:
                self.parse_digest(block)

        if not self.metadata:
            raise ManifestException("No metadata")

    def parse_metadata(self, lines):
        # Skip the first line
        for line in lines[1:]:
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

    def parse_cms(self, lines):
        if '--END CMS--' not in lines[-1]:
            raise ManifestException("Can NOT find end of sigature block")
        self.signature = '\n'.join(lines)

    def parse_digest(self, lines):
        desc = {}
        for line in lines:
            (key, value, remain) = self.__split_line(line)
            if key in DIGEST_KEYS:
                desc[key] = value
            else:
                raise ManifestException("Unrecognized file digest line %s:" % line)

            if key == 'Source':
                self.digests[value] = (None, None)
            elif key == 'Algorithm':
                #validate algorithm
                desc['Algorithm'] = desc['Algorithm'].upper()
                if desc['Algorithm'] not in SUPPORTED_HASH_ALGO:
                    raise ManifestException("Unsupported hash algorithm: %s" % desc['Algorithm'])

            #validate hash
            if desc.get('Algorithm') and desc.get('Hash') and desc.get('Source'):
                hash = utils.cal_file_hash(self.root, desc['Source'], desc['Algorithm'])
                if hash != desc['Hash']:
                    raise ManifestException("Mismatched hash for file %s" % desc['Source'])
                # nothing is wrong, let's store this and start a new round
                self.digests[desc['Source']] = (desc['Algorithm'], desc['Hash'])
                desc = {}

    def parse_non_mano_artifacts(self, lines):
        # Skip the first line
        identifier = None
        for line in lines[1:]:
            if re.match(NON_MANO_ARTIFACT_RE, line):
                # new non mano artifact identifier
                identifier = line[:-1]
                self.non_mano_artifacts[identifier] = []
            else:
                (key, value, remain) = self.__split_line(line)
                if key == 'Source' and value and not remain and identifier:
                    # check for file existence
                    utils.check_file_dir(self.root, value)
                    self.non_mano_artifacts[identifier].append(value)
                else:
                    raise ManifestException("Unrecogized non mano artifacts line %s:" % line)

    def add_file(self, rel_path, algo='SHA256'):
        '''Add file to the manifest and calculate the digest
        '''
        if algo:
            algo = algo.upper()
            if algo not in SUPPORTED_HASH_ALGO:
                raise ManifestException("Unsupported hash algorithm: %s" % algo)
            hash = utils.cal_file_hash(self.root, rel_path, algo)
        else:
            hash = None
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
        # non_mano_artifacts
        if self.non_mano_artifacts:
            ret += "\nnon_mano_artifact_sets:\n"
            for (key, sources) in six.iteritems(self.non_mano_artifacts):
                ret += key + ":\n"
                for s in sources:
                    ret += "Source: %s\n" % s
        # degist
        for (key, digest) in six.iteritems(self.digests):
            ret += "\n"
            ret += "Source: %s\n" % key
            if digest[0]:
                ret += "Algorithm: %s\n" % digest[0]
                ret += "Hash: %s\n" % digest[1]
        if self.digests:
            # empty line between digest and signature section
            ret += "\n"
        # signature
        if  self.signature:
            ret += self.signature
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

    def save_to_temp_without_cms(self):
        # we need to strip cms block with out changing the order of the
        # file digest content before we verify the signature
        skip = False
        lines = []
        with open(os.path.join(self.root, self.path), 'rU') as fp:
            for line in fp:
                if '--BEGIN CMS--' in line:
                    skip = True
                elif '--END CMS--' in line:
                    skip = False
                elif not skip:
                    lines.append(line)
        content = ''.join(lines)
        tmpfile = tempfile.NamedTemporaryFile(mode='w',delete=False)
        tmpfile.write(content)
        tmpfile.close()
        return tmpfile.name
