package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.domain.dto.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final SecurityUtil securityUtil;
        private final UserService userService;

        @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
        private long refreshTokenExpiration;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
                        UserService userService) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.userService = userService;
        }

        @PostMapping("/auth/login")
        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
                // Nạp input gồm username/password vào Security
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsername(),
                                loginDTO.getPassword());

                // xác thực người dùng => cần viết hàm loadUserByUsername
                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                // create access_token
                String access_token = this.securityUtil.createAccessToken(authentication, loginDTO);
                // set thông tin người dùng đăng nhập vào context (sử dụng sau này)
                SecurityContextHolder.getContext().setAuthentication(authentication);

                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
                User curUser = this.userService.getUserByUserName(loginDTO.getUsername());
                if (curUser != null) {
                        userLogin.setId(curUser.getId());
                        userLogin.setEmail(curUser.getEmail());
                        userLogin.setName(curUser.getName());
                }

                ResLoginDTO res = new ResLoginDTO();
                res.setAccessToken(access_token);
                res.setUserLogin(userLogin);
                // create refreshToken
                String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);

                // update user
                this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

                // set cookies
                ResponseCookie resCookies = ResponseCookie.from("refresh_token", refresh_token)
                                .httpOnly(true)
                                .secure(true) // using only for https
                                .path("/")
                                .maxAge(refreshTokenExpiration) // expiration
                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                                .body(res);
        }

        @GetMapping("/auth/account")
        @ApiMessage("Get user information")
        public ResponseEntity<ResLoginDTO.UserLogin> getUserAccount() {
                String email = SecurityUtil.getCurrentUserLogin().isPresent()
                                ? SecurityUtil.getCurrentUserLogin().get()
                                : "";

                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
                User curUser = this.userService.getUserByUserName(email);
                if (curUser != null) {
                        userLogin.setId(curUser.getId());
                        userLogin.setEmail(curUser.getEmail());
                        userLogin.setName(curUser.getName());
                }

                return ResponseEntity.ok().body(userLogin);
        }

}
