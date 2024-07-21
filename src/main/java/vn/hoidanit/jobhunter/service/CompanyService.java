package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
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

    public List<Company> fetchAllCompanies() {
        return this.companyRepository.findAll();
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
