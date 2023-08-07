package school.hei.haapi.endpoint.rest.mapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import school.hei.haapi.endpoint.rest.model.StudentTranscriptVersion;

@Component
@AllArgsConstructor
@Slf4j
public class StudentTranscriptVersionMapper {
  public StudentTranscriptVersion toRest(
      school.hei.haapi.model.StudentTranscriptVersion studentTranscriptVersion) {
    return new StudentTranscriptVersion()
        .id(studentTranscriptVersion.getId())
        .transcriptId(studentTranscriptVersion.getTranscript().getId())
        .ref(studentTranscriptVersion.getRef())
        .createdByUserId(studentTranscriptVersion.getResponsible().getId())
        .createdByUserRole(studentTranscriptVersion.getResponsible().getRole().name())
        .creationDatetime(studentTranscriptVersion.getCreationDatetime());
  }
}
