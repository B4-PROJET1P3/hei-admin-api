package school.hei.haapi.service.aws;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Getter
public class S3Conf {
    private final String bucketName;

    private final Region region;

    public S3Conf(@Value("${aws.bucket.name}") String bucketName,
                  @Value("${aws.region}") String region) {
        this.region = Region.of(region);
        this.bucketName = bucketName;
    }

    @Bean
    public S3Client getS3Client(){
        return S3Client.builder()
                .region(region)
                .build();
    }
}
