package com.iut.banque.facade;

import com.iut.banque.constants.LoginConstants;
import com.iut.banque.interfaces.IDao;
import com.iut.banque.modele.Gestionnaire;
import com.iut.banque.modele.Utilisateur;

import com.iut.banque.cryptage.PasswordHasher;

public class LoginManager {

	private IDao dao;
	private Utilisateur user;

	/**
	 * Setter pour la DAO.
	 * 
	 * Utilisé par Spring par Injection de Dependence
	 * 
	 * @param dao
	 *            : la dao nécessaire pour le LoginManager
	 */
	public void setDao(IDao dao) {
		this.dao = dao;
	}

	/**
	 * Méthode pour permettre la connection de l'utilisateur via un login en
	 * confrontant le mdp d'un utilisateur de la base de données avec le mot de
	 * passe donné en paramètre
	 * 
	 * @param userCde
	 *            : un String correspondant au userID de l'utilisateur qui
	 *            cherche à se connecter
	 * @param userPwd
	 *            : un String correspondant au mot de passe qui doit être
	 *            confronté avec celui de la base de données
	 * @return int correspondant aux constantes LoginConstants pour inforer de
	 *         l'état du login
	 */
    public int tryLogin(String userCde, String userPwd) {
        // Récupérer compte utilisateur en fonction du userCde
        user = dao.getUserById(userCde);
        if (user == null) return LoginConstants.LOGIN_FAILED;

        // Mdp en bdd
        String pwdBdd = user.getUserPwd();

        boolean authenticated = false;

        // si mdp pas hash (en clair dans la pdd)
        if (userPwd.equals(pwdBdd)) {
            authenticated = true;

            // hash mdp
            String newHash = PasswordHasher.hashPassword(userPwd);
            user.setUserPwd(newHash);
            dao.updateUser(user);
        }
        // sinon si mdp en bdd déjà hash
        else if (PasswordHasher.verifyPassword(userPwd, pwdBdd)) {
            authenticated = true;
        }

        if (authenticated) {
            if (user instanceof Gestionnaire) {
                return LoginConstants.MANAGER_IS_CONNECTED;
            } else {
                return LoginConstants.USER_IS_CONNECTED;
            }
        }

        return LoginConstants.LOGIN_FAILED;
    }


    /**
	 * Getter pour avoir l'objet Utilisateur de celui qui est actuellement
	 * connecté à l'application
	 * 
	 * @return Utilisateur : l'objet Utilisateur de celui qui est connecté
	 */
	public Utilisateur getConnectedUser() {
		return user;
	}

	/**
	 * Setter pour changer l'utilisateur actuellement connecté à l'application
	 * 
	 * @param user
	 *            : un objet de type Utilisateur (Client ou Gestionnaire) que
	 *            l'on veut définir comme utilisateur actuellement connecté à
	 *            l'application
	 */
	public void setCurrentUser(Utilisateur user) {
		this.user = user;
	}

	/**
	 * Remet l'utilisateur à null et déconnecte la DAO.
	 */
	public void logout() {
		this.user = null;
		dao.disconnect();
	}

    /**
     *
     * Vérifie si l'ancien mot de passe concorde avec celui stocké en bdd
     * puis modification du mot de passe
     *
     * @param userCde
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public boolean resetPassword(String userCde, String oldPassword, String newPassword) {
        user = dao.getUserById(userCde);

        if (user == null) return false;

        // Vérifier l'ancien mot de passe
        if (!PasswordHasher.verifyPassword(oldPassword, user.getUserPwd())) {
            return false;
        }

        // Mettre à jour avec le nouveau mot de passe
        String hashed = PasswordHasher.hashPassword(newPassword);
        user.setUserPwd(hashed);
        dao.updateUser(user);

        return true;
    }
}
