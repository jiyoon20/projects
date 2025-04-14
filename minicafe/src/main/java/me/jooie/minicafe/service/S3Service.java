package me.jooie.minicafe.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class S3Service {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.bucketName}")
    private String bucketName;

    private S3Client s3Client;

    @PostConstruct
    public void init() {

        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }

    public String uploadFile(MultipartFile file, String fileName) {
        try (InputStream inputStream = file.getInputStream()) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, file.getSize()));

            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
        } catch (IOException | S3Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getAllImageUrls(){
        List<String> urlList = new ArrayList<>();
        String continuationToken = null;

        do{
            software.amazon.awssdk.services.s3.model.ListObjectsV2Request.Builder builder =
                    software.amazon.awssdk.services.s3.model.ListObjectsV2Request.builder()
                            .bucket(bucketName).maxKeys(1000);

            if(continuationToken != null)
                builder.continuationToken(continuationToken);

            ListObjectsV2Response result = s3Client.listObjectsV2(builder.build());

            for(S3Object s3Object: result.contents()){
                String url = "https://"+bucketName+".s3."+region+".amazonaws.com/"+s3Object.key();
                urlList.add(url);
            }

            continuationToken = result.nextContinuationToken();
        } while(continuationToken != null);

        return urlList;
    }

    public String getDefaultImageUrl(){
        return "https://"+bucketName+".s3."+region+".amazonaws.com/logo.png";
    }
}
