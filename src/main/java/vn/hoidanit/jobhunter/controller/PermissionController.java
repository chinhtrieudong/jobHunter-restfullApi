package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;

import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/permissions")
    @ApiMessage("Get all permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(@Filter Specification<Permission> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.permissionService.getAll(spec, pageable));
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) {
        // check permission exist
        if (this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Error: Cannot create new permission. Permission already exists.");
        }
        return ResponseEntity.ok().body(this.permissionService.create(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) {
        // check exist by id
        if (this.permissionService.fetchById(permission.getId()) == null) {
            throw new IdInvalidException("Permission with id = " + permission.getId() + " doesn't exist");
        }

        // check exist by module, api path & method
        if (this.permissionService.isPermissionExist(permission))
            throw new IdInvalidException("Error: Cannot update permission. Permission already exists.");

        // update permission
        return ResponseEntity.ok().body(this.permissionService.update(permission));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") long id) {
        // check exist by id
        if (this.permissionService.fetchById(id) == null) {
            throw new IdInvalidException("Permission with id = " + id + " doesn't exist");
        }

        this.permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }
}
