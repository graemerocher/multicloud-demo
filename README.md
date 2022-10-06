# Micronaut + GraalVM Multi Cloud Demo

A sample application that demonstrates how to structure an application for Multi Cloud development and deployment.

The `app` subproject contains the application code with no Cloud specific dependencies or configuration.

The `aws` subproject depends on the `app` project and introduces configuration (defined in `aws/src/main/resources/application-ec2.yml`) and dependencies (defined in `aws/build.gradle`) that integrate the application with services of AWS:

* AWS RDS MySQL
* AWS CloudWatch Metrics
* AWS CloudWatch Tracing
* AWS Secrets Manager  
* AWS S3 Object Storage

The `oci` subproject depends on the `app` project and introduces configuration (defined in `oci/src/main/resources/application-oraclecloud.yml`) and dependencies (defined in `oci/build.gradle`) that integrate the application with services of Oracle Cloud:

* Oracle Cloud Autonomous Transaction Processing (ATP)
* Oracle Cloud Application Monitoring (Metrics)
* Oracle Cloud Application Performance Monitoring (Tracing)
* Oracle Cloud Vault (Secrets)
* Oracle Cloud Object Storage

## AWS Deployment

The `aws-infra` subproject builds and deploys `aws` subproject to the AWS. It uses AWS CDK to achieve this.

To run it you have perform the following steps:

* Install [AWS CDK](https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html#getting_started_install).
* Run `cdk bootstrap` inside `aws-infra` subproject.
* Run `cdk deploy` inside `aws-infra` subproject.

After successful build the application URL will be written to the terminal window.

You can destroy the Cloud resources when you are done by running `cdk destroy`.

## Oracle Cloud Deploment

TODO