    package com.iut.banque.controller;

    import com.iut.banque.facade.LoginManager;
    import com.opensymphony.xwork2.ActionSupport;
    import org.apache.struts2.ServletActionContext;
    import org.springframework.web.context.WebApplicationContext;
    import org.springframework.web.context.support.WebApplicationContextUtils;

    public class ResetPwd extends ActionSupport {
        private String userCde;
        private String oldPassword;
        private String newPassword;
        private String confirmPassword;
        private String message;
        private boolean error = false;
        private boolean result = false;

        public ResetPwd() {
            System.out.println("In constructor from ResetPwd class");
        }

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

        public String reset() {
            System.out.println("In reset method from ResetPwd class");
            System.out.println("userCde: " + userCde);
            System.out.println("oldPassword: " + oldPassword);
            System.out.println("newPassword: " + newPassword);
            System.out.println("confirmPassword: " + confirmPassword);
            try {
                if (!newPassword.equals(confirmPassword)) {
                    message = "Les deux mots de passe ne correspondent pas.";
                    error = true;
                    System.out.println("Error - " + message);
                    return ERROR;
                }

                System.out.println("Avant Login manager");
                System.out.println("\nAvant resetPassord");
                WebApplicationContext context = WebApplicationContextUtils
                        .getWebApplicationContext(ServletActionContext.getServletContext());
                LoginManager manager = (LoginManager) context.getBean("loginManager");

                boolean updated = manager.resetPassword(userCde, oldPassword, newPassword);
                System.out.println("Apres reset password (Updated - " + updated + ")");

                if (updated) {
                    message = "Mot de passe mis à jour avec succès.";
                    System.out.println(message);
                    return SUCCESS;
                } else {
                    message = "Ancien mot de passe incorrect.";
                    error = true;
                    System.out.println("Error - " + message);
                    return ERROR;
                }
            } catch (Exception e) {
                message = "Erreur technique - type : " + e.toString() + " / message : " + e.getMessage();
                error = true;
                System.out.println("Error - " + message);
                return ERROR;
            }
        }
    }