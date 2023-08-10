package school.hei.haapi.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import school.hei.haapi.model.BoundedPageSize;
import school.hei.haapi.model.PageFromOne;
import school.hei.haapi.model.StudentTranscriptClaim;
import school.hei.haapi.model.exception.BadRequestException;
import school.hei.haapi.repository.StudentTranscriptClaimRepository;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@AllArgsConstructor
public class StudentTranscriptClaimService {

  private final StudentTranscriptClaimRepository repository;

  public StudentTranscriptClaim getByIdAndStudentIdAndTranscriptIdAndVersionId(
      String claimId, String versionId, String studentId, String transcriptId) {
    return repository.getByIdAndStudentIdAndTranscriptIdAndVersionId(claimId,
        versionId, transcriptId, studentId);
  }

  public List<StudentTranscriptClaim> getAllByStudentIdAndTranscriptIdAndVersionId(
      String versionId, String studentId, String transcriptId, PageFromOne page,
      BoundedPageSize pageSize) {
    Pageable pageable = PageRequest.of(
        page.getValue() - 1,
        pageSize.getValue(),
        Sort.by(DESC, StudentTranscriptClaim.CREATION_DATETIME));
    return repository.getAllByStudentIdAndTranscriptIdAndVersionId(versionId,
        transcriptId, studentId, pageable);
  }

  public StudentTranscriptClaim save(StudentTranscriptClaim toSave, String claimId,
                                     String versionId,
                                     String transcriptId,
                                     String studentId) {
    if (!toSave.getTranscript().getId().equals(transcriptId) ){
      throw new BadRequestException("Transcript id don't match");
    }

    if (!toSave.getTranscript().getStudent().getId().equals(studentId) ){
      throw new BadRequestException("Student id don't match");
    }

    if (!toSave.getId().equals(claimId)){
      throw new BadRequestException("Claims id don't match");
    }

    if (!toSave.getTranscriptVersion().getId().equals(versionId)){
      throw new BadRequestException("Version id don't match");
    }

    return repository.save(toSave);
  }
}
