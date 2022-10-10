

#!/bin/bash -x
# Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
# Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
#
#
# Description: Sets up Mushop Basic a.k.a. "Monolite".
# Return codes: 0 =
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
#

# Configure firewall
firewall-offline-cmd --add-port=80/tcp
firewall-offline-cmd --add-port=8080/tcp
firewall-offline-cmd --add-port=22/tcp
systemctl restart firewalld

# Install the yum repo
yum clean metadata
yum-config-manager --enable ol7_latest

yum -y install unzip jq

# Install Oracle Instant Client
yum -y install oracle-release-el7
yum-config-manager --enable ol7_oracle_instantclient
yum -y install oracle-instantclient${oracle_client_version}-basic oracle-instantclient${oracle_client_version}-jdbc oracle-instantclient${oracle_client_version}-sqlplus
yum -y install graalvm22-ee-17.x86_64

# Enable and start services
systemctl daemon-reload

######################################
echo "Finished running setup.sh"