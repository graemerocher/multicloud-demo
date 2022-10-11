resource "oci_kms_vault" "multicloud_vault" {
  compartment_id = var.compartment_ocid
  display_name   = "Multicloud"
  vault_type     = "DEFAULT"

  count = var.create_vault ? 1 : 0
}

data "oci_kms_vault" "multicloud_vault_query" {
  #Required
  vault_id = var.vault_ocid == "" ? oci_kms_vault.multicloud_vault[0].id : var.vault_ocid
}

data "oci_kms_key" "multicloud_key_query" {
  #Required
  key_id = var.vault_key_ocid == "" ? oci_kms_key.multicloud_key[0].id : var.vault_key_ocid

  management_endpoint = data.oci_kms_vault.multicloud_vault_query.management_endpoint
}

# Creates OCI Vault key
resource "oci_kms_key" "multicloud_key" {
  compartment_id      = var.compartment_ocid
  display_name        = var.oci_kms_key_display_name
  management_endpoint = data.oci_kms_vault.multicloud_vault_query.management_endpoint

  key_shape {
    algorithm = "AES"
    length    = 32
  }

  count = var.create_vault_key ? 1 : 0
}

resource "oci_vault_secret" "wallet_password" {
  #Required
  compartment_id = var.compartment_ocid
  secret_content {
    #Required
    content_type = "BASE64"

    #Optional
    content = base64encode(random_string.autonomous_database_wallet_password.result)
    name    = "ATP_WALLET_PASSWORD"
  }
  secret_name = "ATP_WALLET_PASSWORD"
  vault_id    = data.oci_kms_vault.multicloud_vault_query.id
  key_id      = data.oci_kms_key.multicloud_key_query.id
}


resource "oci_vault_secret" "database_password" {
  #Required
  compartment_id = var.compartment_ocid
  secret_content {
    #Required
    content_type = "BASE64"

    #Optional
    content = base64encode(random_string.multicloud_db_password.result)
    name    = "ATP_PASSWORD"
  }
  secret_name = "ATP_PASSWORD"
  vault_id    = data.oci_kms_vault.multicloud_vault_query.id
  key_id      = data.oci_kms_key.multicloud_key_query.id
}

resource "oci_vault_secret" "database_ocid" {
  #Required
  compartment_id = var.compartment_ocid
  secret_content {
    #Required
    content_type = "BASE64"

    #Optional
    content = base64encode(oci_database_autonomous_database.autonomous_database.id)
    name    = "ATP_OCID"
  }
  secret_name = "ATP_OCID"
  vault_id    = data.oci_kms_vault.multicloud_vault_query.id
  key_id      = data.oci_kms_key.multicloud_key_query.id
}

resource "oci_vault_secret" "database_username" {
  #Required
  compartment_id = var.compartment_ocid
  secret_content {
    #Required
    content_type = "BASE64"

    #Optional
    content = base64encode("multicloud")
    name    = "ATP_USERNAME"
  }
  secret_name = "ATP_USERNAME"
  vault_id    = data.oci_kms_vault.multicloud_vault_query.id
  key_id      = data.oci_kms_key.multicloud_key_query.id
}

resource "oci_vault_secret" "objectstorage_namespace" {
  #Required
  compartment_id = var.compartment_ocid
  secret_content {
    #Required
    content_type = "BASE64"

    #Optional
    content = base64encode(oci_objectstorage_bucket.multicloud_bucket.namespace)
    name    = "BUCKET_NAMESPACE"
  }
  secret_name = "BUCKET_NAMESPACE"
  vault_id    = data.oci_kms_vault.multicloud_vault_query.id
  key_id      = data.oci_kms_key.multicloud_key_query.id
}

resource "oci_vault_secret" "objectstorage_bucket" {
  #Required
  compartment_id = var.compartment_ocid
  secret_content {
    #Required
    content_type = "BASE64"

    #Optional
    content = base64encode(oci_objectstorage_bucket.multicloud_bucket.name)
    name    = "BUCKET_NAME"
  }
  secret_name = "BUCKET_NAME"
  vault_id    = data.oci_kms_vault.multicloud_vault_query.id
  key_id      = data.oci_kms_key.multicloud_key_query.id
}

resource "oci_vault_secret" "apm_endpoint" {
  #Required
  compartment_id = var.compartment_ocid
  secret_content {
    #Required
    content_type = "BASE64"

    #Optional
    content = base64encode(oci_apm_apm_domain.apm_domain.data_upload_endpoint)
    name    = "APM_ENDPOINT"
  }
  secret_name = "APM_ENDPOINT"
  vault_id    = data.oci_kms_vault.multicloud_vault_query.id
  key_id      = data.oci_kms_key.multicloud_key_query.id
}

resource "oci_vault_secret" "apm_key" {
  #Required
  compartment_id = var.compartment_ocid
  secret_content {
    #Required
    content_type = "BASE64"

    #Optional
    content = base64encode(data.oci_apm_data_keys.apm_keys.data_keys[1].value)
    name    = "APM_KEY"
  }
  secret_name = "APM_KEY"
  vault_id    = data.oci_kms_vault.multicloud_vault_query.id
  key_id      = data.oci_kms_key.multicloud_key_query.id
}