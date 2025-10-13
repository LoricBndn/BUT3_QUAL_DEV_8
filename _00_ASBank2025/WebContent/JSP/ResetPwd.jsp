<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Réinitialiser mot de passe</title>
    <link rel="stylesheet" href="/_00_ASBank2023/style/style.css" />
    <script src="/_00_ASBank2025/js/jquery.js"></script>
</head>

<body>
<h1>Réinitialiser mot de passe</h1>

<s:form id="resetPwd" name="resetPwd" action="resetPwd" method="POST">

    <s:password label="Ancien mot de passe" name="oldPassword" />

    <s:password label="Nouveau mot de passe" name="newPassword" />

    <s:password label="Confirmer le nouveau mot de passe" name="confirmPassword" />

    <s:submit name="submit" value="Confirmer" />
</s:form>

<s:form name="backForm" action="listeCompteManager" method="POST">
    <s:submit name="Retour" value="Retour" />
</s:form>

<s:if test="result">
    <s:if test="!error">
        <div class="success">
            <s:property value="message" />
        </div>
    </s:if>
    <s:else>
        <div class="failure">
            <s:property value="message" />
        </div>
    </s:else>
</s:if>

</body>
<jsp:include page="/JSP/Footer.jsp" />
</html>
