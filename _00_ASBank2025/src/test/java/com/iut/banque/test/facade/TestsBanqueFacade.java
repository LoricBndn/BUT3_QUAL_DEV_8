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
}