package com.noelh.tourguide.service;

import com.noelh.tourguide.dto.ClosestAttractionDTO;
import com.noelh.tourguide.dto.CurrentLocationDTO;
import com.noelh.tourguide.helper.InternalTestHelper;
import com.noelh.tourguide.model.User;
import com.noelh.tourguide.model.UserReward;
import com.noelh.tourguide.tracker.Tracker;
import com.noelh.tourguide.userapi.InternalUserApi;
import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class TourGuideService {

	@Autowired
	private GpsUtil gpsUtil;

	@Autowired
	private RewardsService rewardsService;

	@Autowired
	private InternalUserApi internalUserApi;

	public TripPricer tripPricer = new TripPricer();
	public Tracker tracker = new Tracker(this);

		public List<User> getAllUsers() {
		return internalUserApi.getInternalUserMap().values().stream().collect(Collectors.toList());
	}

		public User getUser(String userName) {
		return internalUserApi.getInternalUserMap(userName);
	}

		public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
				user.getLastVisitedLocation() :
				trackUserLocation(user);
		return visitedLocation;
	}

		public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

		public List<ClosestAttractionDTO> getClosestAttractions(User user) throws NullPointerException{
		return gpsUtil.getAttractions().stream()
				.map(attraction -> new ClosestAttractionDTO(
						attraction.attractionName,
						new Location(attraction.latitude,attraction.longitude),
						user.getLastVisitedLocation().location,
						rewardsService.getDistance(user.getLastVisitedLocation().location,new Location(attraction.latitude,attraction.longitude)),
						rewardsService.getRewardPoints(attraction, user)
				))
				.sorted(Comparator.comparing(ClosestAttractionDTO::getDistanceBetweenUserAndAttraction))
				.limit(5)
				.collect(Collectors.toList());
	}

		public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public List<CurrentLocationDTO> getAllCurrentLocations() {
		return getAllUsers().stream()
				.map(user -> new CurrentLocationDTO(
						user.getUserId().toString(),
						user.getLastVisitedLocation().location
				))
				.collect(Collectors.toList());
	}

		public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(internalUserApi.getTripPricerApiKey(), user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}


//	boolean testMode = true;
//
//	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
//		this.gpsUtil = gpsUtil;
//		this.rewardsService = rewardsService;
//
//		if(testMode) {
//			logger.info("TestMode enabled");
//			logger.debug("Initializing users");
//			initializeInternalUsers();
//			logger.debug("Finished initializing users");
//		}
//		tracker = new Tracker(this);
//		addShutDownHook();
//	}
//
//	public void addUser(User user) {
//		if(!internalUserMap.containsKey(user.getUserName())) {
//			internalUserMap.put(user.getUserName(), user);
//		}
//	}
//
//	private void addShutDownHook() {
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			public void run() {
//				tracker.stopTracking();
//			}
//		});
//	}
//
}
