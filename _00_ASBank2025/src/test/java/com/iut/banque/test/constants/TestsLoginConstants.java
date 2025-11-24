package com.iut.banque.test.constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

import com.iut.banque.constants.LoginConstants;

public class TestsLoginConstants {

    /**
     * Tests des valeurs des constantes
     */
    @Test
    public void testUserIsConnectedValue() {
        assertEquals("USER_IS_CONNECTED devrait valoir 1", 1, LoginConstants.USER_IS_CONNECTED);
    }

    @Test
    public void testManagerIsConnectedValue() {
        assertEquals("MANAGER_IS_CONNECTED devrait valoir 2", 2, LoginConstants.MANAGER_IS_CONNECTED);
    }

    @Test
    public void testLoginFailedValue() {
        assertEquals("LOGIN_FAILED devrait valoir -1", -1, LoginConstants.LOGIN_FAILED);
    }

    @Test
    public void testErrorValue() {
        assertEquals("ERROR devrait valoir -2", -2, LoginConstants.ERROR);
    }

    /**
     * Tests de l'unicité des valeurs
     */
    @Test
    public void testUserIsConnectedDifferentDeManagerIsConnected() {
        assertNotEquals("USER_IS_CONNECTED et MANAGER_IS_CONNECTED doivent être différents",
            LoginConstants.USER_IS_CONNECTED, LoginConstants.MANAGER_IS_CONNECTED);
    }

    @Test
    public void testUserIsConnectedDifferentDeLoginFailed() {
        assertNotEquals("USER_IS_CONNECTED et LOGIN_FAILED doivent être différents",
            LoginConstants.USER_IS_CONNECTED, LoginConstants.LOGIN_FAILED);
    }

    @Test
    public void testUserIsConnectedDifferentDeError() {
        assertNotEquals("USER_IS_CONNECTED et ERROR doivent être différents",
            LoginConstants.USER_IS_CONNECTED, LoginConstants.ERROR);
    }

    @Test
    public void testManagerIsConnectedDifferentDeLoginFailed() {
        assertNotEquals("MANAGER_IS_CONNECTED et LOGIN_FAILED doivent être différents",
            LoginConstants.MANAGER_IS_CONNECTED, LoginConstants.LOGIN_FAILED);
    }

    @Test
    public void testManagerIsConnectedDifferentDeError() {
        assertNotEquals("MANAGER_IS_CONNECTED et ERROR doivent être différents",
            LoginConstants.MANAGER_IS_CONNECTED, LoginConstants.ERROR);
    }

    @Test
    public void testLoginFailedDifferentDeError() {
        assertNotEquals("LOGIN_FAILED et ERROR doivent être différents",
            LoginConstants.LOGIN_FAILED, LoginConstants.ERROR);
    }

    /**
     * Tests de la structure de la classe
     */
    @Test
    public void testClasseEstAbstraite() {
        assertTrue("La classe LoginConstants devrait être abstraite",
            Modifier.isAbstract(LoginConstants.class.getModifiers()));
    }

    @Test
    public void testUserIsConnectedEstPublicStaticFinal() throws Exception {
        Field field = LoginConstants.class.getField("USER_IS_CONNECTED");
        int modifiers = field.getModifiers();

        assertTrue("USER_IS_CONNECTED devrait être public", Modifier.isPublic(modifiers));
        assertTrue("USER_IS_CONNECTED devrait être static", Modifier.isStatic(modifiers));
        assertTrue("USER_IS_CONNECTED devrait être final", Modifier.isFinal(modifiers));
    }

    @Test
    public void testManagerIsConnectedEstPublicStaticFinal() throws Exception {
        Field field = LoginConstants.class.getField("MANAGER_IS_CONNECTED");
        int modifiers = field.getModifiers();

        assertTrue("MANAGER_IS_CONNECTED devrait être public", Modifier.isPublic(modifiers));
        assertTrue("MANAGER_IS_CONNECTED devrait être static", Modifier.isStatic(modifiers));
        assertTrue("MANAGER_IS_CONNECTED devrait être final", Modifier.isFinal(modifiers));
    }

    @Test
    public void testLoginFailedEstPublicStaticFinal() throws Exception {
        Field field = LoginConstants.class.getField("LOGIN_FAILED");
        int modifiers = field.getModifiers();

        assertTrue("LOGIN_FAILED devrait être public", Modifier.isPublic(modifiers));
        assertTrue("LOGIN_FAILED devrait être static", Modifier.isStatic(modifiers));
        assertTrue("LOGIN_FAILED devrait être final", Modifier.isFinal(modifiers));
    }

    @Test
    public void testErrorEstPublicStaticFinal() throws Exception {
        Field field = LoginConstants.class.getField("ERROR");
        int modifiers = field.getModifiers();

        assertTrue("ERROR devrait être public", Modifier.isPublic(modifiers));
        assertTrue("ERROR devrait être static", Modifier.isStatic(modifiers));
        assertTrue("ERROR devrait être final", Modifier.isFinal(modifiers));
    }

    /**
     * Tests de cohérence logique
     */
    @Test
    public void testValeursPositivesRepresentantConnexionReussie() {
        assertTrue("USER_IS_CONNECTED devrait être positif",
            LoginConstants.USER_IS_CONNECTED > 0);
        assertTrue("MANAGER_IS_CONNECTED devrait être positif",
            LoginConstants.MANAGER_IS_CONNECTED > 0);
    }

    @Test
    public void testValeursNegativesRepresentantEchec() {
        assertTrue("LOGIN_FAILED devrait être négatif",
            LoginConstants.LOGIN_FAILED < 0);
        assertTrue("ERROR devrait être négatif",
            LoginConstants.ERROR < 0);
    }

    @Test
    public void testUserIsConnectedPlusPetitQueManagerIsConnected() {
        assertTrue("USER_IS_CONNECTED devrait être plus petit que MANAGER_IS_CONNECTED",
            LoginConstants.USER_IS_CONNECTED < LoginConstants.MANAGER_IS_CONNECTED);
    }

    @Test
    public void testErrorPlusPetitQueLoginFailed() {
        assertTrue("ERROR devrait être plus petit que LOGIN_FAILED",
            LoginConstants.ERROR < LoginConstants.LOGIN_FAILED);
    }

    /**
     * Tests du type des constantes
     */
    @Test
    public void testUserIsConnectedEstUnInt() throws Exception {
        Field field = LoginConstants.class.getField("USER_IS_CONNECTED");
        assertEquals("USER_IS_CONNECTED devrait être de type int",
            int.class, field.getType());
    }

    @Test
    public void testManagerIsConnectedEstUnInt() throws Exception {
        Field field = LoginConstants.class.getField("MANAGER_IS_CONNECTED");
        assertEquals("MANAGER_IS_CONNECTED devrait être de type int",
            int.class, field.getType());
    }

    @Test
    public void testLoginFailedEstUnInt() throws Exception {
        Field field = LoginConstants.class.getField("LOGIN_FAILED");
        assertEquals("LOGIN_FAILED devrait être de type int",
            int.class, field.getType());
    }

    @Test
    public void testErrorEstUnInt() throws Exception {
        Field field = LoginConstants.class.getField("ERROR");
        assertEquals("ERROR devrait être de type int",
            int.class, field.getType());
    }

    /**
     * Tests de non-régression
     */
    @Test
    public void testNombreDeConstantesPubliques() {
        Field[] fields = LoginConstants.class.getFields();
        int constantCount = 0;

        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers) &&
                Modifier.isStatic(modifiers) &&
                Modifier.isFinal(modifiers)) {
                constantCount++;
            }
        }

        assertEquals("La classe devrait avoir exactement 4 constantes publiques",
            4, constantCount);
    }

    @Test
    public void testClasseNaPasDeConstructeurPublic() {
        assertEquals("La classe abstraite ne devrait pas avoir de constructeur public",
            0, LoginConstants.class.getConstructors().length);
    }

    /**
     * Tests d'utilisation pratique
     */
    @Test
    public void testToutesLesValeursPositivesRepresentantSucces() {
        assertTrue("USER_IS_CONNECTED devrait être positif pour représenter un succès",
            LoginConstants.USER_IS_CONNECTED > 0);
        assertTrue("MANAGER_IS_CONNECTED devrait être positif pour représenter un succès",
            LoginConstants.MANAGER_IS_CONNECTED > 0);
    }

    @Test
    public void testToutesLesValeursNegativesRepresentantEchec() {
        assertTrue("LOGIN_FAILED devrait être négatif pour représenter un échec",
            LoginConstants.LOGIN_FAILED < 0);
        assertTrue("ERROR devrait être négatif pour représenter un échec",
            LoginConstants.ERROR < 0);
    }

    @Test
    public void testDifferenciationClaireFSuccesEtEchec() {
        // Toutes les valeurs positives (succès)
        assertTrue(LoginConstants.USER_IS_CONNECTED > 0);
        assertTrue(LoginConstants.MANAGER_IS_CONNECTED > 0);

        // Toutes les valeurs négatives (échec)
        assertTrue(LoginConstants.LOGIN_FAILED < 0);
        assertTrue(LoginConstants.ERROR < 0);

        // Aucune valeur ne devrait être zéro
        assertNotEquals(0, LoginConstants.USER_IS_CONNECTED);
        assertNotEquals(0, LoginConstants.MANAGER_IS_CONNECTED);
        assertNotEquals(0, LoginConstants.LOGIN_FAILED);
        assertNotEquals(0, LoginConstants.ERROR);
    }
}
