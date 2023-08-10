package school.hei.haapi.endpoint.rest.mapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import school.hei.haapi.endpoint.rest.model.StudentTranscriptClaim;
import school.hei.haapi.model.StudentTranscriptVersion;
import school.hei.haapi.model.Transcript;
import school.hei.haapi.model.exception.BadRequestException;
import school.hei.haapi.service.StudentTranscriptVersionService;
import school.hei.haapi.service.TranscriptService;

import java.time.Instant;

import static school.hei.haapi.endpoint.rest.model.StudentTranscriptClaim.StatusEnum.OPEN;

@Component
@AllArgsConstructor
@Slf4j
public class StudentTranscriptClaimMapper {

  private final TranscriptService transcriptService;
  private final StudentTranscriptVersionService studentTranscriptVersionService;

  public StudentTranscriptClaim toRest(
          school.hei.haapi.model.StudentTranscriptClaim studentTranscriptClaim) {
    return new StudentTranscriptClaim()
        .id(studentTranscriptClaim.getId())
        .transcriptId(studentTranscriptClaim.getTranscript().getId())
        .transcriptVersionId(studentTranscriptClaim.getTranscriptVersion().getId())
        .status(studentTranscriptClaim.getStatus())
        .creationDatetime(studentTranscriptClaim.getCreationDatetime())
        .closedDatetime(studentTranscriptClaim.getClosedDatetime())
        .reason(studentTranscriptClaim.getReason());
  }

  public school.hei.haapi.model.StudentTranscriptClaim toDomain(
      StudentTranscriptClaim restStudentTranscriptClaim,
      String transcriptVersionStudentId) {
    final Transcript transcript =
        transcriptService.getByIdAndStudentId(restStudentTranscriptClaim.getTranscriptId(),
            transcriptVersionStudentId);
    if (transcript.getIsDefinitive() && restStudentTranscriptClaim.getClosedDatetime() == null && restStudentTranscriptClaim.getStatus() == OPEN){
      throw new BadRequestException("Transcript.Id = "+ transcript.getId()+" is already definitive");
    }
    final StudentTranscriptVersion version =
        studentTranscriptVersionService.getByIdAndStudentIdAndTranscriptId(
            restStudentTranscriptClaim.getTranscriptVersionId(),
            transcript.getId(),
            transcriptVersionStudentId
        );
    Instant now = Instant.now();
    if (restStudentTranscriptClaim.getCreationDatetime() != null && now.isBefore(restStudentTranscriptClaim.getCreationDatetime())){
      throw new BadRequestException("Creation datetime must be null or before current time "+now);
    }
    if (restStudentTranscriptClaim.getClosedDatetime() != null) {
      if (restStudentTranscriptClaim.getCreationDatetime().isAfter(restStudentTranscriptClaim.getClosedDatetime())) {
        throw new BadRequestException("Closed datetime must be null or less than creation datetime "+ restStudentTranscriptClaim.getCreationDatetime());
      }
    }
    return school.hei.haapi.model.StudentTranscriptClaim.builder()
        .id(restStudentTranscriptClaim.getId())
        .transcript(transcript)
        .transcriptVersion(version)
        .status(restStudentTranscriptClaim.getStatus())
        .creationDatetime(restStudentTranscriptClaim.getCreationDatetime())
        .closedDatetime(restStudentTranscriptClaim.getClosedDatetime())
        .reason(restStudentTranscriptClaim.getReason())
        .build();
  }
}
