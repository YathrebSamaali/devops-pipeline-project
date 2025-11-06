# Étape 1 : Build du projet avec Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copier le fichier pom.xml d'abord (pour profiter du cache Docker)
COPY pom.xml .

# Télécharger les dépendances avant d’ajouter le code source (optimisation du cache)
RUN mvn dependency:go-offline

# Copier le code source du projet
COPY src ./src

# Construire le projet et créer le JAR sans lancer les tests
RUN mvn clean package -DskipTests

# Étape 2 : Image finale légère (sans Maven)
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copier uniquement le JAR depuis la première image
COPY --from=build /app/target/*.jar app.jar

# Exposer le port de l’application
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]
