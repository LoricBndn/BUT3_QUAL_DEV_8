package com.iut.banque.test.util;

import com.iut.banque.util.EmailSender;
import com.iut.banque.config.EmailConfig;
import org.junit.Assume;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests pour l'envoi d'emails
 *
 * Note : Ces tests nécessitent une configuration SMTP valide pour s'exécuter
 */
public class TestEmailSender {

    /**
     * Test d'envoi d'un email de test simple
     *
     * Ce test est ignoré si les variables d'environnement ne sont pas configurées
     */
    @Test
    public void testSendTestEmail() {
        // Skip le test si les identifiants ne sont pas configurés
        Assume.assumeTrue(
                "Les variables d'environnement EMAIL_USERNAME et EMAIL_PASSWORD doivent être définies",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        // Remplacez par votre adresse email pour tester
        String testEmail = EmailConfig.SMTP_USERNAME; // Envoie à soi-même pour tester

        System.out.println("========================================");
        System.out.println("TEST D'ENVOI D'EMAIL");
        System.out.println("========================================");
        System.out.println("Envoi d'un email de test à : " + testEmail);

        boolean result = EmailSender.sendTestEmail(testEmail);

        System.out.println("Résultat : " + (result ? "SUCCÈS" : "ÉCHEC"));
        System.out.println("========================================");

        assertTrue("L'email de test devrait être envoyé avec succès", result);
    }

    /**
     * Test d'envoi d'un email de réinitialisation de mot de passe
     *
     * Ce test est ignoré si les variables d'environnement ne sont pas configurées
     */
    @Test
    public void testSendPasswordResetEmail() {
        // Skip le test si les identifiants ne sont pas configurés
        Assume.assumeTrue(
                "Les variables d'environnement EMAIL_USERNAME et EMAIL_PASSWORD doivent être définies",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        // Remplacez par votre adresse email pour tester
        String testEmail = EmailConfig.SMTP_USERNAME;
        String testToken = "TEST_TOKEN_123456";
        String testUserId = "test.user1";

        System.out.println("========================================");
        System.out.println("TEST D'EMAIL DE RÉINITIALISATION");
        System.out.println("========================================");
        System.out.println("Envoi à : " + testEmail);
        System.out.println("Token : " + testToken);
        System.out.println("UserId : " + testUserId);

        boolean result = EmailSender.sendPasswordResetEmail(testEmail, testToken, testUserId);

        System.out.println("Résultat : " + (result ? "SUCCÈS" : "ÉCHEC"));
        System.out.println("========================================");

        assertTrue("L'email de réinitialisation devrait être envoyé avec succès", result);
    }

    /**
     * Test de vérification de la configuration
     */
    @Test
    public void testEmailConfiguration() {
        System.out.println("========================================");
        System.out.println("VÉRIFICATION DE LA CONFIGURATION EMAIL");
        System.out.println("========================================");
        System.out.println("SMTP Host : " + EmailConfig.SMTP_HOST);
        System.out.println("SMTP Port : " + EmailConfig.SMTP_PORT);
        System.out.println("Username : " + (EmailConfig.SMTP_USERNAME != null ? "Configuré" : "NON CONFIGURÉ"));
        System.out.println("Password : " + (EmailConfig.SMTP_PASSWORD != null ? "Configuré" : "NON CONFIGURÉ"));
        System.out.println("From Email : " + EmailConfig.FROM_EMAIL);
        System.out.println("From Name : " + EmailConfig.FROM_NAME);
        System.out.println("App URL : " + EmailConfig.APP_URL);
        System.out.println("Use TLS : " + EmailConfig.USE_TLS);
        System.out.println("Use Auth : " + EmailConfig.USE_AUTH);
        System.out.println("========================================");

        // Vérifications basiques
        assertNotNull("SMTP_HOST ne doit pas être null", EmailConfig.SMTP_HOST);
        assertNotNull("SMTP_PORT ne doit pas être null", EmailConfig.SMTP_PORT);
        assertNotNull("FROM_EMAIL ne doit pas être null", EmailConfig.FROM_EMAIL);
        assertNotNull("APP_URL ne doit pas être null", EmailConfig.APP_URL);

        // Afficher un avertissement si les identifiants ne sont pas configurés
        if (EmailConfig.SMTP_USERNAME == null || EmailConfig.SMTP_PASSWORD == null) {
            System.out.println("⚠️  ATTENTION : Les variables d'environnement EMAIL_USERNAME et/ou EMAIL_PASSWORD ne sont pas définies");
            System.out.println("⚠️  L'envoi d'emails ne fonctionnera pas sans configuration SMTP valide");
            System.out.println("⚠️  Consultez CONFIGURATION_EMAIL.md pour les instructions de configuration");
        } else {
            System.out.println("✅ Configuration SMTP détectée");
        }
    }

    /**
     * Test de gestion des erreurs avec des identifiants invalides
     *
     * Ce test vérifie que la méthode retourne false en cas d'échec
     */
    @Test
    public void testSendEmailWithInvalidCredentials() {
        // On s'attend à ce que l'envoi échoue si les identifiants ne sont pas configurés
        if (EmailConfig.SMTP_USERNAME == null || EmailConfig.SMTP_PASSWORD == null) {
            boolean result = EmailSender.sendTestEmail("test@example.com");
            assertFalse("L'envoi devrait échouer sans configuration SMTP", result);
        }
    }
}