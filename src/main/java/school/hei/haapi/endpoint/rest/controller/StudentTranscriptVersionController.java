package school.hei.haapi.endpoint.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.haapi.endpoint.rest.mapper.StudentTranscriptVersionMapper;
import school.hei.haapi.endpoint.rest.model.StudentTranscriptVersion;
import school.hei.haapi.model.BoundedPageSize;
import school.hei.haapi.model.PageFromOne;
import school.hei.haapi.service.StudentTranscriptVersionService;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@RestController
@AllArgsConstructor
public class StudentTranscriptVersionController {

    public static final String LATEST = "latest";
    private final StudentTranscriptVersionService service;
    private final StudentTranscriptVersionMapper mapper;

    @GetMapping(value = "/students/{studentId}/transcripts/{transcriptId}/versions/{versionId}")
    public StudentTranscriptVersion getStudentTranscriptVersion(@PathVariable String studentId,
                                                                @PathVariable String transcriptId,
                                                                @PathVariable String versionId) {
        if (versionId.equals("latest")) {
            return mapper.toRest(service.getLatestByStudentIdAndTranscriptId(studentId, transcriptId));
        }
        return mapper.toRest(
                service.getByIdAndStudentIdAndTranscriptId(versionId, transcriptId, studentId));
    }

    @GetMapping(value = "/students/{studentId}/transcripts/{transcriptId}/versions")
    public List<StudentTranscriptVersion> getTranscriptsVersions(
            @PathVariable String studentId,
            @PathVariable String transcriptId,
            @RequestParam PageFromOne page,
            @RequestParam("page_size") BoundedPageSize pageSize) {
        return service.getAllByStudentIdAndTranscriptId(studentId, transcriptId, page, pageSize)
                .stream()
                .map(mapper::toRest)
                .collect(toUnmodifiableList());
    }

    @GetMapping(value = "/students/{studentId}/transcripts/{transcriptId}/versions/{versionId}/raw",
            produces = {APPLICATION_PDF_VALUE})
    public byte[] getStudentTranscriptVersionPdf(
            @PathVariable String studentId,
            @PathVariable String transcriptId,
            @PathVariable String versionId) {
        if (Objects.equals(versionId, LATEST)){
            return service.getStudentTranscriptVersionPdf(studentId, transcriptId);
        }
        return service.getStudentTranscriptVersionPdf(studentId, transcriptId, versionId);
    }

    @PostMapping(value = "/students/{studentId}/transcripts/{transcriptId}/versions/latest/raw",
            consumes = {APPLICATION_PDF_VALUE})
    public StudentTranscriptVersion putStudentTranscriptVersionPdf(
            @PathVariable String studentId,
            @PathVariable String transcriptId,
            @RequestBody byte[] file) {
        return mapper.toRest(service.createLatestVersion(studentId, transcriptId, file));
    }
}