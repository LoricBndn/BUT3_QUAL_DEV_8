#!/bin/bash
set -e

echo "========================================="
echo "ASBank - Docker Build & Test Process"
echo "========================================="

# Attendre que MySQL soit prêt
echo "Waiting for MySQL to be ready..."
while ! nc -z mysql 3306; do
  sleep 1
done
echo "MySQL is ready!"

# Attendre quelques secondes supplémentaires pour que MySQL initialise complètement
sleep 5

# Exécuter les tests
echo ""
echo "========================================="
echo "Running tests..."
echo "========================================="
cd /app
mvn test

# Vérifier si les tests ont réussi
if [ $? -ne 0 ]; then
    echo ""
    echo "========================================="
    echo "TESTS FAILED! Application will not start."
    echo "========================================="
    exit 1
fi

echo ""
echo "========================================="
echo "All tests passed! Building application..."
echo "========================================="

# Builder l'application (les tests ont déjà été exécutés)
mvn package -DskipTests

# Déployer le WAR dans Tomcat
echo ""
echo "========================================="
echo "Deploying application to Tomcat..."
echo "========================================="

# Supprimer les applications par défaut de Tomcat
rm -rf /usr/local/tomcat/webapps/*

# Déployer notre application avec le contexte _00_ASBank2025 (nom du artifactId Maven)
cp /app/target/*.war /usr/local/tomcat/webapps/_00_ASBank2025.war

# Extraire le WAR et remplacer la configuration
cd /usr/local/tomcat/webapps
unzip -q _00_ASBank2025.war -d _00_ASBank2025
rm _00_ASBank2025.war

# Remplacer le fichier de configuration Spring pour Docker
cp /tmp/applicationContext-docker.xml /usr/local/tomcat/webapps/_00_ASBank2025/WEB-INF/applicationContext.xml

echo ""
echo "========================================="
echo "Starting Tomcat..."
echo "========================================="
echo "Application will be available at http://localhost:8081/_00_ASBank2025"
echo ""

# Lancer Tomcat
exec catalina.sh run
