package com.noelh.tourguide.initializer;

import com.noelh.tourguide.service.TourGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TourGuideServiceInitializer implements CommandLineRunner {

    @Autowired
    private TourGuideService tourGuideService;

    @Override
    public void run(String... args) throws Exception {
        tourGuideService.addShutDownHook();
    }
}
