package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateResumeDTO {
    private long id;
    private Instant createdAt;
    private String createdBy;
}
