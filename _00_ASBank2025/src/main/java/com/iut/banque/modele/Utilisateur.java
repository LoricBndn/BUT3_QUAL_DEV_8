package com.iut.banque.modele;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.iut.banque.exceptions.IllegalFormatException;

/**
 * Classe représentant un utilisateur quelconque.
 *
 * La stratégie d'héritage choisie est SINGLE_TABLE. Cela signifie que tous les
 * objets de cette classe et des classes filles sont enregistrés dans une seule
 * table dans la base de donnée. Les champs non utilisés par la classe sont
 * NULL.
 *
 * Lors d'un chargement d'un objet appartenant à une des classes filles, le type
 * de l'objet est choisi grâce à la colonne "type" (c'est une colonne de
 * discrimination).
 */
@Entity
@Table(name = "Utilisateur")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 15)
public abstract class Utilisateur {

    @Id
    @Column(name = "userId")
    private String userId;

    @Column(name = "userPwd")
    private String userPwd;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "male")
    private boolean male;

    @Column(name = "email")
    private String email;

    // Getters et Setters existants
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) throws IllegalFormatException {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    // Nouveau getter et setter pour email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Utilisateur(String nom, String prenom, String adresse, boolean male, String userId, String userPwd) {
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.male = male;
        this.userId = userId;
        this.userPwd = userPwd;
    }

    public Utilisateur() {
        super();
    }

    @Override
    public String toString() {
        return "Utilisateur [userId=" + userId + ", nom=" + nom + ", prenom=" + prenom + ", adresse=" + adresse
                + ", male=" + male + ", userPwd=" + userPwd + ", email=" + email + "]";
    }
}