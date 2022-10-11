terraform {
  required_providers {
    oci = {
      source = "hashicorp/oci"
    }
    random = {
      source = "hashicorp/random"
    }
  }
}

provider "oci" {
region              = var.region
config_file_profile = var.profile
}

resource "oci_core_instance" "app_instance" {
  availability_domain = lookup(data.oci_identity_availability_domains.ADs.availability_domains[var.availability_domain - 1],"name")
  compartment_id      = var.compartment_ocid
  display_name        = var.app_instance_display_name
  shape               = var.instance_shape

  shape_config {
    ocpus = var.instance_ocpus
    memory_in_gbs = var.instance_shape_config_memory_in_gbs
  }

  create_vnic_details {
    subnet_id        = oci_core_subnet.multicloud_subnet.id
    display_name     = var.vnic_display_name
    assign_public_ip = true
  }

  source_details {
    source_type = "image"
    source_id   = lookup(data.oci_core_images.compute_images.images[0], "id")

    # Apply this to set the size of the boot volume that's created for this instance.
    # Otherwise, the default boot volume size of the image is used.
    # This should only be specified when sour ce_type is set to "image".
    #boot_volume_size_in_gbs = "60"
  }
  metadata = {
    user_data           = data.cloudinit_config.init.rendered
    ssh_authorized_keys = var.public_ssh_key == "" ? null : var.public_ssh_key
  }

}

# Cloud Init
data "cloudinit_config" "init" {
  gzip          = true
  base64_encode = true

  part {
    filename     = "cloud-config.yaml"
    content_type = "text/cloud-config"
    content      = local.cloud_init
  }
}

## Files and Templatefiles
locals {
  setup_preflight = file(var.resources.setup-preflight_sh.path)
  setup_template = templatefile(var.resources.setup-template_sh.path,
    {
      oracle_client_version = var.oracle_client_version
    })
  deploy_template = templatefile(var.resources.deploy-template_sh.path,
    {
      oracle_client_version = var.oracle_client_version
      db_name               = oci_database_autonomous_database.autonomous_database.db_name
      atp_pw                = random_string.autonomous_database_admin_password.result
      wallet_par            = "https://objectstorage.${var.region}.oraclecloud.com${oci_objectstorage_preauthrequest.multicloud_wallet_preauth.access_uri}"
      jar_par               = "https://objectstorage.${var.region}.oraclecloud.com${oci_objectstorage_preauthrequest.multicloud_app_preauth.access_uri}"
      compartment_id        = var.compartment_ocid
      vault_id              = data.oci_kms_vault.multicloud_vault_query.id
    })

  multicloud_sql_template = templatefile(var.resources.setup_sql.path,
    {
      multicloud_password = random_string.multicloud_db_password.result
    })

  cloud_init = templatefile(var.resources.cloud-config-template_yaml.path,
    {
      setup_preflight_sh_content     = base64gzip(local.setup_preflight)
      setup_template_sh_content      = base64gzip(local.setup_template)
      deploy_template_content        = base64gzip(local.deploy_template)
      multicloud_sql_template_content = base64gzip(local.multicloud_sql_template)
      multicloud_password             = random_string.multicloud_db_password.result
      db_name                        = oci_database_autonomous_database.autonomous_database.db_name
    })
}