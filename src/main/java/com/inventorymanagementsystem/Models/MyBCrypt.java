package com.inventorymanagementsystem.Models;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;


public interface MyBCrypt {
    // Generate a random salt
    static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return BCrypt.gensalt(12, random); // 12 is the workload factor (default is 10)
    }

    // Hash a password using BCrypt with a random salt
    static String hashPassword(String password) {
        String salt = generateSalt();
        return BCrypt.hashpw(password, salt);
    }

    static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    static boolean isPasswordEqual(String plaintext, String hashPassword) {
        return BCrypt.checkpw(plaintext, hashPassword);
    }
}