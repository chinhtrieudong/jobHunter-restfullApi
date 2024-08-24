package vn.hoidanit.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.turkraft.springfilter.boot.Filter;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a job")
    public ResponseEntity<ResCreateJobDTO> createJob(@RequestBody Job job) {
        return ResponseEntity.ok().body(this.jobService.createJob(job));
    }

    @PutMapping("jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@RequestBody Job job) {
        Optional<Job> jobOptional = this.jobService.fetchJobById(job.getId());
        if (!this.jobService.isExistsById(job.getId())) {
            throw new IdInvalidException("ID does not exist");
        }
        return ResponseEntity.ok().body(this.jobService.updateJob(job, jobOptional.get()));
    }

    @DeleteMapping("jobs/{id}")
    @ApiMessage("Delete job by id")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) {
        Optional<Job> jobOptional = this.jobService.fetchJobById(id);
        if (jobOptional.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        this.jobService.deleteJob(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get a job by id")
    public ResponseEntity<Job> getJob(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (currentJob.isEmpty()) {
            throw new IdInvalidException("Job not found");
        }

        return ResponseEntity.ok().body(currentJob.get());
    }

    @GetMapping("/jobs")
    @ApiMessage("Get all job")
    public ResponseEntity<ResultPaginationDTO> getAllJob(
            @Filter Specification<Job> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.jobService.fetchAllJobs(spec, pageable));
    }
}
