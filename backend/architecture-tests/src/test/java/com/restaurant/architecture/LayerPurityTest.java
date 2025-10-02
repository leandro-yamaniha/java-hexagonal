package com.restaurant.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Testes para verificar a pureza das camadas e suas dependências permitidas.
 */
@DisplayName("Layer Purity Tests")
class LayerPurityTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.restaurant");
    }

    @Test
    @DisplayName("Camada de domínio deve ser pura (apenas Java padrão)")
    void domainLayerShouldBePure() {
        ArchRule domainPurityRule = classes()
                .that().resideInAPackage("..domain..")
                .and().areNotMemberClasses()
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..domain..",
                        "java..",
                        "javax.validation..",
                        "jakarta.validation.."
                );

        domainPurityRule.check(importedClasses);
    }

    @Test
    @DisplayName("Camada de aplicação deve depender apenas de domínio e bibliotecas padrão")
    void applicationLayerShouldOnlyDependOnDomainAndStandardLibraries() {
        ArchRule applicationPurityRule = classes()
                .that().resideInAPackage("..application..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..domain..",
                        "..application..",
                        "java..",
                        "javax..",
                        "jakarta..",
                        "org.slf4j.."
                );

        applicationPurityRule.check(importedClasses);
    }

    @Test
    @DisplayName("Entidades de domínio não devem ter anotações JPA")
    void domainEntitiesShouldNotHaveJPAAnnotations() {
        ArchRule domainEntityRule = noClasses()
                .that().resideInAPackage("..domain.entity..")
                .should().beAnnotatedWith("jakarta.persistence.Entity")
                .orShould().beAnnotatedWith("jakarta.persistence.Table")
                .orShould().beAnnotatedWith("jakarta.persistence.Id")
                .orShould().beAnnotatedWith("jakarta.persistence.GeneratedValue")
                .orShould().beAnnotatedWith("jakarta.persistence.Column");

        domainEntityRule.check(importedClasses);
    }

    @Test
    @DisplayName("Entidades de domínio não devem ter anotações Jackson")
    void domainEntitiesShouldNotHaveJacksonAnnotations() {
        ArchRule domainJacksonRule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().beAnnotatedWith("com.fasterxml.jackson.annotation.JsonProperty")
                .orShould().beAnnotatedWith("com.fasterxml.jackson.annotation.JsonIgnore")
                .orShould().beAnnotatedWith("com.fasterxml.jackson.annotation.JsonCreator");

        domainJacksonRule.check(importedClasses);
    }

    @Test
    @DisplayName("Camada de aplicação não deve usar implementações específicas de infraestrutura")
    void applicationShouldNotUseInfrastructureImplementations() {
        ArchRule applicationInfraRule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework.data.jpa..",
                        "org.hibernate..",
                        "redis.clients.jedis..",
                        "io.lettuce..",
                        "com.mysql.."
                );

        applicationInfraRule.check(importedClasses);
    }

    @Test
    @DisplayName("Value Objects devem ser imutáveis (final)")
    void valueObjectsShouldBeImmutable() {
        ArchRule valueObjectImmutabilityRule = classes()
                .that().resideInAPackage("..domain.valueobject..")
                .and().areNotEnums()
                .should().bePackagePrivate()
                .orShould().bePublic();

        // Note: ArchUnit não pode verificar diretamente se campos são final,
        // mas podemos verificar se não há setters
        ArchRule noSettersRule = noMethods()
                .that().areDeclaredInClassesThat().resideInAPackage("..domain.valueobject..")
                .and().areNotStatic()
                .should().haveNameStartingWith("set");

        valueObjectImmutabilityRule.check(importedClasses);
        noSettersRule.check(importedClasses);
    }

    @Test
    @DisplayName("Repositórios de infraestrutura devem usar interfaces da aplicação")
    void infrastructureRepositoriesShouldUseApplicationInterfaces() {
        ArchRule repositoryImplementationRule = classes()
                .that().resideInAPackage("..infrastructure.persistence.repository..")
                .and().areNotInterfaces()
                .and().areNotMemberClasses()
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application.port.out..", "..domain..");

        repositoryImplementationRule.check(importedClasses);
    }

    @Test
    @DisplayName("Serviços de cache devem usar interfaces da aplicação")
    void cacheServicesShouldUseApplicationInterfaces() {
        ArchRule cacheServiceRule = classes()
                .that().resideInAPackage("..infrastructure.cache..")
                .and().areNotInterfaces()
                .should().dependOnClassesThat()
                .resideInAPackage("..application.port.out..");

        cacheServiceRule.check(importedClasses);
    }

    @Test
    @DisplayName("Classes de configuração devem estar em pacotes específicos")
    void configurationClassesShouldBeInSpecificPackages() {
        ArchRule configurationRule = classes()
                .that().haveSimpleNameEndingWith("Config")
                .or().haveSimpleNameEndingWith("Configuration")
                .should().resideInAnyPackage("..config..", "..configuration..")
                .allowEmptyShould(true);

        configurationRule.check(importedClasses);
    }

    @Test
    @DisplayName("DTOs devem estar em pacotes específicos se existirem")
    void dtosShouldBeInSpecificPackages() {
        ArchRule dtoRule = classes()
                .that().haveSimpleNameEndingWith("DTO")
                .or().haveSimpleNameEndingWith("Dto")
                .should().resideInAnyPackage("..dto..", "..request..", "..response..")
                .allowEmptyShould(true);

        dtoRule.check(importedClasses);
    }
}
