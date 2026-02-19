package com.iut.banque.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protège contre les attaques par force brute en bloquant un userId
 * après un nombre trop élevé de tentatives de connexion échouées.
 */
public class LoginRateLimiter {

    private static final Logger logger = LoggerFactory.getLogger(LoginRateLimiter.class);

    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_DURATION_MS = 15 * 60 * 1000L; // 15 minutes

    private static final ConcurrentMap<String, AttemptInfo> attempts = new ConcurrentHashMap<>();

    private static class AttemptInfo {
        int count;
        long blockedUntil;

        AttemptInfo() {
            this.count = 0;
            this.blockedUntil = 0;
        }
    }

    /**
     * Indique si le userId est actuellement bloqué.
     */
    public static boolean isBlocked(String userId) {
        AttemptInfo info = attempts.get(userId);
        if (info == null) return false;
        if (System.currentTimeMillis() < info.blockedUntil) {
            logger.warn("Connexion refusée pour {} : trop de tentatives échouées", userId);
            return true;
        }
        return false;
    }

    /**
     * Enregistre une tentative de connexion échouée.
     * Bloque le userId si le seuil est atteint.
     */
    public static void recordFailedAttempt(String userId) {
        AttemptInfo info = attempts.computeIfAbsent(userId, k -> new AttemptInfo());
        synchronized (info) {
            if (System.currentTimeMillis() >= info.blockedUntil) {
                info.count++;
                if (info.count >= MAX_ATTEMPTS) {
                    info.blockedUntil = System.currentTimeMillis() + BLOCK_DURATION_MS;
                    info.count = 0;
                    logger.warn("Utilisateur {} bloqué après {} tentatives échouées", userId, MAX_ATTEMPTS);
                }
            }
        }
    }

    /**
     * Réinitialise le compteur après une connexion réussie.
     */
    public static void resetAttempts(String userId) {
        attempts.remove(userId);
    }
}
