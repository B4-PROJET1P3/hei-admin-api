package school.hei.haapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "\"student_transcript_version\"")
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class StudentTranscriptVersion implements Serializable {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "transcript_id")
    private Transcript transcript;

    private Integer ref;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    private User responsible;

    @CreationTimestamp
    private Instant creationDatetime;

    public static final String CREATION_DATETIME = "creationDatetime";
}
