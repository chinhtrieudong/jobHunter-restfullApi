package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role fetchById(long id) {
        Optional<Role> rOptional = this.roleRepository.findById(id);
        if (rOptional.isPresent()) {
            return rOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> rPage = this.roleRepository.findAll(spec, pageable);

        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(rPage.getNumber() + 1);
        meta.setPageSize(rPage.getSize());
        meta.setPages(rPage.getTotalPages());
        meta.setTotal(rPage.getNumberOfElements());

        res.setMeta(meta);
        res.setResult(rPage.getContent());
        return res;
    }

    public Role create(Role r) {
        // check permissions
        if (r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions()
                    .stream().map(X -> X.getId())
                    .collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            r.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(r);
    }

    public Role update(Role r) {
        Role dbRole = this.fetchById(r.getId());
        // check permissions
        if (r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions()
                    .stream().map(X -> X.getId())
                    .collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            r.setPermissions(dbPermissions);
        }

        dbRole.setActive(r.isActive());
        dbRole.setName(r.getName());
        dbRole.setDescription(r.getDescription());
        dbRole.setPermissions(r.getPermissions());
        dbRole = this.roleRepository.save(dbRole);
        return dbRole;
    }

    public void delete(long id) {
        this.roleRepository.deleteById(id);
    }
}
