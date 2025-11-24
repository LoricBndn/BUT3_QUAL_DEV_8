<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Réinitialiser mot de passe</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
<script src="/_00_ASBank2025/js/jquery.js"></script>
</head>
<body>
<div class="container mt-5">
	<div class="row justify-content-center">
		<div class="col-md-6">
			<h1 class="text-center mb-4">Réinitialiser mot de passe</h1>
			<div class="card shadow">
				<div class="card-body">
					<s:form id="resetPwdForm" name="resetPwdForm" action="resetPwdForm" method="POST">
						<s:hidden name="userCde" value="%{userCde}" />
						<s:password label="Ancien mot de passe" name="oldPassword" cssClass="form-control mb-3" />
						<s:password label="Nouveau mot de passe" name="newPassword" cssClass="form-control mb-3" />
						<s:password label="Confirmer le nouveau mot de passe" name="confirmPassword" cssClass="form-control mb-3" />
						<s:submit name="submit" value="Confirmer" cssClass="btn btn-primary w-100" />
					</s:form>
				</div>
			</div>
			<div class="mt-3">
				<s:form name="myForm" action="retourTableauDeBordClient" method="POST">
					<s:submit name="Retour" value="Retour" cssClass="btn btn-secondary w-100" />
				</s:form>
			</div>
			<s:if test="%{error != \"\"}">
				<div class="alert alert-danger mt-3" role="alert">
					<strong>Erreur :</strong> <s:property value="message" />
				</div>
			</s:if>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>
