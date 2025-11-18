package com.iut.banque.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailSender {

    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    /**
     * Envoie un email de réinitialisation de mot de passe
     * ATTENTION : Cette version simule l'envoi d'email pour les tests.
     * Pour une vraie implémentation, ajoutez la dépendance JavaMail dans pom.xml
     *
     * @param toEmail Email du destinataire
     * @param resetToken Token de réinitialisation
     * @param userId Identifiant de l'utilisateur
     * @return true si l'email a été "envoyé" avec succès
     */
    public static boolean sendPasswordResetEmail(String toEmail, String resetToken, String userId) {
        try {
            // Génération du lien de réinitialisation
            String resetLink = "http://localhost:8081/_00_ASBank2025/resetPasswordWithToken?token=" + resetToken + "&userId=" + userId;

            // Simulation d'envoi d'email (affichage dans la console)
            logger.info("========================================");
            logger.info("SIMULATION D'ENVOI D'EMAIL");
            logger.info("========================================");
            logger.info("À : {}", toEmail);
            logger.info("Sujet : Réinitialisation de votre mot de passe - IUT Bank");
            logger.info("----------------------------------------");
            logger.info("Bonjour,");
            logger.info("");
            logger.info("Vous avez demandé à réinitialiser votre mot de passe.");
            logger.info("");
            logger.info("Cliquez sur le lien ci-dessous pour réinitialiser votre mot de passe :");
            logger.info("{}", resetLink);
            logger.info("");
            logger.info("Ce lien est valable pendant 1 heure.");
            logger.info("");
            logger.info("Si vous n'avez pas demandé cette réinitialisation,");
            logger.info("ignorez simplement cet email.");
            logger.info("");
            logger.info("Cordialement,");
            logger.info("L'équipe IUT Bank");
            logger.info("========================================");

            return true;

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email", e);
            return false;
        }
    }
}