<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Email non associé</title>
    <link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
</head>
<body>
<h1>Email non associé à votre compte</h1>

<div class="failure">
    <p><strong>Votre compte n'est pas lié à une adresse email.</strong></p>
    <p>Pour pouvoir utiliser la fonctionnalité de réinitialisation de mot de passe par email,
        vous devez d'abord associer une adresse email à votre compte.</p>
    <p>Veuillez contacter votre conseiller bancaire pour effectuer cette opération.</p>
</div>

<p style="margin-top: 30px;">
    <strong>Coordonnées du service client :</strong><br>
    Téléphone : 03 XX XX XX XX<br>
    Email : support@iutbank.fr<br>
    Horaires : Du lundi au vendredi, 9h-18h
</p>

<s:form name="myFormRetour" action="redirectionLogin" method="POST">
    <s:submit name="Retour" value="Retour à la page de connexion" />
</s:form>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>