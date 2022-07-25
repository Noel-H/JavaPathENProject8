package com.noelh.tourguide.tracker;

import com.noelh.tourguide.model.User;
import com.noelh.tourguide.service.TourGuideService;

public class TrackUser extends Thread {

    private TourGuideService tourGuideService;
    private User user;

    public TrackUser(TourGuideService tourGuideService,User user){
        this.tourGuideService = tourGuideService;
        this.user = user;
    }

    @Override
    public void run() {
        tourGuideService.trackUserLocation(user);
    }
}
