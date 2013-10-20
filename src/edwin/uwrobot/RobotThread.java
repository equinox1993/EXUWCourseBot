/*
 * Yuwei Huang, 2012
 * This file provides the timer task to run the robot.
 */

package edwin.uwrobot;

import java.util.Timer;

public class RobotThread {
	private static int SLN;
	private static Quarter quarter;
	private static int year;
	private static Authorization auth;
	private static Timer timer;

	//sets up and triggers the timer.
	public static void thread(String username, String password, 
			int SLN, Quarter quarter, int year, long period) throws Exception {
		RobotThread.SLN=SLN;
		RobotThread.quarter=quarter;
		RobotThread.year=year;
		auth=new Authorization(username, password); //have the browser authorized by UWNetID.
		 
		timer=new Timer();
		timer.schedule(new RobotTask(), 0, period);
	}
	
	static class RobotTask extends java.util.TimerTask {
		@Override public void run() {
			//scrapes the status of the course.
			CourseScraper scraper=new CourseScraper(auth);
			CourseStatus status=CourseStatus.Unknown;
			try {
				status=scraper.scrape(SLN, quarter, year);
			} catch (Exception e) {
				System.out.println("Scraping course info failed: "+e.getLocalizedMessage());
				return;
			}
			System.out.println("Course Scraping Result: "+status);
			
			if (status!=CourseStatus.Open)
				return;
			//ends this period if the course is not open
			
			//try to register the course
			System.out.println("Trying to register...");
			CourseRegister register=new CourseRegister(auth);
			boolean res=false;
			try {
				res=register.register(SLN);
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				timer.cancel();
				System.exit(-1);
			}
			catch (Exception e) 
			{}
			if (res) {
				System.out.println("Registration succeeded.");
				//Do fxxking exciting things over here :-P
				timer.cancel();
				System.exit(-1);
				
			}
			else
				System.out.println("Registration failed.");
		}
	}
	
	//inputs a string indicating the name of quarters and 
	//outputs its corresponding Quarter object.
	public static Quarter parseQuarter(String quarterName) {
		if (quarterName.equals("Spring"))
			return Quarter.Spring;
		else if (quarterName.equals("Summer"))
			return Quarter.Summer;
		else if (quarterName.equals("Autumn"))
			return Quarter.Autumn;
		else if (quarterName.equals("Winter"))
			return Quarter.Winter;
		
		throw new IllegalArgumentException(
				"The string being parsed should be Spring, Summer, Autumn or Winter");
	}
}
