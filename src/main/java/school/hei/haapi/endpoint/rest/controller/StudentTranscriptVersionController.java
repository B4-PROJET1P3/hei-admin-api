package school.hei.haapi.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.haapi.endpoint.rest.mapper.StudentTranscriptVersionMapper;
import school.hei.haapi.endpoint.rest.model.StudentTranscriptVersion;
import school.hei.haapi.model.BoundedPageSize;
import school.hei.haapi.model.PageFromOne;
import school.hei.haapi.service.StudentTranscriptVersionService;

import static java.util.stream.Collectors.toUnmodifiableList;

@RestController
@AllArgsConstructor
public class StudentTranscriptVersionController {

  private final StudentTranscriptVersionService service;
  private final StudentTranscriptVersionMapper mapper;

  @GetMapping(value = "/students/{studentId}/transcripts/{transcriptId}/versions/{versionId}")
  public StudentTranscriptVersion getStudentTranscriptVersion(@PathVariable String studentId,
                                                              @PathVariable String transcriptId,
                                                              @PathVariable String versionId) {
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
}
