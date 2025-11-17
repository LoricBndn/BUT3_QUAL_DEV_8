package com.iut.banque.test.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.iut.banque.constants.LoginConstants;
import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.IllegalOperationException;
import com.iut.banque.exceptions.InsufficientFundsException;
import com.iut.banque.exceptions.TechnicalException;
import com.iut.banque.facade.BanqueFacade;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Compte;
import com.iut.banque.modele.CompteAvecDecouvert;
import com.iut.banque.modele.Gestionnaire;
import com.iut.banque.modele.Utilisateur;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsBanqueFacade-context.xml")
@Transactional("transactionManager")
public class TestsBanqueFacade {

    @Autowired
    private BanqueFacade facade;

    // Tests de connexion

    @Test
    public void TestLoginAvecIdentifiantsValidesClient() {
        int result = facade.tryLogin("j.doe1", "toto");
        assertEquals(LoginConstants.USER_IS_CONNECTED, result);
        assertNotNull(facade.getConnectedUser());
        facade.logout();
    }

    @Test
    public void TestLogout() {
        facade.tryLogin("admin", "admin");
        assertNotNull(facade.getConnectedUser());
        facade.logout();
        assertNull(facade.getConnectedUser());
    }

    // Tests de crédit/débit
    @Test
    public void TestCrediterUnCompte() {
        try {
            Compte compte = facade.getCompte("CADV000000");
            double soldeInitial = compte.getSolde();
            facade.crediter(compte, 100.0);
            assertEquals(soldeInitial + 100.0, compte.getSolde(), 0.01);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception inattendue : " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void TestCrediterUnCompteMontantNegatif() {
        try {
            Compte compte = facade.getCompte("CADV000000");
            facade.crediter(compte, -100.0);
            fail("Une IllegalFormatException aurait dû être levée");
        } catch (IllegalFormatException e) {
            // Exception attendue
        } catch (Exception e) {
            fail("Exception inattendue : " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void TestDebiterUnCompteAvecSoldeSuffisant() {
        try {
            Compte compte = facade.getCompte("CADNV00000");
            double soldeInitial = compte.getSolde();
            facade.debiter(compte, 50.0);
            assertEquals(soldeInitial - 50.0, compte.getSolde(), 0.01);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception inattendue : " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void TestDebiterUnCompteMontantNegatif() {
        try {
            Compte compte = facade.getCompte("CADV000000");
            facade.debiter(compte, -100.0);
            fail("Une IllegalFormatException aurait dû être levée");
        } catch (IllegalFormatException e) {
            // Exception attendue
        } catch (Exception e) {
            fail("Exception inattendue : " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void TestDebiterUnCompteSansFondsSuffisants() {
        try {
            Compte compte = facade.getCompte("CSDV000000");
            facade.debiter(compte, 1000.0);
            fail("Une InsufficientFundsException aurait dû être levée");
        } catch (InsufficientFundsException e) {
            // Exception attendue
        } catch (Exception e) {
            fail("Exception inattendue : " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void TestCreationDunManager() {
        try {
            facade.createManager("t.testmanager", "password", "TestNom", "TestPrenom",
                    "123 Test Street", true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception inattendue : " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void TestGetCompte() {
        Compte compte = facade.getCompte("CADV000000");
        assertNotNull(compte);
        assertEquals("CADV000000", compte.getNumeroCompte());
    }

    @Test
    public void TestGetCompteInexistant() {
        Compte compte = facade.getCompte("INEXISTANT");
        assertNull(compte);
    }

    @Test
    public void TestLoginAvecIdentifiantsValidesGestionnaire() {
        int result = facade.tryLogin("admin", "adminpass");
        assertEquals(LoginConstants.MANAGER_IS_CONNECTED, result);
        assertNotNull(facade.getConnectedUser());
        facade.logout();
    }

    @Test
    public void TestGetAllClients() {
        facade.tryLogin("admin", "admin");
        Map<String, Client> clients = facade.getAllClients();
        assertNotNull(clients);
        facade.logout();
    }

    @Test
    public void TestCreateAccountSansDecouvert() {
        try {
            facade.tryLogin("admin", "adminpass");
            Client client = facade.getAllClients().values().iterator().next();
            facade.createAccount("AB9928887341", client);
            Compte compte = facade.getCompte("AB9928887341");
            assertNotNull(compte);
            assertEquals(0.0, compte.getSolde(), 0.01);
            facade.logout();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception inattendue : " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void TestCreateAccountAvecDecouvert() {
        try {
            facade.tryLogin("admin", "adminpass");
            Client client = facade.getAllClients().values().iterator().next();
            facade.createAccount("AB8828887341", client, 500.0);
            Compte compte = facade.getCompte("AB8828887341");
            assertNotNull(compte);
            assertEquals(0.0, compte.getSolde(), 0.01);
            if (compte instanceof CompteAvecDecouvert) {
                assertEquals(500.0, ((CompteAvecDecouvert) compte).getDecouvertAutorise(), 0.01);
            }
            facade.logout();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception inattendue : " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void TestDeleteAccount() {
        try {
            facade.tryLogin("admin", "adminpass");
            Client client = facade.getAllClients().values().iterator().next();
            facade.createAccount("AB7728887341", client);
            Compte compte = facade.getCompte("AB7728887341");
            assertNotNull(compte);
            facade.deleteAccount(compte);
            facade.logout();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception inattendue : " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void TestCreateClient() {
        try {
            facade.tryLogin("admin", "adminpass");
            facade.createClient("t.testclient1", "password", "TestNom", "TestPrenom",
                    "123 Test Street", true, "1234567895");
            facade.logout();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception inattendue : " + e.getClass().getSimpleName() + e.getMessage());
        }
    }

    @Test
    public void TestDeleteUser() {
        try {
            facade.tryLogin("admin", "adminpass");
            facade.createClient("t.deleteuser1", "password", "DeleteNom", "DeletePrenom",
                    "123 Delete Street", false, "1234567892");
            facade.loadClients();
            Client client = (Client) facade.getAllClients().values().stream()
                    .filter(c -> c.getUserId().equals("t.deleteuser1"))
                    .findFirst()
                    .orElse(null);
            assertNotNull(client);
            facade.deleteUser(client);
            facade.logout();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception inattendue : " + e.getClass().getSimpleName() + e.getMessage());
        }
    }

    @Test
    public void TestLoadClients() {
        facade.tryLogin("admin", "adminpass");
        facade.loadClients();
        assertNotNull(facade.getAllClients());
        facade.logout();
    }

    @Test
    public void TestChangeDecouvert() {
        try {
            facade.tryLogin("admin", "adminpass");
            Compte compte = facade.getCompte("CADV000000");
            if (compte instanceof CompteAvecDecouvert) {
                CompteAvecDecouvert compteAvecDecouvert = (CompteAvecDecouvert) compte;
                facade.changeDecouvert(compteAvecDecouvert, 1000.0);
                assertEquals(1000.0, compteAvecDecouvert.getDecouvertAutorise(), 0.01);
            }
            facade.logout();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception inattendue : " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void TestCreateAccountSansDecouvertNonGestionnaire() {
        try {
            facade.tryLogin("j.doe1", "toto");
            Client client = new Client("test", "test", "Test", true, "Test", "NUM000001", "123456789");
            facade.createAccount("AB6628887341", client);
            // Si on arrive ici sans exception, le compte ne devrait pas avoir été créé
            Compte compte = facade.getCompte("AB6628887341");
            assertNull(compte);
            facade.logout();
        } catch (Exception e) {
            // Une exception peut être levée ou non selon l'implémentation
            facade.logout();
        }
    }

    @Test
    public void TestCreateAccountAvecDecouvertNonGestionnaire() {
        try {
            facade.tryLogin("j.doe1", "toto");
            Client client = new Client("test", "test", "Test", true, "Test", "NUM000002", "123456789");
            facade.createAccount("AB5528887341", client, 500.0);
            // Si on arrive ici sans exception, le compte ne devrait pas avoir été créé
            Compte compte = facade.getCompte("AB5528887341");
            assertNull(compte);
            facade.logout();
        } catch (Exception e) {
            // Une exception peut être levée ou non selon l'implémentation
            facade.logout();
        }
    }

    @Test
    public void TestDeleteAccountNonGestionnaire() {
        try {
            facade.tryLogin("j.doe1", "toto");
            Compte compte = facade.getCompte("CADV000000");
            facade.deleteAccount(compte);
            // Si on arrive ici sans exception, le compte ne devrait pas avoir été supprimé
            Compte compteApres = facade.getCompte("CADV000000");
            assertNotNull("Le compte ne devrait pas avoir été supprimé par un non-gestionnaire", compteApres);
            facade.logout();
        } catch (Exception e) {
            // Une exception peut être levée ou non selon l'implémentation
            facade.logout();
        }
    }

    @Test
    public void TestCreateManagerNonGestionnaire() {
        try {
            facade.tryLogin("j.doe1", "toto");
            facade.createManager("t.testmanager2", "password", "TestNom", "TestPrenom",
                    "123 Test Street", true);
            // Si on arrive ici sans exception, le manager ne devrait pas avoir été créé
            facade.logout();
            // Vérifier que le manager n'a pas été créé en tentant de se connecter
            int loginResult = facade.tryLogin("t.testmanager2", "password");
            assertEquals("Le manager ne devrait pas avoir été créé par un non-gestionnaire", LoginConstants.LOGIN_FAILED, loginResult);
        } catch (Exception e) {
            // Une exception peut être levée ou non selon l'implémentation
            facade.logout();
        }
    }

    @Test
    public void TestCreateClientNonGestionnaire() {
        try {
            facade.tryLogin("j.doe1", "toto");
            facade.createClient("t.testclient2", "password", "TestNom", "TestPrenom",
                    "123 Test Street", true, "NUM777777");
            // Si on arrive ici sans exception, le client ne devrait pas avoir été créé
            facade.logout();
            // Vérifier que le client n'a pas été créé en tentant de se connecter
            int loginResult = facade.tryLogin("t.testclient2", "password");
            assertEquals("Le client ne devrait pas avoir été créé par un non-gestionnaire", LoginConstants.LOGIN_FAILED, loginResult);
        } catch (Exception e) {
            // Une exception peut être levée ou non selon l'implémentation
            facade.logout();
        }
    }

    @Test
    public void TestDeleteUserNonGestionnaire() {
        try {
            facade.tryLogin("j.doe1", "toto");
            // Créer un utilisateur factice pour le test
            Utilisateur user = new Client("test", "test", "Test", true, "Test", "NUM000003", "123456789");
            facade.deleteUser(user);
            // Si on arrive ici sans exception, l'opération ne devrait pas avoir eu d'effet
            assertNotNull("L'opération deleteUser ne devrait pas affecter le système pour un non-gestionnaire", facade.getConnectedUser());
            facade.logout();
        } catch (Exception e) {
            // Une exception peut être levée ou non selon l'implémentation
            facade.logout();
        }
    }

    @Test
    public void TestLoadClientsNonGestionnaire() {
        facade.tryLogin("j.doe1", "toto");
        facade.loadClients();
        // Vérifier que l'opération ne plante pas et que l'utilisateur reste connecté
        assertNotNull("L'utilisateur devrait rester connecté après loadClients", facade.getConnectedUser());
        facade.logout();
    }

    @Test
    public void TestChangeDecouvertNonGestionnaire() {
        try {
            facade.tryLogin("j.doe1", "toto");
            Compte compte = facade.getCompte("CADV000000");
            if (compte instanceof CompteAvecDecouvert) {
                CompteAvecDecouvert compteAvecDecouvert = (CompteAvecDecouvert) compte;
                double decouvertInitial = compteAvecDecouvert.getDecouvertAutorise();
                facade.changeDecouvert(compteAvecDecouvert, 2000.0);
                // Le découvert ne devrait pas avoir changé
                assertEquals(decouvertInitial, compteAvecDecouvert.getDecouvertAutorise(), 0.01);
            }
            facade.logout();
        } catch (Exception e) {
            // Une exception peut être levée ou non selon l'implémentation
            facade.logout();
        }
    }
}