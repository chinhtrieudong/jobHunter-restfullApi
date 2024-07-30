package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public boolean isExistsById(long id) {
        return this.jobRepository.existsById(id);
    }

    public ResCreateJobDTO createJob(Job job) {
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(j -> j.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }
        // create job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(j -> j.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }

        return dto;
    }

    public ResUpdateJobDTO updateJob(Job job) {
        Optional<Job> jobOptional = this.jobRepository.findById(job.getId());
        ResUpdateJobDTO resDto = new ResUpdateJobDTO();
        if (jobOptional.isPresent()) {
            Job dbJob = jobOptional.get();
            if (job.getSkills() != null) {
                List<Long> skillIds = job.getSkills()
                        .stream().map(j -> j.getId())
                        .collect(Collectors.toList());
                List<Skill> skills = this.skillRepository.findByIdIn(skillIds);
                job.setSkills(skills);
            }
            // create job
            Job currentJob = this.jobRepository.save(dbJob);

            // convert response

            resDto.setId(currentJob.getId());
            resDto.setName(currentJob.getName());
            resDto.setSalary(currentJob.getSalary());
            resDto.setQuantity(currentJob.getQuantity());
            resDto.setLocation(currentJob.getLocation());
            resDto.setLevel(currentJob.getLevel());
            resDto.setStartDate(currentJob.getStartDate());
            resDto.setEndDate(currentJob.getEndDate());
            resDto.setActive(currentJob.isActive());
            resDto.setUpdatedAt(currentJob.getUpdatedAt());
            resDto.setUpdatedBy(currentJob.getUpdatedBy());

            if (currentJob.getSkills() != null) {
                List<String> skills = currentJob.getSkills()
                        .stream().map(j -> j.getName())
                        .collect(Collectors.toList());
                resDto.setSkills(skills);
            }
        }

        return resDto;
    }

    public void deleteJob(long id) {
        this.jobRepository.deleteById(id);
    }

    public Optional<Job> fetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    public ResultPaginationDTO fetchAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageJob.getNumber() + 1);
        meta.setPageSize(pageJob.getSize());
        meta.setPages(pageJob.getTotalPages());
        meta.setTotal(pageJob.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageJob.getContent());
        return rs;
    }
}
