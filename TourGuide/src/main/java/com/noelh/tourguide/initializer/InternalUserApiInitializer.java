package com.noelh.tourguide.initializer;

import com.noelh.tourguide.userapi.InternalUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InternalUserApiInitializer implements CommandLineRunner {

    @Autowired
    private InternalUserApi internalUserApi;

    @Override
    public void run(String... args) throws Exception {
        internalUserApi.runInitializeInternalUsers();
    }
}