<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tableau de bord - Gestionnaire</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
</head>
<body>
<div class="container-fluid">
	<div class="position-absolute top-0 start-0 m-3">
		<s:form name="myForm" action="logout" method="POST">
			<s:submit name="Retour" value="Logout" cssClass="btn btn-danger" />
		</s:form>
	</div>
	<div class="container mt-5 pt-4">
		<h1 class="text-center mb-4">Tableau de bord - Gestionnaire</h1>
		<div class="alert alert-info text-center">
			Bienvenue <strong><s:property value="connectedUser.prenom" /> <s:property value="connectedUser.nom" /></strong> !
		</div>
		<p class="text-center lead">Que voulez vous faire ?</p>
		<div class="row justify-content-center mt-4">
			<div class="col-md-8">
				<div class="list-group">
					<s:url action="listeCompteManager" var="urlListeCompteManager">
						<s:param name="aDecouvert">false</s:param>
					</s:url>
					<s:a href="%{urlListeCompteManager}" cssClass="list-group-item list-group-item-action">
						<i class="bi bi-list-ul"></i> Liste des comptes de la banque
					</s:a>
					<s:url action="listeCompteManager" var="urlListeCompteManagerDecouvert">
						<s:param name="aDecouvert">true</s:param>
					</s:url>
					<s:a href="%{urlListeCompteManagerDecouvert}" cssClass="list-group-item list-group-item-action list-group-item-warning">
						<i class="bi bi-exclamation-triangle"></i> Liste des comptes à découvert de la banque
					</s:a>
					<s:url action="urlAjoutUtilisateur" var="urlAjoutUtilisateur"></s:url>
					<s:a href="%{urlAjoutUtilisateur}" cssClass="list-group-item list-group-item-action list-group-item-primary">
						<i class="bi bi-person-plus"></i> Ajout d'un utilisateur
					</s:a>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>


