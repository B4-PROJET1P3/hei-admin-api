package school.hei.haapi.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.haapi.endpoint.rest.mapper.TranscriptMapper;
import school.hei.haapi.endpoint.rest.model.Transcript;
import school.hei.haapi.model.BoundedPageSize;
import school.hei.haapi.model.PageFromOne;
import school.hei.haapi.service.TranscriptService;

import static java.util.stream.Collectors.toUnmodifiableList;

@RestController
@AllArgsConstructor
public class TranscriptController {

  private final TranscriptService service;
  private final TranscriptMapper mapper;

  @GetMapping(value = "/students/{studentId}/transcripts/{transcriptId}")
  public Transcript getStudentTranscriptById(@PathVariable String studentId,
                                             @PathVariable String transcriptId) {
    return mapper.toRest(service.getByIdAndStudentId(transcriptId, studentId));
  }

  @GetMapping(value = "/students/{studentId}/transcripts")
  public List<Transcript> getStudentTranscripts(@PathVariable String studentId,
                                                @RequestParam PageFromOne page,
                                                @RequestParam("page_size")
                                                BoundedPageSize pageSize) {
    return service.getAllTranscriptsByStudentId(studentId, page, pageSize).stream()
        .map(mapper::toRest)
        .collect(toUnmodifiableList());
  }

  @PutMapping(value = "/students/{studentId}/transcripts")
  public List<Transcript> crudStudentTranscripts(@PathVariable String studentId,
                                                 @RequestBody List<Transcript> transcripts) {
    var saved = service.saveAll(mapper.toDomain(transcripts, studentId));
    return saved.stream()
        .map(mapper::toRest)
        .collect(toUnmodifiableList());
  }
}
