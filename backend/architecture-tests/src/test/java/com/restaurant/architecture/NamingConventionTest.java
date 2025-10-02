package com.restaurant.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Testes para verificar convenções de nomenclatura e estrutura do código.
 */
@DisplayName("Naming Convention Tests")
class NamingConventionTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.restaurant");
    }

    @Test
    @DisplayName("Entidades de domínio devem estar no pacote entity")
    void domainEntitiesShouldBeInEntityPackage() {
        ArchRule entityPackageRule = classes()
                .that().resideInAPackage("..domain..")
                .and().areNotEnums()
                .and().areNotInterfaces()
                .and().doNotHaveSimpleName("package-info")
                .should().resideInAPackage("..domain.entity..")
                .orShould().resideInAPackage("..domain.valueobject..");

        entityPackageRule.check(importedClasses);
    }

    @Test
    @DisplayName("Value Objects devem estar no pacote valueobject")
    void valueObjectsShouldBeInValueObjectPackage() {
        ArchRule valueObjectRule = classes()
                .that().resideInAPackage("..domain.valueobject..")
                .should().beEnums()
                .orShould().haveSimpleNameEndingWith("Value")
                .orShould().haveSimpleNameEndingWith("Money")
                .orShould().haveSimpleNameEndingWith("Status")
                .orShould().haveSimpleNameEndingWith("Category");

        valueObjectRule.check(importedClasses);
    }

    @Test
    @DisplayName("Repositórios devem ter sufixo Repository")
    void repositoriesShouldHaveRepositorySuffix() {
        ArchRule repositoryNamingRule = classes()
                .that().resideInAPackage("..application.port.out..")
                .and().areInterfaces()
                .and().areNotMemberClasses()
                .should().haveSimpleNameEndingWith("Repository")
                .orShould().haveSimpleNameEndingWith("Service");

        repositoryNamingRule.check(importedClasses);
    }

    @Test
    @DisplayName("Use Cases devem ter sufixo UseCase")
    void useCasesShouldHaveUseCaseSuffix() {
        ArchRule useCaseNamingRule = classes()
                .that().resideInAPackage("..application.port.in..")
                .and().areInterfaces()
                .should().haveSimpleNameEndingWith("UseCase");

        useCaseNamingRule.check(importedClasses);
    }

    @Test
    @DisplayName("Serviços de aplicação devem ter sufixo Service")
    void applicationServicesShouldHaveServiceSuffix() {
        ArchRule serviceNamingRule = classes()
                .that().resideInAPackage("..application.service..")
                .and().areNotInterfaces()
                .should().haveSimpleNameEndingWith("Service");

        serviceNamingRule.check(importedClasses);
    }

    @Test
    @DisplayName("Implementações JPA devem ter prefixo Jpa")
    void jpaImplementationsShouldHaveJpaPrefix() {
        ArchRule jpaImplementationRule = classes()
                .that().resideInAPackage("..infrastructure.persistence.repository..")
                .and().areNotInterfaces()
                .and().areNotMemberClasses()
                .and().areNotAnonymousClasses()
                .should().haveSimpleNameStartingWith("Jpa");

        jpaImplementationRule.check(importedClasses);
    }

    @Test
    @DisplayName("Entidades JPA devem ter sufixo Entity")
    void jpaEntitiesShouldHaveEntitySuffix() {
        ArchRule jpaEntityRule = classes()
                .that().resideInAPackage("..infrastructure.persistence.entity..")
                .should().haveSimpleNameEndingWith("Entity");

        jpaEntityRule.check(importedClasses);
    }

    @Test
    @DisplayName("Mappers devem ter sufixo Mapper")
    void mappersShouldHaveMapperSuffix() {
        ArchRule mapperNamingRule = classes()
                .that().resideInAPackage("..infrastructure.persistence.mapper..")
                .and().areNotMemberClasses()
                .and().areNotAnonymousClasses()
                .should().haveSimpleNameEndingWith("Mapper");

        mapperNamingRule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers REST devem ter sufixo Controller ou Resource")
    void restControllersShouldHaveControllerOrResourceSuffix() {
        ArchRule controllerNamingRule = classes()
                .that().resideInAnyPackage("..quarkus..", "..springboot..")
                .and().areNotInterfaces()
                .and().areNotEnums()
                .and().areNotMemberClasses()
                .should().haveSimpleNameEndingWith("Controller")
                .orShould().haveSimpleNameEndingWith("Resource")
                .orShould().haveSimpleNameEndingWith("Config")
                .orShould().haveSimpleNameEndingWith("Application")
                .allowEmptyShould(true);

        controllerNamingRule.check(importedClasses);
    }

    @Test
    @DisplayName("Interfaces não devem ter prefixo I")
    void interfacesShouldNotHaveIPrefix() {
        ArchRule interfaceNamingRule = noClasses()
                .that().areInterfaces()
                .should().haveSimpleNameStartingWith("I");

        interfaceNamingRule.check(importedClasses);
    }

    @Test
    @DisplayName("Classes de teste devem ter sufixo Test")
    void testClassesShouldHaveTestSuffix() {
        ArchRule testNamingRule = classes()
                .that().resideInAPackage("..test..")
                .and().areNotInterfaces()
                .and().areNotEnums()
                .and().areNotMemberClasses()
                .should().haveSimpleNameEndingWith("Test")
                .orShould().haveSimpleNameEndingWith("Tests")
                .allowEmptyShould(true);

        testNamingRule.check(importedClasses);
    }
}
