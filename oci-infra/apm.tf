resource "oci_apm_apm_domain" "apm_domain" {
  #Required
  compartment_id = var.compartment_ocid
  display_name = var.apm_domain_display_name
}

data "oci_apm_data_keys" "apm_keys" {
  #Required
  apm_domain_id = oci_apm_apm_domain.apm_domain.id

}