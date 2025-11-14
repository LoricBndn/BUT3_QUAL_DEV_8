package com.iut.banque.controller;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.iut.banque.cryptage.PasswordHasher;
import com.iut.banque.facade.LoginManager;
import com.iut.banque.modele.Utilisateur;
import com.iut.banque.util.ResetTokenManager;
import com.opensymphony.xwork2.ActionSupport;

public class ResetPasswordWithToken extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private String token;
    private String userId;
    private String newPassword;
    private String confirmPassword;
    private String message;
    private boolean error;
    private final LoginManager loginManager;

    public ResetPasswordWithToken() {
        System.out.println("In constructor from ResetPasswordWithToken class");
        ApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ServletActionContext.getServletContext());
        this.loginManager = (LoginManager) context.getBean("loginManager");
    }

    // Getters et Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
     * Affiche le formulaire de réinitialisation avec validation du token
     */
    public String showForm() {
        System.out.println("Affichage du formulaire de réinitialisation pour token : " + token);

        // Valider le token
        String validatedUserId = ResetTokenManager.validateToken(token);

        if (validatedUserId == null) {
            error = true;
            message = "Le lien de réinitialisation est invalide ou a expiré.";
            return ERROR;
        }

        if (!validatedUserId.equals(userId)) {
            error = true;
            message = "Le lien de réinitialisation ne correspond pas à l'utilisateur.";
            return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Réinitialise le mot de passe avec le token
     */
    public String resetPassword() {
        System.out.println("Réinitialisation du mot de passe pour token : " + token);

        try {
            // Valider le token
            String validatedUserId = ResetTokenManager.validateToken(token);

            if (validatedUserId == null) {
                error = true;
                message = "Le lien de réinitialisation est invalide ou a expiré.";
                return ERROR;
            }

            if (!validatedUserId.equals(userId)) {
                error = true;
                message = "Le lien de réinitialisation ne correspond pas à l'utilisateur.";
                return ERROR;
            }

            // Vérifier que les mots de passe correspondent
            if (!newPassword.equals(confirmPassword)) {
                error = true;
                message = "Les deux mots de passe ne correspondent pas.";
                return ERROR;
            }

            // Vérifier la longueur du mot de passe
            if (newPassword.length() < 6) {
                error = true;
                message = "Le mot de passe doit contenir au moins 6 caractères.";
                return ERROR;
            }

            // Récupérer l'utilisateur
            Utilisateur user = loginManager.getUserById(userId);

            if (user == null) {
                error = true;
                message = "Utilisateur non trouvé.";
                return ERROR;
            }

            // Hacher le nouveau mot de passe
            String hashedPassword = PasswordHasher.hashPassword(newPassword);
            user.setUserPwd(hashedPassword);

            // Mettre à jour l'utilisateur
            loginManager.updateUser(user);

            // Invalider le token
            ResetTokenManager.invalidateToken(token);

            error = false;
            message = "Votre mot de passe a été réinitialisé avec succès. Vous pouvez maintenant vous connecter.";
            return SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            error = true;
            message = "Une erreur technique est survenue : " + e.getMessage();
            return ERROR;
        }
    }
}