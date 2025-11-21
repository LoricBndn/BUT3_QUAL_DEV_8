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

import com.iut.banque.controller.CreerUtilisateur;
import com.iut.banque.facade.BanqueFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsCreerUtilisateur-context.xml")
@Transactional("transactionManager")
public class TestsCreerUtilisateur {

    @Autowired
    private BanqueFacade banqueFacade;

    private CreerUtilisateur creerUtilisateur;

    /**
     * Crée une instance de CreerUtilisateur sans passer par le constructeur
     * pour éviter le problème avec ServletActionContext dans les tests
     */
    @SuppressWarnings("restriction")
    private CreerUtilisateur createTestInstance() throws Exception {
        // Utiliser Unsafe pour allouer une instance sans appeler le constructeur
        Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

        CreerUtilisateur instance = (CreerUtilisateur) unsafe.allocateInstance(CreerUtilisateur.class);

        // Injecter la facade via réflexion
        Field banqueField = CreerUtilisateur.class.getDeclaredField("banque");
        banqueField.setAccessible(true);
        banqueField.set(instance, banqueFacade);

        return instance;
    }

    @Before
    public void setUp() throws Exception {
        // Créer l'instance sans passer par le constructeur
        creerUtilisateur = createTestInstance();

        // Charger les données de test
        banqueFacade.loadClients();
    }

    /**
     * Tests des getters et setters
     */
    @Test
    public void testGetterSetterUserId() {
        creerUtilisateur.setUserId("j.doe10");
        assertEquals("j.doe10", creerUtilisateur.getUserId());
    }

    @Test
    public void testGetterSetterUserIdNull() {
        creerUtilisateur.setUserId(null);
        assertNull("Le userId devrait être null", creerUtilisateur.getUserId());
    }

    @Test
    public void testGetterSetterUserIdVide() {
        creerUtilisateur.setUserId("");
        assertEquals("", creerUtilisateur.getUserId());
    }

    @Test
    public void testGetterSetterNom() {
        creerUtilisateur.setNom("Dupont");
        assertEquals("Dupont", creerUtilisateur.getNom());
    }

    @Test
    public void testGetterSetterNomNull() {
        creerUtilisateur.setNom(null);
        assertNull("Le nom devrait être null", creerUtilisateur.getNom());
    }

    @Test
    public void testGetterSetterNomVide() {
        creerUtilisateur.setNom("");
        assertEquals("", creerUtilisateur.getNom());
    }

    @Test
    public void testGetterSetterPrenom() {
        creerUtilisateur.setPrenom("Jean");
        assertEquals("Jean", creerUtilisateur.getPrenom());
    }

    @Test
    public void testGetterSetterPrenomNull() {
        creerUtilisateur.setPrenom(null);
        assertNull("Le prenom devrait être null", creerUtilisateur.getPrenom());
    }

    @Test
    public void testGetterSetterPrenomVide() {
        creerUtilisateur.setPrenom("");
        assertEquals("", creerUtilisateur.getPrenom());
    }

    @Test
    public void testGetterSetterAdresse() {
        creerUtilisateur.setAdresse("123 Rue de la Paix");
        assertEquals("123 Rue de la Paix", creerUtilisateur.getAdresse());
    }

    @Test
    public void testGetterSetterAdresseNull() {
        creerUtilisateur.setAdresse(null);
        assertNull("L'adresse devrait être null", creerUtilisateur.getAdresse());
    }

    @Test
    public void testGetterSetterAdresseVide() {
        creerUtilisateur.setAdresse("");
        assertEquals("", creerUtilisateur.getAdresse());
    }

    @Test
    public void testGetterSetterUserPwd() {
        creerUtilisateur.setUserPwd("password123");
        assertEquals("password123", creerUtilisateur.getUserPwd());
    }

    @Test
    public void testGetterSetterUserPwdNull() {
        creerUtilisateur.setUserPwd(null);
        assertNull("Le userPwd devrait être null", creerUtilisateur.getUserPwd());
    }

    @Test
    public void testGetterSetterUserPwdVide() {
        creerUtilisateur.setUserPwd("");
        assertEquals("", creerUtilisateur.getUserPwd());
    }

    @Test
    public void testGetterSetterMale() {
        creerUtilisateur.setMale(true);
        assertTrue(creerUtilisateur.isMale());
    }

    @Test
    public void testGetterSetterMaleFalse() {
        creerUtilisateur.setMale(false);
        assertFalse(creerUtilisateur.isMale());
    }

    @Test
    public void testGetterSetterClient() {
        creerUtilisateur.setClient(true);
        assertTrue(creerUtilisateur.isClient());
    }

    @Test
    public void testGetterSetterClientFalse() {
        creerUtilisateur.setClient(false);
        assertFalse(creerUtilisateur.isClient());
    }

    @Test
    public void testGetterSetterNumClient() {
        creerUtilisateur.setNumClient("12345");
        assertEquals("12345", creerUtilisateur.getNumClient());
    }

    @Test
    public void testGetterSetterNumClientNull() {
        creerUtilisateur.setNumClient(null);
        assertNull("Le numClient devrait être null", creerUtilisateur.getNumClient());
    }

    @Test
    public void testGetterSetterNumClientVide() {
        creerUtilisateur.setNumClient("");
        assertEquals("", creerUtilisateur.getNumClient());
    }

    @Test
    public void testGetterSetterMessage() {
        creerUtilisateur.setMessage("Message de test");
        assertEquals("Message de test", creerUtilisateur.getMessage());
    }

    @Test
    public void testGetterSetterMessageNull() {
        creerUtilisateur.setMessage(null);
        assertNull("Le message devrait être null", creerUtilisateur.getMessage());
    }

    @Test
    public void testGetterSetterMessageVide() {
        creerUtilisateur.setMessage("");
        assertEquals("", creerUtilisateur.getMessage());
    }

    @Test
    public void testGetterSetterResult() {
        creerUtilisateur.setResult("SUCCESS");
        assertEquals("SUCCESS", creerUtilisateur.getResult());
    }

    @Test
    public void testGetterSetterResultNull() {
        creerUtilisateur.setResult(null);
        assertNull("Le result devrait être null", creerUtilisateur.getResult());
    }

    @Test
    public void testGetterSetterResultVide() {
        creerUtilisateur.setResult("");
        assertEquals("", creerUtilisateur.getResult());
    }

    /**
     * Tests de la méthode creationUtilisateur() - Cas de succès
     */
    @Test
    public void testCreationClientValide() {
        creerUtilisateur.setUserId("n.client1");
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("Nouveau");
        creerUtilisateur.setPrenom("Client");
        creerUtilisateur.setAdresse("456 Avenue Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(true);
        creerUtilisateur.setNumClient("9999999999");

        String result = creerUtilisateur.creationUtilisateur();

        assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
        assertEquals("Le result devrait être SUCCESS", "SUCCESS", creerUtilisateur.getResult());
        assertNotNull("Le message ne devrait pas être null", creerUtilisateur.getMessage());
        assertTrue("Le message devrait contenir le userId", creerUtilisateur.getMessage().contains("n.client1"));
    }

    @Test
    public void testCreationManagerValide() {
        creerUtilisateur.setUserId("n.manager1");
        creerUtilisateur.setUserPwd("password456");
        creerUtilisateur.setNom("Manager");
        creerUtilisateur.setPrenom("Nouveau");
        creerUtilisateur.setAdresse("789 Boulevard Test");
        creerUtilisateur.setMale(false);
        creerUtilisateur.setClient(false);

        String result = creerUtilisateur.creationUtilisateur();

        assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
        assertEquals("Le result devrait être SUCCESS", "SUCCESS", creerUtilisateur.getResult());
        assertNotNull("Le message ne devrait pas être null", creerUtilisateur.getMessage());
        assertTrue("Le message devrait contenir le userId", creerUtilisateur.getMessage().contains("n.manager1"));
    }

    @Test
    public void testCreationClientFeminin() {
        creerUtilisateur.setUserId("c.femme1");
        creerUtilisateur.setUserPwd("password789");
        creerUtilisateur.setNom("Dupont");
        creerUtilisateur.setPrenom("Marie");
        creerUtilisateur.setAdresse("321 Rue Test");
        creerUtilisateur.setMale(false);
        creerUtilisateur.setClient(true);
        creerUtilisateur.setNumClient("8888888888");

        String result = creerUtilisateur.creationUtilisateur();

        assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
        assertEquals("Le result devrait être SUCCESS", "SUCCESS", creerUtilisateur.getResult());
    }

    /**
     * Tests de la méthode creationUtilisateur() - Validation des données
     */
    @Test
    public void testCreationDeuxUtilisateursAvecIdsDifferents() {
        // Créer un premier utilisateur
        creerUtilisateur.setUserId("u.premier1");
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("Premier");
        creerUtilisateur.setPrenom("User");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(false);
        String result1 = creerUtilisateur.creationUtilisateur();
        assertEquals("La première création devrait réussir", "SUCCESS", result1);

        // Créer un second utilisateur avec un userId différent
        creerUtilisateur.setUserId("u.second1");
        creerUtilisateur.setUserPwd("password456");
        creerUtilisateur.setNom("Second");
        creerUtilisateur.setPrenom("User");
        creerUtilisateur.setAdresse("456 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(false);

        String result2 = creerUtilisateur.creationUtilisateur();

        assertEquals("La seconde création devrait réussir", "SUCCESS", result2);
        assertNotNull("Le message ne devrait pas être null", creerUtilisateur.getMessage());
        assertTrue("Le message devrait contenir le userId",
                creerUtilisateur.getMessage().contains("u.second1"));
    }

    @Test
    public void testCreationDeuxClientsAvecNumClientsDifferents() {
        // Créer un premier client
        creerUtilisateur.setUserId("p.client1");
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("Premier");
        creerUtilisateur.setPrenom("Client");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(true);
        creerUtilisateur.setNumClient("9999888877");
        String result1 = creerUtilisateur.creationUtilisateur();
        assertEquals("La première création devrait réussir", "SUCCESS", result1);

        // Créer un second client avec un numClient différent
        creerUtilisateur.setUserId("s.client1");
        creerUtilisateur.setUserPwd("password456");
        creerUtilisateur.setNom("Second");
        creerUtilisateur.setPrenom("Client");
        creerUtilisateur.setAdresse("456 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(true);
        creerUtilisateur.setNumClient("9999777766");

        String result2 = creerUtilisateur.creationUtilisateur();

        assertEquals("La seconde création devrait réussir", "SUCCESS", result2);
        assertNotNull("Le message ne devrait pas être null", creerUtilisateur.getMessage());
        assertTrue("Le message devrait contenir le userId",
                creerUtilisateur.getMessage().contains("s.client1"));
    }

    @Test
    public void testCreationAvecUserIdFormatValide() {
        creerUtilisateur.setUserId("x.valid123");
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("Test");
        creerUtilisateur.setPrenom("Valid");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(false);

        String result = creerUtilisateur.creationUtilisateur();

        assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
        assertEquals("Le result devrait être SUCCESS", "SUCCESS", creerUtilisateur.getResult());
        assertNotNull("Le message ne devrait pas être null", creerUtilisateur.getMessage());
    }

    @Test
    public void testCreationClientAvecNumClientFormatValide10Chiffres() {
        creerUtilisateur.setUserId("t.numvalid1");
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("Test");
        creerUtilisateur.setPrenom("Format");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(true);
        creerUtilisateur.setNumClient("1234567890");

        String result = creerUtilisateur.creationUtilisateur();

        assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
        assertEquals("Le result devrait être SUCCESS", "SUCCESS", creerUtilisateur.getResult());
        assertNotNull("Le message ne devrait pas être null", creerUtilisateur.getMessage());
    }

    /**
     * Tests de cas limites
     */
    @Test
    public void testCreationAvecUserIdTresLong() {
        creerUtilisateur.setUserId("a".repeat(100));
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("Test");
        creerUtilisateur.setPrenom("Long");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(false);

        String result = creerUtilisateur.creationUtilisateur();

        // Un userId très long peut être accepté selon les validations en place
        // Le test vérifie simplement que la méthode retourne un résultat
        assertNotNull("Le résultat ne devrait pas être null", result);
        assertTrue("Le résultat devrait être SUCCESS ou ERROR",
                result.equals("SUCCESS") || result.equals("ERROR"));
    }

    @Test
    public void testCreationAvecNomAvecCaracteresSpeciaux() {
        creerUtilisateur.setUserId("t.special1");
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("O'Connor");
        creerUtilisateur.setPrenom("Jean-Pierre");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(true);
        creerUtilisateur.setNumClient("6666666666");

        String result = creerUtilisateur.creationUtilisateur();

        assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
    }

    @Test
    public void testCreationAvecAdresseLongue() {
        creerUtilisateur.setUserId("t.adresse1");
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("Test");
        creerUtilisateur.setPrenom("Adresse");
        creerUtilisateur.setAdresse("123 Rue de la République, Appartement 45, Bâtiment C, 75000 Paris, France");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(true);
        creerUtilisateur.setNumClient("5555555555");

        String result = creerUtilisateur.creationUtilisateur();

        assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
    }

    @Test
    public void testCreationAvecMotDePasseComplexe() {
        creerUtilisateur.setUserId("t.pwd1");
        creerUtilisateur.setUserPwd("P@ssw0rd!2024#Secure");
        creerUtilisateur.setNom("Test");
        creerUtilisateur.setPrenom("Password");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(false);

        String result = creerUtilisateur.creationUtilisateur();

        assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
    }

    @Test
    public void testCreationAvecNumClientFormatValide() {
        creerUtilisateur.setUserId("t.numclient1");
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("Test");
        creerUtilisateur.setPrenom("NumClient");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(true);
        creerUtilisateur.setNumClient("0012345678");

        String result = creerUtilisateur.creationUtilisateur();

        assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
    }

    /**
     * Tests de vérification des attributs après exécution
     */
    @Test
    public void testMessageApresCreationReussie() {
        creerUtilisateur.setUserId("t.message1");
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("Test");
        creerUtilisateur.setPrenom("Message");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(false);

        creerUtilisateur.creationUtilisateur();

        assertNotNull("Le message ne devrait pas être null", creerUtilisateur.getMessage());
        assertTrue("Le message devrait contenir 'bien été crée'",
                creerUtilisateur.getMessage().contains("bien été crée"));
        assertTrue("Le message devrait contenir le userId",
                creerUtilisateur.getMessage().contains("t.message1"));
    }

    @Test
    public void testMessageApresCreationReussieContientUserId() {
        creerUtilisateur.setUserId("u.testmsg1");
        creerUtilisateur.setUserPwd("password123");
        creerUtilisateur.setNom("Test");
        creerUtilisateur.setPrenom("Message");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(false);

        String result = creerUtilisateur.creationUtilisateur();

        assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
        assertNotNull("Le message ne devrait pas être null", creerUtilisateur.getMessage());
        assertTrue("Le message devrait contenir le userId",
                creerUtilisateur.getMessage().contains("u.testmsg1"));
        assertEquals("Le result devrait être SUCCESS", "SUCCESS", creerUtilisateur.getResult());
    }

    @Test
    public void testCreationMultiplesClientsSuccessifs() {
        // Première création
        creerUtilisateur.setUserId("c.un1");
        creerUtilisateur.setUserPwd("password1");
        creerUtilisateur.setNom("Un");
        creerUtilisateur.setPrenom("Client");
        creerUtilisateur.setAdresse("123 Test");
        creerUtilisateur.setMale(true);
        creerUtilisateur.setClient(true);
        creerUtilisateur.setNumClient("1111111111");

        String result1 = creerUtilisateur.creationUtilisateur();
        assertEquals("Le premier résultat devrait être SUCCESS", "SUCCESS", result1);

        // Deuxième création
        creerUtilisateur.setUserId("c.deux1");
        creerUtilisateur.setUserPwd("password2");
        creerUtilisateur.setNom("Deux");
        creerUtilisateur.setPrenom("Client");
        creerUtilisateur.setAdresse("456 Test");
        creerUtilisateur.setMale(false);
        creerUtilisateur.setClient(true);
        creerUtilisateur.setNumClient("2222222222");

        String result2 = creerUtilisateur.creationUtilisateur();
        assertEquals("Le deuxième résultat devrait être SUCCESS", "SUCCESS", result2);
    }
}
