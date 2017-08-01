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

from setuptools import setup
import sys

if sys.version_info < (2, 7):
    sys.exit('VNF SDK requires Python 2.7+')
if sys.version_info >= (3, 0):
    sys.exit('VNF SDK does not support Python 3')

setup(
    name='vnfsdk',
    version='0.1',
    description='VNF SDK CSAR package tool',
    license='Apache License Version 2.0',

    author='GigaSpaces',
    author_email='info@gigaspaces.com',

    url='http://open-o.org/',

    classifiers=[
        'Development Status :: 4 - Beta',
        'Environment :: Console',
        'Environment :: Web Environment',
        'Intended Audience :: Developers',
        'Intended Audience :: System Administrators',
        'License :: OSI Approved :: Apache Software License',
        'Operating System :: OS Independent',
        'Programming Language :: Python',
        'Topic :: Software Development :: Libraries :: Python Modules',
        'Topic :: System :: Networking',
        'Topic :: System :: Systems Administration'],

    packages=[
        'packager',
        'cli'
    ],

    package_dir={
        'packager': 'packager',
        'cli': 'cli',
    },

    entry_points={
        'console_scripts': [
            'vnfsdk = cli.__main__:main']
    },
    # Please make sure this is in sync with src/aria/requirements.txt
    install_requires=[
        'ruamel.yaml<0.12.0,>=0.11.12',
        'requests<2.14.0,>=2.3.0',
        'logging',
        'clint==0.5.1',
        'CacheControl[filecache]==0.11.6',
        'lockfile',
        'Jinja2==2.8',
        'apache-ariatosca==0.1.1'])

