package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume) {
        // check id exists
        boolean isExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isExist) {
            throw new IdInvalidException("User id/Job id doesn't exist");
        }

        // create new resume
        return ResponseEntity.ok().body(this.resumeService.createResume(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) {
        // check id exist
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(resume.getId());
        if (reqResumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume with id = " + resume.getId() + "doesn't exist");
        }

        Resume reqResume = reqResumeOptional.get();
        reqResume.setStatus(resume.getStatus());
        return ResponseEntity.ok().body(this.resumeService.updateResume(reqResume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete resume by id")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) {
        Optional<Resume> dbResume = this.resumeService.fetchById(id);
        if (dbResume.isEmpty()) {
            throw new IdInvalidException("Resume with id = " + id + " doesn't exist");
        }

        this.resumeService.deleteResume(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get resume by id")
    public ResponseEntity<ResFetchResumeDTO> getResumeById(@PathVariable("id") long id) {
        Optional<Resume> dbResume = this.resumeService.fetchById(id);
        if (dbResume.isEmpty()) {
            throw new IdInvalidException("Resume with id = " + id + " doesn't exist");
        }

        return ResponseEntity.ok().body(this.resumeService.getResume(dbResume.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Get all resumes")
    public ResponseEntity<ResultPaginationDTO> getAllResumes(
            @Filter Specification<Resume> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.fetchAllResumes(spec, pageable));
    }

}
