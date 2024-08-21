package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUserDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User handleCreateUser(User newUser) {
        if (newUser.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.fetchById(newUser.getCompany().getId());
            newUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }

        return this.userRepository.save(newUser);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageUser.getNumber() + 1);
        meta.setPageSize(pageUser.getSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        result.setMeta(meta);
        result.setResult(pageUser.getContent());

        List<ResUserDTO> listUser = pageUser.getContent().stream().map(item -> new ResUserDTO(
                item.getId(),
                item.getEmail(),
                item.getName(),
                item.getGender(),
                item.getAddress(),
                item.getAge(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                new ResUserDTO.Company(
                        item.getCompany() != null ? item.getCompany().getId() : 0,
                        item.getCompany() != null ? item.getCompany().getName() : null)))
                .collect(Collectors.toList());

        result.setResult(listUser);
        return result;
    }

    public User handleUpdateUser(User reqUser) {
        User existingUser = this.fetchUserById(reqUser.getId());

        if (existingUser != null) {
            existingUser.setName(reqUser.getName());
            existingUser.setEmail(reqUser.getEmail());
            existingUser.setPassword(reqUser.getPassword());
            if (reqUser.getCompany() != null) {
                existingUser.setCompany(this.companyService.fetchById(reqUser.getCompany().getId())
                        .orElse(null));
            }
            existingUser = this.userRepository.save(existingUser);
        }
        return existingUser;
    }

    public User getUserByUserName(String email) {
        return this.userRepository.findByEmail(email);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setEmail(user.getEmail());
        res.setCreatedAt(user.getCreatedAt());
        if (user.getCompany() != null) {
            res.setCompany(new ResCreateUserDTO.Company(user.getCompany().getId(), user.getCompany().getName()));
        }
        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        if (user.getCompany() != null) {
            res.setCompany(new ResUserDTO.Company(user.getCompany().getId(), user.getCompany().getName()));
        }
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setUpdateAt(user.getUpdatedAt());
        res.setCompany(new ResUpdateUserDTO.Company(user.getCompany().getId(), user.getCompany().getName()));
        return res;
    }

    public void updateUserToken(String token, String email) {
        User curUser = this.getUserByUserName(email);
        if (curUser != null) {

            curUser.setRefreshToken(token);
            this.userRepository.save(curUser);
        }
    }

    public User fetchUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findUserByRefreshTokenAndEmail(token, email);
    }
}
