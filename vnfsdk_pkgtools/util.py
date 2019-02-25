from pathlib2 import Path


def get_project_root():
    """Returns project root folder."""
    return str(Path(__file__).parent.parent.absolute())
