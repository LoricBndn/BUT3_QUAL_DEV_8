package com.iut.banque.test.controller;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.iut.banque.controller.Connect;
import com.iut.banque.facade.BanqueFacade;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Compte;
import com.iut.banque.modele.Utilisateur;

import static org.junit.Assert.*;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsConnect-context.xml")
@Transactional("transactionManager")
public class TestsConnect {

	@Autowired
	private BanqueFacade banqueFacade;

	private Connect connect;

	/**
	 * Crée une instance de Connect sans passer par le constructeur
	 * pour éviter le problème avec ServletActionContext dans les tests
	 */
	@SuppressWarnings("restriction")
	private Connect createTestInstance() throws Exception {
		// Utiliser Unsafe pour allouer une instance sans appeler le constructeur
		Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
		unsafeField.setAccessible(true);
		sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

		Connect instance = (Connect) unsafe.allocateInstance(Connect.class);

		// Injecter la facade via réflexion
		Field banqueField = Connect.class.getDeclaredField("banque");
		banqueField.setAccessible(true);
		banqueField.set(instance, banqueFacade);

		return instance;
	}

	@Before
	public void setUp() throws Exception {
		// Créer l'instance sans passer par le constructeur
		connect = createTestInstance();

		// Charger les données de test
		banqueFacade.loadClients();

		// S'assurer qu'aucun utilisateur n'est connecté au début de chaque test
		banqueFacade.logout();
	}

	/**
	 * Tests des getters et setters
	 */
	@Test
	public void testGetterSetterUserCde() {
		connect.setUserCde("j.doe1");
		assertEquals("j.doe1", connect.getUserCde());
	}

	@Test
	public void testGetterSetterUserCdeNull() {
		connect.setUserCde(null);
		assertNull("Le userCde devrait être null", connect.getUserCde());
	}

	@Test
	public void testGetterSetterUserCdeVide() {
		connect.setUserCde("");
		assertEquals("", connect.getUserCde());
	}

	@Test
	public void testGetterSetterUserPwd() {
		connect.setUserPwd("password123");
		assertEquals("password123", connect.getUserPwd());
	}

	@Test
	public void testGetterSetterUserPwdNull() {
		connect.setUserPwd(null);
		assertNull("Le userPwd devrait être null", connect.getUserPwd());
	}

	@Test
	public void testGetterSetterUserPwdVide() {
		connect.setUserPwd("");
		assertEquals("", connect.getUserPwd());
	}

	/**
	 * Tests de la méthode login() - Cas d'erreur
	 */
	@Test
	public void testLoginAvecUserCdeNull() {
		connect.setUserCde(null);
		connect.setUserPwd("toto");
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testLoginAvecUserPwdNull() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd(null);
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testLoginAvecLesDeuxNull() {
		connect.setUserCde(null);
		connect.setUserPwd(null);
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testLoginAvecMauvaisMotDePasse() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("wrongpassword");
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testLoginAvecMauvaisUserCde() {
		connect.setUserCde("utilisateur.inexistant");
		connect.setUserPwd("toto");
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testLoginAvecUserCdeVide() {
		connect.setUserCde("");
		connect.setUserPwd("toto");
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testLoginAvecUserPwdVide() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("");
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	/**
	 * Tests de la méthode login() - Cas de succès
	 */
	@Test
	public void testLoginUtilisateurValide() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("toto");
		String result = connect.login();
		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
	}

	@Test
	public void testLoginManagerValide() {
		connect.setUserCde("admin");
		connect.setUserPwd("adminpass");
		String result = connect.login();
		assertEquals("Le résultat devrait être SUCCESSMANAGER", "SUCCESSMANAGER", result);
	}

	@Test
	public void testLoginAvecEspacesDebutEtFin() {
		connect.setUserCde("  j.doe1  ");
		connect.setUserPwd("toto");
		String result = connect.login();
		assertEquals("Le résultat devrait être SUCCESS car userCde est trimé", "SUCCESS", result);
	}

	@Test
	public void testLoginAvecUserCdeAvecEspaces() {
		connect.setUserCde("j.doe1   ");
		connect.setUserPwd("toto");
		String result = connect.login();
		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
	}

	/**
	 * Tests de la méthode getConnectedUser()
	 */
	@Test
	public void testGetConnectedUserApresLogin() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("toto");
		connect.login();

		Utilisateur user = connect.getConnectedUser();
		assertNotNull("L'utilisateur connecté ne devrait pas être null", user);
		assertEquals("L'utilisateur devrait être j.doe1", "j.doe1", user.getUserId());
	}

	@Test
	public void testGetConnectedUserAvantLogin() {
		Utilisateur user = connect.getConnectedUser();
		assertNull("L'utilisateur connecté devrait être null avant le login", user);
	}

	@Test
	public void testGetConnectedUserApresLoginManager() {
		connect.setUserCde("admin");
		connect.setUserPwd("adminpass");
		connect.login();

		Utilisateur user = connect.getConnectedUser();
		assertNotNull("L'utilisateur connecté ne devrait pas être null", user);
		assertEquals("L'utilisateur devrait être admin", "admin", user.getUserId());
	}

	/**
	 * Tests de la méthode getAccounts()
	 */
	@Test
	public void testGetAccountsApresLoginClient() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("toto");
		connect.login();

		Map<String, Compte> accounts = connect.getAccounts();
		assertNotNull("La liste des comptes ne devrait pas être null", accounts);
		assertTrue("Le client devrait avoir au moins un compte", accounts.size() > 0);
	}

	@Test
	public void testGetAccountsContientComptesAttendus() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("toto");
		connect.login();

		Map<String, Compte> accounts = connect.getAccounts();
		assertTrue("Le client devrait avoir des comptes", accounts.size() > 0);
	}

	/**
	 * Tests de la méthode logout()
	 */
	@Test
	public void testLogout() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("toto");
		connect.login();

		String result = connect.logout();
		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);

		Utilisateur user = connect.getConnectedUser();
		assertNull("L'utilisateur connecté devrait être null après logout", user);
	}

	@Test
	public void testLogoutAvantLogin() {
		String result = connect.logout();
		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
	}

	@Test
	public void testLogoutPuisLogin() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("toto");
		connect.login();
		connect.logout();

		connect.setUserCde("j.doe2");
		connect.setUserPwd("toto");
		String result = connect.login();
		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);

		Utilisateur user = connect.getConnectedUser();
		assertNotNull("L'utilisateur connecté ne devrait pas être null", user);
		assertEquals("L'utilisateur devrait être j.doe2", "j.doe2", user.getUserId());
	}

	/**
	 * Tests de cas limites
	 */
	@Test
	public void testLoginPlusieursUtilisateursSuccessifs() {
		// Premier login
		connect.setUserCde("j.doe1");
		connect.setUserPwd("toto");
		String result1 = connect.login();
		assertEquals("Le premier résultat devrait être SUCCESS", "SUCCESS", result1);

		// Logout
		connect.logout();

		// Deuxième login
		connect.setUserCde("admin");
		connect.setUserPwd("adminpass");
		String result2 = connect.login();
		assertEquals("Le deuxième résultat devrait être SUCCESSMANAGER", "SUCCESSMANAGER", result2);
	}

	@Test
	public void testLoginAvecCaracteresSpeciaux() {
		connect.setUserCde("user@test.com");
		connect.setUserPwd("P@ssw0rd!");
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testLoginAvecMotDePasseTresLong() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("a".repeat(1000));
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testLoginAvecUserCdeTresLong() {
		connect.setUserCde("a".repeat(1000));
		connect.setUserPwd("toto");
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testLoginInsensibleALaCasse() {
		connect.setUserCde("J.DOE1");
		connect.setUserPwd("toto");
		String result = connect.login();
		assertEquals("Le résultat devrait être SUCCESS car MySQL est insensible à la casse par défaut", "SUCCESS", result);
	}

	@Test
	public void testLoginMotDePasseSensibleALaCasse() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("TOTO");
		String result = connect.login();
		assertEquals("Le résultat devrait être ERROR car le mot de passe est sensible à la casse", "ERROR", result);
	}

	@Test
	public void testMultiplesLogoutSuccessifs() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("toto");
		connect.login();

		String result1 = connect.logout();
		assertEquals("Le premier résultat devrait être SUCCESS", "SUCCESS", result1);

		String result2 = connect.logout();
		assertEquals("Le deuxième résultat devrait être SUCCESS", "SUCCESS", result2);
	}

	@Test
	public void testGetConnectedUserApresLogout() {
		connect.setUserCde("j.doe1");
		connect.setUserPwd("toto");
		connect.login();
		connect.logout();

		Utilisateur user = connect.getConnectedUser();
		assertNull("L'utilisateur connecté devrait être null après logout", user);
	}

	@Test
	public void testLoginEchecPuisSucces() {
		// Tentative échouée
		connect.setUserCde("j.doe1");
		connect.setUserPwd("wrongpassword");
		String result1 = connect.login();
		assertEquals("Le premier résultat devrait être ERROR", "ERROR", result1);

		// Tentative réussie
		connect.setUserCde("j.doe1");
		connect.setUserPwd("toto");
		String result2 = connect.login();
		assertEquals("Le deuxième résultat devrait être SUCCESS", "SUCCESS", result2);

		Utilisateur user = connect.getConnectedUser();
		assertNotNull("L'utilisateur connecté ne devrait pas être null", user);
	}
}
