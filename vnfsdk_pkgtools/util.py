import os
import os.path as path

import vnfsdk_pkgtools


def get_project_root():
    """Returns project root folder."""
    return path.abspath(path.join(path.dirname(vnfsdk_pkgtools.__file__), os.pardir))
