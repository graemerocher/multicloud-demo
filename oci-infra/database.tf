resource "oci_database_autonomous_database" "autonomous_database" {
  admin_password           = random_string.autonomous_database_admin_password.result
  compartment_id           = var.compartment_ocid
  cpu_core_count           = var.autonomous_database_cpu_core_count
  data_storage_size_in_tbs = var.autonomous_database_data_storage_size_in_tbs
  db_name                  = var.autonomous_database_name
  display_name             = var.autonomous_database_name
  whitelisted_ips          = [""]
  is_free_tier             = true
  license_model            = var.autonomous_database_license_model
}

## DB Wallet
resource "oci_database_autonomous_database_wallet" "autonomous_database_wallet" {
  autonomous_database_id = oci_database_autonomous_database.autonomous_database.id
  password               = random_string.autonomous_database_wallet_password.result
  generate_type          = var.autonomous_database_wallet_generate_type
  base64_encode_content  = true
}