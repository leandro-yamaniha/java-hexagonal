package com.restaurant.micronaut;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Restaurant Management API - Micronaut",
        version = "1.0.0",
        description = "REST API for restaurant management system using Micronaut framework"
    )
)
public class MicronautApplication {

    public static void main(String[] args) {
        Micronaut.run(MicronautApplication.class, args);
    }
}
