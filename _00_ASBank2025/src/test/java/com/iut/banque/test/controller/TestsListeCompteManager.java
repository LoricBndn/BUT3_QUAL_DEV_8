package com.iut.banque.test.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.iut.banque.controller.ListeCompteManager;
import com.iut.banque.facade.BanqueFacade;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Compte;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsListeCompteManager-context.xml")
@Transactional("transactionManager")
public class TestsListeCompteManager {

	@Autowired
	private BanqueFacade banqueFacade;

	private ListeCompteManager listeCompteManager;

	/**
	 * Crée une instance de ListeCompteManager sans passer par le constructeur
	 * pour éviter le problème avec ServletActionContext dans les tests
	 */
	@SuppressWarnings("restriction")
	private ListeCompteManager createTestInstance() throws Exception {
		// Utiliser Unsafe pour allouer une instance sans appeler le constructeur
		Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
		unsafeField.setAccessible(true);
		sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

		ListeCompteManager instance = (ListeCompteManager) unsafe
				.allocateInstance(ListeCompteManager.class);

		// Injecter la facade via réflexion
		Field banqueField = ListeCompteManager.class.getDeclaredField("banque");
		banqueField.setAccessible(true);
		banqueField.set(instance, banqueFacade);

		return instance;
	}

	@Before
	public void setUp() throws Exception {
		// Créer l'instance sans passer par le constructeur
		listeCompteManager = createTestInstance();

		// Charger les données de test
		banqueFacade.tryLogin("admin", "adminpass");
		banqueFacade.loadClients();
	}

	/**
	 * Tests des getters et setters
	 */
	@Test
	public void testGetterSetterADecouvert() {
		listeCompteManager.setaDecouvert(true);
		assertTrue("Le champ aDecouvert devrait être true", listeCompteManager.isaDecouvert());
	}

	@Test
	public void testGetterSetterADecouvertFalse() {
		listeCompteManager.setaDecouvert(false);
		assertFalse("Le champ aDecouvert devrait être false", listeCompteManager.isaDecouvert());
	}

	@Test
	public void testGetterSetterCompte() {
		Compte compte = banqueFacade.getCompte("AB7328887341");
		listeCompteManager.setCompte(compte);
		assertEquals("Le compte devrait être celui défini", compte, listeCompteManager.getCompte());
	}

	@Test
	public void testGetterSetterCompteNull() {
		listeCompteManager.setCompte(null);
		assertNull("Le compte devrait être null", listeCompteManager.getCompte());
	}

	@Test
	public void testGetterSetterClient() {
		Client client = (Client) banqueFacade.getAllClients().get("j.doe1");
		listeCompteManager.setClient(client);
		assertEquals("Le client devrait être celui défini", client, listeCompteManager.getClient());
	}

	@Test
	public void testGetterSetterClientNull() {
		listeCompteManager.setClient(null);
		assertNull("Le client devrait être null", listeCompteManager.getClient());
	}

	/**
	 * Tests de la méthode getAllClients()
	 */
	@Test
	public void testGetAllClients() {
		Map<String, Client> clients = listeCompteManager.getAllClients();
		assertNotNull("La liste des clients ne devrait pas être null", clients);
		assertTrue("La liste des clients devrait contenir au moins un client", clients.size() > 0);
	}

	@Test
	public void testGetAllClientsContientClientsAttendus() {
		Map<String, Client> clients = listeCompteManager.getAllClients();
		assertTrue("La liste devrait contenir j.doe1", clients.containsKey("j.doe1"));
		assertTrue("La liste devrait contenir j.doe2", clients.containsKey("j.doe2"));
	}

	/**
	 * Tests de la méthode deleteUser()
	 */
	@Test
	public void testDeleteUserSansCompte() {
		try {
			Client client = (Client) banqueFacade.getAllClients().get("g.pasdecompte");
			assertNotNull("Le client g.pasdecompte devrait exister", client);

			listeCompteManager.setClient(client);
			String result = listeCompteManager.deleteUser();

			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
			assertNotNull("UserInfo devrait être défini", listeCompteManager.getUserInfo());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Une exception inattendue a été levée : " + e.getClass().getSimpleName());
		}
	}

	@Test
	public void testDeleteUserAvecComptesVidesSupprime() {
		try {
			Client client = (Client) banqueFacade.getAllClients().get("g.descomptesvides");
			assertNotNull("Le client g.descomptesvides devrait exister", client);

			listeCompteManager.setClient(client);
			String result = listeCompteManager.deleteUser();

			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
			assertEquals("UserInfo devrait contenir l'identité du client",
					client.getIdentity(), listeCompteManager.getUserInfo());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Une exception inattendue a été levée : " + e.getClass().getSimpleName());
		}
	}

	@Test
	public void testDeleteUserAvecComptesNonVidesRetourneNonemptyaccount() {
		Client client = (Client) banqueFacade.getAllClients().get("j.doe1");
		assertNotNull("Le client j.doe1 devrait exister", client);

		listeCompteManager.setClient(client);
		String result = listeCompteManager.deleteUser();

		assertEquals("Le résultat devrait être NONEMPTYACCOUNT", "NONEMPTYACCOUNT", result);
		assertNotNull("UserInfo devrait être défini", listeCompteManager.getUserInfo());
	}

	/**
	 * Tests de la méthode deleteAccount()
	 */
	@Test
	public void testDeleteAccountAvecSoldeZero() {
		try {
			Compte compte = banqueFacade.getCompte("CADV000000");
			assertNotNull("Le compte CADV000000 devrait exister", compte);
			assertEquals("Le solde devrait être 0", 0.0, compte.getSolde(), 0.01);

			listeCompteManager.setCompte(compte);
			String result = listeCompteManager.deleteAccount();

			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
			assertEquals("CompteInfo devrait contenir le numéro de compte",
					"CADV000000", listeCompteManager.getCompteInfo());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Une exception inattendue a été levée : " + e.getClass().getSimpleName());
		}
	}

	@Test
	public void testDeleteAccountSansDecouvertAvecSoldeZero() {
		try {
			Compte compte = banqueFacade.getCompte("CSDV000000");
			assertNotNull("Le compte CSDV000000 devrait exister", compte);
			assertEquals("Le solde devrait être 0", 0.0, compte.getSolde(), 0.01);

			listeCompteManager.setCompte(compte);
			String result = listeCompteManager.deleteAccount();

			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
			assertEquals("CompteInfo devrait contenir le numéro de compte",
					"CSDV000000", listeCompteManager.getCompteInfo());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Une exception inattendue a été levée : " + e.getClass().getSimpleName());
		}
	}

	@Test
	public void testDeleteAccountAvecSoldeNonNulRetourneNonemptyaccount() {
		Compte compte = banqueFacade.getCompte("CADNV00000");
		assertNotNull("Le compte CADNV00000 devrait exister", compte);
		assertNotEquals("Le solde devrait être différent de 0", 0.0, compte.getSolde(), 0.01);

		listeCompteManager.setCompte(compte);
		String result = listeCompteManager.deleteAccount();

		assertEquals("Le résultat devrait être NONEMPTYACCOUNT", "NONEMPTYACCOUNT", result);
		assertNotNull("CompteInfo devrait être défini", listeCompteManager.getCompteInfo());
	}

	@Test
	public void testDeleteAccountSansDecouvertAvecSoldeNonNulRetourneNonemptyaccount() {
		Compte compte = banqueFacade.getCompte("CSDNV00000");
		assertNotNull("Le compte CSDNV00000 devrait exister", compte);
		assertNotEquals("Le solde devrait être différent de 0", 0.0, compte.getSolde(), 0.01);

		listeCompteManager.setCompte(compte);
		String result = listeCompteManager.deleteAccount();

		assertEquals("Le résultat devrait être NONEMPTYACCOUNT", "NONEMPTYACCOUNT", result);
		assertNotNull("CompteInfo devrait être défini", listeCompteManager.getCompteInfo());
	}

	/**
	 * Tests de cas limites
	 */
	@Test
	public void testDeleteAccountPuisGetAllClients() {
		try {
			// Supprimer un compte
			Compte compte = banqueFacade.getCompte("CADV000000");
			listeCompteManager.setCompte(compte);
			String result = listeCompteManager.deleteAccount();
			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);

			// Récupérer tous les clients
			Map<String, Client> clients = listeCompteManager.getAllClients();
			assertNotNull("La liste des clients ne devrait pas être null", clients);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Une exception inattendue a été levée : " + e.getClass().getSimpleName());
		}
	}

	@Test
	public void testGetUserInfoAvantDeleteUser() {
		assertNull("UserInfo devrait être null avant deleteUser", listeCompteManager.getUserInfo());
	}

	@Test
	public void testGetCompteInfoAvantDeleteAccount() {
		assertNull("CompteInfo devrait être null avant deleteAccount", listeCompteManager.getCompteInfo());
	}

	@Test
	public void testDeleteUserDefinitUserInfo() {
		Client client = (Client) banqueFacade.getAllClients().get("g.pasdecompte");
		listeCompteManager.setClient(client);

		try {
			listeCompteManager.deleteUser();
			assertNotNull("UserInfo devrait être défini après deleteUser", listeCompteManager.getUserInfo());
			assertTrue("UserInfo devrait contenir le nom du client",
					listeCompteManager.getUserInfo().contains("TEST"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Une exception inattendue a été levée : " + e.getClass().getSimpleName());
		}
	}

	@Test
	public void testDeleteAccountDefinitCompteInfo() {
		Compte compte = banqueFacade.getCompte("CADV000000");
		listeCompteManager.setCompte(compte);

		try {
			listeCompteManager.deleteAccount();
			assertNotNull("CompteInfo devrait être défini après deleteAccount",
					listeCompteManager.getCompteInfo());
			assertEquals("CompteInfo devrait contenir le numéro de compte",
					"CADV000000", listeCompteManager.getCompteInfo());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Une exception inattendue a été levée : " + e.getClass().getSimpleName());
		}
	}

	/**
	 * Tests avec plusieurs opérations successives
	 */
	@Test
	public void testDeleteAccountDeuxComptesSuccessifs() {
		try {
			// Premier compte
			Compte compte1 = banqueFacade.getCompte("CADV000000");
			listeCompteManager.setCompte(compte1);
			String result1 = listeCompteManager.deleteAccount();
			assertEquals("Le premier résultat devrait être SUCCESS", "SUCCESS", result1);

			// Deuxième compte
			Compte compte2 = banqueFacade.getCompte("CSDV000000");
			listeCompteManager.setCompte(compte2);
			String result2 = listeCompteManager.deleteAccount();
			assertEquals("Le deuxième résultat devrait être SUCCESS", "SUCCESS", result2);

			// Vérifier que compteInfo a bien été mis à jour
			assertEquals("CompteInfo devrait contenir le numéro du deuxième compte",
					"CSDV000000", listeCompteManager.getCompteInfo());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Une exception inattendue a été levée : " + e.getClass().getSimpleName());
		}
	}

	@Test
	public void testAlternanceSuccesEtEchec() {
		// Succès
		Compte compteVide = banqueFacade.getCompte("CADV000000");
		listeCompteManager.setCompte(compteVide);
		String result1 = listeCompteManager.deleteAccount();
		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result1);

		// Échec
		Compte compteNonVide = banqueFacade.getCompte("CADNV00000");
		listeCompteManager.setCompte(compteNonVide);
		String result2 = listeCompteManager.deleteAccount();
		assertEquals("Le résultat devrait être NONEMPTYACCOUNT", "NONEMPTYACCOUNT", result2);
	}
}
