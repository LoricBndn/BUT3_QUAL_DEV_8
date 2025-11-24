<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Application IUT Bank</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
<link href="/_00_ASBank2025/style/favicon.ico" rel="icon" type="image/x-icon" />
</head>
<body>
<div class="container mt-5">
	<h1 class="text-center mb-4">Bienvenue sur l'application IUT Bank 2025</h1>
	<div class="text-center mb-4">
		<img src="https://dptinfo.iutmetz.univ-lorraine.fr/lp/img/Logo_IUT_Metz.Info_UL.small.png" alt="logo" class="img-fluid" />
	</div>
	<div class="text-center mb-4">
		<button type="button" class="btn btn-info" onclick="DisplayMessage()">Information</button>
	</div>
	<div class="text-center">
		<s:url action="redirectionLogin" var="redirectionLogin" ></s:url>
		<s:a href="%{redirectionLogin}" cssClass="btn btn-primary btn-lg">Page de Login</s:a>
	</div>
</div>
<script type="text/javascript">
	function DisplayMessage() {
		alert('Ce TD a été donné pour les AS dans le cadre du cours de CO Avancé (Promotion 2017-2018)');
	}
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>