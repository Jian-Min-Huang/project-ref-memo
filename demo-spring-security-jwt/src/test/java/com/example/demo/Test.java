package com.example.demo;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Test {

    public static void main(String[] args) {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//
//        String encode1 = bCryptPasswordEncoder.encode("!QAZ2wsx");
//        System.out.println(encode1);
//
//        System.out.println(bCryptPasswordEncoder.matches("!QAZ2wsx", encode1));
        String password = "5";
        String hashpw = BCrypt.hashpw(password, BCrypt.gensalt(14));
        System.out.println(hashpw);
        for (int i = 0; i < 10; i++) {
            long l1 = System.currentTimeMillis();
            System.out.print(i + ", " + BCrypt.checkpw(i + "", hashpw));
            long l2 = System.currentTimeMillis();
            System.out.println(", " + (l2-l1));
        }
    }
}
