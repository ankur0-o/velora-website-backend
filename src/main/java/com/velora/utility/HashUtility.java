package com.velora.utility;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class HashUtility {

  // Generate a salted hash for the password
  public String hashPassword(String password) throws NoSuchAlgorithmException {
    byte[] salt = generateSalt();
    String saltBase64 = Base64.getEncoder().encodeToString(salt);

    String hash = generateHash(password, salt);
    return saltBase64 + ":" + hash; // Return in the format salt:hash
  }

  // Validate the password against the stored hash
  public boolean validatePassword(String password, String storedHash) throws NoSuchAlgorithmException {
    String[] parts = storedHash.split(":");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid stored hash format");
    }
    String saltBase64 = parts[0];
    String hash = parts[1];

    byte[] salt = Base64.getDecoder().decode(saltBase64);
    String generatedHash = generateHash(password, salt);

    return generatedHash.equals(hash);
  }

  // Generate a hash using SHA-256 and the provided salt
  private String generateHash(String password, byte[] salt) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(salt);
    byte[] hashedBytes = digest.digest(password.getBytes());
    return Base64.getEncoder().encodeToString(hashedBytes);
  }

  // Generate a random salt
  private byte[] generateSalt() {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return salt;
  }
}