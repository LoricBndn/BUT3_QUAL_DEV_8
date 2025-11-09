package com.iut.banque.controller;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.iut.banque.facade.LoginManager;
import com.iut.banque.modele.Utilisateur;
import com.iut.banque.util.EmailSender;
import com.iut.banque.util.ResetTokenManager;
import com.opensymphony.xwork2.ActionSupport;

public class ForgotPassword extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private String userCde;
    private String message;
    private boolean error;
    private final LoginManager loginManager;

    public ForgotPassword() {
        System.out.println("In constructor from ForgotPassword class");
        ApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ServletActionContext.getServletContext());
        this.loginManager = (LoginManager) context.getBean("loginManager");
    }

    // Getters et Setters
    public String getUserCde() {
        return userCde;
    }

    public void setUserCde(String userCde) {
        this.userCde = userCde;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * Affiche le formulaire de demande de réinitialisation
     */
    public String showForm() {
        return SUCCESS;
    }

    /**
     * Traite la demande de réinitialisation de mot de passe
     */
    public String requestReset() {
        System.out.println("Demande de réinitialisation pour l'utilisateur : " + userCde);

        try {
            // Récupérer l'utilisateur
            Utilisateur user = loginManager.getUserById(userCde);

            if (user == null) {
                error = true;
                message = "Utilisateur non trouvé.";
                return ERROR;
            }

            // Vérifier si l'utilisateur a un email
            String email = user.getEmail();
            if (email == null || email.trim().isEmpty()) {
                error = true;
                message = "Votre compte n'est pas lié à une adresse email. Veuillez contacter votre conseiller pour associer un email à votre compte.";
                return "NOEMAIL";
            }

            // Générer un token de réinitialisation
            String resetToken = ResetTokenManager.generateToken(userCde);

            // Envoyer l'email
            boolean emailSent = EmailSender.sendPasswordResetEmail(email, resetToken, userCde);

            if (emailSent) {
                error = false;
                message = "Un email de réinitialisation a été envoyé à l'adresse associée à votre compte.";
                return SUCCESS;
            } else {
                error = true;
                message = "Erreur lors de l'envoi de l'email. Veuillez réessayer plus tard.";
                return ERROR;
            }

        } catch (Exception e) {
            e.printStackTrace();
            error = true;
            message = "Une erreur technique est survenue : " + e.getMessage();
            return ERROR;
        }
    }
}