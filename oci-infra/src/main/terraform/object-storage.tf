data "oci_objectstorage_namespace" "user_namespace" {
  compartment_id = var.compartment_ocid
}

resource "oci_objectstorage_bucket" "multicloud_bucket" {
  compartment_id = var.compartment_ocid
  name           = var.bucket_name
  namespace      = data.oci_objectstorage_namespace.user_namespace.namespace
}

resource "oci_objectstorage_object" "multicloud_media" {
  for_each = fileset("../../../../../app/src/main/resources/images", "**")

  bucket        = oci_objectstorage_bucket.multicloud_bucket.name
  namespace     = oci_objectstorage_bucket.multicloud_bucket.namespace
  object        = each.value
  source        = "../../../../../app/src/main/resources/images/${each.value}"
  cache_control = "max-age=604800, public, no-transform"
}

resource "oci_objectstorage_object" "multicloud_wallet" {
  bucket    = oci_objectstorage_bucket.multicloud_bucket.name
  content   = oci_database_autonomous_database_wallet.autonomous_database_wallet.content
  namespace = data.oci_objectstorage_namespace.user_namespace.namespace
  object    = "multicloud_atp_wallet"
}

resource "oci_objectstorage_preauthrequest" "multicloud_wallet_preauth" {
  access_type  = "ObjectRead"
  bucket       = oci_objectstorage_bucket.multicloud_bucket.name
  name         = "multicloud_wallet_preauth"
  namespace    = data.oci_objectstorage_namespace.user_namespace.namespace
  time_expires = timeadd(timestamp(), "30m")
  object_name  = oci_objectstorage_object.multicloud_wallet.object
}

resource "oci_objectstorage_object" "multicloud_app" {

  bucket        = oci_objectstorage_bucket.multicloud_bucket.name
  namespace     = oci_objectstorage_bucket.multicloud_bucket.namespace
  object        = "app.jar"
  source        = "../../../../../oci/build/libs/oci-0.1-all.jar"
  cache_control = "max-age=604800, public, no-transform"
}

resource "oci_objectstorage_preauthrequest" "multicloud_app_preauth" {
  access_type  = "ObjectRead"
  bucket       = oci_objectstorage_bucket.multicloud_bucket.name
  name         = "multicloud_app_preauth"
  namespace    = data.oci_objectstorage_namespace.user_namespace.namespace
  time_expires = timeadd(timestamp(), "30m")
  object_name  = oci_objectstorage_object.multicloud_app.object
}