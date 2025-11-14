package com.iut.banque.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.iut.banque.controller.ResetPwd;
import org.junit.Before;
import org.junit.Test;

public class TestsResetPwd {

    private ResetPwd resetPwd;

    @Before
    public void setUp() {
        resetPwd = new ResetPwd();
    }

    /**
     * Tests du constructeur
     */
    @Test
    public void testConstructeur() {
        ResetPwd action = new ResetPwd();
        assertFalse("L'erreur devrait être à false par défaut", action.isError());
        assertFalse("Le résultat devrait être à false par défaut", action.isResult());
    }

    /**
     * Tests des getters et setters
     */
    @Test
    public void testGetterSetterUserCde() {
        resetPwd.setUserCde("j.doe1");
        assertEquals("j.doe1", resetPwd.getUserCde());
    }

    @Test
    public void testGetterSetterUserCdeNull() {
        resetPwd.setUserCde(null);
        assertEquals(null, resetPwd.getUserCde());
    }

    @Test
    public void testGetterSetterOldPassword() {
        resetPwd.setOldPassword("oldPass123");
        assertEquals("oldPass123", resetPwd.getOldPassword());
    }

    @Test
    public void testGetterSetterOldPasswordNull() {
        resetPwd.setOldPassword(null);
        assertEquals(null, resetPwd.getOldPassword());
    }

    @Test
    public void testGetterSetterNewPassword() {
        resetPwd.setNewPassword("newPass456");
        assertEquals("newPass456", resetPwd.getNewPassword());
    }

    @Test
    public void testGetterSetterNewPasswordNull() {
        resetPwd.setNewPassword(null);
        assertEquals(null, resetPwd.getNewPassword());
    }

    @Test
    public void testGetterSetterConfirmPassword() {
        resetPwd.setConfirmPassword("newPass456");
        assertEquals("newPass456", resetPwd.getConfirmPassword());
    }

    @Test
    public void testGetterSetterConfirmPasswordNull() {
        resetPwd.setConfirmPassword(null);
        assertEquals(null, resetPwd.getConfirmPassword());
    }

    @Test
    public void testGetterSetterMessage() {
        resetPwd.setMessage("Message de test");
        assertEquals("Message de test", resetPwd.getMessage());
    }

    @Test
    public void testGetterSetterMessageNull() {
        resetPwd.setMessage(null);
        assertEquals(null, resetPwd.getMessage());
    }

    @Test
    public void testGetterSetterMessageVide() {
        resetPwd.setMessage("");
        assertEquals("", resetPwd.getMessage());
    }

    @Test
    public void testGetterSetterError() {
        resetPwd.setError(true);
        assertTrue(resetPwd.isError());
    }

    @Test
    public void testGetterSetterErrorFalse() {
        resetPwd.setError(false);
        assertFalse(resetPwd.isError());
    }

    @Test
    public void testGetterSetterResult() {
        resetPwd.setResult(true);
        assertTrue(resetPwd.isResult());
    }

    @Test
    public void testGetterSetterResultFalse() {
        resetPwd.setResult(false);
        assertFalse(resetPwd.isResult());
    }

    /**
     * Tests de la méthode reset() - Validation des mots de passe
     */
    @Test
    public void testResetAvecMotsDePasseNonCorrespondants() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("newPass456");
        resetPwd.setConfirmPassword("differentPass789");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPwd.getMessage());
    }

    @Test
    public void testResetAvecMotsDePasseVidesNonCorrespondants() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("");
        resetPwd.setConfirmPassword("something");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPwd.getMessage());
    }

    @Test
    public void testResetAvecNouveauMotDePasseNull() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword(null);
        resetPwd.setConfirmPassword("something");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    @Test
    public void testResetAvecConfirmPasswordNull() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("something");
        resetPwd.setConfirmPassword(null);

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    @Test
    public void testResetAvecLesDeuxNouveauxMotsDePasseNull() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword(null);
        resetPwd.setConfirmPassword(null);

        String result = resetPwd.reset();

        // Deux null sont égaux selon equals(), donc pas d'erreur de correspondance
        // Mais une erreur devrait survenir plus tard (NullPointerException)
        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    @Test
    public void testResetAvecMotsDePasseIdentiquesVides() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("");
        resetPwd.setConfirmPassword("");

        String result = resetPwd.reset();

        // Les mots de passe correspondent, mais le reset échouera probablement
        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    @Test
    public void testResetAvecMotsDePasseComplexesNonCorrespondants() {
        String password1 = "P@ssw0rd!2024#Secure";
        String password2 = "P@ssw0rd!2024#Secur3";
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldComplexPass");
        resetPwd.setNewPassword(password1);
        resetPwd.setConfirmPassword(password2);

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPwd.getMessage());
    }

    @Test
    public void testResetAvecMotsDePasseAvecEspaces() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("new Pass 456");
        resetPwd.setConfirmPassword("new Pass 456");

        String result = resetPwd.reset();

        // Les mots de passe correspondent, mais le reset échouera probablement
        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    @Test
    public void testResetAvecMotsDePasseTresLongs() {
        String longPassword = "a".repeat(100);
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword(longPassword);
        resetPwd.setConfirmPassword(longPassword + "different");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPwd.getMessage());
    }

    @Test
    public void testResetAvecCaracteresSpeciaux() {
        String specialPassword = "P@ssw0rd!#$%^&*()_+-=[]{}|;:',.<>?/~`";
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword(specialPassword);
        resetPwd.setConfirmPassword(specialPassword + "x");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPwd.getMessage());
    }

    /**
     * Tests avec différents formats de userCde
     */
    @Test
    public void testResetAvecUserCdeValide() {
        resetPwd.setUserCde("a.utilisateur928");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("newPass456");
        resetPwd.setConfirmPassword("differentPass789");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    @Test
    public void testResetAvecUserCdeNull() {
        resetPwd.setUserCde(null);
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("newPass456");
        resetPwd.setConfirmPassword("newPass456");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    @Test
    public void testResetAvecUserCdeVide() {
        resetPwd.setUserCde("");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("newPass456");
        resetPwd.setConfirmPassword("newPass456");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    @Test
    public void testResetAvecOldPasswordNull() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword(null);
        resetPwd.setNewPassword("newPass456");
        resetPwd.setConfirmPassword("newPass456");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    @Test
    public void testResetAvecOldPasswordVide() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("");
        resetPwd.setNewPassword("newPass456");
        resetPwd.setConfirmPassword("newPass456");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    /**
     * Tests de vérification des attributs après exécution
     */
    @Test
    public void testAttributsParDefautAvantReset() {
        assertFalse("L'attribut error devrait être false par défaut", resetPwd.isError());
        assertFalse("L'attribut result devrait être false par défaut", resetPwd.isResult());
        assertEquals(null, resetPwd.getMessage());
        assertEquals(null, resetPwd.getUserCde());
        assertEquals(null, resetPwd.getOldPassword());
        assertEquals(null, resetPwd.getNewPassword());
        assertEquals(null, resetPwd.getConfirmPassword());
    }

    @Test
    public void testMessageContientTexteAttendu() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("newPass456");
        resetPwd.setConfirmPassword("differentPass789");

        resetPwd.reset();

        assertTrue("Le message devrait contenir le texte attendu",
                resetPwd.getMessage().contains("mots de passe"));
    }

    @Test
    public void testErreurEstTrueQuandMotsDePasseNonCorrespondants() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("newPass456");
        resetPwd.setConfirmPassword("differentPass789");

        resetPwd.reset();

        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    /**
     * Tests de cas limites
     */
    @Test
    public void testResetAvecTousLesChampsMemeValeur() {
        resetPwd.setUserCde("test");
        resetPwd.setOldPassword("test");
        resetPwd.setNewPassword("test");
        resetPwd.setConfirmPassword("test");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
    }

    @Test
    public void testResetAvecMotsDePasseUnicode() {
        String unicodePassword = "パスワード123";
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword(unicodePassword);
        resetPwd.setConfirmPassword(unicodePassword + "差");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPwd.getMessage());
    }

    @Test
    public void testResetAvecEspacesDebutEtFin() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword(" newPass456 ");
        resetPwd.setConfirmPassword("newPass456");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPwd.getMessage());
    }

    @Test
    public void testResetAvecCasseDifferente() {
        resetPwd.setUserCde("j.doe1");
        resetPwd.setOldPassword("oldPass123");
        resetPwd.setNewPassword("NewPass456");
        resetPwd.setConfirmPassword("newpass456");

        String result = resetPwd.reset();

        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'attribut error devrait être true", resetPwd.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPwd.getMessage());
    }
}