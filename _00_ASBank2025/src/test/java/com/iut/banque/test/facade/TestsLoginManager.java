package com.iut.banque.test.facade;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import com.iut.banque.constants.LoginConstants;
import com.iut.banque.facade.LoginManager;
import com.iut.banque.interfaces.IDao;
import com.iut.banque.modele.Utilisateur;
import com.iut.banque.cryptage.PasswordHasher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsBanqueManager-context.xml")
@Transactional("transactionManager")
public class TestsLoginManager {

    @Autowired
    private LoginManager lm;

    @Autowired
    private IDao dao;

    @Test
    public void contextLoads() {
        assertNotNull(lm);
        assertNotNull(dao);
    }

    @Test
    public void testTryLoginWithPlainPassword() {
        try {
            String userId = "admin";
            String plainPwd = "adminpass";

            Utilisateur u = dao.getUserById(userId);

            // Mdp en clair dans la bdd
            u.setUserPwd(plainPwd);
            dao.updateUser(u);

            int result = lm.tryLogin(userId, plainPwd);
            assertTrue("L'authentification doit réussir après migration",
                    result == LoginConstants.USER_IS_CONNECTED || result == LoginConstants.MANAGER_IS_CONNECTED);

            u.setUserPwd(plainPwd);
            dao.updateUser(u);
        } catch (Exception e) {
            assertTrue("Exception inattendue : " + e.getMessage(), false);
        }
    }

    @Test
    public void testResetPasswordAndLoginWithNewPassword() {
        try {
            String userId = "admin";
            String oldPlain = "adminpass";
            String newPwd = "admin";

            Utilisateur u = dao.getUserById(userId);

            // avec mdp hash
            u.setUserPwd(PasswordHasher.hashPassword(oldPlain));
            dao.updateUser(u);

            boolean ok = lm.resetPassword(userId, oldPlain, newPwd);
            assertTrue("resetPassword doit retourner true", ok);

            int result = lm.tryLogin(userId, newPwd);
            assertTrue("Connexion avec le nouveau mot de passe doit réussir",
                    result == LoginConstants.USER_IS_CONNECTED || result == LoginConstants.MANAGER_IS_CONNECTED);

            u.setUserPwd(oldPlain);
            dao.updateUser(u);
        } catch (Exception e) {
            assertTrue("Exception inattendue : " + e.getMessage(), false);
        }
    }
}
