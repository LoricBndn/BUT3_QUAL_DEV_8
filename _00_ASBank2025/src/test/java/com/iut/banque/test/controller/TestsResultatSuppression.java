package com.iut.banque.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.iut.banque.controller.ResultatSuppression;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Compte;
import com.iut.banque.modele.CompteAvecDecouvert;
import com.iut.banque.modele.CompteSansDecouvert;

public class TestsResultatSuppression {

    private ResultatSuppression resultatSuppression;
    private Client clientTest;

    @Before
    public void setUp() {
        resultatSuppression = new ResultatSuppression();
        clientTest = new Client();
    }

    /**
     * Tests du constructeur
     */
    @Test
    public void testConstructeur() {
        ResultatSuppression result = new ResultatSuppression();
        assertFalse("L'erreur devrait être à false par défaut", result.isError());
        assertFalse("isAccount devrait être à false par défaut", result.isAccount());
        assertNull("Le compte devrait être null par défaut", result.getCompte());
        assertNull("Le client devrait être null par défaut", result.getClient());
    }

    /**
     * Tests des getters et setters - Compte
     */
    @Test
    public void testGetterSetterCompte() throws Exception {
        Compte compte = new CompteSansDecouvert("FR1234567890", 1000.0, clientTest);
        resultatSuppression.setCompte(compte);
        assertEquals(compte, resultatSuppression.getCompte());
    }

    @Test
    public void testGetterSetterCompteNull() {
        resultatSuppression.setCompte(null);
        assertNull("Le compte devrait être null", resultatSuppression.getCompte());
    }

    @Test
    public void testGetterSetterCompteAvecDecouvert() throws Exception {
        Compte compte = new CompteAvecDecouvert("FR9876543210", 2000.0, 500.0, clientTest);
        resultatSuppression.setCompte(compte);
        assertEquals(compte, resultatSuppression.getCompte());
    }

    /**
     * Tests des getters et setters - Client
     */
    @Test
    public void testGetterSetterClient() {
        Client client = new Client();
        resultatSuppression.setClient(client);
        assertEquals(client, resultatSuppression.getClient());
    }

    @Test
    public void testGetterSetterClientNull() {
        resultatSuppression.setClient(null);
        assertNull("Le client devrait être null", resultatSuppression.getClient());
    }

    /**
     * Tests des getters et setters - CompteInfo
     */
    @Test
    public void testGetterSetterCompteInfo() {
        resultatSuppression.setCompteInfo("Compte FR123456");
        assertEquals("Compte FR123456", resultatSuppression.getCompteInfo());
    }

    @Test
    public void testGetterSetterCompteInfoNull() {
        resultatSuppression.setCompteInfo(null);
        assertNull("compteInfo devrait être null", resultatSuppression.getCompteInfo());
    }

    @Test
    public void testGetterSetterCompteInfoVide() {
        resultatSuppression.setCompteInfo("");
        assertEquals("", resultatSuppression.getCompteInfo());
    }

    @Test
    public void testGetterSetterCompteInfoAvecDetails() {
        String info = "Compte FR123456 - Solde: 1000.00€";
        resultatSuppression.setCompteInfo(info);
        assertEquals(info, resultatSuppression.getCompteInfo());
    }

    /**
     * Tests des getters et setters - UserInfo
     */
    @Test
    public void testGetterSetterUserInfo() {
        resultatSuppression.setUserInfo("John Doe");
        assertEquals("John Doe", resultatSuppression.getUserInfo());
    }

    @Test
    public void testGetterSetterUserInfoNull() {
        resultatSuppression.setUserInfo(null);
        assertNull("userInfo devrait être null", resultatSuppression.getUserInfo());
    }

    @Test
    public void testGetterSetterUserInfoVide() {
        resultatSuppression.setUserInfo("");
        assertEquals("", resultatSuppression.getUserInfo());
    }

    @Test
    public void testGetterSetterUserInfoAvecDetails() {
        String info = "Client: John Doe - ID: j.doe1";
        resultatSuppression.setUserInfo(info);
        assertEquals(info, resultatSuppression.getUserInfo());
    }

    /**
     * Tests des getters et setters - Error
     */
    @Test
    public void testGetterSetterError() {
        resultatSuppression.setError(true);
        assertTrue(resultatSuppression.isError());
    }

    @Test
    public void testGetterSetterErrorFalse() {
        resultatSuppression.setError(false);
        assertFalse(resultatSuppression.isError());
    }

    /**
     * Tests des getters et setters - ErrorMessage avec logique
     */
    @Test
    public void testGetterSetterErrorMessage() {
        resultatSuppression.setErrorMessage("Une erreur est survenue");
        assertEquals("Une erreur est survenue", resultatSuppression.getErrorMessage());
        assertTrue("L'attribut error devrait être true quand un message est défini",
                resultatSuppression.isError());
    }

    @Test
    public void testGetterSetterErrorMessageNull() {
        resultatSuppression.setErrorMessage(null);
        assertNull("errorMessage devrait être null", resultatSuppression.getErrorMessage());
        assertFalse("L'attribut error devrait être false quand le message est null",
                resultatSuppression.isError());
    }

    @Test
    public void testGetterSetterErrorMessageVide() {
        resultatSuppression.setErrorMessage("");
        assertEquals("", resultatSuppression.getErrorMessage());
        assertFalse("L'attribut error devrait être false quand le message est vide",
                resultatSuppression.isError());
    }

    @Test
    public void testSetErrorMessageMetErrorATrue() {
        assertFalse("error devrait être false initialement", resultatSuppression.isError());
        resultatSuppression.setErrorMessage("Erreur de suppression");
        assertTrue("error devrait passer à true après setErrorMessage avec un message",
                resultatSuppression.isError());
    }

    @Test
    public void testSetErrorMessageAvecMessagePuisNull() {
        resultatSuppression.setErrorMessage("Erreur");
        assertTrue("error devrait être true", resultatSuppression.isError());

        resultatSuppression.setErrorMessage(null);
        assertFalse("error devrait être false après avoir mis le message à null",
                resultatSuppression.isError());
    }

    @Test
    public void testSetErrorMessageAvecMessagePuisVide() {
        resultatSuppression.setErrorMessage("Erreur");
        assertTrue("error devrait être true", resultatSuppression.isError());

        resultatSuppression.setErrorMessage("");
        assertFalse("error devrait être false après avoir mis le message vide",
                resultatSuppression.isError());
    }

    /**
     * Tests des getters et setters - isAccount
     */
    @Test
    public void testGetterSetterIsAccount() {
        resultatSuppression.setAccount(true);
        assertTrue(resultatSuppression.isAccount());
    }

    @Test
    public void testGetterSetterIsAccountFalse() {
        resultatSuppression.setAccount(false);
        assertFalse(resultatSuppression.isAccount());
    }

    /**
     * Tests de scénarios combinés
     */
    @Test
    public void testScenarioSuppressionCompteReussie() throws Exception {
        Compte compte = new CompteSansDecouvert("FR1112223334", 500.0, clientTest);
        resultatSuppression.setCompte(compte);
        resultatSuppression.setCompteInfo("Compte FR1112223334 supprimé");
        resultatSuppression.setAccount(true);
        resultatSuppression.setError(false);

        assertNotNull("Le compte ne devrait pas être null", resultatSuppression.getCompte());
        assertEquals("Compte FR1112223334 supprimé", resultatSuppression.getCompteInfo());
        assertTrue("isAccount devrait être true", resultatSuppression.isAccount());
        assertFalse("error devrait être false", resultatSuppression.isError());
    }

    @Test
    public void testScenarioSuppressionClientReussie() {
        Client client = new Client();
        resultatSuppression.setClient(client);
        resultatSuppression.setUserInfo("Client John Doe supprimé");
        resultatSuppression.setAccount(false);
        resultatSuppression.setError(false);

        assertNotNull("Le client ne devrait pas être null", resultatSuppression.getClient());
        assertEquals("Client John Doe supprimé", resultatSuppression.getUserInfo());
        assertFalse("isAccount devrait être false", resultatSuppression.isAccount());
        assertFalse("error devrait être false", resultatSuppression.isError());
    }

    @Test
    public void testScenarioSuppressionAvecErreur() {
        resultatSuppression.setErrorMessage("Impossible de supprimer le compte");
        resultatSuppression.setAccount(true);

        assertTrue("error devrait être true", resultatSuppression.isError());
        assertEquals("Impossible de supprimer le compte", resultatSuppression.getErrorMessage());
        assertTrue("isAccount devrait être true", resultatSuppression.isAccount());
    }

    @Test
    public void testScenarioCompletSuppressionCompte() throws Exception {
        Compte compte = new CompteAvecDecouvert("FR3334445556", 1500.0, 300.0, clientTest);
        resultatSuppression.setCompte(compte);
        resultatSuppression.setCompteInfo("Compte avec découvert FR3334445556");
        resultatSuppression.setAccount(true);
        resultatSuppression.setError(false);
        resultatSuppression.setErrorMessage(null);

        assertEquals(compte, resultatSuppression.getCompte());
        assertEquals("Compte avec découvert FR3334445556", resultatSuppression.getCompteInfo());
        assertTrue(resultatSuppression.isAccount());
        assertFalse(resultatSuppression.isError());
        assertNull(resultatSuppression.getErrorMessage());
    }

    /**
     * Tests de cas limites
     */
    @Test
    public void testTousLesAttributsNull() {
        resultatSuppression.setCompte(null);
        resultatSuppression.setClient(null);
        resultatSuppression.setCompteInfo(null);
        resultatSuppression.setUserInfo(null);
        resultatSuppression.setErrorMessage(null);

        assertNull(resultatSuppression.getCompte());
        assertNull(resultatSuppression.getClient());
        assertNull(resultatSuppression.getCompteInfo());
        assertNull(resultatSuppression.getUserInfo());
        assertNull(resultatSuppression.getErrorMessage());
        assertFalse(resultatSuppression.isError());
    }

    @Test
    public void testMessagesErreurLongs() {
        String longMessage = "Une erreur très longue est survenue lors de la tentative de suppression " +
                "du compte bancaire en raison de contraintes de base de données.";
        resultatSuppression.setErrorMessage(longMessage);

        assertEquals(longMessage, resultatSuppression.getErrorMessage());
        assertTrue(resultatSuppression.isError());
    }

    @Test
    public void testMessagesAvecCaracteresSpeciaux() {
        String messageSpecial = "Erreur: Échec de la suppression (code: 500) - Veuillez réessayer!";
        resultatSuppression.setErrorMessage(messageSpecial);

        assertEquals(messageSpecial, resultatSuppression.getErrorMessage());
        assertTrue(resultatSuppression.isError());
    }

    @Test
    public void testAlternanceCompteClient() throws Exception {
        // Simuler une suppression de compte
        Compte compte = new CompteSansDecouvert("FR5556667778", 750.0, clientTest);
        resultatSuppression.setCompte(compte);
        resultatSuppression.setAccount(true);

        assertEquals(compte, resultatSuppression.getCompte());
        assertTrue(resultatSuppression.isAccount());

        // Changer pour une suppression de client
        Client client = new Client();
        resultatSuppression.setClient(client);
        resultatSuppression.setAccount(false);

        assertEquals(client, resultatSuppression.getClient());
        assertFalse(resultatSuppression.isAccount());
    }

    @Test
    public void testMultiplesChangementsErrorMessage() {
        resultatSuppression.setErrorMessage("Erreur 1");
        assertTrue(resultatSuppression.isError());

        resultatSuppression.setErrorMessage("Erreur 2");
        assertTrue(resultatSuppression.isError());

        resultatSuppression.setErrorMessage("");
        assertFalse(resultatSuppression.isError());

        resultatSuppression.setErrorMessage("Erreur 3");
        assertTrue(resultatSuppression.isError());
    }
}
