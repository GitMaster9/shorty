package com.example.shorty.generator;

import java.util.Random;

public class StringGenerator {
    public static String urlCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    public static String passwordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz!@#$%&*()_+-=[]|,./?><{}";

    public static String generateUrl() {
        int stringSize = 7;

        StringBuilder randomString = new StringBuilder(stringSize);
        Random random = new Random();

        for (int i = 0; i < stringSize; i++) {
            int randomIndex = random.nextInt(urlCharacters.length());
            char randomChar = urlCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    public static String generatePassword() {
        int passwordSize = 10;

        StringBuilder randomString = new StringBuilder(passwordSize);
        Random random = new Random();

        for (int i = 0; i < passwordSize; i++) {
            int randomIndex = random.nextInt(passwordCharacters.length());
            char randomChar = passwordCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
}
