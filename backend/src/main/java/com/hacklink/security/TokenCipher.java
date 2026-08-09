package com.hacklink.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class TokenCipher {
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;
    private final byte[] key;
    private final SecureRandom secureRandom = new SecureRandom();

    public TokenCipher(@Value("${hacklink.oauth-encryption-key:development-only-oauth-key-32-chars!}") String configuredKey) {
        byte[] raw = configuredKey.getBytes(StandardCharsets.UTF_8);
        if (raw.length < 32) {
            throw new IllegalArgumentException("OAuth encryption key must contain at least 32 bytes");
        }
        this.key = java.util.Arrays.copyOf(raw, 32);
    }

    public String encrypt(String plaintext) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to encrypt integration token", exception);
        }
    }

    public String decrypt(String value) {
        try {
            byte[] combined = Base64.getDecoder().decode(value);
            byte[] iv = java.util.Arrays.copyOfRange(combined, 0, IV_LENGTH);
            byte[] ciphertext = java.util.Arrays.copyOfRange(combined, IV_LENGTH, combined.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(TAG_LENGTH, iv));
            return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to decrypt integration token", exception);
        }
    }
}
