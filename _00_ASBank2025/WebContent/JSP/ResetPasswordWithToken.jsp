<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Réinitialiser le mot de passe</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
</head>
<body>
<div class="container mt-5">
	<div class="row justify-content-center">
		<div class="col-md-6">
			<h1 class="text-center mb-4">Réinitialiser votre mot de passe</h1>
			<s:if test="error">
				<div class="alert alert-danger" role="alert">
					<s:property value="message" />
				</div>
				<s:form name="myFormRetour" action="redirectionLogin" method="POST">
					<s:submit name="Retour" value="Retour à la page de connexion" cssClass="btn btn-primary w-100" />
				</s:form>
			</s:if>
			<s:else>
				<s:if test="%{message != null && message != ''}">
					<div class="alert alert-success" role="alert">
						<s:property value="message" />
					</div>
					<s:form name="myFormRetour" action="redirectionLogin" method="POST">
						<s:submit name="Retour" value="Aller à la page de connexion" cssClass="btn btn-primary w-100" />
					</s:form>
				</s:if>
				<s:else>
					<div class="card shadow">
						<div class="card-body">
							<p class="text-muted">Entrez votre nouveau mot de passe pour l'utilisateur : <strong><s:property value="userId" /></strong></p>
							<s:form id="resetPasswordForm" name="resetPasswordForm" action="resetPasswordWithTokenSubmit" method="POST">
								<s:hidden name="token" value="%{token}" />
								<s:hidden name="userId" value="%{userId}" />
								<s:password label="Nouveau mot de passe" name="newPassword" required="true" cssClass="form-control mb-3" />
								<s:password label="Confirmer le nouveau mot de passe" name="confirmPassword" required="true" cssClass="form-control mb-3" />
								<s:submit value="Réinitialiser le mot de passe" cssClass="btn btn-primary w-100" />
							</s:form>
						</div>
					</div>
				</s:else>
			</s:else>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>