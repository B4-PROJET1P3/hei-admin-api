package school.hei.haapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.haapi.service.aws.S3Service;

@Component
@AllArgsConstructor
public class FileService {
    private static final String KEY_PATTERN = "%s/%s/%s";

    public String getKey(String studentId, String transcriptId, String versionId) {
        return String.format(KEY_PATTERN, studentId, transcriptId, versionId);
    }
}