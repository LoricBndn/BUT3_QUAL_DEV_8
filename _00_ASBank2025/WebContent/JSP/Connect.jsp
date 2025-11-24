<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tableau de bord</title>
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
    <div class="position-absolute top-0 start-0 m-3" style="margin-top: 4rem !important;">
        <s:a action="urlResetPwd" cssClass="btn btn-warning">
            <s:param name="userCde" value="connectedUser.userId" />
            Changer mon mot de passe
        </s:a>
    </div>
	<div class="container mt-5 pt-4">
		<h1 class="text-center mb-4">Tableau de bord</h1>
		<div class="alert alert-info text-center">
			Bienvenue <strong><s:property value="connectedUser.prenom" /> <s:property value="connectedUser.nom" /></strong> !
		</div>
		<p class="text-center lead">Voici l'état de vos comptes :</p>
		<div class="table-responsive">
			<table class="table table-striped table-hover table-bordered">
				<thead class="table-primary">
					<tr>
						<th>Numéro de compte</th>
						<th>Type de compte</th>
						<th>Solde actuel</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="accounts">
						<tr>
							<td>
								<s:url action="urlDetail" var="urlDetail">
									<s:param name="compte"><s:property value="key" /></s:param>
								</s:url>
								<s:a href="%{urlDetail}" cssClass="text-decoration-none fw-bold">
									<s:property value="key" />
								</s:a>
							</td>
							<s:if test="%{value.className == \"CompteAvecDecouvert\"}">
								<td><span class="badge bg-success">Découvert possible</span></td>
							</s:if>
							<s:else>
								<td><span class="badge bg-secondary">Simple</span></td>
							</s:else>
							<s:if test="%{value.solde >= 0}">
								<td><s:property value="value.solde" /> €</td>
							</s:if>
							<s:else>
								<td class="text-danger fw-bold"><s:property value="value.solde" /> €</td>
							</s:else>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>