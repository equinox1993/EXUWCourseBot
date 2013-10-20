/*
 * Yuwei Huang, 2012
 * This file provides the main method to set up and trigger the robot.
 */

import java.io.Console;
import edwin.uwrobot.RobotThread;


public class RobotMain {
	public static void main(String[] args) throws Exception {
		String username, password;
		int SLN, year;
		String quarter;
		long period;
		if (args.length==6) {
			//Gets all settings from the command line arguments
			username=args[0];
			password=args[1];
			SLN=Integer.parseInt(args[2]);
			year=Integer.parseInt(args[3]);
			quarter=args[4];
			period=(long)(Double.parseDouble(args[5])*1000.0);
		} else if (args.length==0) {
			//Zero argument -- gets settings on run-time
			Console console=System.console();
			System.out.print("Username: ");
			username=console.readLine();
			System.out.print("Password: ");
			password=String.valueOf(console.readPassword());
			System.out.print("SLN: ");
			SLN=Integer.parseInt(console.readLine());
			System.out.print("Year: ");
			year=Integer.parseInt(console.readLine());
			System.out.print("Quarter: ");
			quarter=console.readLine();
			System.out.print("Period(sec): ");
			period=(long)(Double.parseDouble(console.readLine())*1000.0);
		} else {
			//Syntax error (not exactly 6 arguments) -- prints help info
			System.out.println("RobotMain [username] [password] [SLN] [year] [quarter] [period]");
			System.out.println();
			System.out.println("Repeatedly attempts to register for the course with given SLN.");
			System.out.println();
			System.out.println("quarter	-	Spring, Summer, Autumn or Winter.");
			System.out.println("year	-	The academic year you plan to register for.");
			System.out.println("period	-	The time in seconds between each attempts of registration.");
			return;
		}
		
		try {
			RobotThread.thread(username, password, SLN, RobotThread.parseQuarter(quarter), year, period);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
}
