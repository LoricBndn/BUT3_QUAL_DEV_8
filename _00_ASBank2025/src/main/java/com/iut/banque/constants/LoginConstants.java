package com.iut.banque.constants;

/**
 * Classe abstraite contenant des constantes statiques utilisées pour indiquer
 * le status d'une tentative de login.
 */
public abstract class LoginConstants {

	public static final int USER_IS_CONNECTED = 1;
	public static final int MANAGER_IS_CONNECTED = 2;
	public static final int LOGIN_FAILED = -1;
	public static final int ERROR = -2;
	public static final int TOO_MANY_ATTEMPTS = -3;

	private LoginConstants() {
		// Private constructor to prevent instantiation
	}
}
