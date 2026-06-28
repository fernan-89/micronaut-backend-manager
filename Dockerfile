# ==============================================================================
# 🏗️ ETAPA 1: COMPILAÇÃO (BUILD) - COM JDK 25
# ==============================================================================
FROM gradle:jdk25 AS builder

# Define o diretório de trabalho dentro do container de build
WORKDIR /home/gradle/project

# Copia os arquivos de configuração do Gradle PRIMEIRO
COPY gradle/ gradle/
COPY build.gradle settings.gradle gradlew ./

# CORREÇÃO CRÍTICA: Copia as propriedades e configurações da CLI para o Gradle ler as versões do Micronaut
COPY gradle.properties micronaut-cli.yml ./

# Copia o código fonte do ecossistema ThinkLab Manager
COPY src/ src/

# Executa o build do Micronaut gerando o shadow/optimized JAR (ignora os testes na compilação da imagem)
RUN ./gradlew shadowJar --no-daemon -x test

# ==============================================================================
# 🚀 ETAPA 2: EXECUÇÃO (RUNTIME SUPER SEGURO) - COM JRE 25
# ==============================================================================
FROM eclipse-temurin:25-jre-jammy

# Cria um usuário do sistema não-root por motivos estritos de segurança de infraestrutura
RUN useradd -u 1001 -m sre-user

# Define o diretório onde o microsserviço vai rodar
WORKDIR /app

# Copia o JAR otimizado gerado na Etapa 1 para o container de execução
COPY --from=builder /home/gradle/project/build/libs/thinklab-*-all.jar ./thinklab-manager.jar

# Altera a propriedade da pasta para o usuário não-root
RUN chown -R sre-user:sre-user /app

# Modifica o contexto de execução para o usuário seguro
USER sre-user

# Expõe a porta padrão do Micronaut
EXPOSE 8080

# Define as variáveis de ambiente padrões
ENV MICRONAUT_ENVIRONMENTS=prod \
    MONGO_URI="mongodb://localhost:27017/enterprise-infra-db"

# Comando de inicialização otimizando os parâmetros de memória da JVM para containers
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-jar", "thinklab-manager.jar"]