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
        // j.doe1 a un email vide dans les données de test
        forgotPassword.setUserCde("j.doe1");
        String result = forgotPassword.requestReset();

        // L'utilisateur j.doe1 n'a pas d'email, le résultat devrait être NOEMAIL
        assertEquals("Le résultat devrait être NOEMAIL car j.doe1 n'a pas d'email", "NOEMAIL", result);
        assertTrue("L'erreur devrait être true", forgotPassword.isError());
        assertNotNull("Le message ne devrait pas être null", forgotPassword.getMessage());
        assertTrue("Le message devrait mentionner l'absence d'email",
            forgotPassword.getMessage().toLowerCase().contains("email"));
    }

    /**
     * Tests de la méthode requestReset() - Cas de succès
     */
    @Test
    public void testRequestResetAvecUtilisateurValideAvecEmail() {
        // Tester avec un utilisateur valide qui a un email
        // admin a un email dans les données de test
        forgotPassword.setUserCde("admin");
        String result = forgotPassword.requestReset();

        // Le résultat devrait être SUCCESS ou ERROR selon la configuration SMTP
        // En environnement de test sans configuration SMTP, ce sera ERROR
        assertTrue("Le résultat devrait être SUCCESS ou error",
            result.equals("SUCCESS") || result.equals("error"));

        if (result.equals("SUCCESS")) {
            assertFalse("L'erreur devrait être false", forgotPassword.isError());
            assertTrue("Le message devrait contenir 'email'",
                forgotPassword.getMessage().toLowerCase().contains("email"));
        } else {
            assertTrue("L'erreur devrait être true", forgotPassword.isError());
            assertNotNull("Le message d'erreur ne devrait pas être null", forgotPassword.getMessage());
        }
    }

    @Test
    public void testRequestResetAvecUserCdeAvecEspaces() {
        forgotPassword.setUserCde("  j.doe1  ");
        String result = forgotPassword.requestReset();

        // Avec des espaces, l'utilisateur n'est pas trouvé (pas de trim)
        // donc le résultat devrait être ERROR
        assertEquals("Le résultat devrait être ERROR car l'utilisateur avec espaces n'est pas trouvé", "error", result);
        assertTrue("L'erreur devrait être true", forgotPassword.isError());
        assertEquals("Le message devrait indiquer que l'utilisateur n'est pas trouvé",
            "Utilisateur non trouvé.", forgotPassword.getMessage());
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

        // j.doe1 n'a pas d'email, donc le résultat devrait être NOEMAIL
        assertEquals("Le résultat devrait être NOEMAIL car j.doe1 n'a pas d'email", "NOEMAIL", result);
        assertTrue("L'erreur devrait être true", forgotPassword.isError());
    }

    @Test
    public void testRequestResetAvecUserCdeMinusculesMajuscules() {
        forgotPassword.setUserCde("J.DOE1");
        String result = forgotPassword.requestReset();

        // Le résultat dépend de la sensibilité à la casse de la base de données
        // Si l'utilisateur est trouvé (J.DOE1 = j.doe1), il n'a pas d'email donc NOEMAIL
        // Sinon, l'utilisateur n'est pas trouvé donc ERROR
        assertTrue("Le résultat devrait être error ou NOEMAIL",
            result.equals("error") || result.equals("NOEMAIL"));
        assertTrue("L'erreur devrait être true", forgotPassword.isError());
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
        // Utiliser admin qui a un email pour tester le cas de succès potentiel
        forgotPassword.setUserCde("admin");
        String result = forgotPassword.requestReset();

        if (result.equals("SUCCESS")) {
            assertFalse("L'erreur devrait être false en cas de succès", forgotPassword.isError());
            assertNotNull("Le message de succès ne devrait pas être null", forgotPassword.getMessage());
            assertTrue("Le message de succès devrait contenir 'email'",
                forgotPassword.getMessage().toLowerCase().contains("email"));
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

    /**
     * Tests spécifiques pour les nouvelles fonctionnalités (après merge)
     */
    @Test
    public void testRequestResetRetourneNOEMAILQuandEmailVide() {
        // Tester explicitement le cas où l'email est vide
        forgotPassword.setUserCde("j.doe2");  // j.doe2 a aussi un email vide
        String result = forgotPassword.requestReset();

        assertEquals("Le résultat devrait être NOEMAIL", "NOEMAIL", result);
        assertTrue("L'erreur devrait être true", forgotPassword.isError());
        assertTrue("Le message devrait mentionner le conseiller",
            forgotPassword.getMessage().toLowerCase().contains("conseiller"));
    }

    @Test
    public void testRequestResetMessageContientInfosUtiles() {
        // Vérifier que les messages d'erreur sont informatifs
        forgotPassword.setUserCde("j.doe1");
        forgotPassword.requestReset();

        String message = forgotPassword.getMessage();
        assertNotNull("Le message ne devrait pas être null", message);
        assertFalse("Le message ne devrait pas être vide", message.isEmpty());
        // Le message pour NOEMAIL devrait expliquer comment résoudre le problème
        assertTrue("Le message devrait indiquer comment associer un email",
            message.contains("conseiller") || message.contains("associer"));
    }

    @Test
    public void testRequestResetAvecUtilisateurAdminAvecEmail() {
        // Test spécifique pour admin qui a un email configuré
        forgotPassword.setUserCde("admin");
        String result = forgotPassword.requestReset();

        // Le résultat ne devrait PAS être NOEMAIL car admin a un email
        assertFalse("Le résultat ne devrait pas être NOEMAIL pour admin", result.equals("NOEMAIL"));

        // Il devrait être SUCCESS ou ERROR selon la config SMTP
        assertTrue("Le résultat devrait être SUCCESS ou error",
            result.equals("SUCCESS") || result.equals("error"));

        // Dans tous les cas, un message devrait être présent
        assertNotNull("Un message devrait être présent", forgotPassword.getMessage());
        assertFalse("Le message ne devrait pas être vide", forgotPassword.getMessage().isEmpty());
    }

    @Test
    public void testRequestResetCoherenceEntreResultatEtMessage() {
        // Test pour vérifier la cohérence entre le résultat et le message
        forgotPassword.setUserCde("j.doe1");
        String result = forgotPassword.requestReset();

        if (result.equals("NOEMAIL")) {
            assertTrue("Le message devrait mentionner l'email",
                forgotPassword.getMessage().toLowerCase().contains("email"));
        } else if (result.equals("SUCCESS")) {
            assertTrue("Le message devrait mentionner l'envoi",
                forgotPassword.getMessage().toLowerCase().contains("envoyé") ||
                forgotPassword.getMessage().toLowerCase().contains("email"));
        } else if (result.equals("error")) {
            assertNotNull("Le message d'erreur ne devrait pas être null",
                forgotPassword.getMessage());
        }
    }
}
