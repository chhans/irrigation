package common;

public class Log {
	public static TimeThread time;
	
	public static void log(String message) {
		if (time.getCurrentTime() != null) {
			System.out.println(time.getFormattedTime() + ": " + message);
		} else {
			System.out.println("TIME ERROR: Unable to get current time");
		}
	}
}
