<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Formulaire de création d'un compte</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
<script src="/_00_ASBank2025/js/jquery.js"></script>
<script src="/_00_ASBank2025/js/jsCreerCompte.js"></script>
</head>
<body>
<div class="container mt-5">
	<h1 class="text-center mb-4">Créer un nouveau compte</h1>
	<div class="row justify-content-center">
		<div class="col-md-8">
			<div class="alert alert-info">
				<strong>Client choisi :</strong> <s:property value="client" />
			</div>
			<div class="card shadow">
				<div class="card-body">
					<s:form id="myForm" name="myForm" action="addAccount" method="POST">
						<input type="hidden" name="client" value="<s:property value='client.userId' />">
						<s:textfield label="Numéro de compte" name="numeroCompte" cssClass="form-control mb-3" />
						<div class="mb-3">
							<label class="form-label">Type</label>
							<s:radio label="Type" name="avecDecouvert" list="#{false:'Compte sans découvert',true:'Compte avec découvert'}" value="false" cssClass="form-check-input" />
						</div>
						<s:textfield label="Découvert autorisé" name="decouvertAutorise" cssClass="form-control mb-3" />
						<s:submit name="submit" value="Créer le compte" cssClass="btn btn-primary w-100" />
					</s:form>
				</div>
			</div>
			<s:form name="myForm" action="listeCompteManager" method="POST" cssClass="mt-3">
				<s:submit name="Retour" value="Retour" cssClass="btn btn-secondary w-100" />
			</s:form>
			<s:if test="result">
				<s:if test="!error">
					<div class="alert alert-success mt-3" role="alert">
						<s:property value="message" />
					</div>
				</s:if>
				<s:else>
					<div class="alert alert-danger mt-3" role="alert">
						<s:property value="message" />
					</div>
				</s:else>
			</s:if>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>