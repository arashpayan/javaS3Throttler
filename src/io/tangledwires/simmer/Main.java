package io.tangledwires.simmer;

import software.amazon.awssdk.auth.AwsCredentials;
import software.amazon.awssdk.auth.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.StorageClass;
import software.amazon.awssdk.sync.RequestBody;

public class Main {

    public static void main(String[] args) {
        final String filePath = "** PUT THE FILE PATH HERE **";
        final String accessKeyId = "** PUT YOUR AWS ACCESS KEY ID HERE **";
        final String secretKey = "** PUT YOUR AWS SECRET KEY HERE **";
        final String bucket = "** PUT YOUR BUCKET NAME HERE **";
        final String dstKey = "some-key.jpg";
        final int txSpeed = 512 * 1024; // 512KB/second

        try {
            AwsCredentials s3Creds = new AwsCredentials(accessKeyId, secretKey);
            S3Client client = S3Client.builder()
                    .region(Region.US_WEST_1)
                    .credentialsProvider(new AwsCredentialsProvider() {
                        @Override
                        public AwsCredentials getCredentials() {
                            return s3Creds;
                        }
                    }).build();
            PutObjectRequest putObjReq = PutObjectRequest.builder()
                    .storageClass(StorageClass.REDUCED_REDUNDANCY)
                    .key(dstKey)
                    .bucket(bucket)
                    .build();
            ThrottledFileInputStream is = new ThrottledFileInputStream(filePath, txSpeed);
            RequestBody reqBody = RequestBody.of(is, is.available());
            System.out.println("Starting upload…");
            long startMs = System.currentTimeMillis();
            PutObjectResponse response = client.putObject(putObjReq, reqBody);
            long endMs = System.currentTimeMillis();
            System.out.println("Finished uploading!");
            System.out.println("duration: " + (endMs-startMs) + " milliseconds");
            System.out.println("response: " + response.toString());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
