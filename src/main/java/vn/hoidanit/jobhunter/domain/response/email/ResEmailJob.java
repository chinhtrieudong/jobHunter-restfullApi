package vn.hoidanit.jobhunter.domain.response.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Skill;

import java.util.List;

@Getter
@Setter
public class ResEmailJob {
    private String name;
    private double salary;
    private CompanyEmail company;
    private List<SkillEmail> skills;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CompanyEmail {
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SkillEmail {
        private String name;
    }
}
