package common;

public class Log {
	public static TimeThread time;
	
	public static void log(String message, String deviceName) {
		if (time.getCurrentTime() != null) {
			System.out.println(time.getFormattedTime() + "\t\t" + deviceName + "\t\t " + message);
		} else {
			System.out.println("TIME ERROR: Unable to get current time");
		}
	}

}
