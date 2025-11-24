<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Page de connexion</title>
<link href="https://cdn.jsdelivr.net/npm/bootswatch@5/dist/zephyr/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/_00_ASBank2025/style/style.css" />
</head>
<body>
<div class="container">
<div class="row justify-content-center align-items-center min-vh-100">
<div class="col-12 col-sm-10 col-md-6 col-lg-5">
<div class="card shadow mx-auto">
<div class="card-body p-4">
<h1 class="card-title text-center mb-4">Login</h1>
<s:form name="myForm" action="controller.Connect.login.action" method="POST" cssClass="text-center">
    <div class="mb-3">
        <s:textfield label="Code user" name="userCde" cssClass="form-control" />
    </div>
    <div class="mb-3">
        <s:password label="Password" name="userPwd" cssClass="form-control" />
    </div>
    <s:submit name="submit" cssClass="btn btn-primary w-100 mb-3" value="Se connecter" />
</s:form>

<div class="text-center mt-3">
    <s:url action="forgotPasswordForm" var="forgotPasswordUrl" />
    <s:a href="%{forgotPasswordUrl}" cssClass="text-decoration-none">Mot de passe oublié ?</s:a>
</div>

<s:form name="myFormRetour" action="retourAccueil" method="POST" cssClass="mt-3 text-center">
    <s:submit name="Retour" value="Retour à l'accueil" cssClass="btn btn-secondary w-100" />
</s:form>
</div>
</div>
</div>
</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>