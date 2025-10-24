package com.goodee.corpdesk.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String[] usernames = {"admin", "admin001", "admin002", "admin003", "admin004", "admin005"};

        System.out.println("=== BCrypt 해시 생성 결과 ===\n");

        for (String username : usernames) {
            String encodedPassword = encoder.encode(username);
            System.out.println("Username: " + username);
            System.out.println("Encoded Password: " + encodedPassword);
            System.out.println();
        }

        System.out.println("\n=== INSERT 문 ===\n");

        for (String username : usernames) {
            String encodedPassword = encoder.encode(username);
            System.out.println("-- " + username);
            System.out.println("UPDATE employee SET password = '" + encodedPassword + "' WHERE username = '" + username + "';");
            System.out.println();
        }
    }
}
