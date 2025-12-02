# Étape 1 : Build du projet avec Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copier le pom.xml en premier pour optimiser le cache Docker
COPY pom.xml .

# Télécharger les dépendances Maven
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Construire le projet (ignorer les tests)
RUN mvn clean package -DskipTests


# Étape 2 : Image finale légère pour exécuter l'app Spring Boot
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port correct (8087)
EXPOSE 8087

# Lancer l'application
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]
