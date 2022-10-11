#!/bin/bash -x
# Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
# Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
#
#
# Description: Sets up Mushop Basic a.k.a. "Monolite".
# Return codes: 0 =
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
#

get_object() {
    out_file=$1
    os_uri=$2
    success=1
    for i in $(seq 1 9); do
        echo "trying ($i) $2"
        http_status=$(curl -w '%%{http_code}' -L -s -o $1 $2)
        if [ "$http_status" -eq "200" ]; then
            success=0
            echo "saved to $1"
            break
        else
             sleep 15
        fi
    done
    return $success
}

# get artifacts from object storage
get_object /root/wallet.64 ${wallet_par}
# Setup ATP wallet files
base64 --decode /root/wallet.64 > /root/wallet.zip
unzip /root/wallet.zip -d /usr/lib/oracle/${oracle_client_version}/client64/lib/network/admin/

# Init DB
sqlplus ADMIN/"${atp_pw}"@${db_name}_tp @/root/multicloud.sql

# Get jar
export COMPARTMENT_ID=${compartment_id}
export VAULT_OCID=${vault_id}

get_object /root/app.jar ${jar_par}

java -version

java -jar /root/app.jar