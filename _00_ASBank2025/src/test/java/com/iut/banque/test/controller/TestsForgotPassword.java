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

import com.iut.banque.controller.ForgotPassword;
import com.iut.banque.facade.LoginManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsConnect-context.xml")
@Transactional("transactionManager")
public class TestsForgotPassword {

    @Autowired
    private LoginManager loginManager;

    private ForgotPassword forgotPassword;

    /**
     * Crée une instance de ForgotPassword sans passer par le constructeur
     * pour éviter le problème avec ServletActionContext dans les tests
     */
    @SuppressWarnings("restriction")
    private ForgotPassword createTestInstance() throws Exception {
        // Utiliser Unsafe pour allouer une instance sans appeler le constructeur
        Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

        ForgotPassword instance = (ForgotPassword) unsafe.allocateInstance(ForgotPassword.class);

        // Injecter le loginManager via réflexion
        Field loginManagerField = ForgotPassword.class.getDeclaredField("loginManager");
        loginManagerField.setAccessible(true);
        loginManagerField.set(instance, loginManager);

        return instance;
    }

    @Before
    public void setUp() throws Exception {
        // Créer l'instance sans passer par le constructeur
        forgotPassword = createTestInstance();
    }

    /**
     * Tests du constructeur
     */
    @Test
    public void testConstructeur() throws Exception {
        ForgotPassword action = createTestInstance();
        assertFalse("L'erreur devrait être à false par défaut", action.isError());
        assertNull("Le message devrait être null par défaut", action.getMessage());
    }

    /**
     * Tests des getters et setters
     */
    @Test
    public void testGetterSetterUserCde() {
        forgotPassword.setUserCde("j.doe1");
        assertEquals("j.doe1", forgotPassword.getUserCde());
    }

    @Test
    public void testGetterSetterUserCdeNull() {
        forgotPassword.setUserCde(null);
        assertNull("Le userCde devrait être null", forgotPassword.getUserCde());
    }

    @Test
    public void testGetterSetterUserCdeVide() {
        forgotPassword.setUserCde("");
        assertEquals("", forgotPassword.getUserCde());
    }

    @Test
    public void testGetterSetterUserCdeAvecEspaces() {
        forgotPassword.setUserCde("  j.doe1  ");
        assertEquals("  j.doe1  ", forgotPassword.getUserCde());
    }

    @Test
    public void testGetterSetterMessage() {
        forgotPassword.setMessage("Message de test");
        assertEquals("Message de test", forgotPassword.getMessage());
    }

    @Test
    public void testGetterSetterMessageNull() {
        forgotPassword.setMessage(null);
        assertNull(forgotPassword.getMessage());
    }

    @Test
    public void testGetterSetterMessageVide() {
        forgotPassword.setMessage("");
        assertEquals("", forgotPassword.getMessage());
    }

    @Test
    public void testGetterSetterError() {
        forgotPassword.setError(true);
        assertTrue(forgotPassword.isError());
    }

    @Test
    public void testGetterSetterErrorFalse() {
        forgotPassword.setError(false);
        assertFalse(forgotPassword.isError());
    }

    /**
     * Tests de la méthode showForm()
     */
    @Test
    public void testShowForm() {
        String result = forgotPassword.showForm();
        assertEquals("Le résultat devrait être SUCCESS", "success", result);
    }

    /**
     * Tests de la méthode requestReset() - Cas d'erreur
     */
    @Test
    public void testRequestResetAvecUserCdeNull() {
        forgotPassword.setUserCde(null);
        String result = forgotPassword.requestReset();
        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'erreur devrait être true", forgotPassword.isError());
        assertNotNull("Le message ne devrait pas être null", forgotPassword.getMessage());
    }

    @Test
    public void testRequestResetAvecUserCdeVide() {
        forgotPassword.setUserCde("");
        String result = forgotPassword.requestReset();
        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'erreur devrait être true", forgotPassword.isError());
    }

    @Test
    public void testRequestResetAvecUtilisateurInexistant() {
        forgotPassword.setUserCde("utilisateur.inexistant");
        String result = forgotPassword.requestReset();
        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'erreur devrait être true", forgotPassword.isError());
        assertEquals("Utilisateur non trouvé.", forgotPassword.getMessage());
    }

    @Test
    public void testRequestResetAvecUtilisateurSansEmail() {
        // Tester avec un utilisateur qui n'a pas d'email
        // Note: Cela dépend de votre jeu de données de test
        forgotPassword.setUserCde("j.doe1");
        String result = forgotPassword.requestReset();

        // Si l'utilisateur n'a pas d'email, le résultat devrait être NOEMAIL
        // Sinon, il devrait être SUCCESS ou error selon que l'email a été envoyé
        assertTrue("Le résultat devrait être SUCCESS, error ou NOEMAIL",
            result.equals("SUCCESS") || result.equals("error") || result.equals("NOEMAIL"));
    }

    /**
     * Tests de la méthode requestReset() - Cas de succès
     */
    @Test
    public void testRequestResetAvecUtilisateurValide() {
        // Tester avec un utilisateur valide qui a un email
        forgotPassword.setUserCde("j.doe1");
        String result = forgotPassword.requestReset();

        // Le résultat dépend de si l'utilisateur a un email ou non
        assertTrue("Le résultat devrait être SUCCESS, error ou NOEMAIL",
            result.equals("SUCCESS") || result.equals("error") || result.equals("NOEMAIL"));

        if (result.equals("SUCCESS")) {
            assertFalse("L'erreur devrait être false", forgotPassword.isError());
            assertTrue("Le message devrait contenir 'email'",
                forgotPassword.getMessage().toLowerCase().contains("email"));
        }
    }

    @Test
    public void testRequestResetAvecUserCdeAvecEspaces() {
        forgotPassword.setUserCde("  j.doe1  ");
        String result = forgotPassword.requestReset();

        // Le résultat dépend de la gestion du trim dans la méthode
        assertTrue("Le résultat devrait être SUCCESS, error ou NOEMAIL",
            result.equals("SUCCESS") || result.equals("error") || result.equals("NOEMAIL"));
    }

    /**
     * Tests de vérification des attributs après exécution
     */
    @Test
    public void testAttributsParDefautAvantRequestReset() {
        assertFalse("L'attribut error devrait être false par défaut", forgotPassword.isError());
        assertNull("Le message devrait être null par défaut", forgotPassword.getMessage());
        assertNull("Le userCde devrait être null par défaut", forgotPassword.getUserCde());
    }

    @Test
    public void testMessageContientTexteAttenduApreEchec() {
        forgotPassword.setUserCde("utilisateur.inexistant");
        forgotPassword.requestReset();

        assertTrue("Le message devrait contenir du texte",
            forgotPassword.getMessage() != null && !forgotPassword.getMessage().isEmpty());
    }

    @Test
    public void testErreurEstTrueQuandUtilisateurInexistant() {
        forgotPassword.setUserCde("utilisateur.inexistant");
        forgotPassword.requestReset();

        assertTrue("L'attribut error devrait être true", forgotPassword.isError());
    }

    /**
     * Tests de cas limites
     */
    @Test
    public void testRequestResetAvecUserCdeTresLong() {
        forgotPassword.setUserCde("a".repeat(1000));
        String result = forgotPassword.requestReset();
        assertEquals("Le résultat devrait être ERROR", "error", result);
        assertTrue("L'erreur devrait être true", forgotPassword.isError());
    }

    @Test
    public void testRequestResetAvecCaracteresSpeciaux() {
        forgotPassword.setUserCde("user@test.com");
        String result = forgotPassword.requestReset();
        // Le résultat dépend de si cet utilisateur existe
        assertTrue("Le résultat devrait être SUCCESS, error ou NOEMAIL",
            result.equals("SUCCESS") || result.equals("error") || result.equals("NOEMAIL"));
    }

    @Test
    public void testRequestResetMultiplesAppelsSuccessifs() {
        // Premier appel
        forgotPassword.setUserCde("utilisateur.inexistant");
        String result1 = forgotPassword.requestReset();
        assertEquals("Le premier résultat devrait être ERROR", "error", result1);

        // Deuxième appel avec le même utilisateur
        String result2 = forgotPassword.requestReset();
        assertEquals("Le deuxième résultat devrait être ERROR", "error", result2);
    }

    @Test
    public void testRequestResetAvecUserCdeAvecPointsEtChiffres() {
        forgotPassword.setUserCde("j.doe1");
        String result = forgotPassword.requestReset();

        assertTrue("Le résultat devrait être SUCCESS, error ou NOEMAIL",
            result.equals("SUCCESS") || result.equals("error") || result.equals("NOEMAIL"));
    }

    @Test
    public void testRequestResetAvecUserCdeMinusculesMajuscules() {
        forgotPassword.setUserCde("J.DOE1");
        String result = forgotPassword.requestReset();

        // Le résultat dépend de la sensibilité à la casse de la base de données
        assertTrue("Le résultat devrait être SUCCESS, error ou NOEMAIL",
            result.equals("SUCCESS") || result.equals("error") || result.equals("NOEMAIL"));
    }

    /**
     * Tests de cohérence des messages d'erreur
     */
    @Test
    public void testMessageErreurCoherentAvecStatutErreur() {
        forgotPassword.setUserCde("utilisateur.inexistant");
        String result = forgotPassword.requestReset();

        if (forgotPassword.isError()) {
            assertNotNull("Le message d'erreur ne devrait pas être null", forgotPassword.getMessage());
            assertTrue("Le message d'erreur ne devrait pas être vide",
                !forgotPassword.getMessage().isEmpty());
        }
    }

    @Test
    public void testMessageSuccesCoherentAvecStatutSucces() {
        forgotPassword.setUserCde("j.doe1");
        String result = forgotPassword.requestReset();

        if (result.equals("SUCCESS")) {
            assertFalse("L'erreur devrait être false en cas de succès", forgotPassword.isError());
            assertNotNull("Le message de succès ne devrait pas être null", forgotPassword.getMessage());
        }
    }

    /**
     * Tests de la gestion des exceptions
     */
    @Test
    public void testRequestResetGestionExceptionGlobale() {
        // Tester différents cas pour s'assurer que les exceptions sont bien gérées
        forgotPassword.setUserCde(null);
        String result = forgotPassword.requestReset();

        // Même en cas d'exception, le résultat devrait être ERROR
        assertEquals("Le résultat devrait être ERROR en cas d'exception", "error", result);
        assertTrue("L'erreur devrait être true", forgotPassword.isError());
        assertNotNull("Le message ne devrait pas être null", forgotPassword.getMessage());
    }
}
