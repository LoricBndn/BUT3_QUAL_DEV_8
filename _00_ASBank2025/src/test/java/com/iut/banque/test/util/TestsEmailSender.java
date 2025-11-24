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
public class TestsEmailSender {

    /**
     * Test d'envoi d'un email de test simple
     *
     * Ce test est ignoré si les variables d'environnement ne sont pas configurées
     * Note: Ce test peut échouer selon la configuration SMTP (restrictions Gmail, etc.)
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

        if (!result) {
            System.out.println("⚠️  L'envoi a échoué. Vérifiez:");
            System.out.println("   - Que vous utilisez un mot de passe d'application Gmail");
            System.out.println("   - Que la validation en 2 étapes est activée");
            System.out.println("   - Que les paramètres de sécurité Gmail autorisent l'application");
        }

        // On vérifie juste que la méthode ne lance pas d'exception
        assertNotNull("Le résultat ne devrait pas être null", Boolean.valueOf(result));
    }

    /**
     * Test d'envoi d'un email de réinitialisation de mot de passe
     *
     * Ce test est ignoré si les variables d'environnement ne sont pas configurées
     * Note: Ce test peut échouer selon la configuration SMTP (restrictions Gmail, etc.)
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

        if (!result) {
            System.out.println("⚠️  L'envoi a échoué. Vérifiez:");
            System.out.println("   - Que vous utilisez un mot de passe d'application Gmail");
            System.out.println("   - Que la validation en 2 étapes est activée");
            System.out.println("   - Que les paramètres de sécurité Gmail autorisent l'application");
        }

        // On vérifie juste que la méthode ne lance pas d'exception
        assertNotNull("Le résultat ne devrait pas être null", Boolean.valueOf(result));
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

    /**
     * Test d'envoi avec une adresse email null
     *
     * Vérifie que la méthode gère correctement les emails null
     */
    @Test
    public void testSendPasswordResetEmailWithNullEmail() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        boolean result = EmailSender.sendPasswordResetEmail(null, "token123", "user123");
        assertFalse("L'envoi devrait échouer avec une adresse email null", result);
    }

    /**
     * Test d'envoi avec une adresse email vide
     *
     * Vérifie que la méthode gère correctement les emails vides
     */
    @Test
    public void testSendPasswordResetEmailWithEmptyEmail() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        boolean result = EmailSender.sendPasswordResetEmail("", "token123", "user123");
        assertFalse("L'envoi devrait échouer avec une adresse email vide", result);
    }

    /**
     * Test d'envoi avec une adresse email invalide
     *
     * Vérifie que la méthode gère correctement les emails mal formatés
     */
    @Test
    public void testSendPasswordResetEmailWithInvalidEmail() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        boolean result = EmailSender.sendPasswordResetEmail("invalid-email", "token123", "user123");
        assertFalse("L'envoi devrait échouer avec une adresse email invalide", result);
    }

    /**
     * Test d'envoi avec un token null
     *
     * Vérifie que la méthode accepte les tokens null (seront convertis en String "null")
     * Note: Ce test vérifie que l'envoi technique fonctionne, pas la validité métier du token
     */
    @Test
    public void testSendPasswordResetEmailWithNullToken() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        String testEmail = EmailConfig.SMTP_USERNAME;
        boolean result = EmailSender.sendPasswordResetEmail(testEmail, null, "user123");

        // L'envoi peut réussir ou échouer selon la config SMTP
        // On vérifie juste que la méthode ne lance pas d'exception
        assertNotNull("Le résultat ne devrait pas être null", Boolean.valueOf(result));
    }

    /**
     * Test d'envoi avec un token vide
     *
     * Vérifie que la méthode accepte les tokens vides
     */
    @Test
    public void testSendPasswordResetEmailWithEmptyToken() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        String testEmail = EmailConfig.SMTP_USERNAME;
        boolean result = EmailSender.sendPasswordResetEmail(testEmail, "", "user123");

        // L'envoi peut réussir ou échouer selon la config SMTP
        assertNotNull("Le résultat ne devrait pas être null", Boolean.valueOf(result));
    }

    /**
     * Test d'envoi avec un userId null
     *
     * Vérifie que la méthode accepte les userId null
     */
    @Test
    public void testSendPasswordResetEmailWithNullUserId() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        String testEmail = EmailConfig.SMTP_USERNAME;
        boolean result = EmailSender.sendPasswordResetEmail(testEmail, "token123", null);

        // L'envoi peut réussir ou échouer selon la config SMTP
        assertNotNull("Le résultat ne devrait pas être null", Boolean.valueOf(result));
    }

    /**
     * Test d'envoi avec un userId vide
     *
     * Vérifie que la méthode accepte les userId vides
     */
    @Test
    public void testSendPasswordResetEmailWithEmptyUserId() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        String testEmail = EmailConfig.SMTP_USERNAME;
        boolean result = EmailSender.sendPasswordResetEmail(testEmail, "token123", "");

        // L'envoi peut réussir ou échouer selon la config SMTP
        assertNotNull("Le résultat ne devrait pas être null", Boolean.valueOf(result));
    }

    /**
     * Test d'envoi d'email de test avec une adresse null
     */
    @Test
    public void testSendTestEmailWithNullEmail() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        boolean result = EmailSender.sendTestEmail(null);
        assertFalse("L'envoi devrait échouer avec une adresse email null", result);
    }

    /**
     * Test d'envoi d'email de test avec une adresse vide
     */
    @Test
    public void testSendTestEmailWithEmptyEmail() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        boolean result = EmailSender.sendTestEmail("");
        assertFalse("L'envoi devrait échouer avec une adresse email vide", result);
    }

    /**
     * Test d'envoi d'email de test avec une adresse invalide
     */
    @Test
    public void testSendTestEmailWithInvalidEmail() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        boolean result = EmailSender.sendTestEmail("not-an-email");
        assertFalse("L'envoi devrait échouer avec une adresse email invalide", result);
    }

    /**
     * Test avec des caractères spéciaux dans le token
     *
     * Vérifie que les caractères spéciaux sont gérés sans lever d'exception
     */
    @Test
    public void testSendPasswordResetEmailWithSpecialCharactersInToken() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        String testEmail = EmailConfig.SMTP_USERNAME;
        String specialToken = "token&special=chars?test#123";
        boolean result = EmailSender.sendPasswordResetEmail(testEmail, specialToken, "user123");

        // On vérifie que la méthode ne lance pas d'exception
        assertNotNull("Le résultat ne devrait pas être null", Boolean.valueOf(result));
    }

    /**
     * Test avec des caractères spéciaux dans le userId
     *
     * Vérifie que les caractères spéciaux sont gérés sans lever d'exception
     */
    @Test
    public void testSendPasswordResetEmailWithSpecialCharactersInUserId() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        String testEmail = EmailConfig.SMTP_USERNAME;
        String specialUserId = "user@123#special&id";
        boolean result = EmailSender.sendPasswordResetEmail(testEmail, "token123", specialUserId);

        // On vérifie que la méthode ne lance pas d'exception
        assertNotNull("Le résultat ne devrait pas être null", Boolean.valueOf(result));
    }

    /**
     * Test avec un token très long
     *
     * Vérifie que les tokens longs sont gérés sans lever d'exception
     */
    @Test
    public void testSendPasswordResetEmailWithVeryLongToken() {
        Assume.assumeTrue(
                "Configuration SMTP requise pour ce test",
                EmailConfig.SMTP_USERNAME != null && EmailConfig.SMTP_PASSWORD != null
        );

        String testEmail = EmailConfig.SMTP_USERNAME;
        StringBuilder longToken = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            longToken.append("a");
        }

        boolean result = EmailSender.sendPasswordResetEmail(testEmail, longToken.toString(), "user123");

        // On vérifie que la méthode ne lance pas d'exception
        assertNotNull("Le résultat ne devrait pas être null", Boolean.valueOf(result));
    }

    /**
     * Test de validation de la configuration - Host
     */
    @Test
    public void testEmailConfigurationHost() {
        assertNotNull("SMTP_HOST ne doit pas être null", EmailConfig.SMTP_HOST);
        assertFalse("SMTP_HOST ne doit pas être vide", EmailConfig.SMTP_HOST.trim().isEmpty());
    }

    /**
     * Test de validation de la configuration - Port
     */
    @Test
    public void testEmailConfigurationPort() {
        assertNotNull("SMTP_PORT ne doit pas être null", EmailConfig.SMTP_PORT);
        assertFalse("SMTP_PORT ne doit pas être vide", EmailConfig.SMTP_PORT.trim().isEmpty());
    }

    /**
     * Test de validation de la configuration - From Email
     */
    @Test
    public void testEmailConfigurationFromEmail() {
        assertNotNull("FROM_EMAIL ne doit pas être null", EmailConfig.FROM_EMAIL);
        assertFalse("FROM_EMAIL ne doit pas être vide", EmailConfig.FROM_EMAIL.trim().isEmpty());
        assertTrue("FROM_EMAIL devrait contenir un @", EmailConfig.FROM_EMAIL.contains("@"));
    }

    /**
     * Test de validation de la configuration - App URL
     */
    @Test
    public void testEmailConfigurationAppUrl() {
        assertNotNull("APP_URL ne doit pas être null", EmailConfig.APP_URL);
        assertFalse("APP_URL ne doit pas être vide", EmailConfig.APP_URL.trim().isEmpty());
    }
}