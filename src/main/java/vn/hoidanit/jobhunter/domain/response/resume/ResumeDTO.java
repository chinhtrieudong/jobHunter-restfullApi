package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.ResumeStatusEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDTO {
    private long id;
    private String email;
    private String url;
    private ResumeStatusEnum status;

    private Instant createdAt;
    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;

    private UserDTO user;
    private JobDTO job;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserDTO {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class JobDTO {
        private long id;
        private String name;
    }
}
