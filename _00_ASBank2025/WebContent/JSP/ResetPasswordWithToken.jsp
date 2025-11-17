<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>

<html lang="fr">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Réinitialiser le mot de passe</title>
    <link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
</head>
<body>
<h1>Réinitialiser votre mot de passe</h1>

<s:if test="error">
    <div class="failure">
        <s:property value="message" />
    </div>
    <s:form name="myFormRetour" action="redirectionLogin" method="POST">
        <s:submit name="Retour" value="Retour à la page de connexion" />
    </s:form>
</s:if>
<s:else>
    <s:if test="%{message != null && message != ''}">
        <div class="success">
            <s:property value="message" />
        </div>
        <s:form name="myFormRetour" action="redirectionLogin" method="POST">
            <s:submit name="Retour" value="Aller à la page de connexion" />
        </s:form>
    </s:if>
    <s:else>
        <p>Entrez votre nouveau mot de passe pour l'utilisateur : <strong><s:property value="userId" /></strong></p>

        <s:form id="resetPasswordForm" name="resetPasswordForm" action="resetPasswordWithTokenSubmit" method="POST">
            <s:hidden name="token" value="%{token}" />
            <s:hidden name="userId" value="%{userId}" />

            <s:password label="Nouveau mot de passe" name="newPassword" required="true" />
            <s:password label="Confirmer le nouveau mot de passe" name="confirmPassword" required="true" />

            <s:submit value="Réinitialiser le mot de passe" />
        </s:form>
    </s:else>
</s:else>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>