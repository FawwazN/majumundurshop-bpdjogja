package com.majumundur.majumundurshop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/greet/{name}")
    public String greet(@PathVariable String name) {
        return "Hello " + name;
    }

    @GetMapping("/user")
    public String getUserInfo(@RequestParam String name) {
        return name;
    }

    @GetMapping("/greets/{name}")
    public String greetUser(@PathVariable String name , @RequestParam String email) {
        return name + " " + email;
    }
}
