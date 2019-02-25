import os

import vnfsdk_pkgtools


def get_project_root():
    """Returns project root folder."""
    return os.path.abspath(os.path.join(os.path.dirname(vnfsdk_pkgtools.__file__),os.pardir))
