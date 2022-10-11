import org.gradle.internal.impldep.org.eclipse.jgit.lib.ObjectChecker.type

plugins {
    id("org.jetbrains.gradle.terraform") version "1.4.2"
}

val configFile: File = file(System.getProperty("user.home") + "/.oci/config")

var profile = "DEFAULT"

if (project.hasProperty("profile")) {
    profile = project.property("profile").toString()
}

var tenancyId = ""
var region = ""
var parsing = false

configFile.forEachLine {
    if (it.startsWith("[") && it.endsWith("]")) {
        parsing = false
    }
    if (it == "[$profile]") {
        parsing = true
    }
    if (parsing && it.contains("tenancy=")) {
        tenancyId = it.replace("tenancy=", "")
    }
    if (parsing && it.contains("region=")) {
        region = it.replace("region=", "")
    }
}

terraform {

    version = "1.3.2"
    configFile

    sourceSets {

        main {
            // apply and destroy are dangerous! put some checks on who can execute them!
            executeApplyOnlyIf { System.getenv("ENABLE_TF_APPLY") == "true" }
            executeDestroyOnlyIf { System.getenv("ENABLE_TF_DESTROY") == "true" }

            // variables in your sources without a default can be filled here
            planVariables = mapOf(
                "region" to region,
                "profile" to profile,
                "compartment_ocid" to tenancyId,
                "tenancy_ocid" to tenancyId
            )
        }
    }
}

afterEvaluate {
    tasks.getByName("terraformApply") {
        dependsOn(":oci:shadowJar")
    }
}