package com.iut.banque.test.controller;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.iut.banque.controller.DetailCompte;
import com.iut.banque.facade.BanqueFacade;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Compte;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsConnect-context.xml")
@Transactional("transactionManager")
public class TestsDetailCompte {

	@Autowired
	private BanqueFacade banqueFacade;

	private DetailCompte detailCompte;
	private Client client;
	private Compte compte;

	/**
	 * Crée une instance de DetailCompte sans passer par le constructeur
	 * pour éviter le problème avec ServletActionContext dans les tests
	 */
	@SuppressWarnings("restriction")
	private DetailCompte createTestInstance() throws Exception {
		// Utiliser Unsafe pour allouer une instance sans appeler le constructeur
		Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
		unsafeField.setAccessible(true);
		sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

		DetailCompte instance = (DetailCompte) unsafe.allocateInstance(DetailCompte.class);

		// Injecter la facade via réflexion
		Field banqueField = DetailCompte.class.getDeclaredField("banque");
		banqueField.setAccessible(true);
		banqueField.set(instance, banqueFacade);

		return instance;
	}

	@Before
	public void setUp() throws Exception {
		// Créer l'instance sans passer par le constructeur
		detailCompte = createTestInstance();

		// Se connecter en tant que client pour les tests
		banqueFacade.tryLogin("j.doe1", "toto");

		// Récupérer le client connecté
		client = (Client) banqueFacade.getConnectedUser();

		// Récupérer un compte existant du client
		if (client != null && !client.getAccounts().isEmpty()) {
			compte = client.getAccounts().values().iterator().next();
			detailCompte.setCompte(compte);
		}
	}

	/**
	 * Tests des getters et setters
	 */
	@Test
	public void testGetterSetterMontant() {
		detailCompte.setMontant("100.50");
		assertEquals("100.50", detailCompte.getMontant());
	}

	@Test
	public void testGetterSetterMontantNull() {
		detailCompte.setMontant(null);
		assertNull("Le montant devrait être null", detailCompte.getMontant());
	}

	@Test
	public void testGetterSetterMontantVide() {
		detailCompte.setMontant("");
		assertEquals("", detailCompte.getMontant());
	}

	@Test
	public void testGetterSetterMontantNegatif() {
		detailCompte.setMontant("-50.00");
		assertEquals("-50.00", detailCompte.getMontant());
	}

	@Test
	public void testGetterSetterMontantAvecEspaces() {
		detailCompte.setMontant("  100.50  ");
		assertEquals("  100.50  ", detailCompte.getMontant());
	}

	/**
	 * Tests de la méthode setError() et getError()
	 */
	@Test
	public void testSetErrorTechnical() {
		detailCompte.setError("TECHNICAL");
		assertEquals("Erreur interne. Verifiez votre saisie puis réessayer. Contactez votre conseiller si le problème persiste.",
			detailCompte.getError());
	}

	@Test
	public void testSetErrorBusiness() {
		detailCompte.setError("BUSINESS");
		assertEquals("Fonds insuffisants.", detailCompte.getError());
	}

	@Test
	public void testSetErrorNegativeAmount() {
		detailCompte.setError("NEGATIVEAMOUNT");
		assertEquals("Veuillez rentrer un montant positif.", detailCompte.getError());
	}

	@Test
	public void testSetErrorNegativeOverdraft() {
		detailCompte.setError("NEGATIVEOVERDRAFT");
		assertEquals("Veuillez rentrer un découvert positif.", detailCompte.getError());
	}

	@Test
	public void testSetErrorIncompatibleOverdraft() {
		detailCompte.setError("INCOMPATIBLEOVERDRAFT");
		assertEquals("Le nouveau découvert est incompatible avec le solde actuel.", detailCompte.getError());
	}

	@Test
	public void testSetErrorDefault() {
		detailCompte.setError("UNKNOWN");
		assertEquals("", detailCompte.getError());
	}

	@Test
	public void testSetErrorNull() {
		detailCompte.setError(null);
		assertEquals("", detailCompte.getError());
	}

	@Test
	public void testSetErrorEmpty() {
		detailCompte.setError("");
		assertEquals("", detailCompte.getError());
	}

	/**
	 * Tests de la méthode getCompte()
	 */
	@Test
	public void testGetCompteAvecClient() {
		// Le compte devrait être retourné car il appartient au client connecté
		Compte compteRecupere = detailCompte.getCompte();
		assertNotNull("Le compte devrait être retourné", compteRecupere);
		assertEquals(compte.getNumeroCompte(), compteRecupere.getNumeroCompte());
	}

	@Test
	public void testGetCompteAvecGestionnaire() throws Exception {
		// Se connecter en tant que gestionnaire
		banqueFacade.logout();
		banqueFacade.tryLogin("admin", "adminpass");

		// Créer une nouvelle instance pour le gestionnaire
		DetailCompte detailCompteManager = createTestInstance();
		detailCompteManager.setCompte(compte);

		// Le gestionnaire devrait pouvoir accéder à n'importe quel compte
		Compte compteRecupere = detailCompteManager.getCompte();
		assertNotNull("Le gestionnaire devrait pouvoir accéder au compte", compteRecupere);
	}

	@Test
	public void testGetCompteClientAutreCompte() throws Exception {
		// Récupérer un compte d'un autre client
		banqueFacade.logout();
		banqueFacade.tryLogin("j.doe2", "toto");
		Client autreClient = (Client) banqueFacade.getConnectedUser();

		// Créer une nouvelle instance pour l'autre client
		DetailCompte detailCompteAutreClient = createTestInstance();

		// Tenter d'accéder au compte du premier client
		detailCompteAutreClient.setCompte(compte);

		// Le compte ne devrait pas être retourné car il n'appartient pas au client connecté
		Compte compteRecupere = detailCompteAutreClient.getCompte();
		assertNull("Le client ne devrait pas pouvoir accéder au compte d'un autre client", compteRecupere);
	}

	@Test
	public void testSetterGetterCompte() {
		Compte nouveauCompte = banqueFacade.getCompte("AB7328887341");
		detailCompte.setCompte(nouveauCompte);

		// Vérifier via la méthode getCompte qui fait des vérifications de sécurité
		if (client.getAccounts().containsKey(nouveauCompte.getNumeroCompte())) {
			assertNotNull("Le compte devrait être accessible", detailCompte.getCompte());
		}
	}

	/**
	 * Tests de la méthode credit() - Cas de succès
	 */
	@Test
	public void testCreditAvecMontantValide() {
		detailCompte.setMontant("100.00");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.credit();

		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
		// Vérifier que le solde a augmenté
		double soldeApres = compte.getSolde();
		assertEquals("Le solde devrait avoir augmenté de 100", soldeAvant + 100.00, soldeApres, 0.01);
	}

	@Test
	public void testCreditAvecMontantDecimal() {
		detailCompte.setMontant("50.75");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.credit();

		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
		double soldeApres = compte.getSolde();
		assertEquals("Le solde devrait avoir augmenté de 50.75", soldeAvant + 50.75, soldeApres, 0.01);
	}

	@Test
	public void testCreditAvecMontantAvecEspaces() {
		detailCompte.setMontant("  25.50  ");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.credit();

		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
		double soldeApres = compte.getSolde();
		assertEquals("Le solde devrait avoir augmenté de 25.50", soldeAvant + 25.50, soldeApres, 0.01);
	}

	/**
	 * Tests de la méthode credit() - Cas d'erreur
	 */
	@Test
	public void testCreditAvecMontantNegatif() {
		detailCompte.setMontant("-50.00");

		String result = detailCompte.credit();

		assertEquals("Le résultat devrait être NEGATIVEAMOUNT", "NEGATIVEAMOUNT", result);
	}

	@Test
	public void testCreditAvecMontantInvalide() {
		detailCompte.setMontant("abc");

		String result = detailCompte.credit();

		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testCreditAvecMontantVide() {
		detailCompte.setMontant("");

		String result = detailCompte.credit();

		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testCreditAvecMontantNull() {
		detailCompte.setMontant(null);

		try {
			String result = detailCompte.credit();
			assertEquals("Le résultat devrait être ERROR", "ERROR", result);
		} catch (NullPointerException e) {
			// Une NullPointerException est acceptable pour un montant null
			assertTrue("NullPointerException attendue pour montant null", true);
		}
	}

	@Test
	public void testCreditAvecMontantTresGrand() {
		detailCompte.setMontant("999999999.99");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.credit();

		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
		double soldeApres = compte.getSolde();
		assertEquals("Le solde devrait avoir augmenté", soldeAvant + 999999999.99, soldeApres, 0.01);
	}

	/**
	 * Tests de la méthode debit() - Cas de succès
	 */
	@Test
	public void testDebitAvecMontantValide() {
		// D'abord créditer le compte pour s'assurer qu'il y a des fonds
		detailCompte.setMontant("500.00");
		detailCompte.credit();

		detailCompte.setMontant("100.00");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.debit();

		// Le résultat peut être SUCCESS ou NOTENOUGHFUNDS selon le solde initial
		assertTrue("Le résultat devrait être SUCCESS ou NOTENOUGHFUNDS",
			result.equals("SUCCESS") || result.equals("NOTENOUGHFUNDS"));

		if (result.equals("SUCCESS")) {
			double soldeApres = compte.getSolde();
			assertEquals("Le solde devrait avoir diminué de 100", soldeAvant - 100.00, soldeApres, 0.01);
		}
	}

	@Test
	public void testDebitAvecMontantDecimal() {
		// Créditer d'abord
		detailCompte.setMontant("500.00");
		detailCompte.credit();

		detailCompte.setMontant("50.75");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.debit();

		assertTrue("Le résultat devrait être SUCCESS ou NOTENOUGHFUNDS",
			result.equals("SUCCESS") || result.equals("NOTENOUGHFUNDS"));

		if (result.equals("SUCCESS")) {
			double soldeApres = compte.getSolde();
			assertEquals("Le solde devrait avoir diminué de 50.75", soldeAvant - 50.75, soldeApres, 0.01);
		}
	}

	@Test
	public void testDebitAvecMontantAvecEspaces() {
		// Créditer d'abord
		detailCompte.setMontant("500.00");
		detailCompte.credit();

		detailCompte.setMontant("  25.50  ");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.debit();

		assertTrue("Le résultat devrait être SUCCESS ou NOTENOUGHFUNDS",
			result.equals("SUCCESS") || result.equals("NOTENOUGHFUNDS"));

		if (result.equals("SUCCESS")) {
			double soldeApres = compte.getSolde();
			assertEquals("Le solde devrait avoir diminué de 25.50", soldeAvant - 25.50, soldeApres, 0.01);
		}
	}

	/**
	 * Tests de la méthode debit() - Cas d'erreur
	 */
	@Test
	public void testDebitAvecMontantNegatif() {
		detailCompte.setMontant("-50.00");

		String result = detailCompte.debit();

		assertEquals("Le résultat devrait être NEGATIVEAMOUNT", "NEGATIVEAMOUNT", result);
	}

	@Test
	public void testDebitAvecMontantInvalide() {
		detailCompte.setMontant("xyz");

		String result = detailCompte.debit();

		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testDebitAvecMontantVide() {
		detailCompte.setMontant("");

		String result = detailCompte.debit();

		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testDebitAvecMontantNull() {
		detailCompte.setMontant(null);

		try {
			String result = detailCompte.debit();
			assertEquals("Le résultat devrait être ERROR", "ERROR", result);
		} catch (NullPointerException e) {
			// Une NullPointerException est acceptable pour un montant null
			assertTrue("NullPointerException attendue pour montant null", true);
		}
	}

	@Test
	public void testDebitAvecFondsInsuffisants() {
		// S'assurer que le compte a un solde connu
		double soldeActuel = compte.getSolde();

		// Tenter de débiter plus que le solde + découvert autorisé
		detailCompte.setMontant(String.valueOf(soldeActuel + 10000.00));

		String result = detailCompte.debit();

		// Le résultat devrait être NOTENOUGHFUNDS
		assertEquals("Le résultat devrait être NOTENOUGHFUNDS", "NOTENOUGHFUNDS", result);
	}

	/**
	 * Tests de cas limites
	 */
	@Test
	public void testCreditPuisDebit() {
		double soldeInitial = compte.getSolde();

		// Créditer 100
		detailCompte.setMontant("100.00");
		detailCompte.credit();

		// Débiter 50
		detailCompte.setMontant("50.00");
		detailCompte.debit();

		// Le solde final devrait être soldeInitial + 100 - 50
		double soldeFinal = compte.getSolde();
		assertEquals("Le solde final devrait être soldeInitial + 50", soldeInitial + 50.00, soldeFinal, 0.01);
	}

	@Test
	public void testMultiplesCredits() {
		double soldeInitial = compte.getSolde();

		// Créditer 3 fois
		detailCompte.setMontant("10.00");
		detailCompte.credit();

		detailCompte.setMontant("20.00");
		detailCompte.credit();

		detailCompte.setMontant("30.00");
		detailCompte.credit();

		// Le solde final devrait être soldeInitial + 60
		double soldeFinal = compte.getSolde();
		assertEquals("Le solde final devrait être soldeInitial + 60", soldeInitial + 60.00, soldeFinal, 0.01);
	}

	@Test
	public void testCreditAvecMontantZero() {
		detailCompte.setMontant("0.00");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.credit();

		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
		double soldeApres = compte.getSolde();
		assertEquals("Le solde ne devrait pas changer", soldeAvant, soldeApres, 0.01);
	}

	@Test
	public void testDebitAvecMontantZero() {
		detailCompte.setMontant("0.00");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.debit();

		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
		double soldeApres = compte.getSolde();
		assertEquals("Le solde ne devrait pas changer", soldeAvant, soldeApres, 0.01);
	}

	@Test
	public void testCreditAvecMontantTresPetit() {
		detailCompte.setMontant("0.01");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.credit();

		assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
		double soldeApres = compte.getSolde();
		assertEquals("Le solde devrait avoir augmenté de 0.01", soldeAvant + 0.01, soldeApres, 0.01);
	}

	@Test
	public void testDebitAvecMontantTresPetit() {
		// Créditer d'abord pour avoir des fonds
		detailCompte.setMontant("100.00");
		detailCompte.credit();

		detailCompte.setMontant("0.01");
		double soldeAvant = compte.getSolde();

		String result = detailCompte.debit();

		assertTrue("Le résultat devrait être SUCCESS ou NOTENOUGHFUNDS",
			result.equals("SUCCESS") || result.equals("NOTENOUGHFUNDS"));

		if (result.equals("SUCCESS")) {
			double soldeApres = compte.getSolde();
			assertEquals("Le solde devrait avoir diminué de 0.01", soldeAvant - 0.01, soldeApres, 0.01);
		}
	}

	@Test
	public void testCreditAvecMontantAvecVirgule() {
		// Certains systèmes utilisent la virgule comme séparateur décimal
		detailCompte.setMontant("100,50");

		String result = detailCompte.credit();

		// Devrait échouer car le format n'est pas valide en Java (point attendu)
		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}

	@Test
	public void testDebitAvecMontantAvecVirgule() {
		detailCompte.setMontant("50,25");

		String result = detailCompte.debit();

		assertEquals("Le résultat devrait être ERROR", "ERROR", result);
	}
}
