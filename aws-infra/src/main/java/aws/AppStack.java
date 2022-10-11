package aws;


import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.CloudFormationInit;
import software.amazon.awscdk.services.ec2.InitFile;
import software.amazon.awscdk.services.ec2.Instance;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.MachineImage;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.rds.Credentials;
import software.amazon.awscdk.services.rds.CredentialsBaseOptions;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine;
import software.amazon.awscdk.services.rds.MySqlInstanceEngineProps;
import software.amazon.awscdk.services.rds.MysqlEngineVersion;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.amazon.awscdk.services.secretsmanager.Secret;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;


public class AppStack extends Stack {

    // namespace for automatically added database credentials
    private static final String SECRET_NAME = "/config/application/";
    // namespace for custom added database credentials
    private static final String SECRET_NAME_2 = "/config/application_ec2/";
    public AppStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public AppStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        // create bucket
        Bucket bucket = Bucket.Builder.create(this, "Micronaut bucket")
                .versioned(false)
                .publicReadAccess(false)
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .removalPolicy(RemovalPolicy.DESTROY)
                .autoDeleteObjects(true)
                .build();

        // add images and jar to it
        BucketDeployment deploy_assets = BucketDeployment.Builder.create(this, "Deploy assets")
                .sources(List.of(
                        Source.asset("../app/src/main/resources/images"),
                        Source.asset("../aws/build/libs/"))
                )
                .destinationBucket(bucket)
                .build();

        // crete vpc
        Vpc vpc = Vpc.Builder.create(this, "TheVPC")
                // disable NAT Gateways for free tier. It is paid service.
                .natGateways(0)
                .subnetConfiguration(
                        List.of(SubnetConfiguration.builder()
                                .cidrMask(24).name("ingress")
                                .subnetType(SubnetType.PUBLIC).build())
                )
                .maxAzs(2).build();

        // create security group for RDS
        SecurityGroup securityGroupRds = SecurityGroup.Builder.create(this, "RDS ingress").vpc(vpc)
                .securityGroupName("ingress_rds")
                .allowAllOutbound(true)
                .build();

        securityGroupRds.addIngressRule(Peer.anyIpv4(), Port.tcp(3306), "Allow connections from outside");

        // create security group for EC2
        SecurityGroup securityGroupEC2 = SecurityGroup.Builder.create(this, "EC2 ingress").vpc(vpc)
                .securityGroupName("ingress_ec2")
                .allowAllOutbound(true)
                .build();
        securityGroupEC2.addIngressRule(Peer.anyIpv4(), Port.tcp(8080), "allow connections to micronaut app");

        // uncomment this if you want to debug stuff on instance
        //securityGroupEC2.addIngressRule(Peer.anyIpv4(), Port.tcp(22), "allow ssh");


        // create RDS mysql database (connections from internet are allowed)
        DatabaseInstance dbInstance = DatabaseInstance.Builder.create(this, "RDS Database")
                .databaseName("micronaut")
                .engine(
                        DatabaseInstanceEngine.mysql(MySqlInstanceEngineProps.builder()
                                .version(MysqlEngineVersion.VER_8_0_28).build())
                )
                .credentials(Credentials.fromGeneratedSecret("cloudworld", CredentialsBaseOptions.builder()
                        .secretName(SECRET_NAME)
                        .build()))
                .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PUBLIC).build())
                // take T2 Micro instance
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                .removalPolicy(RemovalPolicy.DESTROY)
                        .vpc(vpc)
                        .securityGroups(List.of(securityGroupRds))
                        .build();

        // Create secret where the bucket name will be stored
        Secret bucketNameSecret = Secret.Builder.create(this, "Store bucket name into secrets")
                .secretName(SECRET_NAME_2)
                .removalPolicy(RemovalPolicy.DESTROY)
                .secretObjectValue(
                        Map.of(
                                "bucketName",
                                SecretValue.Builder.create(bucket.getBucketName()).build()
                        )
                ).build();


        // create ec2 instance
        Role ecRole = Role.Builder.create(this, "ecRole")
                .assumedBy(new ServicePrincipal("ec2.amazonaws.com"))
                .build();

        // add privileges to ec2 instance
        ecRole.addToPolicy(PolicyStatement.Builder.create()
                        .effect(Effect.ALLOW)
                        .resources(List.of("*"))
                        .actions(List.of(
                                "secretsmanager:*",
                                "s3:*",
                                "cloudwatch:*",
                                "xray:*"
                        ))
                .build());

        // grant privileges
        bucketNameSecret.grantRead(ecRole);
        bucket.grantReadWrite(ecRole);
        dbInstance.getSecret().grantRead(ecRole);

        Instance ecInstance = Instance.Builder.create(this, "EC2 application instance")
                // create T2 Micro instance
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                // Initialise it from s3 jar from previous step, can be changed to nativeImage
                .init(CloudFormationInit.fromElements(
                        InitFile.fromS3Object("/var/www/aws.jar", deploy_assets.getDeployedBucket(), "aws-0.1-all.jar")
                        )
                )
                .allowAllOutbound(true)
                .role(ecRole)
                .machineImage(MachineImage.latestAmazonLinux())
                .securityGroup(securityGroupEC2)
                .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PUBLIC).build())
                // Create dependency graph to database instance. We want to initialise database instance first then we want to start Micronaut application when database is ready.
                .vpc(dbInstance.getVpc()).build();

        ecInstance.addUserData(
                // download and install oracle jdk 17
                "wget -q --no-check-certificate -c --header \"Cookie: oraclelicense=accept-securebackup-cookie\" https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.rpm  && sudo rpm -Uvh jdk-17_linux-x64_bin.rpm",
                // download and install OTEL (Open Telemetry) Collector
                "wget -q --no-check-certificate -c --header \"Cookie: oraclelicense=accept-securebackup-cookie\" https://aws-otel-collector.s3.amazonaws.com/amazon_linux/amd64/latest/aws-otel-collector.rpm  && sudo rpm -Uvh aws-otel-collector.rpm",
                // Start OTEL service
                "sudo /opt/aws/aws-otel-collector/bin/aws-otel-collector-ctl -a start",
                // Start micronaut application
                "sudo nohup java -jar /var/www/aws.jar &"
        );

        // Output micronaut app address
        CfnOutput.Builder.create(this, "output")
                .value("http://" + ecInstance.getInstancePublicIp() + ":8080")
                .build();

    }
}