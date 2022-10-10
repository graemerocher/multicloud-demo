
# Gets a list of Availability Domains
data "oci_identity_availability_domains" "ADs" {
  compartment_id = var.tenancy_ocid
}

data "oci_core_images" "oracle_linux_image" {
  compartment_id           = var.tenancy_ocid
  operating_system         = "Oracle Linux"
  operating_system_version = "7.6"
}

# Choose an Availability Domain
variable "availability_domain" {
  default = 3
}

data "oci_limits_services" "compute_services" {
  compartment_id = var.tenancy_ocid

  filter {
    name   = "name"
    values = ["compute"]
  }
}

### Passwords using random_string instead of random_password to be compatible with ORM (Need to update random provider)
resource "random_string" "autonomous_database_wallet_password" {
  length           = 16
  special          = true
  min_upper        = 3
  min_lower        = 3
  min_numeric      = 3
  min_special      = 3
  override_special = "{}#^*<>[]%~"
}

resource "random_string" "autonomous_database_admin_password" {
  length           = 16
  special          = true
  min_upper        = 3
  min_lower        = 3
  min_numeric      = 3
  min_special      = 3
  override_special = "{}#^*<>[]%~"
}

resource "random_string" "multicloud_db_password" {
  length           = 16
  special          = true
  min_upper        = 3
  min_lower        = 3
  min_numeric      = 3
  min_special      = 3
  override_special = "{}#^*<>[]%~"
}

data "oci_core_images" "compute_images" {
  compartment_id           = var.compartment_ocid
  operating_system         = var.image_operating_system
  operating_system_version = var.image_operating_system_version
  shape                    = "VM.Standard.E2.1.Micro"
  sort_by                  = "TIMECREATED"
  sort_order               = "DESC"
}