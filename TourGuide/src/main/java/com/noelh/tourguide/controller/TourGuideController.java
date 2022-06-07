package com.noelh.tourguide.controller;

import com.jsoniter.output.JsonStream;
import com.noelh.tourguide.dto.ClosestAttractionDTO;
import com.noelh.tourguide.model.UserReward;
import com.noelh.tourguide.service.TourGuideService;
import gpsUtil.location.VisitedLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;

import java.util.List;

@RestController
@RequestMapping("")
@Slf4j
public class TourGuideController {

    @Autowired
    private TourGuideService tourGuideService;

    @GetMapping("/")
    public String getIndex() {
        log.info("GET /");
        return "Greetings from TourGuide!";
    }

    @GetMapping("/getLocation/{userName}")
    public ResponseEntity<String> getLocation(@PathVariable("userName") String userName) {
        log.info("GET /getLocation/{}", userName);
        VisitedLocation visitedLocation;
        try {
            visitedLocation = tourGuideService.getUserLocation(tourGuideService.getUser(userName));
        } catch (NullPointerException e) {
            log.error("GET /getLocation/{} - ERROR : {} - UserName : {}, not found", userName, e.getMessage(), userName);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(JsonStream.serialize(visitedLocation.location));
    }

    @GetMapping("/getClosestAttractions/{userName}")
    public ResponseEntity<String> getClosestAttractions(@PathVariable("userName") String userName) {
        log.info("GET /getClosestAttractions/{}", userName);
        List<ClosestAttractionDTO> closestAttractionsList;
        try {
            closestAttractionsList = tourGuideService.getClosestAttractions(tourGuideService.getUser(userName));
        } catch (NullPointerException e) {
            log.error("GET /getClosestAttractions/{} - ERROR : {} - UserName : {}, not found", userName, e.getMessage(), userName);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(JsonStream.serialize(closestAttractionsList));
    }

    @GetMapping("/getRewards/{userName}")
    public ResponseEntity<String> getRewards(@PathVariable("userName") String userName) {
        log.info("GET /getRewards/{}", userName);
        List<UserReward> userRewardList;
        try {
            userRewardList = tourGuideService.getUserRewards(tourGuideService.getUser(userName));
        } catch (NullPointerException e){
            log.error("GET /getRewards/{} - ERROR : {} - UserName : {}, not found",userName, e.getMessage(), userName);
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(JsonStream.serialize(userRewardList));
    }

    @GetMapping("/getAllCurrentLocations")
    public ResponseEntity<String> getAllCurrentLocations() {
        log.info("GET /getAllCurrentLocations");
        return ResponseEntity.ok(JsonStream.serialize(tourGuideService.getAllCurrentLocations()));
    }

    @GetMapping("/getTripDeals/{userName}")
    public ResponseEntity<String> getTripDeals(@PathVariable("userName") String userName) {
        log.info("GET /getTripDeals/{}", userName);
        List<Provider> providers;
        try {
            providers = tourGuideService.getTripDeals(tourGuideService.getUser(userName));
        } catch (NullPointerException e){
            log.error("GET /getTripDeals/{} - ERROR : {} - UserName : {}, not found",userName, e.getMessage(), userName);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(JsonStream.serialize(providers));
    }
}