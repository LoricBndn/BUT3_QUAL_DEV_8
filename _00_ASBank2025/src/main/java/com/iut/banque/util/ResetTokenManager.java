package com.iut.banque.util;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iut.banque.interfaces.IDao;

public class ResetTokenManager {

    private static final Logger logger = LoggerFactory.getLogger(ResetTokenManager.class);
    private static final long TOKEN_VALIDITY_DURATION = 3600000; // 1 heure

    // Singleton Spring — null si pas de contexte Spring (fallback mémoire)
    private static ResetTokenManager instance;

    // Fallback en mémoire (contextes sans Spring ou sans DAO)
    private static final Map<String, TokenInfo> inMemoryTokens = new HashMap<>();

    private IDao dao;

    public void setDao(IDao dao) {
        this.dao = dao;
        instance = this;
    }

    // ==================== API statique (rétrocompatible) ====================

    public static String generateToken(String userId) {
        String token = generateTokenValue();
        long expirationTime = System.currentTimeMillis() + TOKEN_VALIDITY_DURATION;

        if (instance != null && instance.dao != null) {
            instance.dao.deleteExpiredResetTokens();
            instance.dao.saveResetToken(token, userId, expirationTime);
        } else {
            cleanExpiredTokensInMemory();
            inMemoryTokens.put(token, new TokenInfo(userId, expirationTime));
        }

        logger.info("Token généré pour l'utilisateur {} : {}", userId, token);
        return token;
    }

    public static String validateToken(String token) {
        if (token == null) return null;
        if (instance != null && instance.dao != null) {
            String userId = instance.dao.getResetTokenUserId(token);
            if (userId == null) {
                logger.warn("Token non trouvé ou expiré : {}", token);
            } else {
                logger.info("Token valide pour l'utilisateur : {}", userId);
            }
            return userId;
        }
        return validateTokenInMemory(token);
    }

    public static void invalidateToken(String token) {
        if (instance != null && instance.dao != null) {
            instance.dao.deleteResetToken(token);
        } else {
            inMemoryTokens.remove(token);
        }
        logger.info("Token invalidé : {}", token);
    }

    // ==================== Méthodes privées ====================

    private static String generateTokenValue() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static String validateTokenInMemory(String token) {
        TokenInfo info = inMemoryTokens.get(token);
        if (info == null) {
            logger.warn("Token non trouvé : {}", token);
            return null;
        }
        if (System.currentTimeMillis() > info.expirationTime) {
            logger.warn("Token expiré pour l'utilisateur : {}", info.userId);
            inMemoryTokens.remove(token);
            return null;
        }
        logger.info("Token valide pour l'utilisateur : {}", info.userId);
        return info.userId;
    }

    private static void cleanExpiredTokensInMemory() {
        long currentTime = System.currentTimeMillis();
        inMemoryTokens.entrySet().removeIf(e -> currentTime > e.getValue().expirationTime);
    }

    private static class TokenInfo {
        final String userId;
        final long expirationTime;

        TokenInfo(String userId, long expirationTime) {
            this.userId = userId;
            this.expirationTime = expirationTime;
        }
    }
}
