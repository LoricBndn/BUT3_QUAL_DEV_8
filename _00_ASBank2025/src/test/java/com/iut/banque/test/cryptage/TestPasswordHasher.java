package com.iut.banque.test.cryptage;

import com.iut.banque.cryptage.PasswordHasher;
import org.junit.Assert;
import org.junit.Test;

public class TestPasswordHasher {

    @Test
    public void testHashPasswordSucceed() {
        String motDePasse = "MonSuperMotDePasse";
        Assert.assertTrue(PasswordHasher.verifyPassword(motDePasse, PasswordHasher.hashPassword(motDePasse)));
    }

    @Test
    public void testHashPasswordFailed() {
        String motDePasse = "MonSuperMotDePasse";
        Assert.assertFalse(PasswordHasher.verifyPassword(motDePasse, PasswordHasher.hashPassword("MonMotDePasseEstNul")));
    }
}
