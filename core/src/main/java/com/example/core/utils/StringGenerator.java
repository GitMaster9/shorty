package com.example.core.utils;

import java.util.Random;

public class StringGenerator {
    public static final String urlCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    public static final String passwordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz!@#$%&*()_+-=[]|,./?><{}";
    public static final int urlStringSize = 7;
    public static final int passwordStringSize = 10;

    public static String generateRandomString(StringGeneratorType stringGeneratorType) {
        int stringSize = 0;
        String charactersToUse = "";

        switch (stringGeneratorType) {
            case PASSWORD -> {
                stringSize = passwordStringSize;
                charactersToUse = passwordCharacters;
            }
            case URL -> {
                stringSize = urlStringSize;
                charactersToUse = urlCharacters;
            }
        }

        final StringBuilder randomString = new StringBuilder(stringSize);
        final Random random = new Random();

        for (int i = 0; i < stringSize; i++) {
            int randomIndex = random.nextInt(charactersToUse.length());
            char randomChar = charactersToUse.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
}
