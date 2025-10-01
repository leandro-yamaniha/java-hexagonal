package com.restaurant.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Testes de arquitetura para garantir a integridade da arquitetura hexagonal
 * e suas convenções no sistema de gerenciamento de restaurante.
 */
@DisplayName("Hexagonal Architecture Tests")
class HexagonalArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.restaurant");
    }

    @Test
    @DisplayName("Deve respeitar a arquitetura em camadas hexagonal")
    void shouldRespectHexagonalLayeredArchitecture() {
        ArchRule layeredArchitectureRule = layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                
                // Definição das camadas
                .layer("Domain").definedBy("..domain..")
                .layer("Application").definedBy("..application..")
                .layer("Infrastructure").definedBy("..infrastructure..")
                .optionalLayer("Quarkus").definedBy("..quarkus..")
                .optionalLayer("SpringBoot").definedBy("..springboot..")
                .optionalLayer("Micronaut").definedBy("..micronaut..")
                
                // Regras de dependência
                .whereLayer("Domain").mayNotAccessAnyLayer()
                .whereLayer("Application").mayOnlyAccessLayers("Domain")
                .whereLayer("Infrastructure").mayOnlyAccessLayers("Domain", "Application")
                .whereLayer("Quarkus").mayOnlyAccessLayers("Domain", "Application", "Infrastructure")
                .whereLayer("SpringBoot").mayOnlyAccessLayers("Domain", "Application", "Infrastructure")
                .whereLayer("Micronaut").mayOnlyAccessLayers("Domain", "Application", "Infrastructure");

        layeredArchitectureRule.check(importedClasses);
    }

    @Test
    @DisplayName("Camada de domínio não deve depender de nenhuma outra camada")
    void domainLayerShouldNotDependOnAnyOtherLayer() {
        ArchRule domainRule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application..", "..infrastructure..", "..quarkus..", "..springboot..", "..micronaut..");

        domainRule.check(importedClasses);
    }

    @Test
    @DisplayName("Camada de aplicação deve depender apenas da camada de domínio")
    void applicationLayerShouldOnlyDependOnDomainLayer() {
        ArchRule applicationRule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..infrastructure..", "..quarkus..", "..springboot..", "..micronaut..");

        applicationRule.check(importedClasses);
    }

    @Test
    @DisplayName("Camada de infraestrutura pode depender de domínio e aplicação")
    void infrastructureLayerCanDependOnDomainAndApplication() {
        ArchRule infrastructureRule = noClasses()
                .that().resideInAPackage("..infrastructure..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..quarkus..", "..springboot..", "..micronaut..");

        infrastructureRule.check(importedClasses);
    }

    @Test
    @DisplayName("Domínio não deve usar anotações de frameworks")
    void domainShouldNotUseFrameworkAnnotations() {
        ArchRule domainFrameworkRule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().beAnnotatedWith("org.springframework..")
                .orShould().beAnnotatedWith("jakarta.enterprise..")
                .orShould().beAnnotatedWith("jakarta.inject..")
                .orShould().beAnnotatedWith("jakarta.persistence..")
                .orShould().beAnnotatedWith("org.hibernate..")
                .orShould().beAnnotatedWith("com.fasterxml.jackson..");

        domainFrameworkRule.check(importedClasses);
    }

    @Test
    @DisplayName("Domínio não deve importar bibliotecas de frameworks")
    void domainShouldNotImportFrameworkLibraries() {
        ArchRule domainImportRule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.enterprise..",
                        "jakarta.inject..",
                        "jakarta.persistence..",
                        "org.hibernate..",
                        "com.fasterxml.jackson..",
                        "redis.clients..",
                        "io.lettuce..",
                        "com.mysql.."
                );

        domainImportRule.check(importedClasses);
    }
}
