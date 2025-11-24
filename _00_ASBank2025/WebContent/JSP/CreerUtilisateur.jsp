<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Formulaire de création d'utilisateur</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
<script src="/_00_ASBank2025/js/jquery.js"></script>
<script src="/_00_ASBank2025/js/jsCreerUtilisateur.js"></script>
</head>
<body>
<div class="container-fluid">
	<div class="position-absolute top-0 start-0 m-3">
		<s:form name="myForm" action="logout" method="POST">
			<s:submit name="Retour" value="Logout" cssClass="btn btn-danger" />
		</s:form>
	</div>
	<div class="container mt-5 pt-4">
		<h1 class="text-center mb-4">Créer un nouvel utilisateur</h1>
		<div class="row justify-content-center">
			<div class="col-md-9 col-lg-7">
				<div class="card shadow">
					<div class="card-body">
						<s:form id="myForm" name="myForm" action="ajoutUtilisateur" method="POST">
							<div class="mb-3">
								<s:textfield label="Code utilisateur" name="userId" cssClass="form-control" />
							</div>
							<div class="mb-3">
								<s:textfield label="Nom" name="nom" cssClass="form-control" />
							</div>
							<div class="mb-3">
								<s:textfield label="Prenom" name="prenom" cssClass="form-control" />
							</div>
							<div class="mb-3">
								<s:textfield label="Adresse" name="adresse" cssClass="form-control" />
							</div>
							<div class="mb-3">
								<s:password label="Password" name="userPwd" cssClass="form-control" />
							</div>
							<div class="mb-4">
								<label class="form-label d-block fw-bold">Sexe</label>
								<div class="form-check form-check-inline">
									<input type="radio" class="form-check-input" name="male" value="true" id="sexeHomme" checked>
									<label class="form-check-label" for="sexeHomme">Homme</label>
								</div>
								<div class="form-check form-check-inline">
									<input type="radio" class="form-check-input" name="male" value="false" id="sexeFemme">
									<label class="form-check-label" for="sexeFemme">Femme</label>
								</div>
							</div>
							<div class="mb-4">
								<label class="form-label d-block fw-bold">Type</label>
								<div class="form-check form-check-inline">
									<input type="radio" class="form-check-input" name="client" value="true" id="typeClient" checked>
									<label class="form-check-label" for="typeClient">Client</label>
								</div>
								<div class="form-check form-check-inline">
									<input type="radio" class="form-check-input" name="client" value="false" id="typeManager">
									<label class="form-check-label" for="typeManager">Manager</label>
								</div>
							</div>
							<div class="mb-3">
								<s:textfield label="Numéro de client" name="numClient" cssClass="form-control" />
							</div>
							<s:submit name="submit" value="Créer l'utilisateur" cssClass="btn btn-primary w-100 mt-3" />
						</s:form>
					</div>
				</div>
				<s:form name="myForm" action="retourTableauDeBordManager" method="POST" cssClass="mt-3">
					<s:submit name="Retour" value="Retour" cssClass="btn btn-secondary w-100" />
				</s:form>
				<s:if test="(result == \"SUCCESS\")">
					<div class="alert alert-success mt-3" role="alert">
						<s:property value="message" />
					</div>
				</s:if>
				<s:else>
					<div class="alert alert-danger mt-3" role="alert">
						<s:property value="message" />
					</div>
				</s:else>
			</div>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>