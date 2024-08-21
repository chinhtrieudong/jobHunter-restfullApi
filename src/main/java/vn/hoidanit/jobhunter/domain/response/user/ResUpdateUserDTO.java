package vn.hoidanit.jobhunter.domain.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updateAt;
    private Company company;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Company {
        private long id;
        private String name;
    }
}
