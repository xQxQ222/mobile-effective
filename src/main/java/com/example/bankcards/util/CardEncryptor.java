package com.example.bankcards.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@Converter
public class CardEncryptor implements AttributeConverter<String, String> {
    private final String secretKey;

    private static final String ALGORITHM = "AES";

    public CardEncryptor(@Value("${card.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String encrypt(String cardNumber) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(getKey(), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(cardNumber.getBytes()));
    }

    public String decrypt(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(getKey(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return encrypt(attribute);
        } catch (Exception e) {
            log.error("Error while convert card number");
            return null;
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return decrypt(dbData);
        } catch (Exception e) {
            log.error("Error while convert card number");
            return null;
        }
    }

    private byte[] getKey() {
        return secretKey.getBytes();
    }
}
