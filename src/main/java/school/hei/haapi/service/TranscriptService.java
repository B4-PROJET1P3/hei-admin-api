package school.hei.haapi.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.hei.haapi.model.BoundedPageSize;
import school.hei.haapi.model.PageFromOne;
import school.hei.haapi.model.Transcript;
import school.hei.haapi.model.exception.NotFoundException;
import school.hei.haapi.repository.TranscriptRepository;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@AllArgsConstructor
public class TranscriptService {

  private final TranscriptRepository transcriptRepository;

  public Transcript getByIdAndStudentId(String transcriptId, String studentId) {
    return transcriptRepository.getTranscriptByIdAndStudentId(transcriptId, studentId)
        .orElseThrow(() -> new NotFoundException(
            "Transcript.Id = " + transcriptId + "for Student.Id=" + studentId + " not found"));
  }

  public List<Transcript> getAllTranscriptsByStudentId(String studentId, PageFromOne page,
                                                       BoundedPageSize pageSize) {
    Pageable pageable = PageRequest.of(
        page.getValue() - 1,
        pageSize.getValue(),
        Sort.by(DESC, Transcript.CREATION_DATETIME_ATTRIBUTE));
    return transcriptRepository.getAllTranscriptsByStudentId(studentId, pageable);
  }

  @Transactional
  public List<Transcript> saveAll(List<Transcript> transcripts) {
    return transcriptRepository.saveAll(transcripts);
  }
}
