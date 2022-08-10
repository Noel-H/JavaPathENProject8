package com.noelh.tourguide.service;

import com.noelh.tourguide.dto.ClosestAttractionDTO;
import com.noelh.tourguide.model.User;
import com.noelh.tourguide.tracker.Tracker;
import com.noelh.tourguide.userapi.InternalUserApi;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import tripPricer.TripPricer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TourGuideServiceTest {

    @InjectMocks
    private TourGuideService tourGuideService;

    @Mock
    private Tracker tracker;

    @Mock
    private GpsUtil gpsUtil;

    @Mock
    private RewardsService rewardsService;

    @Mock
    private InternalUserApi internalUserApi;

    @Mock
    private TripPricer tripPricer;

    @Test
    public void getUserLocation_Should_Return_VisitedLocation(){
        //Given
        User user = new User(new UUID(1,1),"","","");
        VisitedLocation visitedLocation = new VisitedLocation(new UUID(2,2),new Location(3,3),new Date());
        user.addToVisitedLocations(visitedLocation);

        //When
        VisitedLocation result = tourGuideService.getUserLocation(user);

        //Then
        assertThat(result).isEqualTo(visitedLocation);

    }

    @Test
    public void getUserLocation_Should_Return_VisitedLocation_With_User_Without_VisitedLocation(){
        //Given
        User user = new User(new UUID(1,1),"","","");
        VisitedLocation visitedLocation = new VisitedLocation(new UUID(2,2),new Location(3,3),new Date());
        when(gpsUtil.getUserLocation(any())).thenReturn(visitedLocation);

        //When
        VisitedLocation result = tourGuideService.getUserLocation(user);

        //Then
        verify(gpsUtil,times(1)).getUserLocation(any());
        assertThat(result).isEqualTo(visitedLocation);
    }

    @Test
    public void getClosestAttractions_Should_Return_List_Of_ClosestAttractionDTO(){
        //Given
        User user = new User(new UUID(1,1),"","","");
        Location userLocation = new Location(909.0,909.0);
        VisitedLocation visitedLocation = new VisitedLocation(new UUID(2,2),userLocation,new Date());
        user.addToVisitedLocations(visitedLocation);

        Location location = new Location(101.0,101.0);
        Location location2 = new Location(202.0,202.0);
        Location location3 = new Location(303.0,303.0);
        Location location4 = new Location(404.0,404.0);
        Location location5 = new Location(505.0,505.0);
        Location location6 = new Location(606.0,606.0);

        Attraction attraction = new Attraction("Attraction",
                "City1",
                "State1",
                location.latitude,
                location.longitude);

        Attraction attraction2 = new Attraction("Attraction2",
                "City2",
                "State2",
                location2.latitude,
                location2.longitude);

        Attraction attraction3 = new Attraction("Attraction3",
                "City3",
                "State3",
                location3.latitude,
                location3.longitude);

        Attraction attraction4 = new Attraction("Attraction4",
                "City4",
                "State4",
                location4.latitude,
                location4.longitude);

        Attraction attraction5 = new Attraction("Attraction5",
                "City5",
                "State5",
                location5.latitude,
                location5.longitude);

        Attraction attraction6 = new Attraction("Attraction6",
                "City6",
                "State6",
                location6.latitude,
                location6.longitude);

        ClosestAttractionDTO closestAttractionDTO = new ClosestAttractionDTO("Attraction",
                new Location(101.0,101.0),
                user.getLastVisitedLocation().location,
                8888.0,
                9);

        ClosestAttractionDTO closestAttractionDTO2 = new ClosestAttractionDTO("Attraction2",
                new Location(202.0,202.0),
                user.getLastVisitedLocation().location,
                7777.0,
                8);

        ClosestAttractionDTO closestAttractionDTO3 = new ClosestAttractionDTO("Attraction3",
                new Location(303.0,303.0),
                user.getLastVisitedLocation().location,
                9999.9,
                30);

        ClosestAttractionDTO closestAttractionDTO4 = new ClosestAttractionDTO("Attraction4",
                new Location(404.0,404.0),
                user.getLastVisitedLocation().location,
                4444.0,
                5);

        ClosestAttractionDTO closestAttractionDTO5 = new ClosestAttractionDTO("Attraction5",
                new Location(505.0,505.0),
                user.getLastVisitedLocation().location,
                5555.0,
                6);

        ClosestAttractionDTO closestAttractionDTO6 = new ClosestAttractionDTO("Attraction6",
                new Location(606.0,606.0),
                user.getLastVisitedLocation().location,
                6666.0,
                7);

        List<Attraction> attractionList = new ArrayList<>();
        attractionList.add(attraction);
        attractionList.add(attraction2);
        attractionList.add(attraction3);
        attractionList.add(attraction4);
        attractionList.add(attraction5);
        attractionList.add(attraction6);

        List<ClosestAttractionDTO> expectedResult = new ArrayList<>();
        expectedResult.add(closestAttractionDTO4);
        expectedResult.add(closestAttractionDTO5);
        expectedResult.add(closestAttractionDTO6);
        expectedResult.add(closestAttractionDTO2);
        expectedResult.add(closestAttractionDTO);

        when(gpsUtil.getAttractions()).thenReturn(attractionList);
        when(rewardsService.getDistance(any(),any())).thenReturn(8888.0,
                7777.0,
                9999.0,
                4444.0,
                5555.0,
                6666.0);
        when(rewardsService.getRewardPoints(any(),any())).thenReturn(9,
                8,
                30,
                5,
                6,
                7);

        //When
        List<ClosestAttractionDTO> result = tourGuideService.getClosestAttractions(user);

        //Then
        assertThat(result.size()).isEqualTo(5);

        assertThat(result.get(0).getName()).isEqualTo(expectedResult.get(0).getName());
        assertThat(result.get(0).getLocation().latitude).isEqualTo(expectedResult.get(0).getLocation().latitude);
        assertThat(result.get(0).getLocation().longitude).isEqualTo(expectedResult.get(0).getLocation().longitude);
        assertThat(result.get(0).getUserLocation()).isEqualTo(expectedResult.get(0).getUserLocation());
        assertThat(result.get(0).getDistanceBetweenUserAndAttraction()).isEqualTo(expectedResult.get(0).getDistanceBetweenUserAndAttraction());
        assertThat(result.get(0).getRewardPoint()).isEqualTo(expectedResult.get(0).getRewardPoint());

        assertThat(result.get(1).getName()).isEqualTo(expectedResult.get(1).getName());
        assertThat(result.get(1).getLocation().latitude).isEqualTo(expectedResult.get(1).getLocation().latitude);
        assertThat(result.get(1).getLocation().longitude).isEqualTo(expectedResult.get(1).getLocation().longitude);
        assertThat(result.get(1).getUserLocation()).isEqualTo(expectedResult.get(1).getUserLocation());
        assertThat(result.get(1).getDistanceBetweenUserAndAttraction()).isEqualTo(expectedResult.get(1).getDistanceBetweenUserAndAttraction());
        assertThat(result.get(1).getRewardPoint()).isEqualTo(expectedResult.get(1).getRewardPoint());

        assertThat(result.get(2).getName()).isEqualTo(expectedResult.get(2).getName());
        assertThat(result.get(2).getLocation().latitude).isEqualTo(expectedResult.get(2).getLocation().latitude);
        assertThat(result.get(2).getLocation().longitude).isEqualTo(expectedResult.get(2).getLocation().longitude);
        assertThat(result.get(2).getUserLocation()).isEqualTo(expectedResult.get(2).getUserLocation());
        assertThat(result.get(2).getDistanceBetweenUserAndAttraction()).isEqualTo(expectedResult.get(2).getDistanceBetweenUserAndAttraction());
        assertThat(result.get(2).getRewardPoint()).isEqualTo(expectedResult.get(2).getRewardPoint());

        assertThat(result.get(3).getName()).isEqualTo(expectedResult.get(3).getName());
        assertThat(result.get(3).getLocation().latitude).isEqualTo(expectedResult.get(3).getLocation().latitude);
        assertThat(result.get(3).getLocation().longitude).isEqualTo(expectedResult.get(3).getLocation().longitude);
        assertThat(result.get(3).getUserLocation()).isEqualTo(expectedResult.get(3).getUserLocation());
        assertThat(result.get(3).getDistanceBetweenUserAndAttraction()).isEqualTo(expectedResult.get(3).getDistanceBetweenUserAndAttraction());
        assertThat(result.get(3).getRewardPoint()).isEqualTo(expectedResult.get(3).getRewardPoint());

        assertThat(result.get(4).getName()).isEqualTo(expectedResult.get(4).getName());
        assertThat(result.get(4).getLocation().latitude).isEqualTo(expectedResult.get(4).getLocation().latitude);
        assertThat(result.get(4).getLocation().longitude).isEqualTo(expectedResult.get(4).getLocation().longitude);
        assertThat(result.get(4).getUserLocation()).isEqualTo(expectedResult.get(4).getUserLocation());
        assertThat(result.get(4).getDistanceBetweenUserAndAttraction()).isEqualTo(expectedResult.get(4).getDistanceBetweenUserAndAttraction());
        assertThat(result.get(4).getRewardPoint()).isEqualTo(expectedResult.get(4).getRewardPoint());
    }
}