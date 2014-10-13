package common;

public class DeviceStatus {
	public static MotionStatus motionStatus = MotionStatus.UNREGISTERED;
	public static SprinklerStatus sprinklerStatus = SprinklerStatus.UNREGISTERED;
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