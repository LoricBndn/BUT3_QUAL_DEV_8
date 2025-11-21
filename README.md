# 🏦 ASBank2025

**ASBank2025** est une application web de gestion bancaire développée dans le cadre du module de **Qualité de Développement (BUT3)**.  
Elle permet la gestion des comptes clients, des transactions bancaires ainsi que l'administration des utilisateurs via une interface web.  
L'application utilise une architecture MVC robuste basée sur **Struts 2 + Spring + Hibernate**.

---

## 📑 Table des Matières
- [Fonctionnalités](#-fonctionnalités)
- [Stack Technique](#-stack-technique)
- [Architecture](#-architecture)
- [Installation et Démarrage](#-installation-et-démarrage)
  - [Via Docker (Recommandé)](#via-docker-recommandé)
  - [Installation Manuelle](#installation-manuelle)
- [Utilisation](#-utilisation)
- [Tests et Qualité](#-tests-et-qualité)
- [Auteurs](#-auteurs)

---

## 🚀 Fonctionnalités

L'application distingue deux rôles principaux :

### 👤 Espace Client
- Authentification sécurisée (mots de passe hachés SHA-256)
- Consultation des comptes (soldes, détails)
- Distinction entre comptes simples et comptes avec découvert autorisé
- Opérations : crédit, débit (avec vérification du solde/plafond)
- Gestion du profil : réinitialisation du mot de passe (simulation d’envoi d’email)

### 👨‍💼 Espace Gestionnaire (Manager)
- Gestion des utilisateurs : création/suppression de clients et managers
- Gestion des comptes :
  - Création de comptes pour un client existant
  - Suppression (si solde à 0)
  - Modification du découvert autorisé
- Vue globale : liste de tous les clients et comptes  
  (inclut un filtre pour les comptes à découvert)

---

## 🛠 Stack Technique

- **Langage :** Java 17 (OpenJDK / Eclipse Temurin)  
- **Framework Web (MVC) :** Apache Struts 2.3  
- **Inversion de Contrôle :** Spring Framework 4.2  
- **Persistance (ORM) :** Hibernate 5.1 + JPA  
- **Base de données :** MySQL 8.0  
- **Build & Dépendances :** Maven 3  
- **Frontend :** JSP, JSTL, CSS3, jQuery  
- **Qualité & Tests :** JUnit 4, SonarCloud  
- **Conteneurisation :** Docker & Docker Compose (Tomcat 9)

---

## 🏗 Architecture

Le projet suit une **architecture en couches stricte** :

- **Vue (JSP/Struts Tags)** — Interface utilisateur  
  `WebContent/JSP`
- **Contrôleur (Struts Actions)** — Gestion des requêtes HTTP  
  `com.iut.banque.controller`
- **Façade (Pattern Facade)** — Point d'entrée métier  
  `com.iut.banque.facade`
- **Métier (Model)** — Logique métier  
  `com.iut.banque.modele`
- **DAO** — Accès aux données via Hibernate  
  `com.iut.banque.dao`

L’injection de dépendances est assurée par **Spring** via :  
`WEB-INF/applicationContext.xml`

---

## ⚡ Installation et Démarrage

### Via Docker (Recommandé)

La méthode la plus simple : tout est automatisé (MySQL + Tomcat + build Maven).

**Prérequis :** Docker & Docker Compose

**Commandes :**
```bash
docker-compose up --build
```

**Accès :**

- Application : http://localhost:8081/_00_ASBank2025

- MySQL interne : port 3306 (exposé localement sur 3307)

  **Note** : Le premier démarrage peut être long (initialisation MySQL + compilation Maven + tests unitaires).

### Installation Manuelle

**Prérequis** : JDK 17, Maven, Serveur Tomcat 9, MySQL Server.

**Base de données** :

- Créez une base de données but3_qual_prod.

- Importez le script _00_ASBank2025/script/03-init-prod.sql.

- Configurez src/main/webapp/WEB-INF/applicationContext.xml avec vos identifiants MySQL.

**Build** :

```bash
cd _00_ASBank2025
mvn clean package
```

**Déploiement** :

- Copiez le fichier .war généré dans le dossier webapps de votre Tomcat.

- Démarrez Tomcat.

## 💻 Utilisation

Voici les comptes de démonstration par défaut (définis dans les scripts SQL d'initialisation) :

| Rôle | Identifiant | Mot de passe | Description |
| ---- | ----------- | ------------ | ----------- |
| Gestionnaire	| admin	| adminpass |	Accès complet |
| Client	| client1 |	clientpass1	| Comptes standards |
| Client	| client2 |	clientpass2 |	Comptes à découvert |

**Simulation Email** :
Lors d’une réinitialisation de mot de passe, aucun email réel n’est envoyé.
Le lien de réinitialisation apparaît dans les **logs Tomcat/Docker**.

## 🧪 Tests et Qualité

### Tests Unitaires

Le projet inclut des tests JUnit couvrant DAO, Modèle et Contrôleur.

Pour exécuter les tests :

```bash
mvn test
```

### Analyse SonarCloud

Analyse automatique configurée via GitHub Actions.

- Workflow : ```.github/workflows/sonarcloud.yml```

- Script local : ```RunSonar.bat``` (nécessite un token Sonar)

## 👥 Auteurs

Projet réalisé dans le cadre du BUT Informatique — Qualité de Développement.

- **Étudiants** : Loric Bondon / Théo Schaller / Baptiste Brodier

- **Sujet basé sur** : IUT Metz — Département Informatique (Promotion 2025–2026)
