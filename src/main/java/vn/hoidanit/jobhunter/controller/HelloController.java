package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.error.IdInvalidException;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getHelloWorld() {
        if (true)
            throw new IdInvalidException("CHECK");
        return "Hello World (Hỏi Dân IT & Eric)";
    }
}
