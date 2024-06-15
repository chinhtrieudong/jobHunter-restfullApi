package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User newUser) {
        User user = this.userService.handleCreateUser(newUser);
        return user;
    }

    @DeleteMapping("/user/{id}")
    public long deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return id;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") long id) {
        User curUser = this.userService.fetchUserById(id);
        return curUser;
    }

    @GetMapping("/user")
    public List<User> getAllUser() {
        List<User> users = this.userService.fetchAllUser();
        return users;
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User reqUser) {
        User user = this.userService.handleUpdateUser(reqUser);
        return user;
    }
}
