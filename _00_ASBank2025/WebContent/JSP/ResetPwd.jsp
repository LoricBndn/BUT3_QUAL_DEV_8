<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Réinitialiser mot de passe</title>
    <link rel="stylesheet" href="/_00_ASBank2023/style/style.css" />
    <script src="/_00_ASBank2023/js/jquery.js"></script>
</head>

<body>
<h1>Réinitialiser mot de passe</h1>

<%--<p>Debug : userCde = <s:property value="userCde"/></p>--%>

<s:form id="resetPwdForm" name="resetPwdForm" action="resetPwdForm" method="POST">

    <s:hidden name="userCde" value="%{userCde}" />

    <s:password label="Ancien mot de passe" name="oldPassword" />

    <s:password label="Nouveau mot de passe" name="newPassword" />

    <s:password label="Confirmer le nouveau mot de passe" name="confirmPassword" />

    <s:submit name="submit" value="Confirmer" />
</s:form>

<div class="btnBack">
    <s:form name="myForm" action="retourTableauDeBordClient" method="POST">
        <s:submit name="Retour" value="Retour" />
    </s:form>
</div>

<s:if test="%{error != \"\"}">
    <div class="failure">
        Erreur :
        <s:property value="message" />
    </div>
</s:if>

</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>
