package com.mutzin.droneplatform.infrastructure.aws;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
import java.util.Map;

@Component
public class S3StsClient {

    private final StsClient stsClient;
    private final String roleArn;
    private final String region;
    private final String bucketName;
    private final int durationSeconds;

    public S3StsClient(
            @Value("${aws.access.key-id}") String accessKey,
            @Value("${aws.access.secret-key}") String secretKey,
            @Value("${aws.s3.role-arn}") String roleArn,
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${aws.s3.sts-duration-seconds}") int durationSeconds) {

        this.roleArn = roleArn;
        this.region = region;
        this.bucketName = bucketName;
        this.durationSeconds = durationSeconds;

        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );

        this.stsClient = StsClient.builder()
                .region(software.amazon.awssdk.regions.Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public Map<String, Object> getTemporaryCredentials(String droneId) {
        AssumeRoleRequest roleRequest = AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .roleSessionName("drone-session-" + droneId)
                .durationSeconds(durationSeconds)
                .build();

        AssumeRoleResponse roleResponse = stsClient.assumeRole(roleRequest);
        if (roleResponse == null || roleResponse.credentials() == null) {
            return null;
        }

        Credentials creds = roleResponse.credentials();

        Map<String, Object> response = new HashMap<>();

        // STS
        response.put("accessKeyId", creds.accessKeyId());
        response.put("secretAccessKey", creds.secretAccessKey());
        response.put("sessionToken", creds.sessionToken());
        response.put("expiration", creds.expiration().toString());
        response.put("region", this.region);
        response.put("bucketName", this.bucketName);

        return response;
    }
}