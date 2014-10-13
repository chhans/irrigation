package common;

public class DeviceStatus {
	public static MotionStatus motionStatus = MotionStatus.UNREGISTERED;
	public static SprinklerStatus sprinklerStatus = SprinklerStatus.UNREGISTERED;
	public static int humidityStatus = Integer.MIN_VALUE; //MIN_VALUE signifies unregistered, -1 signifies no value has been registered yet.
	private static DeviceStatus instance = null;
	private DeviceStatus() {}
	
	public static synchronized DeviceStatus getInstance() {
		if (instance == null) {
			instance = new DeviceStatus();
		}
		return instance;
	}
	
	public enum SprinklerStatus {
		OFF, ON, UNREGISTERED;
	}
	
	public enum MotionStatus {
		MOTION, NO_MOTION, UNREGISTERED;
	}

}