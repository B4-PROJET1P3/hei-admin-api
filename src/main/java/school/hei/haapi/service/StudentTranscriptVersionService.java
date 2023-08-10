package school.hei.haapi.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import school.hei.haapi.endpoint.rest.security.AuthProvider;
import school.hei.haapi.model.BoundedPageSize;
import school.hei.haapi.model.PageFromOne;
import school.hei.haapi.model.StudentTranscriptVersion;
import school.hei.haapi.model.Transcript;
import school.hei.haapi.model.User;
import school.hei.haapi.model.exception.NotFoundException;
import school.hei.haapi.repository.StudentTranscriptVersionRepository;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@AllArgsConstructor
public class StudentTranscriptVersionService {

    private final StudentTranscriptVersionRepository repository;
    private final FileService fileService;
    private final TranscriptService transcriptService;
    private final UserService userService;

    public StudentTranscriptVersion getByIdAndStudentIdAndTranscriptId(
            String versionId, String transcriptId, String studentId) {
        return
                repository.getByIdAndStudentIdAndTranscriptId(versionId,
                                transcriptId, studentId)
                        .orElseThrow(() -> new NotFoundException("Version.Id=" + versionId + " not found."));
    }

    public StudentTranscriptVersion getLatestByStudentIdAndTranscriptId(String studentId, String transcriptId) {
        return repository.getLatestByStudentIdAndTranscriptId(
                studentId, transcriptId, PageRequest.of(0, 1)
        ).get(0);
    }

    public List<StudentTranscriptVersion> getAllByStudentIdAndTranscriptId(
            String studentId, String transcriptId, PageFromOne page, BoundedPageSize pageSize) {
        Pageable pageable = PageRequest.of(
                page.getValue() - 1,
                pageSize.getValue(),
                Sort.by(DESC, StudentTranscriptVersion.CREATION_DATETIME));
        return repository.getAllByStudentIdAndTranscriptId(studentId,
                transcriptId, pageable);
    }

    public byte[] getStudentTranscriptVersionPdf(
            String studentId,
            String transcriptId) {
        var latest = getLatestByStudentIdAndTranscriptId(studentId, transcriptId);
        return fileService.downloadPdf(studentId, transcriptId, latest.getId());
    }

    public byte[] getStudentTranscriptVersionPdf(
            String studentId,
            String transcriptId,
            String versionId) {
        return fileService.downloadPdf(studentId, transcriptId, versionId);
    }

    public StudentTranscriptVersion createLatestVersion(
            String studentId,
            String transcriptId,
            byte[] file) {
        Transcript transcript = transcriptService.getByIdAndStudentId(transcriptId, studentId);
        User responsible = userService.getById(AuthProvider.getPrincipal().getUserId());

        List<StudentTranscriptVersion> versions = repository
                .getAllByCreationDatetimeDesc(studentId, transcriptId);

        int ref = versions.isEmpty() ? 1 : versions.get(0).getRef() + 1;

        StudentTranscriptVersion toCreate = StudentTranscriptVersion.builder()
                .id(randomUUID().toString())
                .transcript(transcript)
                .responsible(responsible)
                .ref(ref)
                .build();

        StudentTranscriptVersion saved = repository.save(toCreate);

        fileService.uploadPdf(
                studentId, transcriptId, saved.getId(), file);

        return saved;
    }
}
