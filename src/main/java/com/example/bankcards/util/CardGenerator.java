package com.example.bankcards.util;

import java.security.SecureRandom;
import java.util.Objects;

public class CardGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateCardNumber(String bin, int length) {
        Objects.requireNonNull(bin, "bin must not be null");
        if (!bin.matches("\\d+")) {
            throw new IllegalArgumentException("BIN must contain only digits");
        }
        if (length <= bin.length() + 1) {
            throw new IllegalArgumentException("length must be greater than bin length + 1 (for check digit)");
        }

        StringBuilder sb = new StringBuilder(bin);
        int randomDigits = length - bin.length() - 1;
        for (int i = 0; i < randomDigits; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        int check = calculateLuhnCheckDigit(sb.toString());
        sb.append(check);
        return formatCardNumber(sb.toString());
    }

    private static int calculateLuhnCheckDigit(String numberWithoutCheckDigit) {
        int sum = 0;
        boolean doubleIt = true;
        for (int i = numberWithoutCheckDigit.length() - 1; i >= 0; i--) {
            int digit = numberWithoutCheckDigit.charAt(i) - '0';
            int val = digit;
            if (doubleIt) {
                val = val * 2;
                if (val > 9) val -= 9;
            }
            sum += val;
            doubleIt = !doubleIt;
        }
        int mod = sum % 10;
        return (10 - mod) % 10;
    }

    private static boolean validateLuhn(String cardNumber) {
        String digits = cardNumber.replaceAll("\\D", "");
        int sum = 0;
        boolean doubleIt = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int d = digits.charAt(i) - '0';
            int val = d;
            if (doubleIt) {
                val = val * 2;
                if (val > 9) val -= 9;
            }
            sum += val;
            doubleIt = !doubleIt;
        }
        return sum % 10 == 0;
    }

    public static String formatCardNumber(String digitsOnly) {
        String clean = digitsOnly.replaceAll("\\D", "");
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < clean.length(); i++) {
            if (i > 0 && i % 4 == 0) out.append(' ');
            out.append(clean.charAt(i));
        }
        return out.toString();
    }
}
