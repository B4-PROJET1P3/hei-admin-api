package school.hei.haapi.service.aws;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.haapi.model.exception.ApiException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

import static school.hei.haapi.model.exception.ApiException.ExceptionType.SERVER_EXCEPTION;
import static software.amazon.awssdk.services.s3.model.ChecksumAlgorithm.SHA256;

@Component
@AllArgsConstructor
public class S3Service {
    public static final String PDF_EXTENSION = "pdf";
    private final S3Conf conf;
    private final S3Client client;

    private String uploadPdf(String key, byte[] data) {
        var response = client.putObject(request ->
                        request
                                .bucket(conf.getBucketName())
                                .key(key)
                                .checksumAlgorithm(SHA256)
                                .contentType(PDF_EXTENSION)
                , RequestBody.fromBytes(data)
        );
        return response.checksumSHA256();
    }

    private byte[] downloadPdf(String key) {
        var response = client.getObject(request ->
                request
                        .bucket(conf.getBucketName())
                        .key(key)
        );
        try {
            return response.readAllBytes();
        } catch (IOException e) {
            throw new ApiException(SERVER_EXCEPTION, e);
        }
    }
}
