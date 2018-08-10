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

import functools
import json
import logging
import os
import pkg_resources
import re

from toscaparser.common.exception import ValidationError
from toscaparser.tosca_template import ToscaTemplate

from vnfsdk_pkgtools import validator
from vnfsdk_pkgtools.validator import utils

LOG = logging.getLogger(__name__)


class HpaSchemaDefError(ValueError):
    pass


class HpaValueError(ValueError):
    pass


class ToscaparserValidator(validator.ValidatorBase):
    def __init__(self):
        super(ToscaparserValidator, self).__init__()
        self._load_hpa_definition()

    def _load_hpa_definition(self):
        conf = pkg_resources.resource_filename(__name__, "hpa.yaml")
        defs = utils.load_definitions(conf, {})
        self.hpa_schema_version = defs.get('metadata', {}).get('version')
        self.hpa_schemas = defs.get('schemas', {})
        self.hpa_mappings = defs.get('mappings', [])
        #validate schema defined in hpa.yaml is correct
        if not self.hpa_schema_version:
            msg = "No defined version in metadata"
            raise HpaSchemaDefError(msg)
        for mapping in self.hpa_mappings:
            schema = mapping.get('schema')
            if schema not in self.hpa_schemas:
                msg = "schema %s not found in hpa schema definitions" % schema
                raise HpaSchemaDefError(msg)
            if not mapping.get('type') or not mapping.get('key'):
                msg = "type or key not defined in hpa mappings"
                raise HpaSchemaDefError(msg)

    def validate(self, reader):
        entry_path = os.path.join(reader.destination,
                                       reader.entry_definitions)
        try:
            #TODO set debug_mode due to upstream bug
            #     https://jira.opnfv.org/browse/PARSER-181
            self.tosca = ToscaTemplate(path=entry_path,
                                       no_required_paras_check=True,
                                       debug_mode=True)
        except ValidationError as e:
            LOG.error(e.message)
            raise e
        self.validate_hpa()

    def is_type(self, node, tosca_type):
        if node is None:
            return False
        elif node.type == tosca_type:
            return True
        else:
            return self.is_type(node.parent_type, tosca_type)

    def extract_value(self, node, key):
        if node is None:
            return None

        (cur_key, _, pending) = key.partition('##')

        prefix = None
        prop = cur_key
        if ':' in cur_key:
            (prefix, prop) = cur_key.split(':', 1)
        if prefix == 'capability':
            getter = getattr(node, 'get_capability', None)
            if not getter:
                raise HpaSchemaDefError("not find capability %s" % prop)
        elif prefix == 'property' or prefix is None:
            getter = getattr(node, 'get_property_value', None)
            if not getter and isinstance(node, dict):
                getter = getattr(node, 'get')
        else:
            raise HpaSchemaDefError("unknown prefix in mapping "
                                    "key %s" % cur_key)
        value = getter(prop)

        if not pending:
            return value
        elif isinstance(value, list):
            return list(map(functools.partial(self.extract_value,
                                              key=pending),
                            value))
        else:
            return self.extract_value(value, pending)

    @staticmethod
    def validate_value(refkey, hpa_schema, value):
        if value is None:
            return
        if not isinstance(value, dict):
            msg = "node %s: value %s is not a map of string"
            raise HpaValueError(msg % (refkey, value))
        for (key, hpa_value) in value.iteritems():
            if key not in hpa_schema:
                msg = "node %s: %s is NOT a valid HPA key"
                raise HpaValueError(msg  % (refkey, key))
            try:
                hpa_dict = json.loads(hpa_value)
            except:
                msg = "node %s, HPA key %s: %s is NOT a valid json encoded string"
                raise HpaValueError(msg % (refkey, key, hpa_value.encode('ascii', 'replace')))
            if not isinstance(hpa_dict, dict):
                msg = "node %s, HPA key %s: %s is NOT a valid json encoded string of dict"
                raise HpaValueError(msg % (refkey, key, hpa_value.encode('ascii', 'replace')))
            for (attr, val) in hpa_dict.iteritems():
                if attr not in hpa_schema[key]:
                    msg = "node %s, HPA key %s: %s is NOT valid HPA attribute"
                    raise HpaValueError(msg % (refkey, key, attr))
                attr_schema = hpa_schema[key][attr]
                if not re.match(attr_schema, str(val)):
                    msg = ("node %s, HPA key %s, attr %s: %s is not a valid HPA "
                          "attr value, expected re pattern is %s")
                    raise HpaValueError(msg % (refkey, key, attr, val.encode('ascii','replace'), attr_schema))

    def validate_hpa_value(self, refkey, hpa_schema, values):
        if isinstance(values, list):
            for value in values:
                self.validate_value(refkey, hpa_schema, value)
        elif isinstance(values, dict):
            self.validate_value(refkey, hpa_schema, values)

    def validate_hpa(self):
        for node in getattr(self.tosca, 'nodetemplates', []):
            for mapping in self.hpa_mappings:
                if self.is_type(node, mapping['type']):
                    value = self.extract_value(node, mapping['key'])
                    if value:
                        refkey = node.name + '->' + mapping['key']
                        LOG.debug("Checking HPA values %s of node %s "
                                  "against schema %s", value, refkey, mapping['schema'])
                        self.validate_hpa_value(refkey,
                                                self.hpa_schemas[mapping['schema']],
                                                value)


