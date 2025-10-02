package com.restaurant.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.persistence.Entity;

@OpenAPIDefinition(
    info = @Info(
        title = "Restaurant Management API - Micronaut",
        version = "1.0.0",
        description = "REST API for restaurant management system using Micronaut framework"
    )
)
@Introspected(packages = "com.restaurant.infrastructure.persistence.entity", includedAnnotations = Entity.class)
public class MicronautApplication {

    public static void main(String[] args) {
        Micronaut.run(MicronautApplication.class, args);
    }
}
