package com.example.shorty.generator;

import java.util.Random;

public class StringGenerator {
    public static String urlCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    public static String passwordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz!@#$%&*()_+-=[]|,./?><{}";

    public static String generateUrl(String urlStart) {
        int stringSize = 7;
        //String urlCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

        StringBuilder randomString = new StringBuilder(stringSize);
        Random random = new Random();

        for (int i = 0; i < stringSize; i++) {
            int randomIndex = random.nextInt(urlCharacters.length());
            char randomChar = urlCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return urlStart + randomString;
    }

    public static String generatePassword() {
        int passwordSize = 10;
        //String passwordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz!@#$%&*()_+-=[]|,./?><{}";

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
