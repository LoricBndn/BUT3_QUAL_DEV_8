<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Résultat de la suppression</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
</head>
<body>
<div class="container mt-5">
	<h1 class="text-center mb-4">Résultat de la suppression</h1>
	<div class="row justify-content-center">
		<div class="col-md-8">
			<s:if test="!error">
				<div class="alert alert-success" role="alert">
					<h4 class="alert-heading">Succès !</h4>
					<s:if test="isAccount()">
						Le compte <strong><s:property value="compteInfo"/></strong> du client <strong><s:property value="client" /></strong> a bien été supprimé.
					</s:if>
					<s:else>
						Le client <strong><s:property value="userInfo"/></strong> a bien été supprimé.
					</s:else>
				</div>
			</s:if>
			<s:else>
				<div class="alert alert-danger" role="alert">
					<h4 class="alert-heading">Erreur de suppression</h4>
					<s:if test="%{errorMessage == \"NONEMPTYACCOUNT\"}">
						<s:if test="isAccount()">
							<p>Une erreur est parvenue lors de la suppression du compte <strong><s:property value="compte" /></strong> du client <strong><s:property value="client" /></strong>.</p>
							<p>Le compte a un solde différent de zéro (<span class="text-danger fw-bold"><s:property value="compte.solde" /> €</span>).</p>
						</s:if>
						<s:else>
							<p>Une erreur est parvenue lors de la suppression du client <strong><s:property value="client" /></strong>.</p>
							<p>Les comptes suivants ont un solde différent de zéro :</p>
							<ul class="mb-0">
								<s:iterator value="client.comptesAvecSoldeNonNul">
									<li><s:property value="value" /> (<span class="text-danger fw-bold"><s:property value="value.solde" /> €</span>)</li>
								</s:iterator>
							</ul>
						</s:else>
					</s:if>
					<s:else>
						<s:if test="isAccount()">
							<p>Une erreur est parvenue lors de la suppression du compte <strong><s:property value="compte" /></strong> du client <strong><s:property value="client" /></strong>.</p>
							<p class="mb-0">Veuillez réessayer.</p>
						</s:if>
						<s:else>
							<p>Une erreur est parvenue lors de la suppression du client <strong><s:property value="client" /></strong>.</p>
							<p class="mb-0">Veuillez réessayer.</p>
						</s:else>
					</s:else>
				</div>
			</s:else>
			<s:form name="myForm" action="listeCompteManager" method="POST">
				<s:submit name="Retour" value="Retour" cssClass="btn btn-primary w-100" />
			</s:form>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>