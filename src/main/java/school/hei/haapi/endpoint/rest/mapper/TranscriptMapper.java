package school.hei.haapi.endpoint.rest.mapper;

import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import school.hei.haapi.endpoint.rest.model.Transcript;
import school.hei.haapi.model.User;
import school.hei.haapi.model.exception.BadRequestException;
import school.hei.haapi.model.exception.NotFoundException;
import school.hei.haapi.service.UserService;

import static java.util.stream.Collectors.toUnmodifiableList;

@Component
@AllArgsConstructor
@Slf4j
public class TranscriptMapper {

  private final UserService userService;

  public Transcript toRest(school.hei.haapi.model.Transcript transcript) {
    return new Transcript()
        .id(transcript.getId())
        .studentId(transcript.getStudent().getId())
        .semester(transcript.getSemester())
        .academicYear(transcript.getAcademicYear())
        .isDefinitive(transcript.getIsDefinitive())
        .creationDatetime(transcript.getCreationDatetime());
  }

  public school.hei.haapi.model.Transcript toDomain(Transcript restTranscript, User transcriptStudent) {
    if (!Objects.equals(transcriptStudent.getId(), restTranscript.getStudentId())) {
      throw new BadRequestException(
          "Student.Id = " + restTranscript.getStudentId() + "does not match Path Variable" +
              transcriptStudent.getId());
    }
    return school.hei.haapi.model.Transcript.builder()
        .id(restTranscript.getId())
        .student(transcriptStudent)
        .semester(restTranscript.getSemester())
        .academicYear(restTranscript.getAcademicYear())
        .isDefinitive(restTranscript.getIsDefinitive())
        .creationDatetime(restTranscript.getCreationDatetime())
        .build();
  }

  public List<school.hei.haapi.model.Transcript> toDomain(List<Transcript> restTranscripts,
                                                          String transcriptStudentId) {
    final User student = userService.getById(transcriptStudentId);
    return restTranscripts.stream()
        .map(rest -> toDomain(rest, student))
        .collect(toUnmodifiableList());
  }
}
