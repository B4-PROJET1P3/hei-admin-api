package school.hei.haapi.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.haapi.model.Transcript;

import java.util.List;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, String> {
    List<Transcript> getByStudentId(String studentId, Pageable pageable);

    Transcript getByStudentIdAndId(String studentId, String transcriptId);
}
