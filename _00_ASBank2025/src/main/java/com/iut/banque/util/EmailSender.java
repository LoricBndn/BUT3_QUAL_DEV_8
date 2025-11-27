package com.iut.banque.util;

import com.iut.banque.config.EmailConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitaire pour l'envoi d'emails
 */
public class EmailSender {

    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    /**
     * Envoie un email de réinitialisation de mot de passe
     *
     * @param toEmail Email du destinataire
     * @param resetToken Token de réinitialisation
     * @param userId Identifiant de l'utilisateur
     * @return true si l'email a été envoyé avec succès, false sinon
     */
    public static boolean sendPasswordResetEmail(String toEmail, String resetToken, String userId) {
        try {
            // Vérification des paramètres de configuration
            if (EmailConfig.SMTP_USERNAME == null || EmailConfig.SMTP_PASSWORD == null) {
                logger.error("ERREUR : Les identifiants SMTP ne sont pas configurés.");
                logger.error("Veuillez définir les variables d'environnement EMAIL_USERNAME et EMAIL_PASSWORD");
                return false;
            }

            // Configuration des propriétés SMTP
            Properties properties = new Properties();
            properties.put("mail.smtp.host", EmailConfig.SMTP_HOST);
            properties.put("mail.smtp.port", EmailConfig.SMTP_PORT);
            properties.put("mail.smtp.auth", String.valueOf(EmailConfig.USE_AUTH));

            if (EmailConfig.USE_TLS) {
                properties.put("mail.smtp.starttls.enable", "true");
            } else {
                properties.put("mail.smtp.ssl.enable", "true");
            }

            // Configuration supplémentaire pour éviter les problèmes SSL
            properties.put("mail.smtp.ssl.trust", EmailConfig.SMTP_HOST);
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

            // Création de la session avec authentification
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            EmailConfig.SMTP_USERNAME,
                            EmailConfig.SMTP_PASSWORD
                    );
                }
            });

            // Mode debug (à désactiver en production)
            session.setDebug(true);

            // Création du message
            Message message = new MimeMessage(session);

            // Expéditeur
            message.setFrom(new InternetAddress(EmailConfig.FROM_EMAIL, EmailConfig.FROM_NAME));

            // Destinataire
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            // Sujet
            message.setSubject("Réinitialisation de votre mot de passe - IUT Bank");

            // Date d'envoi
            message.setSentDate(new Date());

            // Génération du lien de réinitialisation
            String resetLink = EmailConfig.APP_URL + "/resetPasswordWithToken?token=" + resetToken + "&userId=" + userId;

            // Contenu HTML de l'email
            String htmlContent = buildEmailHtml(resetLink);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Envoi du message
            Transport.send(message);

            logger.info("========================================");
            logger.info("EMAIL ENVOYÉ AVEC SUCCÈS");
            logger.info("========================================");
            logger.info("À : {}", toEmail);
            logger.info("Sujet : Réinitialisation de votre mot de passe - IUT Bank");
            logger.info("========================================");

            return true;

        } catch (MessagingException e) {
            logger.error("========================================");
            logger.error("ERREUR LORS DE L'ENVOI DE L'EMAIL");
            logger.error("========================================");
            logger.error("Type d'erreur : {}", e.getClass().getSimpleName());
            logger.error("Message : {}", e.getMessage());
            logger.error("Stack trace:", e);
            logger.error("========================================");
            return false;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'envoi de l'email : {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Construit le contenu HTML de l'email
     *
     * @param resetLink Lien de réinitialisation
     * @return Contenu HTML de l'email
     */
    private static String buildEmailHtml(String resetLink) {
        return "<!DOCTYPE html>" +
                "<html lang='fr'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Réinitialisation de mot de passe</title>" +
                "</head>" +
                "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "    <div style='background-color: #0066cc; color: white; padding: 20px; text-align: center;'>" +
                "        <h1 style='margin: 0;'>IUT Bank</h1>" +
                "    </div>" +
                "    <div style='background-color: #f9f9f9; padding: 30px; border: 1px solid #ddd;'>" +
                "        <h2 style='color: #0066cc;'>Réinitialisation de votre mot de passe</h2>" +
                "        <p>Bonjour,</p>" +
                "        <p>Vous avez demandé à réinitialiser votre mot de passe pour votre compte IUT Bank.</p>" +
                "        <p>Cliquez sur le bouton ci-dessous pour créer un nouveau mot de passe :</p>" +
                "        <div style='text-align: center; margin: 30px 0;'>" +
                "            <a href='" + resetLink + "' style='background-color: #0066cc; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold;'>Réinitialiser mon mot de passe</a>" +
                "        </div>" +
                "        <p>Ou copiez-collez ce lien dans votre navigateur :</p>" +
                "        <p style='background-color: #eee; padding: 10px; word-break: break-all; font-size: 12px;'>" + resetLink + "</p>" +
                "        <div style='background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0;'>" +
                "            <p style='margin: 0; color: #856404;'><strong>⚠️ Important :</strong></p>" +
                "            <ul style='margin: 10px 0 0 0; color: #856404;'>" +
                "                <li>Ce lien est valable pendant <strong>1 heure</strong></li>" +
                "                <li>Si vous n'avez pas demandé cette réinitialisation, ignorez simplement cet email</li>" +
                "                <li>Votre mot de passe actuel reste inchangé tant que vous ne créez pas un nouveau mot de passe</li>" +
                "            </ul>" +
                "        </div>" +
                "        <p style='color: #666; font-size: 14px; margin-top: 30px;'>Cordialement,<br>L'équipe IUT Bank</p>" +
                "    </div>" +
                "    <div style='text-align: center; padding: 20px; color: #999; font-size: 12px;'>" +
                "        <p>Cet email a été envoyé automatiquement, merci de ne pas y répondre.</p>" +
                "        <p>&copy; 2025 IUT Bank - Tous droits réservés</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Méthode de test pour vérifier la configuration
     * À utiliser uniquement pour tester la configuration SMTP
     *
     * @param toEmail Email de test
     * @return true si l'email de test a été envoyé avec succès
     */
    public static boolean sendTestEmail(String toEmail) {
        try {
            if (EmailConfig.SMTP_USERNAME == null || EmailConfig.SMTP_PASSWORD == null) {
                logger.error("ERREUR : Les identifiants SMTP ne sont pas configurés.");
                return false;
            }

            Properties properties = new Properties();
            properties.put("mail.smtp.host", EmailConfig.SMTP_HOST);
            properties.put("mail.smtp.port", EmailConfig.SMTP_PORT);
            properties.put("mail.smtp.auth", String.valueOf(EmailConfig.USE_AUTH));

            if (EmailConfig.USE_TLS) {
                properties.put("mail.smtp.starttls.enable", "true");
            } else {
                properties.put("mail.smtp.ssl.enable", "true");
            }

            properties.put("mail.smtp.ssl.trust", EmailConfig.SMTP_HOST);
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            EmailConfig.SMTP_USERNAME,
                            EmailConfig.SMTP_PASSWORD
                    );
                }
            });

            session.setDebug(true);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EmailConfig.FROM_EMAIL, EmailConfig.FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Email de test - IUT Bank");
            message.setSentDate(new Date());
            message.setText("Ceci est un email de test pour vérifier la configuration SMTP.\n\nSi vous recevez cet email, la configuration est correcte !");

            Transport.send(message);

            logger.info("Email de test envoyé avec succès à {}", toEmail);
            return true;

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email de test : {}", e.getMessage(), e);
            return false;
        }
    }
}