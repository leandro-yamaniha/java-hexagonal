package com.restaurant.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class QuarkusRestaurantApplication implements QuarkusApplication {

    public static void main(String... args) {
        Quarkus.run(QuarkusRestaurantApplication.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        System.out.println("Restaurant Management System - Quarkus Application Started");
        Quarkus.waitForExit();
        return 0;
    }
}
