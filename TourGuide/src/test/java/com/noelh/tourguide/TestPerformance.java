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
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class TestPerformance {

	@Test
	public void highVolumeTrackLocation() {
		StopWatch stopWatch = new StopWatch();
		long timeLimitOf15Minutes = 900000;
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

		stopWatch.start();
		tracker.trackAllUser(userList);
		stopWatch.stop();

		assertThat(stopWatch.getTotalTimeMillis()).isLessThan(timeLimitOf15Minutes);
		System.out.println("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTotalTimeMillis()) + " seconds.");
		userList.forEach(user ->
				assertThat(user.getVisitedLocations().size()).isEqualTo(1));
	}

	@Test
	public void highVolumeGetRewards() {
		StopWatch stopWatch = new StopWatch();
		long timeLimitOf20Minutes = 1200000;

		Locale.setDefault(Locale.US);
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil,rewardCentral);
		InternalUserApi internalUserApi = new InternalUserApi();
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, internalUserApi);
		Tracker tracker = new Tracker(tourGuideService);
		internalUserApi.runInitializeInternalUsers();
		List<User> userList = tourGuideService.getAllUsers();
		userList.forEach(User::clearUserRewards);

		stopWatch.start();
		tracker.calculateAllUserRewards(userList);
		stopWatch.stop();

		assertThat(stopWatch.getTotalTimeMillis()).isLessThan(timeLimitOf20Minutes);
		System.out.println("Calculation Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTotalTimeMillis()) + " seconds.");

	}

}
