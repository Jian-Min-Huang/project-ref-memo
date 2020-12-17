package com.example.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("!QAZ2wsx");
        System.out.println(encode);
    }
}
