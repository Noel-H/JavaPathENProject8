package com.noelh.tourguide.service;

import com.noelh.tourguide.dto.ClosestAttractionDTO;
import com.noelh.tourguide.model.User;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TourGuideServiceTest extends TestCase {

    @InjectMocks
    private TourGuideService tourGuideService;

    @Mock
    private GpsUtil gpsUtil;

    @Mock
    private RewardsService rewardsService;

//    @Test
//    public void GetUser() {
//        //Given
//        User user = new User(new UUID(1,1),"internalUserTest1","","");
//        Map<String, User> internalUserTestMap = new HashMap<>();
//        internalUserTestMap.put("internalUserTest1", user);
//
//        //When
//        User result = tourGuideService.getUser("internalUserTest1");
//
//        //Then
//        assertThat(user).isEqualTo(result);
//    }

    @Test
    public void GetUserLocation_Should_Pass_When_User_Have_VisitedLocation() {
        //Given
        VisitedLocation visitedLocation = new VisitedLocation(new UUID(2,2),new Location(3,3),new Date());
        User user = new User(new UUID(1,1),"internalUserTest1","","");
        user.addToVisitedLocations(visitedLocation);
        //When
        VisitedLocation result = tourGuideService.getUserLocation(user);
        //Then
        assertThat(visitedLocation).isEqualTo(result);
    }

    @Test
    public void GetUserLocation_Should_Pass_When_User_Have_No_VisitedLocation() {
        //Given
        VisitedLocation visitedLocation = new VisitedLocation(new UUID(2,2),new Location(3,3),new Date());
        User user = new User(new UUID(1,1),"internalUserTest1","","");
        when(gpsUtil.getUserLocation(any())).thenReturn(visitedLocation);
        doNothing().when(rewardsService).calculateRewards(user);
        //When
        VisitedLocation result = tourGuideService.getUserLocation(user);
        //Then
        assertThat(visitedLocation).isEqualTo(result);
    }

    @Test
    public void GetClosestAttractions() {
        //Given
        User user = new User(new UUID(1,1),"internalUserTest1","","");
        VisitedLocation visitedLocation = new VisitedLocation(new UUID(2,2),new Location(100,100),new Date());
        user.addToVisitedLocations(visitedLocation);

        List<Attraction> attractionList = new ArrayList<>();
        Attraction attraction = new Attraction("Attraction0","","",0,0);
        Attraction attraction1 = new Attraction("Attraction1","","",1,1);
        Attraction attraction2 = new Attraction("Attraction2","","",2,2);
        Attraction attraction3 = new Attraction("Attraction3","","",3,3);
        Attraction attraction4 = new Attraction("Attraction4","","",4,4);
        Attraction attraction5 = new Attraction("Attraction5","","",5,5);
        attractionList.add(attraction);
        attractionList.add(attraction1);
        attractionList.add(attraction2);
        attractionList.add(attraction3);
        attractionList.add(attraction4);
        attractionList.add(attraction5);

        List<ClosestAttractionDTO> expectedResult = new ArrayList<>();
        ClosestAttractionDTO closestAttractionDTO = new ClosestAttractionDTO("Attraction0",new Location(attraction.latitude,attraction.longitude),user.getLastVisitedLocation().location,11.7,11);
        ClosestAttractionDTO closestAttractionDTO1 = new ClosestAttractionDTO("Attraction1",new Location(attraction1.latitude,attraction1.longitude),user.getLastVisitedLocation().location,11.8,11);
        ClosestAttractionDTO closestAttractionDTO2 = new ClosestAttractionDTO("Attraction2",new Location(attraction2.latitude,attraction2.longitude),user.getLastVisitedLocation().location,11.9,11);
        ClosestAttractionDTO closestAttractionDTO3 = new ClosestAttractionDTO("Attraction3",new Location(attraction3.latitude,attraction3.longitude),user.getLastVisitedLocation().location,11.10,11);
        ClosestAttractionDTO closestAttractionDTO4 = new ClosestAttractionDTO("Attraction4",new Location(attraction4.latitude,attraction4.longitude),user.getLastVisitedLocation().location,11.11,11);
        expectedResult.add(closestAttractionDTO4);
        expectedResult.add(closestAttractionDTO3);
        expectedResult.add(closestAttractionDTO2);
        expectedResult.add(closestAttractionDTO1);
        expectedResult.add(closestAttractionDTO);

        when(gpsUtil.getAttractions()).thenReturn(attractionList);
        when(rewardsService.getDistance(user.getLastVisitedLocation().location,new Location(attraction4.latitude,attraction4.longitude))).thenReturn(11.11);
        when(rewardsService.getDistance(user.getLastVisitedLocation().location,new Location(attraction3.latitude,attraction3.longitude))).thenReturn(11.10);
        when(rewardsService.getDistance(user.getLastVisitedLocation().location,new Location(attraction2.latitude,attraction2.longitude))).thenReturn(11.9);
        when(rewardsService.getDistance(user.getLastVisitedLocation().location,new Location(attraction1.latitude,attraction1.longitude))).thenReturn(11.8);
        when(rewardsService.getDistance(user.getLastVisitedLocation().location,new Location(attraction.latitude,attraction.longitude))).thenReturn(11.7);
        when(rewardsService.getRewardPoints(any(),any())).thenReturn(11);
        //When
        List<ClosestAttractionDTO> result = tourGuideService.getClosestAttractions(user);

        //Then
        assertThat(expectedResult).isEqualTo(result);
    }

    @Test
    public void GetUserRewards() {
    }

    @Test
    public void GetAllCurrentLocations() {
    }

    @Test
    public void GetTripDeals() {
    }

//    public void GetAllUsers() {
//    }
//
//    public void AddUser() {
//    }
//
//    public void TrackUserLocation() {
//    }
}