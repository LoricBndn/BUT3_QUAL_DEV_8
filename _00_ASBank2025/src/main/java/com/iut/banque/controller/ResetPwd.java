package com.iut.banque.controller;

import com.iut.banque.facade.LoginManager;
import com.opensymphony.xwork2.ActionSupport;

public class ResetPwd extends ActionSupport {
    private String userCde;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String message;
    private boolean error = false;
    private boolean result = false;

    // Getters/Setters
    public String getUserCde() { return userCde; }
    public void setUserCde(String userCde) { this.userCde = userCde; }
    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public boolean isResult() { return result; }
    public void setResult(boolean result) { this.result = result; }

    @Override
    public String execute() {
        try {
            if (!newPassword.equals(confirmPassword)) {
                message = "Les mots de passe ne correspondent pas.";
                error = true;
                return ERROR;
            }

            LoginManager manager = new LoginManager();
            boolean updated = manager.resetPassword(userCde, oldPassword, newPassword);

            if (updated) {
                message = "Mot de passe mis à jour avec succès.";
                return SUCCESS;
            } else {
                message = "Ancien mot de passe incorrect.";
                error = true;
                return ERROR;
            }
        } catch (Exception e) {
            message = "Erreur technique : " + e.getMessage();
            error = true;
            return ERROR;
        }
    }
}