package com.iut.banque.cryptage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    private static final int BCRYPT_WORK_FACTOR = 12;

    /**
     * Hache un mot de passe en utilisant BCrypt
     * @param password Le mot de passe à hacher
     * @return Le hash BCrypt du mot de passe
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_WORK_FACTOR));
    }

    /**
     * Vérifie si un mot de passe correspond au hash stocké.
     * Supporte BCrypt (nouveau) et SHA-256 (ancien, pour migration transparente).
     * @param password Le mot de passe à vérifier
     * @param storedHash Le hash stocké
     * @return true si le mot de passe correspond, false sinon
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (storedHash != null && storedHash.startsWith("$2")) {
            return BCrypt.checkpw(password, storedHash);
        }
        return sha256Hash(password).equals(storedHash);
    }

    private static String sha256Hash(String password) {
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
            throw new RuntimeException("Erreur lors du hashage SHA-256", e);
        }
    }
}
