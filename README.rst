ONAP VNFSDK CSAR Package Tool
========================
VNFSDK package tool provides VNF product DevOps engineers with the tools to manage the VNF package content. The tools are provided in a form of a shared library (Python module) that can be used in other projects. A CLI is also provided out-of-the box for DevOps to use the library with their scripts and automation framework.

Source Code: https://git.onap.org/vnfsdk/pkgtools

Usage
-----
- Create CSAR package
      $ vnfsdk csar-create -d DESTINATION [–manifest MANIFEST] [–history HISTORY] [–tests TESTS] [–licenses LICENSES] source entry
- Extract CSAR package
      $ vnfsdk csar-open -d DESTINATION source
- Validate CSAR package
      $ vnfsdk csar-validate source

All commands have -h switch which displays help and description of all parameters.
