<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Erreur de Connexion</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
</head>
<body>
<div class="container mt-5">
	<div class="row justify-content-center">
		<div class="col-md-6">
			<div class="alert alert-danger" role="alert">
				<h4 class="alert-heading">Erreur de connexion !</h4>
				<p>Vous avez probablement entré de mauvais identifiants</p>
				<hr>
				<p class="mb-0">
					<s:url action="redirectionLogin" var="redirectionLogin" ></s:url>
					<s:a href="%{redirectionLogin}" cssClass="btn btn-primary">Cliquez ici</s:a> pour revenir à l'écran de login
				</p>
			</div>
			<div class="alert alert-info mt-3" role="alert">
				Si le problème persiste, veuillez contacter votre conseiller
			</div>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>