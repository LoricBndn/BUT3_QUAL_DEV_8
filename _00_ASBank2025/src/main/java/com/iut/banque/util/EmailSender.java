package com.iut.banque.util;

public class EmailSender {

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
            String resetLink = "http://localhost:8080/_00_ASBank2025/resetPasswordWithToken?token=" + resetToken + "&userId=" + userId;

            // Simulation d'envoi d'email (affichage dans la console)
            System.out.println("========================================");
            System.out.println("SIMULATION D'ENVOI D'EMAIL");
            System.out.println("========================================");
            System.out.println("À : " + toEmail);
            System.out.println("Sujet : Réinitialisation de votre mot de passe - IUT Bank");
            System.out.println("----------------------------------------");
            System.out.println("Bonjour,");
            System.out.println("");
            System.out.println("Vous avez demandé à réinitialiser votre mot de passe.");
            System.out.println("");
            System.out.println("Cliquez sur le lien ci-dessous pour réinitialiser votre mot de passe :");
            System.out.println(resetLink);
            System.out.println("");
            System.out.println("Ce lien est valable pendant 1 heure.");
            System.out.println("");
            System.out.println("Si vous n'avez pas demandé cette réinitialisation,");
            System.out.println("ignorez simplement cet email.");
            System.out.println("");
            System.out.println("Cordialement,");
            System.out.println("L'équipe IUT Bank");
            System.out.println("========================================");

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            return false;
        }
    }
}