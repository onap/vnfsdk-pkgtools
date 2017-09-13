#!/usr/bin/env python

#
# Copyright (c) 2016-2017 AT&T Inc. All rights reserved.
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
from setuptools import setup
import sys

if sys.version_info < (2, 7):
    sys.exit('VNF SDK requires Python 2.7+')
if sys.version_info >= (3, 0):
    sys.exit('VNF SDK does not support Python 3')


root_dir = os.path.dirname(__file__)
install_requires = []
extras_require = {}

os.system("sudo apt-get install update")
os.system("sudo apt-get install upgrade")
os.system("sudo apt-get install build-essential")
os.system("cd ~/pkgtools/src/veslibrary/ves_clibrary/evel/evel-library/bldjobs;make clean;make all")
os.system("cd ~/pkgtools/src/veslibrary/ves_clibrary/evel/evel-library/bldjobs;make install")
os.system("sudo apt-get install openjdk-8-jre")
os.system("sudo apt-get install openjdk-8-jdk")
os.system("sudo apt-get install ant")
os.system("cd ~/pkgtools/src/veslibrary/ves_javalibrary;ant jar")
print("set Java app classpath to ~/pkgtools/src/veslibrary/ves_javalibrary/dist/*")


