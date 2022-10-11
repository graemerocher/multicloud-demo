output "app_id" {
  description = "Public ip"
  value       = format("http://%s:8080", oci_core_instance.app_instance.public_ip)
}