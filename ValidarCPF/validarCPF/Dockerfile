# Use uma imagem base do Maven para compilar o projeto
FROM maven:3.8.5-openjdk-17-slim AS build

# Configure o diretório de trabalho
WORKDIR /app

# Copie os arquivos de projeto para o contêiner
COPY . .

# Execute o build do Maven
RUN mvn clean package

# Use uma imagem base do JDK para executar o aplicativo
FROM openjdk:17-jdk-slim

# Copie o arquivo JAR gerado pelo Maven para o contêiner
COPY --from=build /app/target/consulta-cpf-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que a aplicação usará
EXPOSE 8080

# Execute a aplicação
ENTRYPOINT ["java", "-jar", "/app.jar"]