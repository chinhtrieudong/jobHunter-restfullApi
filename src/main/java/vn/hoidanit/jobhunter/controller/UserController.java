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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUserDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.userService.fetchAllUsers(spec, pageable));
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody User newUser) {
        boolean isEmailExist = this.userService.isEmailExist(newUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email " + newUser.getEmail() + " đã tồn tại, vui lòng sử dung email khác");
        }
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        User savedUser = this.userService.handleCreateUser(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResCreateUserDTO(savedUser));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        User curUser = this.userService.fetchUserById(id);
        if (curUser == null) {
            throw new IdInvalidException("Không tồn tại user với id = " + id);
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch user by id")
    public ResponseEntity<ResUserDTO> getUser(@PathVariable("id") long id)
            throws IdInvalidException {
        User curUser = this.userService.fetchUserById(id);
        if (curUser == null) {
            throw new IdInvalidException("Không tồn tại user với id = " + id);
        }
        return ResponseEntity.ok().body(this.userService.convertToResUserDTO(curUser));
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User reqUser) {
        User curUser = this.userService.handleUpdateUser(reqUser);
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(curUser));
    }
}
