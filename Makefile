#
# Copyright (c) 2016 GigaSpaces Technologies Ltd. All rights reserved.
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


.PHONY: clean requirements
.DEFAULT_GOAL = requirements

clean:
	find . -type d -name '*.egg-info' -exec rm -rf {} \;
	find . -type d -name '.coverage' -exec rm -rf {} \;
	find . -type f -name '.coverage' -delete

requirements:
	pip install --upgrade --requirement "requirements.txt"
