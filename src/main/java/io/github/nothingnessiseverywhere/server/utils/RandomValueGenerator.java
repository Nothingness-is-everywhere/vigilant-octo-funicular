package io.github.nothingnessiseverywhere.server.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomValueGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomValue(int length) {
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}