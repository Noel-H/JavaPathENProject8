package com.noelh.tourguide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import rewardCentral.RewardCentral;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class RewardsServiceTest {

    @InjectMocks
    private RewardsService rewardsService;

    @Mock
    private GpsUtil gpsUtil;

    @Mock
    private RewardCentral rewardCentral;

    @Test
    public void isWithinAttractionProximity_Should_Return_False(){
        //Given
        Location location = new Location(101.0,101.0);
        Location location2 = new Location(103.9,103.9);

        Attraction attraction = new Attraction("Attraction",
                "City1",
                "State1",
                location.latitude,
                location.longitude);

        //When
        boolean result = rewardsService.isWithinAttractionProximity(attraction, location2);

        //Then
        assertThat(result).isFalse();
    }

    @Test
    public void isWithinAttractionProximity_Should_Return_True(){
        //Given
        Location location = new Location(101.0,101.0);
        Location location2 = new Location(103.8,103.8);

        Attraction attraction = new Attraction("Attraction",
                "City1",
                "State1",
                location.latitude,
                location.longitude);

        //When
        boolean result = rewardsService.isWithinAttractionProximity(attraction, location2);

        //Then
        assertThat(result).isTrue();
    }

}