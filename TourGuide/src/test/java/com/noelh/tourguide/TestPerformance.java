package com.noelh.tourguide;

import com.noelh.tourguide.model.User;
import com.noelh.tourguide.service.RewardsService;
import com.noelh.tourguide.service.TourGuideService;
import com.noelh.tourguide.tracker.Tracker;
import com.noelh.tourguide.userapi.InternalUserApi;
import gpsUtil.GpsUtil;
import org.junit.Test;
import org.springframework.util.StopWatch;
import rewardCentral.RewardCentral;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class TestPerformance {

	/*
	 * A note on performance improvements:
	 *
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *
	 *
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent.
	 *
	 *     These are performance metrics that we are trying to hit:
	 *
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 *
	 *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */

//	@Ignore
//	@Test
//	public void highVolumeTrackLocation() {
//		Locale.setDefault(Locale.US);
//		GpsUtil gpsUtil = new GpsUtil();
//		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
//		// Users should be incremented up to 100,000, and test finishes within 15 minutes
//		InternalTestHelper.setInternalUserNumber(100);
//		TourGuideService tourGuideService = new TourGuideService();
//
//		List<User> allUsers = new ArrayList<>();
//		allUsers = tourGuideService.getAllUsers();
//
//	    StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
//		for(User user : allUsers) {
//			tourGuideService.trackUserLocation(user);
//		}
//		stopWatch.stop();
//		tourGuideService.tracker.stopTracking();
//
//		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
//		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
//	}


//	@Ignore
//	@Test
//	public void highVolumeGetRewards() {
//		GpsUtil gpsUtil = new GpsUtil();
//		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
//
//		// Users should be incremented up to 100,000, and test finishes within 20 minutes
//		InternalTestHelper.setInternalUserNumber(100);
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
//		TourGuideService tourGuideService = new TourGuideService();
//
//	    Attraction attraction = gpsUtil.getAttractions().get(0);
//		List<User> allUsers = new ArrayList<>();
//		allUsers = tourGuideService.getAllUsers();
//		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));
//
//	    allUsers.forEach(u -> rewardsService.calculateRewards(u));
//
//		for(User user : allUsers) {
//			assertTrue(user.getUserRewards().size() > 0);
//		}
//		stopWatch.stop();
//		tourGuideService.tracker.stopTracking();
//
//		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
//		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
//	}

	@Test
	public void highVolumeTrackLocation() throws InterruptedException {
		//Given
		// chrono
		StopWatch stopWatch = new StopWatch();
		long timeLimitOf15Minutes = 900000;
		// test prerequis
		Locale.setDefault(Locale.US);
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil,rewardCentral);
		InternalUserApi internalUserApi = new InternalUserApi();
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);
		Tracker tracker = new Tracker(tourGuideService);
		internalUserApi.runInitializeInternalUsers();
		List<User> userList = tourGuideService.getAllUsers();
		userList.forEach(User::clearVisitedLocations);
		//When
		//start chrono
		stopWatch.start();
		//run test
		tracker.trackAllUser(userList);
		//stop chrono
		stopWatch.stop();

		//Then
		//assert result chrono
		assertThat(stopWatch.getTotalTimeMillis()).isLessThan(timeLimitOf15Minutes);
		System.out.println("StopWatch time = " + stopWatch.getTotalTimeMillis());
		userList.forEach(user ->
				assertThat(user.getVisitedLocations().size()).isEqualTo(1));
	}

//    @Test
//    public void highVolumeGetRewards() {
//		//Given
//		StopWatch stopWatch = new StopWatch();
//		long timeLimitOf20Minutes = 1200000;
//
//		//When
//		stopWatch.start();
//
//		stopWatch.stop();
//
//		//Then
//		assertThat(stopWatch.getTotalTimeMillis()).isLessThan(timeLimitOf20Minutes);
//		System.out.println(stopWatch.getTotalTimeMillis());
//	}

}
