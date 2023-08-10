package school.hei.haapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.haapi.service.aws.S3Service;

@Component
@AllArgsConstructor
public class FileService {
    private final S3Service service;
    private static final String KEY_PATTERN = "%s/%s/%s";

    private String getKey(String studentId, String transcriptId, String versionId) {
        return String.format(KEY_PATTERN, studentId, transcriptId, versionId);
    }
    public String uploadPdf(String studentId, String transcriptId, String versionId, byte[] data){
        return service.uploadPdf(getKey(studentId, transcriptId, versionId), data);
    }
    public byte[] downloadPdf(String studentId, String transcriptId, String versionId){
        return service.downloadPdf(getKey(studentId, transcriptId, versionId));
    }
}