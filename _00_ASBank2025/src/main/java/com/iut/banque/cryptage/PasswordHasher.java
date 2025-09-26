package com.iut.banque.cryptage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    /**
     * Hache un mot de passe en utilisant SHA-256
     * @param password Le mot de passe à hacher
     * @return Le hash du mot de passe en hexadécimal
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hashage du mot de passe", e);
        }
    }

    /**
     * Vérifie si un mot de passe correspond au hash stocké
     * @param password Le mot de passe à vérifier
     * @param storedHash Le hash stocké
     * @return true si le mot de passe correspond, false sinon
     */
    public static boolean verifyPassword(String password, String storedHash) {
        String newHash = hashPassword(password);
        return newHash.equals(storedHash);
    }
}