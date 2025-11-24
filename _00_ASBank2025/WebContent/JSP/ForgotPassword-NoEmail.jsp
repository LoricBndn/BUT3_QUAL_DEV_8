<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Email non associé</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
</head>
<body>
<div class="container mt-5">
	<div class="row justify-content-center">
		<div class="col-md-8">
			<h1 class="text-center mb-4">Email non associé à votre compte</h1>
			<div class="alert alert-warning" role="alert">
				<h4 class="alert-heading">Votre compte n'est pas lié à une adresse email</h4>
				<p>Pour pouvoir utiliser la fonctionnalité de réinitialisation de mot de passe par email, vous devez d'abord associer une adresse email à votre compte.</p>
				<hr>
				<p class="mb-0">Veuillez contacter votre conseiller bancaire pour effectuer cette opération.</p>
			</div>
			<div class="card shadow mt-4">
				<div class="card-header bg-primary text-white">
					<h5 class="mb-0">Coordonnées du service client</h5>
				</div>
				<div class="card-body">
					<p class="mb-1"><strong>Téléphone :</strong> 03 XX XX XX XX</p>
					<p class="mb-1"><strong>Email :</strong> support@iutbank.fr</p>
					<p class="mb-0"><strong>Horaires :</strong> Du lundi au vendredi, 9h-18h</p>
				</div>
			</div>
			<s:form name="myFormRetour" action="redirectionLogin" method="POST" cssClass="mt-4">
				<s:submit name="Retour" value="Retour à la page de connexion" cssClass="btn btn-primary w-100" />
			</s:form>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>