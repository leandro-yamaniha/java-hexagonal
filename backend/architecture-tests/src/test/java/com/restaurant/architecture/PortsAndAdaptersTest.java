package com.restaurant.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Testes específicos para verificar a implementação correta do padrão Ports and Adapters.
 */
@DisplayName("Ports and Adapters Tests")
class PortsAndAdaptersTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.restaurant");
    }

    @Test
    @DisplayName("Ports de entrada devem ser interfaces")
    void inboundPortsShouldBeInterfaces() {
        ArchRule inboundPortsRule = classes()
                .that().resideInAPackage("..application.port.in..")
                .and().areNotMemberClasses()
                .should().beInterfaces();

        inboundPortsRule.check(importedClasses);
    }

    @Test
    @DisplayName("Ports de saída devem ser interfaces")
    void outboundPortsShouldBeInterfaces() {
        ArchRule outboundPortsRule = classes()
                .that().resideInAPackage("..application.port.out..")
                .and().areNotMemberClasses()
                .should().beInterfaces();

        outboundPortsRule.check(importedClasses);
    }

    @Test
    @DisplayName("Serviços de aplicação devem usar ports de entrada")
    void applicationServicesShouldUseInboundPorts() {
        ArchRule serviceImplementationRule = classes()
                .that().resideInAPackage("..application.service..")
                .should().dependOnClassesThat()
                .resideInAPackage("..application.port.in..");

        serviceImplementationRule.check(importedClasses);
    }

    @Test
    @DisplayName("Adapters de infraestrutura devem implementar ports de saída")
    void infrastructureAdaptersShouldImplementOutboundPorts() {
        ArchRule adapterImplementationRule = classes()
                .that().resideInAPackage("..infrastructure..")
                .and().areNotInterfaces()
                .and().areNotEnums()
                .and().areNotMemberClasses()
                .and().resideOutsideOfPackage("..infrastructure.persistence.entity..")
                .and().resideOutsideOfPackage("..infrastructure.persistence.mapper..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application.port.out..", "..domain..");

        adapterImplementationRule.check(importedClasses);
    }

    @Test
    @DisplayName("Serviços de aplicação devem depender apenas de ports de saída")
    void applicationServicesShouldOnlyDependOnOutboundPorts() {
        ArchRule servicesDependencyRule = classes()
                .that().resideInAPackage("..application.service..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..domain..",
                        "..application.port.in..",
                        "..application.port.out..",
                        "java..",
                        "javax..",
                        "jakarta..",
                        "org.slf4j.."
                );

        servicesDependencyRule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers/Resources devem depender apenas de ports de entrada")
    void controllersShouldOnlyDependOnInboundPorts() {
        ArchRule controllersRule = classes()
                .that().resideInAnyPackage("..quarkus..", "..springboot..")
                .and().haveSimpleNameEndingWith("Controller")
                .or().haveSimpleNameEndingWith("Resource")
                .should().onlyAccessClassesThat()
                .resideInAnyPackage(
                        "..domain..",
                        "..application.port.in..",
                        "java..",
                        "javax..",
                        "jakarta..",
                        "org.springframework..",
                        "org.slf4j..",
                        "com.fasterxml.jackson.."
                )
                .allowEmptyShould(true);

        controllersRule.check(importedClasses);
    }

    @Test
    @DisplayName("Repositórios JPA não devem ser acessados diretamente por serviços")
    void jpaRepositoriesShouldNotBeAccessedDirectlyByServices() {
        ArchRule jpaRepositoryAccessRule = noClasses()
                .that().resideInAPackage("..application.service..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure.persistence.repository..");

        jpaRepositoryAccessRule.check(importedClasses);
    }

    @Test
    @DisplayName("Entidades JPA não devem ser usadas fora da camada de infraestrutura")
    void jpaEntitiesShouldNotBeUsedOutsideInfrastructure() {
        ArchRule jpaEntityUsageRule = noClasses()
                .that().resideOutsideOfPackage("..infrastructure..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure.persistence.entity..");

        jpaEntityUsageRule.check(importedClasses);
    }

    @Test
    @DisplayName("Mappers devem ser usados apenas na camada de infraestrutura")
    void mappersShouldOnlyBeUsedInInfrastructure() {
        ArchRule mapperUsageRule = noClasses()
                .that().resideOutsideOfPackage("..infrastructure..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure.persistence.mapper..");

        mapperUsageRule.check(importedClasses);
    }

    @Test
    @DisplayName("Ports devem ser apenas interfaces")
    void portsShouldOnlyBeInterfaces() {
        ArchRule portImplementationRule = classes()
                .that().resideInAPackage("..application.port..")
                .and().areNotMemberClasses()
                .should().beInterfaces();

        portImplementationRule.check(importedClasses);
    }
}
