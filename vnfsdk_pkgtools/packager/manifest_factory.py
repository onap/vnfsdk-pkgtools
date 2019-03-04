from vnfsdk_pkgtools.packager import manifest
from vnfsdk_pkgtools.packager.csar_type import VNF_CSAR_TYPE, PNF_CSAR_TYPE


def create_manifest(root_path, manifest_path, csar_type=VNF_CSAR_TYPE):

    if csar_type == VNF_CSAR_TYPE:
        return manifest.Manifest(root_path,manifest_path)
    elif csar_type == PNF_CSAR_TYPE:
        raise Exception("Under construction. Not supported yet!")
    else:
        raise Exception("Unrecognized manifest type '{}'".format(csar_type))
