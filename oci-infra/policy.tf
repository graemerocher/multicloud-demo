resource "oci_identity_policy" "multicloud_basic_policies" {
  name           = "multicloud-basic-policies"
  description    = "Policies created by terraform for MultiCloud Project"
  compartment_id = var.compartment_ocid
  statements     = local.multicloud_basic_policies_statement
}

resource "oci_identity_dynamic_group" "multicloud_dynamic_group" {
  #Required
  compartment_id = var.tenancy_ocid
  description = "Dynamic group created by terraform for MultiCloud Project"
  matching_rule = "ALL {instance.compartment.id = '${var.compartment_ocid}'}"
  name = var.dynamic_group_name
}

locals {
  multicloud_basic_policies_statement = concat(
    local.allow_group_manage_vault_keys_statements,
  )
}

locals {
  allow_group_manage_vault_keys_statements = [
    "allow dynamic-group ${var.dynamic_group_name} to read secret-family in compartment id ${var.compartment_ocid}",
    "Allow dynamic-group ${var.dynamic_group_name} to manage objects in compartment id ${var.compartment_ocid}",
    "Allow dynamic-group ${var.dynamic_group_name} to use metrics in compartment id ${var.compartment_ocid}"
  ]
}