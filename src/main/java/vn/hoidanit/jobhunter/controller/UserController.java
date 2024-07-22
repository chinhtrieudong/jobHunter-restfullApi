package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.userService.fetchAllUser(spec, pageable));
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User newUser) {

        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        User user = this.userService.handleCreateUser(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id > 100) {
            throw new IdInvalidException("error !!");
        }
        User curUser = this.userService.fetchUserById(id);
        return ResponseEntity.ok().body(curUser);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User reqUser) {
        User user = this.userService.handleUpdateUser(reqUser);
        return ResponseEntity.ok(user);
    }
}
