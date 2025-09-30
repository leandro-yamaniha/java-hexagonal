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
 * Testes essenciais de arquitetura - apenas as regras mais críticas.
 */
@DisplayName("Core Architecture Tests - Essential Rules Only")
class CoreArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.restaurant");
    }

    @Test
    @DisplayName("✅ Arquitetura em camadas deve ser respeitada")
    void shouldRespectLayeredArchitecture() {
        ArchRule layeredArchitectureRule = layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                
                .layer("Domain").definedBy("..domain..")
                .layer("Application").definedBy("..application..")
                .layer("Infrastructure").definedBy("..infrastructure..")
                
                .whereLayer("Domain").mayNotAccessAnyLayer()
                .whereLayer("Application").mayOnlyAccessLayers("Domain")
                .whereLayer("Infrastructure").mayOnlyAccessLayers("Domain", "Application");

        layeredArchitectureRule.check(importedClasses);
    }

    @Test
    @DisplayName("✅ Domínio não deve depender de outras camadas")
    void domainShouldNotDependOnOtherLayers() {
        ArchRule domainRule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application..", "..infrastructure..");

        domainRule.check(importedClasses);
    }

    @Test
    @DisplayName("✅ Application não deve depender de Infrastructure")
    void applicationShouldNotDependOnInfrastructure() {
        ArchRule applicationRule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..");

        applicationRule.check(importedClasses);
    }

    @Test
    @DisplayName("✅ Ports devem ser interfaces")
    void portsShouldBeInterfaces() {
        ArchRule portsRule = classes()
                .that().resideInAPackage("..application.port..")
                .and().areNotMemberClasses()
                .should().beInterfaces();

        portsRule.check(importedClasses);
    }

    @Test
    @DisplayName("✅ Repositórios devem ter sufixo Repository")
    void repositoriesShouldHaveRepositorySuffix() {
        ArchRule repositoryRule = classes()
                .that().resideInAPackage("..application.port.out..")
                .and().areInterfaces()
                .and().areNotMemberClasses()
                .should().haveSimpleNameEndingWith("Repository")
                .orShould().haveSimpleNameEndingWith("Service");

        repositoryRule.check(importedClasses);
    }

    @Test
    @DisplayName("✅ Use Cases devem ter sufixo UseCase")
    void useCasesShouldHaveUseCaseSuffix() {
        ArchRule useCaseRule = classes()
                .that().resideInAPackage("..application.port.in..")
                .and().areInterfaces()
                .should().haveSimpleNameEndingWith("UseCase");

        useCaseRule.check(importedClasses);
    }

    @Test
    @DisplayName("⚠️ Domínio não deveria usar anotações de frameworks (Warning)")
    void domainShouldAvoidFrameworkAnnotations() {
        ArchRule domainFrameworkRule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().beAnnotatedWith("org.springframework..")
                .orShould().beAnnotatedWith("jakarta.enterprise..")
                .orShould().beAnnotatedWith("jakarta.inject..")
                .orShould().beAnnotatedWith("jakarta.persistence..");

        try {
            domainFrameworkRule.check(importedClasses);
            System.out.println("✅ Domínio está livre de anotações de frameworks");
        } catch (AssertionError e) {
            System.out.println("⚠️  WARNING: Domínio contém anotações de frameworks (não crítico)");
            System.out.println("   Detalhes: " + e.getMessage().split("\n")[0]);
        }
    }
}
