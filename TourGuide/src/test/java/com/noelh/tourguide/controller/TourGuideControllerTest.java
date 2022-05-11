package com.noelh.tourguide.controller;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TourGuideController.class)
public class TourGuideControllerTest extends TestCase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getIndex_Should_Return_Ok() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void getLocation_Should_Return_Ok() throws Exception {
        mockMvc.perform(get("/getLocation/TestUserName"))
                .andExpect(status().isOk());
    }

    @Test
    public void getNearbyAttractions_Should_Return_Ok() throws Exception {
        mockMvc.perform(get("/getNearbyAttractions/TestUserName"))
                .andExpect(status().isOk());
    }

    @Test
    public void getRewards_Should_Return_Ok() throws Exception {
        mockMvc.perform(get("/getRewards/TestUserName"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllCurrentLocations_Should_Return_Ok() throws Exception {
        mockMvc.perform(get("/getAllCurrentLocations"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTripDeals_Should_Return_Ok() throws Exception {
        mockMvc.perform(get("/getTripDeals/TestUserName"))
                .andExpect(status().isOk());
    }

}