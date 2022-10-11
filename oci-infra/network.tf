resource "oci_core_vcn" "multicloud_vcn" {
  dns_label      = "vcn1"
  cidr_block     = var.vcn_cidr_block
  compartment_id = var.compartment_ocid
  display_name   = var.vcn_display_name
}

resource "oci_core_subnet" "multicloud_subnet" {
  vcn_id                     = oci_core_vcn.multicloud_vcn.id
  cidr_block                 = oci_core_vcn.multicloud_vcn.cidr_block
  compartment_id             = var.compartment_ocid
  display_name               = var.subnet_display_name
  route_table_id             = oci_core_route_table.multicloud_route_table.id
  prohibit_public_ip_on_vnic = false
  dns_label                  = "sub1"
  security_list_ids          = [oci_core_security_list.tf_public_security_list.id]
}

resource "oci_core_internet_gateway" "multicloud_ig" {
  #Required
  compartment_id =  var.compartment_ocid
  vcn_id         =  oci_core_vcn.multicloud_vcn.id

  display_name = var.internet_gateway_display_name
}

resource "oci_core_route_table" "multicloud_route_table" {
  #Required
  compartment_id = var.compartment_ocid
  vcn_id         = oci_core_vcn.multicloud_vcn.id

  #Optional
  display_name = var.multicloud_route_table_display_name
  route_rules {
    destination       = "0.0.0.0/0"
    destination_type  = "CIDR_BLOCK"
    network_entity_id = oci_core_internet_gateway.multicloud_ig.id
    #Required
  }
}

resource "oci_core_security_list" "tf_public_security_list"{
  compartment_id = var.compartment_ocid
  vcn_id         = oci_core_vcn.multicloud_vcn.id
  display_name   = "security-list-for-public-subnet"

  egress_security_rules {
    stateless        = false
    destination      = "0.0.0.0/0"
    destination_type = "CIDR_BLOCK"
    protocol         = "all"
  }

  ingress_security_rules {
    stateless   = false
    source      = "0.0.0.0/0"
    source_type = "CIDR_BLOCK"
    protocol    = "6"
    tcp_options {
      min = 22
      max = 22
    }
  }

  # Example of adding ports 1521-1521.
  ingress_security_rules {
    stateless   = false
    source      = "0.0.0.0/0"
    source_type = "CIDR_BLOCK"
    # Get protocol numbers from https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml TCP is 6
    protocol    = "6"
    tcp_options {
      min = 1521
      max = 1521
    }
  }

  ingress_security_rules {
    stateless   = false
    source      = "0.0.0.0/0"
    source_type = "CIDR_BLOCK"
    # Get protocol numbers from https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml TCP is 6
    protocol    = "6"
    tcp_options {
      min = 8080
      max = 8080
    }
  }

}