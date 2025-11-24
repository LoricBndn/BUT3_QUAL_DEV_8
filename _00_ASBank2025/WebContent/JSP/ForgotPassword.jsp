<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Mot de passe oublié</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
</head>
<body>
<div class="container mt-5">
	<div class="row justify-content-center">
		<div class="col-md-6">
			<div class="card shadow">
				<div class="card-body">
					<h1 class="card-title text-center mb-4">Mot de passe oublié</h1>
					<p class="text-muted">Entrez votre identifiant pour recevoir un lien de réinitialisation par email.</p>
					<s:form id="forgotPasswordForm" name="forgotPasswordForm" action="forgotPasswordRequest" method="POST">
						<s:textfield label="Identifiant utilisateur" name="userCde" required="true" cssClass="form-control mb-3" />
						<s:submit value="Envoyer le lien de réinitialisation" cssClass="btn btn-primary w-100" />
					</s:form>
					<s:form name="myFormRetour" action="redirectionLogin" method="POST" cssClass="mt-3">
						<s:submit name="Retour" value="Retour à la page de connexion" cssClass="btn btn-secondary w-100" />
					</s:form>
				</div>
			</div>
			<s:if test="%{message != null && message != ''}">
				<s:if test="error">
					<div class="alert alert-danger mt-3" role="alert">
						<s:property value="message" />
					</div>
				</s:if>
				<s:else>
					<div class="alert alert-success mt-3" role="alert">
						<s:property value="message" />
						<hr>
						<p class="mb-0 small">
							<strong>Note:</strong> En mode développement, le lien apparaît dans la console du serveur.
						</p>
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