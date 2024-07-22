package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleSaveCompany(Company company) {
        return companyRepository.save(company);
    }

    public ResultPaginationDTO fetchAll(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);

        Meta meta = new Meta();
        ResultPaginationDTO rs = new ResultPaginationDTO();

        meta.setPage(pageCompany.getNumber() + 1);
        meta.setPageSize(pageCompany.getSize());
        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageCompany.getContent());
        return rs;
    }

    public Optional<Company> fetchById(long id) {
        return this.companyRepository.findById(id);
    }

    public void deleteById(long id) {
        this.companyRepository.deleteById(id);
    }

    public Company handleUpdateCompany(Company reqCompany) {
        Optional<Company> companyOptional = this.companyRepository.findById(reqCompany.getId());
        if (companyOptional.isPresent()) {
            Company curCompany = companyOptional.get();
            curCompany.setName(reqCompany.getName());
            curCompany.setAddress(reqCompany.getAddress());
            curCompany.setDescription(reqCompany.getDescription());
            curCompany.setLogo(reqCompany.getLogo());

            return this.companyRepository.save(curCompany);
        }
        return null;
    }
}
