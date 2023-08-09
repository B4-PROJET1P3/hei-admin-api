package school.hei.haapi.endpoint.rest.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.haapi.endpoint.rest.mapper.StudentTranscriptClaimMapper;
import school.hei.haapi.endpoint.rest.model.StudentTranscriptClaim;
import school.hei.haapi.model.BoundedPageSize;
import school.hei.haapi.model.PageFromOne;
import school.hei.haapi.service.StudentTranscriptClaimService;

import static java.util.stream.Collectors.toUnmodifiableList;

@RestController
@AllArgsConstructor
public class StudentTranscriptClaimController {

  private final StudentTranscriptClaimService service;
  private final StudentTranscriptClaimMapper mapper;

  @GetMapping(value = "/students/{studentId}/transcripts/{transcriptId}/versions/{versionId}/claims")
  public List<StudentTranscriptClaim> getStudentTranscriptClaims(@PathVariable String studentId,
                                                                 @PathVariable String transcriptId,
                                                                 @PathVariable String versionId,
                                                                 @RequestParam PageFromOne page,
                                                                 @RequestParam("page_size")
                                                                 BoundedPageSize pageSize) {
    return service.getAllByStudentIdAndTranscriptIdAndVersionId(
            versionId, studentId, transcriptId, page, pageSize).stream()
        .map(mapper::toRest)
        .collect(toUnmodifiableList());
  }

  @GetMapping(value = "/students/{studentId}/transcripts/{transcriptId}/versions/{versionId}/claims/{claimId}")
  public StudentTranscriptClaim getStudentClaimOfTranscriptVersion(@PathVariable String studentId,
                                                                   @PathVariable
                                                                   String transcriptId,
                                                                   @PathVariable String versionId,
                                                                   @PathVariable String claimId) {
    return mapper.toRest(service.
        getByIdAndStudentIdAndTranscriptIdAndVersionId(
            claimId, versionId, studentId, transcriptId));
  }

  @PutMapping(value = "/students/{studentId}/transcripts/{transcriptId}/versions/{versionId}/claims/{claimId}")
  public StudentTranscriptClaim putStudentClaimsOfTranscriptVersion(
      @RequestBody StudentTranscriptClaim studentTranscriptClaim,
      @PathVariable String studentId,
      @PathVariable String transcriptId,
      @PathVariable String versionId,
      @PathVariable String claimId) {
    school.hei.haapi.model.StudentTranscriptClaim claim =
        mapper.toDomain(studentTranscriptClaim, studentId);
    return mapper.toRest(service.save(
        claim, claimId, versionId, transcriptId, studentId));
  }
}
