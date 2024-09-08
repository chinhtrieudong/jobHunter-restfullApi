package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission p) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(
                p.getModule(),
                p.getApiPath(),
                p.getMethod());
    }

    public Permission fetchById(long id) {
        Optional<Permission> pOptional = this.permissionRepository.findById(id);
        if (pOptional.isPresent()) {
            return pOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO getAll(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pPage = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pPage.getNumber() + 1);
        meta.setPageSize(pPage.getSize());
        meta.setPages(pPage.getTotalPages());
        meta.setTotal(pPage.getTotalElements());

        res.setMeta(meta);
        res.setResult(pPage.getContent());
        return res;
    }

    public Permission create(Permission p) {
        return this.permissionRepository.save(p);
    }

    public Permission update(Permission p) {
        Permission permissionDB = this.fetchById(p.getId());
        if (permissionDB != null) {
            permissionDB.setName(p.getName());
            permissionDB.setApiPath(p.getApiPath());
            permissionDB.setMethod(p.getMethod());
            permissionDB.setModule(p.getModule());

            // update
            permissionDB = this.permissionRepository.save(p);
            return permissionDB;
        }

        return null;
    }

    public void delete(long id) {
        // delete permission_role
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission curPermission = permissionOptional.get();
        curPermission.getRoles().forEach(role -> role.getPermissions().remove(curPermission));

        // delete permission
        this.permissionRepository.delete(curPermission);
    }

    public boolean isSameName(Permission permission) {
        Permission permissionDB = this.fetchById(permission.getId());
        if (permissionDB != null) {
            if (permissionDB.getName().equals(permission.getName())) {
                return true;
            }
        }
        return false;
    }
}
