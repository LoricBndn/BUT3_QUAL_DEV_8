package com.iut.banque.config;

public class EmailConfig {

    // Configuration SMTP - À adapter selon votre fournisseur

    // Pour Gmail
    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_PORT = "587";
    public static final String SMTP_USERNAME = System.getenv("EMAIL_USERNAME");
    public static final String SMTP_PASSWORD = System.getenv("EMAIL_PASSWORD");

    // Configuration de l'email d'envoi
    public static final String FROM_EMAIL = "noreply@iutbank.fr";
    public static final String FROM_NAME = "IUT Bank";

    // Configuration de l'application
    public static final String APP_URL = "http://localhost:8081/_00_ASBank2025";

    // Paramètres de sécurité
    public static final boolean USE_TLS = true;
    public static final boolean USE_AUTH = true;

    private static String getProperty(String key) {
        return System.getProperty(key);
    }
}