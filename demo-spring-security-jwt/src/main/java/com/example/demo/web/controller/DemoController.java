package com.example.demo.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public ResponseEntity<String> find() {
        System.out.println("Hey");
        return ResponseEntity.ok("Demo");
    }

}
