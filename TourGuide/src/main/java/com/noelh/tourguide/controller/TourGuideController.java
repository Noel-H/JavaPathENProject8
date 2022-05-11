package com.noelh.tourguide.controller;

import java.util.List;

import com.noelh.tourguide.service.TourGuideService;
import com.noelh.tourguide.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

@RestController
@RequestMapping("")
//annotation slf4g
public class TourGuideController {

//	@Autowired
//    private TourGuideService tourGuideService;
	
    @GetMapping("/")
    public String getIndex() {
        //log
        return "Greetings from TourGuide!";
    }
    
    @GetMapping("/getLocation/{userName}")
    public String getLocation(@PathVariable("userName") String userName) {
        //log
//    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
//		return JsonStream.serialize(visitedLocation.location);
		return "Location of : " + userName;
    }
    
    //  TODO: Change this method to no longer return a List of Attractions.
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral
    @GetMapping("/getNearbyAttractions/{userName}")
    public String getNearbyAttractions(@PathVariable("userName") String userName) {
        //log
//    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
//    	return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
        return "Nearby Attraction of : " + userName;
    }
    
    @GetMapping("/getRewards/{userName}")
    public String getRewards(@PathVariable("userName") String userName) {
        //log
//    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
        return "Rewards of : " + userName;
    }

    // TODO: Get a list of every user's most recent location as JSON
    //- Note: does not use gpsUtil to query for their current location,
    //        but rather gathers the user's current location from their stored location history.
    //
    // Return object should be the just a JSON mapping of userId to Locations similar to:
    //     {
    //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
    //        ...
    //     }
    @GetMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
    	//log
//    	return JsonStream.serialize("");
        return "All current locations";
    }
    
    @GetMapping("/getTripDeals/{userName}")
    public String getTripDeals(@PathVariable("userName") String userName) {
        //log
//    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
//    	return JsonStream.serialize(providers);
        return "Trip Deals of : " + userName;
    }

    // need to move that method in a service
//    private User getUser(String userName) {
//    	return tourGuideService.getUser(userName);
//    }
}