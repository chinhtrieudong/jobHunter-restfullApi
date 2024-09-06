package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    @ApiMessage("Get all roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.roleService.getRoles(spec, pageable));
    }

    @PostMapping("/roles")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        // check name
        if (this.roleService.existByName(role.getName())) {
            throw new IdInvalidException("Role with name = " + role.getName() + " already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) {
        // check id
        if (this.roleService.fetchById(role.getId()) == null)
            throw new IdInvalidException("Role with id = " + role.getId() + " doesn't exists");

        // check name
        // if (this.roleService.existByName(role.getName()))
        // throw new IdInvalidException("Role with name = " + role.getName() + " already
        // exists");

        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.update(role));
    }

    @DeleteMapping("roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") long id) {
        // check id
        if (this.roleService.fetchById(id) == null)
            throw new IdInvalidException("Role with id = " + id + " doesn't exists");

        this.roleService.delete(id);
        return ResponseEntity.ok().body(null);
    }
}
