package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    @ApiMessage("fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<Company> spec, Pageable pageable) {

        return ResponseEntity.ok().body(this.companyService.fetchAll(spec, pageable));
    }

    @GetMapping("companies/{id}")
    @ApiMessage("fetch company by id")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable("id") long id) {
        Optional<Company> cOptional = this.companyService.fetchById(id);
        return ResponseEntity.ok().body(cOptional.get());
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> postMethodName(@Valid @RequestBody Company company) {
        return ResponseEntity.ok(this.companyService.handleSaveCompany(company));
    }

    @PutMapping("companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany) {
        return ResponseEntity.ok().body(this.companyService.handleUpdateCompany(reqCompany));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        this.companyService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
