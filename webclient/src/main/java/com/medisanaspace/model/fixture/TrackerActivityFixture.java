package com.medisanaspace.model.fixture;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.medisanaspace.model.TrackerActivity;
import com.medisanaspace.model.TrackerActivityEntry;

/**
 * Generator for random TrackerActivities entries.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 * @version $Revision: 1.0 $
 */
public class TrackerActivityFixture {

	private final TrackerActivity trackerActivity;

	private Date expectedMeasurementDate;
	private Set<TrackerActivityEntry> expectedTrackerActivityEntries;

//	private final int MEAN_NUMBER_ACTIVITIES = 9;
//	private final int STANDARD_DERIVATION_NUMBER_ACTIVITIES = 4;

/**
 * ProfilePoint class defines an interval in which a user will be active.
 * A day profile, is then defined as a List of ProfilePoints.
 
 * Parameters:
 * From, to: define the interval of possible activity start times)
 * minSize, maxSize: define the how long the occurring activity can be.
 * minNumSteps, maxNumSteps: define a range of min/max steps of this activity.
 *
 * The time specifications are understood as 15 minutes units, beginning 
 * with 0:00 as "0" and 0:15 as 1. Thus, a day is divided into 25*4=96 time units.
 * 
 * */
	private class ProfilePoint {
		int from;
		int to;
		int minSize;
		int maxSize;
		int minNumSteps;
		int maxNumSteps;
		
		public ProfilePoint(int from, int to, int minSize, int maxSize,
				int minNumSteps, int maxNumSteps) {
			this.from = from;
			this.to = to;
			this.minSize = minSize;
			this.maxSize = maxSize;
			this.minNumSteps = minNumSteps;
			this.maxNumSteps = maxNumSteps;
		}

		public int getFrom() {
			return from;
		}

		public void setFrom(int from) {
			this.from = from;
		}

		public int getTo() {
			return to;
		}

		public void setTo(int to) {
			this.to = to;
		}

		public int getMinSize() {
			return minSize;
		}

		public void setMinSize(int minSize) {
			this.minSize = minSize;
		}

		public int getMaxSize() {
			return maxSize;
		}

		public void setMaxSize(int maxSize) {
			this.maxSize = maxSize;
		}

		public int getMinNumSteps() {
			return minNumSteps;
		}

		public void setMinNumSteps(int minNumSteps) {
			this.minNumSteps = minNumSteps;
		}

		public int getMaxNumSteps() {
			return maxNumSteps;
		}

		public void setMaxNumSteps(int maxNumSteps) {
			this.maxNumSteps = maxNumSteps;
		}
	}

	/**
	 * Constructor for TrackerActivityFixture.
	 * 
	 * @param index
	 *            int
	 * @param maxEntries
	 *            int
	 */
	public TrackerActivityFixture(final int index, final int maxEntries) {

		this.expectedMeasurementDate = new Date(new Date().getTime()
				- maxEntries * 3600L * 24L * 1000L + index
				* 3600L * 1000L * 24L);

		this.trackerActivity = new TrackerActivity();

		this.expectedTrackerActivityEntries = new HashSet<TrackerActivityEntry>();
		
		ArrayList<ProfilePoint> weekprofileList= new ArrayList<ProfilePoint>();
		ArrayList<ProfilePoint> earlyBirdProfileList= new ArrayList<ProfilePoint>();
		ArrayList<ProfilePoint> sportsManProfileList= new ArrayList<ProfilePoint>();
		
		ArrayList<ProfilePoint> weekendProfileList= new ArrayList<ProfilePoint>();
		
		// Define the day profiles
		// between 7h and 10h with a length of 2*15 to 4*15 minutes and min 200 steps and max steps 1000
		weekprofileList.add(new ProfilePoint(7*4, 10*4, 1, 4, 200, 1000));
		weekprofileList.add(new ProfilePoint(16*4, 19*4, 1, 4, 200, 1000));
		
		earlyBirdProfileList.add(new ProfilePoint(6*4, 9*4, 2, 5, 600, 4000));
		earlyBirdProfileList.add(new ProfilePoint(11*4, 13*4, 1, 2, 200, 1000));
		earlyBirdProfileList.add(new ProfilePoint(16*4, 19*4, 3, 6, 400, 2000));
		
		sportsManProfileList.add(new ProfilePoint(8*4, 10*4, 3, 6, 1000, 5000));
		sportsManProfileList.add(new ProfilePoint(14*4, 17*4, 2, 8, 1000, 10000));
		sportsManProfileList.add(new ProfilePoint(19*4, 23*4, 2, 8, 500, 1000));
		
		weekendProfileList.add(new ProfilePoint(8*4, 10*4, 2, 6, 300, 2000));
		weekendProfileList.add(new ProfilePoint(13*4, 15*4, 3, 8, 500, 3000));
		weekendProfileList.add(new ProfilePoint(18*4, 22*4, 3, 8, 500, 2500));
		ArrayList<ArrayList> lists = new ArrayList<ArrayList>();
		lists.add(earlyBirdProfileList);
		lists.add(weekendProfileList);
		lists.add(sportsManProfileList);
		lists.add(weekprofileList);
		
		// choose a profile at random
		ArrayList<ProfilePoint> profileList;
		Random r = new Random();
		profileList = lists.get(r.nextInt(lists.size()));
	
		int i = 24; // start at 6 o'clock
		// 15 min ^= 1 timeslot => 24*15 timeslots
		while (i <= 96) {
			ProfilePoint currentPoint = null;
			for(ProfilePoint point: profileList){
				if ( (point.getFrom() < i) && (i <= point.getTo()) ){
					currentPoint=point;
					break;
				}
			}
			if(currentPoint!=null){
				TrackerActivityEntry  activityEntry = new TrackerActivityEntry();
				int acticityTime;
				// retrieve random time slot out of the given range
				do {
				  double val = r.nextGaussian() * ((currentPoint.getTo()-currentPoint.getFrom())/2) + ((currentPoint.getTo()+currentPoint.getFrom())/2);
				  acticityTime = (int) Math.round(val);
				} while (acticityTime <= currentPoint.getFrom() || acticityTime >= currentPoint.getTo()); // everything outside the 75% area
				
				 int intervallSize = r.nextInt(currentPoint.getMaxSize() - currentPoint.getMinSize())+currentPoint.getMinSize();
				 int steps = r.nextInt(currentPoint.getMaxNumSteps() - currentPoint.getMinNumSteps())+currentPoint.getMinNumSteps();
				 activityEntry.setSteps(steps);
				 activityEntry.setDistance(steps*0.0007f); // 70cm ^= foot length
				 activityEntry.setStartTime(acticityTime*15); // time in minutes
				 activityEntry.setDuration(intervallSize*15); // duration in minutes
				 activityEntry.setCalories(0); //TODO
				 activityEntry.setRunningSteps(0); //TODO
				 
				this.expectedTrackerActivityEntries.add(activityEntry);
				i= currentPoint.getTo()+1;
				profileList.remove(currentPoint);
			}else{
				// random low event 10% probability for an activity occurring
				if( r.nextInt(100)<=10 ){
					TrackerActivityEntry  activityEntry = new TrackerActivityEntry();
				
					 int intervallSize = r.nextInt(3)+1; // minimal 1
					 int steps = r.nextInt(300)+1;// minimal 1 step
					 
					 activityEntry.setSteps(steps);
					 activityEntry.setDistance(steps*0.0007f); // 70cm ^= foot length
					 activityEntry.setStartTime(i*15); // time in minutes
					 activityEntry.setDuration(intervallSize*15); // duration in minutes
					 activityEntry.setCalories(0); //TODO
					 activityEntry.setRunningSteps(0); //TODO
					 
					 this.expectedTrackerActivityEntries.add(activityEntry);
					 i+=intervallSize;
				}
				i++;
			}
			


		}


		this.trackerActivity.setMeasurementDate(expectedMeasurementDate);

		this.trackerActivity
				.setTrackerActivityEntries(expectedTrackerActivityEntries);
	}

	/**
	 * Method getExpectedMeasurementDate.
	 * 
	 * @return Date
	 */
	public Date getExpectedMeasurementDate() {
		return expectedMeasurementDate;
	}

	/**
	 * Method setExpectedMeasurementDate.
	 * 
	 * @param expectedMeasurementDate
	 *            Date
	 */
	public void setExpectedMeasurementDate(Date expectedMeasurementDate) {
		this.expectedMeasurementDate = expectedMeasurementDate;
	}

	/**
	 * Method getTrackerActivity.
	 * 
	 * @return TrackerActivity
	 */
	public TrackerActivity getTrackerActivity() {
		return trackerActivity;
	}

}
