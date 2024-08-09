package com.tom.registration_service.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";

    public String generatePassword() {
        SecureRandom random = new SecureRandom();


        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }

        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        for (int i = 0; i < 3; i++) {
            password.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }

        for (int i = 0; i < password.length(); i++) {
            int randomPosition = random.nextInt(password.length());
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(randomPosition));
            password.setCharAt(randomPosition, temp);
        }

        return password.toString();
    }



}



