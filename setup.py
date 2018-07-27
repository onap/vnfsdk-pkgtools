#!/usr/bin/env python

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

import os
from setuptools import find_packages, setup
import sys

if sys.version_info < (2, 7):
    sys.exit('VNF SDK requires Python 2.7+')
if sys.version_info >= (3, 0):
    sys.exit('VNF SDK does not support Python 3')


root_dir = os.path.dirname(__file__)
install_requires = []
extras_require = {}

with open(os.path.join(root_dir, 'requirements.txt')) as requirements:
    for requirement in requirements.readlines():
        # get rid of comments or trailing comments
        requirement = requirement.split('#')[0].strip()
        if not requirement:
            continue # skip empty and comment lines
        # dependencies which use environment markers have to go in as
        # conditional dependencies under "extra_require", see more at:
        # https://wheel.readthedocs.io/en/latest/index.html#defining-conditional-dependencies
        if ';' in requirement:
            package, condition = requirement.split(';')
            cond_name = ':{0}'.format(condition.strip())
            extras_require.setdefault(cond_name, [])
            extras_require[cond_name].append(package.strip())
        else:
            install_requires.append(requirement)

extras_require['aria'] = 'apache-ariatosca==0.1.1'

version = { }
with open(os.path.join(root_dir, 'vnfsdk_pkgtools/version.py')) as fp:
    exec(fp.read(), version)

setup(
    name='vnfsdk',
    version=version['__version__'],
    description='VNFSDK CSAR package tool',
    long_description="VNFSDK CSAR package tool in ONAP",
    license='Apache License Version 2.0',

    author='ONAP',

    url='https://git.onap.org/vnfsdk/pkgtools',

    classifiers=[
        'Development Status :: 4 - Beta',
        'Environment :: Console',
        'Environment :: Web Environment',
        'Intended Audience :: Developers',
        'Intended Audience :: System Administrators',
        'License :: OSI Approved :: Apache Software License',
        'Operating System :: OS Independent',
        'Programming Language :: Python',
        'Programming Language :: Python :: 2',
        'Programming Language :: Python :: 2.7',
        'Topic :: Software Development :: Libraries :: Python Modules',
        'Topic :: System :: Networking',
        'Topic :: System :: Systems Administration'],

    packages=find_packages(exclude=['tests*']),

    entry_points={
        'console_scripts': [
            'vnfsdk = vnfsdk_pkgtools.cli.__main__:main'],
        'vnfsdk.pkgtools.validator': [
            'aria = vnfsdk_pkgtools.validator.aria_validator:AriaValidator [aria]'
        ]
    },

    include_package_data=True,
    install_requires=install_requires,
    extras_require=extras_require)

