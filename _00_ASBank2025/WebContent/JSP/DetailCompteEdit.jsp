<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Détail du Compte <s:property value="compte" /></title>
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
		<div class="mb-3">
			<s:form name="myForm" action="listeCompteManager" method="POST">
				<s:submit name="Retour" value="Retour" cssClass="btn btn-secondary" />
			</s:form>
		</div>
		<h1 class="text-center mb-4">Détail du Compte <s:property value="compte" /></h1>
		<div class="card shadow">
			<div class="card-body">
				<div class="row mb-3">
					<div class="col-md-6">
						<p><strong>Type de compte :</strong>
							<s:if test="%{compte.className == \"CompteAvecDecouvert\"}">
								<span class="badge bg-success">Découvert possible</span>
							</s:if>
							<s:else>
								<span class="badge bg-secondary">Simple</span>
							</s:else>
						</p>
					</div>
					<div class="col-md-6">
						<p><strong>Solde :</strong>
							<s:if test="%{compte.solde >= 0}">
								<span class="fs-5"><s:property value="compte.solde" /> €</span>
							</s:if>
							<s:else>
								<span class="fs-5 text-danger fw-bold"><s:property value="compte.solde" /> €</span>
							</s:else>
						</p>
					</div>
				</div>
				<s:if test="%{compte.className == \"CompteAvecDecouvert\"}">
					<div class="alert alert-info">
						<strong>Découvert maximal autorisé :</strong> <s:property value="compte.decouvertAutorise" /> €
					</div>
				</s:if>
			</div>
		</div>
		<div class="card shadow mt-4">
			<div class="card-header bg-primary text-white">
				<h5 class="mb-0">Opérations</h5>
			</div>
			<div class="card-body">
				<s:form name="formOperation" action="creditActionEdit" method="post">
					<s:textfield label="Montant" name="montant" cssClass="form-control mb-3" />
					<input type="hidden" name="compte" value="<s:property value='compte.numeroCompte' />">
					<div class="d-grid gap-2 d-md-flex justify-content-md-center">
						<s:submit value="Crediter" cssClass="btn btn-success" />
						<s:submit value="Debiter" action="debitActionEdit" cssClass="btn btn-warning" />
					</div>
				</s:form>
			</div>
		</div>
		<s:if test="%{compte.className == \"CompteAvecDecouvert\"}">
			<div class="card shadow mt-4">
				<div class="card-header bg-secondary text-white">
					<h5 class="mb-0">Modifier le découvert autorisé</h5>
				</div>
				<div class="card-body">
					<s:form name="formChangeDecouvertAutorise" action="changerDecouvertAutoriseAction" method="post">
						<input type="hidden" name="compte" value="<s:property value='compte.numeroCompte' />">
						<s:textfield label="Découvert autorisé" name="decouvertAutorise" value="%{compte.decouvertAutorise}" cssClass="form-control mb-3" />
						<s:submit value="Mettre à jour" cssClass="btn btn-primary w-100" />
					</s:form>
				</div>
			</div>
		</s:if>
		<s:if test="%{error != \"\"}">
			<div class="alert alert-danger mt-3" role="alert">
				<strong>Erreur :</strong> <s:property value="error" />
			</div>
		</s:if>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>