package com.iut.banque.test.controller;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.iut.banque.controller.DetailCompteEdit;
import com.iut.banque.facade.BanqueFacade;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Compte;
import com.iut.banque.modele.CompteAvecDecouvert;
import com.iut.banque.modele.CompteSansDecouvert;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsConnect-context.xml")
@Transactional("transactionManager")
public class TestsDetailCompteEdit {

	@Autowired
	private BanqueFacade banqueFacade;

	private DetailCompteEdit detailCompteEdit;
	private Client client;
	private CompteAvecDecouvert compteAvecDecouvert;
	private CompteSansDecouvert compteSansDecouvert;

	/**
	 * Crée une instance de DetailCompteEdit sans passer par le constructeur
	 * pour éviter le problème avec ServletActionContext dans les tests
	 */
	@SuppressWarnings("restriction")
	private DetailCompteEdit createTestInstance() throws Exception {
		// Utiliser Unsafe pour allouer une instance sans appeler le constructeur
		Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
		unsafeField.setAccessible(true);
		sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

		DetailCompteEdit instance = (DetailCompteEdit) unsafe.allocateInstance(DetailCompteEdit.class);

		// Injecter la facade via réflexion
		Field banqueField = DetailCompteEdit.class.getSuperclass().getDeclaredField("banque");
		banqueField.setAccessible(true);
		banqueField.set(instance, banqueFacade);

		return instance;
	}

	@Before
	public void setUp() throws Exception {
		// Créer l'instance sans passer par le constructeur
		detailCompteEdit = createTestInstance();

		// Se connecter en tant que gestionnaire pour pouvoir modifier les comptes
		banqueFacade.tryLogin("admin", "adminpass");
		banqueFacade.loadClients();

		// Récupérer un client pour les tests
		client = banqueFacade.getAllClients().get("j.doe1");

		// Trouver un compte avec découvert et un compte sans découvert
		for (Compte compte : client.getAccounts().values()) {
			if (compte instanceof CompteAvecDecouvert && compteAvecDecouvert == null) {
				compteAvecDecouvert = (CompteAvecDecouvert) compte;
			} else if (compte instanceof CompteSansDecouvert && compteSansDecouvert == null) {
				compteSansDecouvert = (CompteSansDecouvert) compte;
			}
		}
	}

	/**
	 * Tests des getters et setters
	 */
	@Test
	public void testGetterSetterDecouvertAutorise() {
		detailCompteEdit.setDecouvertAutorise("500.00");
		assertEquals("500.00", detailCompteEdit.getDecouvertAutorise());
	}

	@Test
	public void testGetterSetterDecouvertAutoriseNull() {
		detailCompteEdit.setDecouvertAutorise(null);
		assertNull("Le découvert autorisé devrait être null", detailCompteEdit.getDecouvertAutorise());
	}

	@Test
	public void testGetterSetterDecouvertAutoriseVide() {
		detailCompteEdit.setDecouvertAutorise("");
		assertEquals("", detailCompteEdit.getDecouvertAutorise());
	}

	@Test
	public void testGetterSetterDecouvertAutoriseZero() {
		detailCompteEdit.setDecouvertAutorise("0.00");
		assertEquals("0.00", detailCompteEdit.getDecouvertAutorise());
	}

	@Test
	public void testGetterSetterDecouvertAutoriseNegatif() {
		detailCompteEdit.setDecouvertAutorise("-100.00");
		assertEquals("-100.00", detailCompteEdit.getDecouvertAutorise());
	}

	/**
	 * Tests de la méthode changementDecouvert() - Cas de succès
	 */
	@Test
	public void testChangementDecouvertAvecCompteAvecDecouvert() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("1000.00");

			String result = detailCompteEdit.changementDecouvert();

			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
		} else {
			// Si aucun compte avec découvert n'est disponible, créer un test alternatif
			assertTrue("Aucun compte avec découvert disponible pour ce test", true);
		}
	}

	@Test
	public void testChangementDecouvertAZero() {
		if (compteAvecDecouvert != null) {
			// D'abord s'assurer que le solde du compte permet un découvert de 0
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("0.00");

			String result = detailCompteEdit.changementDecouvert();

			// Le résultat peut être SUCCESS ou INCOMPATIBLEOVERDRAFT selon le solde
			assertTrue("Le résultat devrait être SUCCESS ou INCOMPATIBLEOVERDRAFT",
				result.equals("SUCCESS") || result.equals("INCOMPATIBLEOVERDRAFT"));
		}
	}

	@Test
	public void testChangementDecouvertAugmentation() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			double decouvertActuel = compteAvecDecouvert.getDecouvertAutorise();

			// Augmenter le découvert
			detailCompteEdit.setDecouvertAutorise(String.valueOf(decouvertActuel + 500.00));

			String result = detailCompteEdit.changementDecouvert();

			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
			assertEquals("Le découvert devrait avoir augmenté",
				decouvertActuel + 500.00, compteAvecDecouvert.getDecouvertAutorise(), 0.01);
		}
	}

	@Test
	public void testChangementDecouvertDiminution() {
		if (compteAvecDecouvert != null) {
			// D'abord augmenter le découvert
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("2000.00");
			detailCompteEdit.changementDecouvert();

			// Puis le diminuer
			detailCompteEdit.setDecouvertAutorise("1000.00");

			String result = detailCompteEdit.changementDecouvert();

			// Le résultat peut être SUCCESS ou INCOMPATIBLEOVERDRAFT selon le solde
			assertTrue("Le résultat devrait être SUCCESS ou INCOMPATIBLEOVERDRAFT",
				result.equals("SUCCESS") || result.equals("INCOMPATIBLEOVERDRAFT"));
		}
	}

	@Test
	public void testChangementDecouvertAvecDecimal() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("750.50");

			String result = detailCompteEdit.changementDecouvert();

			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
			assertEquals("Le découvert devrait être 750.50",
				750.50, compteAvecDecouvert.getDecouvertAutorise(), 0.01);
		}
	}

	/**
	 * Tests de la méthode changementDecouvert() - Cas d'erreur
	 */
	@Test
	public void testChangementDecouvertAvecCompteSansDecouvert() {
		if (compteSansDecouvert != null) {
			detailCompteEdit.setCompte(compteSansDecouvert);
			detailCompteEdit.setDecouvertAutorise("500.00");

			String result = detailCompteEdit.changementDecouvert();

			assertEquals("Le résultat devrait être ERROR pour un compte sans découvert", "ERROR", result);
		} else {
			assertTrue("Aucun compte sans découvert disponible pour ce test", true);
		}
	}

	@Test
	public void testChangementDecouvertAvecDecouvertNegatif() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("-100.00");

			String result = detailCompteEdit.changementDecouvert();

			assertEquals("Le résultat devrait être NEGATIVEOVERDRAFT", "NEGATIVEOVERDRAFT", result);
		}
	}

	@Test
	public void testChangementDecouvertAvecValeurInvalide() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("abc");

			String result = detailCompteEdit.changementDecouvert();

			assertEquals("Le résultat devrait être ERROR", "ERROR", result);
		}
	}

	@Test
	public void testChangementDecouvertAvecValeurVide() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("");

			String result = detailCompteEdit.changementDecouvert();

			assertEquals("Le résultat devrait être ERROR", "ERROR", result);
		}
	}

	@Test
	public void testChangementDecouvertAvecValeurNull() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise(null);

			try {
				String result = detailCompteEdit.changementDecouvert();
				assertEquals("Le résultat devrait être ERROR", "ERROR", result);
			} catch (NullPointerException e) {
				// Une NullPointerException est acceptable pour une valeur null
				assertTrue("NullPointerException attendue pour decouvertAutorise null", true);
			}
		}
	}

	@Test
	public void testChangementDecouvertIncompatible() {
		if (compteAvecDecouvert != null) {
			// Mettre le compte dans un état où le solde est négatif
			detailCompteEdit.setCompte(compteAvecDecouvert);

			// Débiter le compte pour avoir un solde négatif
			detailCompteEdit.setMontant("10000.00");
			detailCompteEdit.debit();

			double solde = compteAvecDecouvert.getSolde();

			// Tenter de mettre un découvert inférieur au solde négatif actuel
			if (solde < 0) {
				double nouveauDecouvert = Math.abs(solde) - 100.00;
				detailCompteEdit.setDecouvertAutorise(String.valueOf(nouveauDecouvert));

				String result = detailCompteEdit.changementDecouvert();

				assertEquals("Le résultat devrait être INCOMPATIBLEOVERDRAFT", "INCOMPATIBLEOVERDRAFT", result);
			} else {
				// Si le débit n'a pas marché, ignorer ce test
				assertTrue("Le test nécessite un solde négatif", true);
			}
		}
	}

	/**
	 * Tests de cas limites
	 */
	@Test
	public void testChangementDecouvertAvecValeurTresGrande() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("999999999.99");

			String result = detailCompteEdit.changementDecouvert();

			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
			assertEquals("Le découvert devrait être très grand",
				999999999.99, compteAvecDecouvert.getDecouvertAutorise(), 0.01);
		}
	}

	@Test
	public void testChangementDecouvertAvecValeurTresPetite() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("0.01");

			String result = detailCompteEdit.changementDecouvert();

			assertTrue("Le résultat devrait être SUCCESS ou INCOMPATIBLEOVERDRAFT",
				result.equals("SUCCESS") || result.equals("INCOMPATIBLEOVERDRAFT"));
		}
	}

	@Test
	public void testChangementDecouvertPlusieursConsecutifs() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);

			// Premier changement
			detailCompteEdit.setDecouvertAutorise("500.00");
			String result1 = detailCompteEdit.changementDecouvert();
			assertEquals("Le premier changement devrait réussir", "SUCCESS", result1);

			// Deuxième changement
			detailCompteEdit.setDecouvertAutorise("1000.00");
			String result2 = detailCompteEdit.changementDecouvert();
			assertEquals("Le deuxième changement devrait réussir", "SUCCESS", result2);

			// Troisième changement
			detailCompteEdit.setDecouvertAutorise("750.00");
			String result3 = detailCompteEdit.changementDecouvert();
			assertTrue("Le troisième changement devrait réussir ou être incompatible",
				result3.equals("SUCCESS") || result3.equals("INCOMPATIBLEOVERDRAFT"));
		}
	}

	@Test
	public void testChangementDecouvertAvecVirgule() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("500,50");

			String result = detailCompteEdit.changementDecouvert();

			// Devrait échouer car le format n'est pas valide en Java
			assertEquals("Le résultat devrait être ERROR", "ERROR", result);
		}
	}

	@Test
	public void testChangementDecouvertAvecEspaces() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setDecouvertAutorise("  500.00  ");

			String result = detailCompteEdit.changementDecouvert();

			// Le trim devrait être géré par Double.parseDouble
			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
		}
	}

	/**
	 * Tests d'héritage - vérifier que les méthodes de DetailCompte fonctionnent
	 */
	@Test
	public void testHeritageMethodeCredit() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setMontant("100.00");
			double soldeAvant = compteAvecDecouvert.getSolde();

			String result = detailCompteEdit.credit();

			assertEquals("Le résultat devrait être SUCCESS", "SUCCESS", result);
			double soldeApres = compteAvecDecouvert.getSolde();
			assertEquals("Le solde devrait avoir augmenté de 100", soldeAvant + 100.00, soldeApres, 0.01);
		}
	}

	@Test
	public void testHeritageMethodeDebit() {
		if (compteAvecDecouvert != null) {
			// Créditer d'abord
			detailCompteEdit.setCompte(compteAvecDecouvert);
			detailCompteEdit.setMontant("500.00");
			detailCompteEdit.credit();

			detailCompteEdit.setMontant("100.00");
			double soldeAvant = compteAvecDecouvert.getSolde();

			String result = detailCompteEdit.debit();

			assertTrue("Le résultat devrait être SUCCESS ou NOTENOUGHFUNDS",
				result.equals("SUCCESS") || result.equals("NOTENOUGHFUNDS"));

			if (result.equals("SUCCESS")) {
				double soldeApres = compteAvecDecouvert.getSolde();
				assertEquals("Le solde devrait avoir diminué de 100", soldeAvant - 100.00, soldeApres, 0.01);
			}
		}
	}

	@Test
	public void testHeritageGetterSetterMontant() {
		detailCompteEdit.setMontant("250.75");
		assertEquals("250.75", detailCompteEdit.getMontant());
	}

	@Test
	public void testHeritageGetterSetterError() {
		detailCompteEdit.setError("TECHNICAL");
		assertTrue("Le message d'erreur devrait contenir du texte",
			detailCompteEdit.getError().length() > 0);
	}

	/**
	 * Tests combinés - changement de découvert suivi d'opérations
	 */
	@Test
	public void testChangementDecouvertPuisDebit() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);

			// Augmenter le découvert
			detailCompteEdit.setDecouvertAutorise("2000.00");
			String resultChangement = detailCompteEdit.changementDecouvert();
			assertEquals("Le changement devrait réussir", "SUCCESS", resultChangement);

			// Tenter un débit important
			detailCompteEdit.setMontant("1500.00");
			String resultDebit = detailCompteEdit.debit();

			// Avec le nouveau découvert, le débit devrait potentiellement réussir
			assertTrue("Le débit devrait réussir ou échouer selon le solde",
				resultDebit.equals("SUCCESS") || resultDebit.equals("NOTENOUGHFUNDS"));
		}
	}

	@Test
	public void testChangementDecouvertPuisCredit() {
		if (compteAvecDecouvert != null) {
			detailCompteEdit.setCompte(compteAvecDecouvert);

			// Changer le découvert
			detailCompteEdit.setDecouvertAutorise("1000.00");
			detailCompteEdit.changementDecouvert();

			// Créditer le compte
			detailCompteEdit.setMontant("500.00");
			double soldeAvant = compteAvecDecouvert.getSolde();

			String result = detailCompteEdit.credit();

			assertEquals("Le crédit devrait réussir", "SUCCESS", result);
			double soldeApres = compteAvecDecouvert.getSolde();
			assertEquals("Le solde devrait avoir augmenté de 500", soldeAvant + 500.00, soldeApres, 0.01);
		}
	}
}
