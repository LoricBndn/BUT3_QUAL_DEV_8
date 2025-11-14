<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Mot de passe oublié</title>
    <link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
</head>
<body>
<h1>Mot de passe oublié</h1>

<p>Entrez votre identifiant pour recevoir un lien de réinitialisation par email.</p>

<s:form id="forgotPasswordForm" name="forgotPasswordForm" action="forgotPasswordRequest" method="POST">
    <s:textfield label="Identifiant utilisateur" name="userCde" required="true" />
    <s:submit value="Envoyer le lien de réinitialisation" />
</s:form>

<s:form name="myFormRetour" action="redirectionLogin" method="POST">
    <s:submit name="Retour" value="Retour à la page de connexion" />
</s:form>

<s:if test="%{message != null && message != ''}">
    <s:if test="error">
        <div class="failure">
            <s:property value="message" />
        </div>
    </s:if>
    <s:else>
        <div class="success">
            <s:property value="message" />
            <p style="margin-top: 20px; font-size: 0.9em; color: #666;">
                <strong>Note:</strong> En mode développement, le lien apparaît dans la console du serveur.
            </p>
        </div>
    </s:else>
</s:if>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>