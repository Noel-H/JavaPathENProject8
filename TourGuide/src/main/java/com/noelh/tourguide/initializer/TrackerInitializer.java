package com.noelh.tourguide.initializer;

import com.noelh.tourguide.tracker.Tracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TrackerInitializer implements CommandLineRunner {

    @Autowired
    private Tracker tracker;

    @Override
    public void run(String... args) throws Exception {
        tracker.runExecutorService();
    }
}
