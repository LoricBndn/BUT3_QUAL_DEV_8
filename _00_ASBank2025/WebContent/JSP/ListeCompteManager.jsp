<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Liste des comptes de la banque</title>
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
		<s:if test="aDecouvert">
			<h1 class="text-center mb-4">Liste des comptes à découvert de la banque</h1>
		</s:if>
		<s:else>
			<h1 class="text-center mb-4">Liste des comptes de la banque</h1>
		</s:else>
		<div class="mb-3">
			<s:form name="myForm" action="retourTableauDeBordManager" method="POST">
				<s:submit name="Retour" value="Retour" cssClass="btn btn-secondary" />
			</s:form>
		</div>
		<s:if test="aDecouvert">
			<p class="lead text-center">Voici les comptes à découvert de la banque :</p>
		</s:if>
		<s:else>
			<p class="lead text-center">Voici l'état des comptes de la banque :</p>
		</s:else>
		<div class="table-responsive">
			<table class="table table-striped table-hover table-bordered">
				<s:iterator value="allClients">
					<s:if test="(value.possedeComptesADecouvert() || !aDecouvert)">
						<thead class="table-light">
							<tr>
								<th colspan="<s:if test="(!aDecouvert)">5</s:if><s:else>3</s:else>">
									<strong>Client :</strong> <s:property value="value.prenom" /> <s:property value="value.nom" />
									<span class="badge bg-info">n°<s:property value="value.numeroClient" /></span>
									<s:if test="(!aDecouvert)">
										<div class="float-end">
											<s:url action="urlAddAccount" var="addAccount">
												<s:param name="client"><s:property value="value.userId" /></s:param>
											</s:url>
											<s:a href="%{addAccount}" cssClass="btn btn-sm btn-success me-1" title="Créer un compte pour ce client">
												<i class="bi bi-plus-circle"></i> Créer compte
											</s:a>
											<s:url action="deleteUser" var="deleteUser">
												<s:param name="client"><s:property value="value.userId" /></s:param>
											</s:url>
											<s:a href="%{deleteUser}" onclick="return confirm('Voulez-vous vraiment supprimer cet utilisateur ?')" cssClass="btn btn-sm btn-danger" title="Supprimer ce client et tous ses comptes associés">
												<i class="bi bi-trash"></i> Supprimer
											</s:a>
										</div>
									</s:if>
								</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="value.accounts">
								<s:if test="(value.solde <0 || !aDecouvert)">
									<tr>
										<td><strong><s:property value="key" /></strong></td>
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
										<s:if test="(!aDecouvert)">
											<s:url action="editAccount" var="editAccount">
												<s:param name="compte"><s:property value="value.numeroCompte" /></s:param>
											</s:url>
											<td>
												<s:a href="%{editAccount}" cssClass="btn btn-sm btn-primary" title="Editer ce compte">
													<i class="bi bi-pencil"></i>
												</s:a>
											</td>
											<td>
												<s:url action="deleteAccount" var="deleteAccount">
													<s:param name="compte"><s:property value="value.numeroCompte" /></s:param>
													<s:param name="client"><s:property value="value.owner.userId" /></s:param>
												</s:url>
												<s:a href="%{deleteAccount}" onclick="return confirm('Voulez-vous vraiment supprimer ce compte ?')" cssClass="btn btn-sm btn-danger" title="Supprimer ce compte">
													<i class="bi bi-trash"></i>
												</s:a>
											</td>
										</s:if>
									</tr>
								</s:if>
							</s:iterator>
						</tbody>
					</s:if>
				</s:iterator>
			</table>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>