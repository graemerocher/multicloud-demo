variable "tenancy_ocid" {}
variable "region" {}
variable "compartment_ocid" {}

variable "profile" {
  default = "DEFAULT"
}

variable "public_ssh_key" {
  default = ""
}

variable "instance_shape" {
  default = "VM.Standard.A1.Flex"
}

variable "internet_gateway_enabled" {
  default = "true"
}

variable "instance_image_ocid" {
  type = map(string)

  default = {
    // See https://docs.us-phoenix-1.oraclecloud.com/images/
    // Oracle-provided image "Oracle-Linux-7.5-2018.10.16-0"
    us-phoenix-1 = "ocid1.image.oc1.phx.aaaaaaaais7ewyp7hg5piujkphwgnd6sqdqefw7dj4hx45zymolbiss3xlsq"

    us-ashburn-1   = "ocid1.image.oc1.iad.aaaaaaaasob4uyxrt3524odfb7nul2twqrxxaiohvtyfcyluvcbt4iesfgtq"
    eu-frankfurt-1 = "ocid1.image.oc1.eu-frankfurt-1.aaaaaaaavvjjlvfzbr5wljwiknmq3vq7sgvxp4liitkolxave5wkfjd47ruq"
    uk-london-1    = "ocid1.image.oc1.uk-london-1.aaaaaaaavxuer26obsl2wedl56d5i5nok3uav7hdqy7tqq5ioqmtlt2xpqxq"
  }
}

variable "autonomous_database_name" {
  default = "MultiCloud"
}
variable "autonomous_database_db_version" {
  default = "19c"
}
variable "autonomous_database_license_model" {
  default = "LICENSE_INCLUDED"
}
variable "autonomous_database_is_free_tier" {
  default = false
}
variable "autonomous_database_cpu_core_count" {
  default = 1
}
variable "autonomous_database_data_storage_size_in_tbs" {
  default = 1
}
variable "autonomous_database_visibility" {
  default = "Public"
}
variable "autonomous_database_wallet_generate_type" {
  default = "SINGLE"
}
variable "oracle_client_version" {
  default = "19.10"
}

variable "image_operating_system" {
  default = "Oracle Linux"
}
variable "image_operating_system_version" {
  default = "7.9"
}

variable "dynamic_group_name" {
  default = "Test"
}

variable "apm_domain_display_name" {
  default = "MultiCloud APM"
}

variable "app_instance_display_name" {
  default = "MultiCloud App"
}

variable "vnic_display_name" {
  default = "primaryvnic"
}

variable "vcn_cidr_block" {
  default = "10.0.0.0/16"
}

variable "vcn_display_name" {
  default = "MultiCloud VCN"
}

variable "subnet_display_name" {
  default = "MultiCloud Subnet"
}

variable "internet_gateway_display_name" {
  default = "MultiCloud Internet Gateway"
}

variable "multicloud_route_table_display_name" {
  default = "MultiCloud Gateway"
}

variable "bucket_name" {
  default = "MultiCloud_Bucket"
}

variable "oci_kms_key_display_name" {
  default = "MultiCloud key"
}

variable "vault_ocid" {
  default = ""
}

variable "vault_key_ocid" {
  default = ""
}

variable "create_vault" {
  default = true
}

variable "create_vault_key" {
  default = true
}

variable "instance_ocpus" {
  default = 1
}
variable "instance_shape_config_memory_in_gbs" {
  default = 16
}