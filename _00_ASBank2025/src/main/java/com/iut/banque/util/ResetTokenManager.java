package com.iut.banque.util;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class ResetTokenManager {

    private static final Map<String, TokenInfo> tokens = new HashMap<String, TokenInfo>();
    private static final long TOKEN_VALIDITY_DURATION = 3600000; // 1 heure en millisecondes

    private static class TokenInfo {
        String userId;
        long expirationTime;

        TokenInfo(String userId, long expirationTime) {
            this.userId = userId;
            this.expirationTime = expirationTime;
        }
    }

    /**
     * Génère un token de réinitialisation unique
     * @param userId L'identifiant de l'utilisateur
     * @return Le token généré
     */
    public static String generateToken(String userId) {
        // Nettoyer les tokens expirés
        cleanExpiredTokens();

        // Générer un token aléatoire sécurisé simple
        SecureRandom random = new SecureRandom();
        long tokenValue = random.nextLong();
        if (tokenValue < 0) {
            tokenValue = -tokenValue;
        }
        String token = Long.toString(tokenValue, 36).toUpperCase();

        // Stocker le token avec sa date d'expiration
        long expirationTime = System.currentTimeMillis() + TOKEN_VALIDITY_DURATION;
        tokens.put(token, new TokenInfo(userId, expirationTime));

        System.out.println("Token généré pour l'utilisateur " + userId + ": " + token);
        return token;
    }

    /**
     * Vérifie si un token est valide et retourne l'userId associé
     * @param token Le token à vérifier
     * @return L'userId si le token est valide, null sinon
     */
    public static String validateToken(String token) {
        TokenInfo info = tokens.get(token);

        if (info == null) {
            System.out.println("Token non trouvé : " + token);
            return null;
        }

        if (System.currentTimeMillis() > info.expirationTime) {
            System.out.println("Token expiré pour l'utilisateur : " + info.userId);
            tokens.remove(token);
            return null;
        }

        System.out.println("Token valide pour l'utilisateur : " + info.userId);
        return info.userId;
    }

    /**
     * Invalide un token après utilisation
     * @param token Le token à invalider
     */
    public static void invalidateToken(String token) {
        tokens.remove(token);
        System.out.println("Token invalidé : " + token);
    }

    /**
     * Nettoie les tokens expirés
     */
    private static void cleanExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<String, TokenInfo>> iterator = tokens.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, TokenInfo> entry = iterator.next();
            if (currentTime > entry.getValue().expirationTime) {
                iterator.remove();
            }
        }
    }
}