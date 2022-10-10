# Copyright (c) 2020 Oracle and/or its affiliates. All rights reserved.
# Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
#

#############################################################################################################################
# NOTE: Before use the terraform local or CloudShell, you need to build the binaries of the application.                    #
#       Instructions here: https://github.com/oracle-quickstart/oci-cloudnative/blob/master/deploy/basic/README.md#build    #
#                                                                                                                           #
#       If using Oracle Resource Manager, the stack already include the binaries. ORM does not use the tfvars               #
#############################################################################################################################

# OCI authentication
tenancy_ocid     = ""
profile = "DEFAULT"
# Deployment compartment
compartment_ocid = ""

# region
region = "us-ashburn-1"