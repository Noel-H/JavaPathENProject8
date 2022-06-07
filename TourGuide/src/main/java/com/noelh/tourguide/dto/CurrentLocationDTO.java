package com.noelh.tourguide.dto;

import gpsUtil.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CurrentLocationDTO {

    private String userId;
    private Location location;
}