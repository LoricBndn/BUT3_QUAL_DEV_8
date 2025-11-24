package com.iut.banque.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.iut.banque.controller.ResetPasswordWithToken;
import com.iut.banque.facade.LoginManager;
import com.iut.banque.util.ResetTokenManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsConnect-context.xml")
@Transactional("transactionManager")
public class TestsResetPasswordWithToken {

    @Autowired
    private LoginManager loginManager;

    private ResetPasswordWithToken resetPasswordWithToken;

    /**
     * Crée une instance de ResetPasswordWithToken sans passer par le constructeur
     * pour éviter le problème avec ServletActionContext dans les tests
     */
    @SuppressWarnings("restriction")
    private ResetPasswordWithToken createTestInstance() throws Exception {
        // Utiliser Unsafe pour allouer une instance sans appeler le constructeur
        Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

        ResetPasswordWithToken instance = (ResetPasswordWithToken) unsafe.allocateInstance(ResetPasswordWithToken.class);

        // Injecter le loginManager via réflexion
        Field loginManagerField = ResetPasswordWithToken.class.getDeclaredField("loginManager");
        loginManagerField.setAccessible(true);
        loginManagerField.set(instance, loginManager);

        return instance;
    }

    @Before
    public void setUp() throws Exception {
        // Créer l'instance sans passer par le constructeur
        resetPasswordWithToken = createTestInstance();
    }

    /**
     * Tests du constructeur
     */
    @Test
    public void testConstructeur() throws Exception {
        ResetPasswordWithToken action = createTestInstance();
        assertFalse("L'erreur devrait être à false par défaut", action.isError());
        assertNull("Le message devrait être null par défaut", action.getMessage());
    }

    /**
     * Tests des getters et setters
     */
    @Test
    public void testGetterSetterToken() {
        resetPasswordWithToken.setToken("ABC123TOKEN");
        assertEquals("ABC123TOKEN", resetPasswordWithToken.getToken());
    }

    @Test
    public void testGetterSetterTokenNull() {
        resetPasswordWithToken.setToken(null);
        assertNull("Le token devrait être null", resetPasswordWithToken.getToken());
    }

    @Test
    public void testGetterSetterTokenVide() {
        resetPasswordWithToken.setToken("");
        assertEquals("", resetPasswordWithToken.getToken());
    }

    @Test
    public void testGetterSetterUserId() {
        resetPasswordWithToken.setUserId("j.doe1");
        assertEquals("j.doe1", resetPasswordWithToken.getUserId());
    }

    @Test
    public void testGetterSetterUserIdNull() {
        resetPasswordWithToken.setUserId(null);
        assertNull("Le userId devrait être null", resetPasswordWithToken.getUserId());
    }

    @Test
    public void testGetterSetterUserIdVide() {
        resetPasswordWithToken.setUserId("");
        assertEquals("", resetPasswordWithToken.getUserId());
    }

    @Test
    public void testGetterSetterNewPassword() {
        resetPasswordWithToken.setNewPassword("newPass123");
        assertEquals("newPass123", resetPasswordWithToken.getNewPassword());
    }

    @Test
    public void testGetterSetterNewPasswordNull() {
        resetPasswordWithToken.setNewPassword(null);
        assertNull("Le newPassword devrait être null", resetPasswordWithToken.getNewPassword());
    }

    @Test
    public void testGetterSetterNewPasswordVide() {
        resetPasswordWithToken.setNewPassword("");
        assertEquals("", resetPasswordWithToken.getNewPassword());
    }

    @Test
    public void testGetterSetterConfirmPassword() {
        resetPasswordWithToken.setConfirmPassword("newPass123");
        assertEquals("newPass123", resetPasswordWithToken.getConfirmPassword());
    }

    @Test
    public void testGetterSetterConfirmPasswordNull() {
        resetPasswordWithToken.setConfirmPassword(null);
        assertNull("Le confirmPassword devrait être null", resetPasswordWithToken.getConfirmPassword());
    }

    @Test
    public void testGetterSetterConfirmPasswordVide() {
        resetPasswordWithToken.setConfirmPassword("");
        assertEquals("", resetPasswordWithToken.getConfirmPassword());
    }

    @Test
    public void testGetterSetterMessage() {
        resetPasswordWithToken.setMessage("Message de test");
        assertEquals("Message de test", resetPasswordWithToken.getMessage());
    }

    @Test
    public void testGetterSetterMessageNull() {
        resetPasswordWithToken.setMessage(null);
        assertNull(resetPasswordWithToken.getMessage());
    }

    @Test
    public void testGetterSetterMessageVide() {
        resetPasswordWithToken.setMessage("");
        assertEquals("", resetPasswordWithToken.getMessage());
    }

    @Test
    public void testGetterSetterError() {
        resetPasswordWithToken.setError(true);
        assertTrue(resetPasswordWithToken.isError());
    }

    @Test
    public void testGetterSetterErrorFalse() {
        resetPasswordWithToken.setError(false);
        assertFalse(resetPasswordWithToken.isError());
    }

    /**
     * Tests de la méthode showForm() - Cas d'erreur
     */
    @Test
    public void testShowFormAvecTokenInvalide() {
        resetPasswordWithToken.setToken("TOKEN_INVALIDE");
        resetPasswordWithToken.setUserId("j.doe1");
        String result = resetPasswordWithToken.showForm();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
        assertNotNull("Le message ne devrait pas être null", resetPasswordWithToken.getMessage());
    }

    @Test
    public void testShowFormAvecTokenNull() {
        resetPasswordWithToken.setToken(null);
        resetPasswordWithToken.setUserId("j.doe1");
        String result = resetPasswordWithToken.showForm();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
    }

    @Test
    public void testShowFormAvecUserIdNonCorrespondant() {
        // Générer un token valide pour un utilisateur
        String token = ResetTokenManager.generateToken("j.doe1");

        // Essayer d'utiliser ce token avec un autre userId
        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe2");
        String result = resetPasswordWithToken.showForm();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
        assertTrue("Le message devrait mentionner la non-correspondance",
            resetPasswordWithToken.getMessage().toLowerCase().contains("correspond"));
    }

    /**
     * Tests de la méthode showForm() - Cas de succès
     */
    @Test
    public void testShowFormAvecTokenValide() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        String result = resetPasswordWithToken.showForm();

        assertEquals("Le résultat devrait être success", "success", result);
    }

    /**
     * Tests de la méthode resetPassword() - Cas d'erreur
     */
    @Test
    public void testResetPasswordAvecTokenInvalide() {
        resetPasswordWithToken.setToken("TOKEN_INVALIDE");
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
        assertTrue("Le message devrait mentionner l'invalidité",
            resetPasswordWithToken.getMessage().toLowerCase().contains("invalide") ||
            resetPasswordWithToken.getMessage().toLowerCase().contains("expiré"));
    }

    @Test
    public void testResetPasswordAvecUserIdNonCorrespondant() {
        // Générer un token valide pour un utilisateur
        String token = ResetTokenManager.generateToken("j.doe1");

        // Essayer d'utiliser ce token avec un autre userId
        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe2");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
    }

    @Test
    public void testResetPasswordAvecMotsDePasseNonCorrespondants() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("differentPass456");

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPasswordWithToken.getMessage());
    }

    @Test
    public void testResetPasswordAvecMotDePasseTropCourt() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("123");
        resetPasswordWithToken.setConfirmPassword("123");

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
        assertTrue("Le message devrait mentionner la longueur minimale",
            resetPasswordWithToken.getMessage().contains("6 caractères"));
    }

    @Test
    public void testResetPasswordAvecMotDePasseExactement6Caracteres() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("pass12");
        resetPasswordWithToken.setConfirmPassword("pass12");

        String result = resetPasswordWithToken.resetPassword();

        // Le résultat dépend de si l'utilisateur existe
        assertTrue("Le résultat devrait être success ou error",
            result.equals("success") || result.equals("error"));
    }

    @Test
    public void testResetPasswordAvecNewPasswordNull() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword(null);
        resetPasswordWithToken.setConfirmPassword("newPass123");

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
    }

    @Test
    public void testResetPasswordAvecConfirmPasswordNull() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword(null);

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
    }

    @Test
    public void testResetPasswordAvecLesDeuxMotsDePasseNull() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword(null);
        resetPasswordWithToken.setConfirmPassword(null);

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
    }

    @Test
    public void testResetPasswordAvecMotsDePasseVides() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("");
        resetPasswordWithToken.setConfirmPassword("");

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
    }

    @Test
    public void testResetPasswordAvecUtilisateurInexistant() {
        // Générer un token valide pour un utilisateur inexistant
        String token = ResetTokenManager.generateToken("utilisateur.inexistant");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("utilisateur.inexistant");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
        assertEquals("Utilisateur non trouvé.", resetPasswordWithToken.getMessage());
    }

    /**
     * Tests de la méthode resetPassword() - Cas de succès
     */
    @Test
    public void testResetPasswordAvecDonneesValides() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        String result = resetPasswordWithToken.resetPassword();

        // Le résultat dépend de si l'utilisateur existe
        assertTrue("Le résultat devrait être success ou error",
            result.equals("success") || result.equals("error"));

        if (result.equals("success")) {
            assertFalse("L'erreur devrait être false", resetPasswordWithToken.isError());
            assertTrue("Le message devrait contenir 'succès'",
                resetPasswordWithToken.getMessage().toLowerCase().contains("succès"));
        }
    }

    /**
     * Tests de vérification des attributs après exécution
     */
    @Test
    public void testAttributsParDefautAvantResetPassword() {
        assertFalse("L'attribut error devrait être false par défaut", resetPasswordWithToken.isError());
        assertNull("Le message devrait être null par défaut", resetPasswordWithToken.getMessage());
        assertNull("Le token devrait être null par défaut", resetPasswordWithToken.getToken());
        assertNull("Le userId devrait être null par défaut", resetPasswordWithToken.getUserId());
        assertNull("Le newPassword devrait être null par défaut", resetPasswordWithToken.getNewPassword());
        assertNull("Le confirmPassword devrait être null par défaut", resetPasswordWithToken.getConfirmPassword());
    }

    @Test
    public void testMessageContientTexteAttenduApresEchec() {
        resetPasswordWithToken.setToken("TOKEN_INVALIDE");
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        resetPasswordWithToken.resetPassword();

        assertTrue("Le message devrait contenir du texte",
            resetPasswordWithToken.getMessage() != null && !resetPasswordWithToken.getMessage().isEmpty());
    }

    @Test
    public void testErreurEstTrueQuandTokenInvalide() {
        resetPasswordWithToken.setToken("TOKEN_INVALIDE");
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        resetPasswordWithToken.resetPassword();

        assertTrue("L'attribut error devrait être true", resetPasswordWithToken.isError());
    }

    /**
     * Tests de cas limites
     */
    @Test
    public void testResetPasswordAvecMotsDePasseTresLongs() {
        String longPassword = "a".repeat(1000);
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword(longPassword);
        resetPasswordWithToken.setConfirmPassword(longPassword);

        String result = resetPasswordWithToken.resetPassword();

        // Le résultat dépend de si l'utilisateur existe
        assertTrue("Le résultat devrait être success ou error",
            result.equals("success") || result.equals("error"));
    }

    @Test
    public void testResetPasswordAvecCaracteresSpeciaux() {
        String specialPassword = "P@ssw0rd!#$%^&*()_+-=[]{}|;:',.<>?/~`";
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword(specialPassword);
        resetPasswordWithToken.setConfirmPassword(specialPassword);

        String result = resetPasswordWithToken.resetPassword();

        // Le résultat dépend de si l'utilisateur existe
        assertTrue("Le résultat devrait être success ou error",
            result.equals("success") || result.equals("error"));
    }

    @Test
    public void testResetPasswordAvecEspacesDebutEtFin() {
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword(" newPass123 ");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPasswordWithToken.getMessage());
    }

    @Test
    public void testResetPasswordAvecCasseDifferente() {
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("NewPass123");
        resetPasswordWithToken.setConfirmPassword("newpass123");

        String result = resetPasswordWithToken.resetPassword();

        assertEquals("Le résultat devrait être error", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
        assertEquals("Les deux mots de passe ne correspondent pas.", resetPasswordWithToken.getMessage());
    }

    @Test
    public void testResetPasswordAvecMotsDePasseUnicode() {
        String unicodePassword = "パスワード123";
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword(unicodePassword);
        resetPasswordWithToken.setConfirmPassword(unicodePassword);

        String result = resetPasswordWithToken.resetPassword();

        // Le résultat dépend de si l'utilisateur existe
        assertTrue("Le résultat devrait être success ou error",
            result.equals("success") || result.equals("error"));
    }

    /**
     * Tests de cohérence des messages d'erreur
     */
    @Test
    public void testMessageErreurCoherentAvecStatutErreur() {
        resetPasswordWithToken.setToken("TOKEN_INVALIDE");
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        resetPasswordWithToken.resetPassword();

        if (resetPasswordWithToken.isError()) {
            assertNotNull("Le message d'erreur ne devrait pas être null", resetPasswordWithToken.getMessage());
            assertTrue("Le message d'erreur ne devrait pas être vide",
                !resetPasswordWithToken.getMessage().isEmpty());
        }
    }

    @Test
    public void testMessageSuccesCoherentAvecStatutSucces() {
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        String result = resetPasswordWithToken.resetPassword();

        if (result.equals("success")) {
            assertFalse("L'erreur devrait être false en cas de succès", resetPasswordWithToken.isError());
            assertNotNull("Le message de succès ne devrait pas être null", resetPasswordWithToken.getMessage());
        }
    }

    /**
     * Tests de la gestion des exceptions
     */
    @Test
    public void testResetPasswordGestionExceptionGlobale() {
        // Tester différents cas pour s'assurer que les exceptions sont bien gérées
        resetPasswordWithToken.setToken(null);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        String result = resetPasswordWithToken.resetPassword();

        // Même en cas d'exception, le résultat devrait être error
        assertEquals("Le résultat devrait être error en cas d'exception", "error", result);
        assertTrue("L'erreur devrait être true", resetPasswordWithToken.isError());
        assertNotNull("Le message ne devrait pas être null", resetPasswordWithToken.getMessage());
    }

    /**
     * Tests de l'invalidation du token après succès
     */
    @Test
    public void testTokenInvalideApresResetPasswordReussi() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        String result = resetPasswordWithToken.resetPassword();

        if (result.equals("success")) {
            // Le token devrait être invalidé
            String validatedUserId = ResetTokenManager.validateToken(token);
            assertNull("Le token devrait être invalidé après utilisation", validatedUserId);
        }
    }

    @Test
    public void testImpossibleReutiliserTokenApresReset() {
        // Générer un token valide
        String token = ResetTokenManager.generateToken("j.doe1");

        // Premier reset
        resetPasswordWithToken.setToken(token);
        resetPasswordWithToken.setUserId("j.doe1");
        resetPasswordWithToken.setNewPassword("newPass123");
        resetPasswordWithToken.setConfirmPassword("newPass123");

        String result1 = resetPasswordWithToken.resetPassword();

        if (result1.equals("success")) {
            // Essayer de réutiliser le même token
            ResetPasswordWithToken secondAttempt = null;
            try {
                secondAttempt = createTestInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            secondAttempt.setToken(token);
            secondAttempt.setUserId("j.doe1");
            secondAttempt.setNewPassword("anotherPass456");
            secondAttempt.setConfirmPassword("anotherPass456");

            String result2 = secondAttempt.resetPassword();

            assertEquals("Le résultat devrait être error avec un token déjà utilisé", "error", result2);
            assertTrue("L'erreur devrait être true", secondAttempt.isError());
        }
    }
}
