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

import logging
import os

from ruamel import yaml

LOG = logging.getLogger(__name__)


def load_definitions(config_file, defaults=None):
    if not os.path.exists(config_file):
        LOG.info("No definitions configuration file %s found! "
                 "Using default config.", config_file)
        definition_cfg = defaults
    else:
        LOG.debug("Loading definitions configuration file: %s", config_file)
        with open(config_file) as cf:
            config = cf.read()
        try:
            definition_cfg = yaml.safe_load(config)
        except yaml.YAMLError as err:
            if hasattr(err, 'problem_mark'):
                mark = err.problem_mark
                errmsg = ("Invalid YAML syntax in Definitions file %(file)s "
                          "at line: %(line)s, column: %(column)s." %
                          dict(file=config_file,
                               line=mark.line + 1,
                               column=mark.column + 1))
            else:
                errmsg = ("YAML error reading Definitions file %s" % config_file)
            LOG.error(errmsg)
            raise

    LOG.debug("Definitions: %s", definition_cfg)
    return definition_cfg
