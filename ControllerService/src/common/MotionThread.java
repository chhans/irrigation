package common;

import motion.MotionSensor;

public class MotionThread extends Thread {
	private MotionSensor sensor = null;
	private boolean running = true;
	
	public MotionThread(MotionSensor sensor, String threadName) {
		super(threadName);
		this.sensor = sensor;
	}
	
	public void run() {
		while (running) {
			if (DeviceStatus.motionStatus != DeviceStatus.MotionStatus.UNREGISTERED) {
				if (sensor.pollMotionSensor() && DeviceStatus.motionStatus == DeviceStatus.MotionStatus.NO_MOTION) {
					Log.log("Motion detected");
					DeviceStatus.motionStatus = DeviceStatus.MotionStatus.MOTION;
				} else if (!sensor.pollMotionSensor() && DeviceStatus.motionStatus == DeviceStatus.MotionStatus.MOTION) {
					DeviceStatus.motionStatus = DeviceStatus.MotionStatus.NO_MOTION;
				}
			}
			
			try {
				Thread.sleep(200);
				if (!running) break;
			} catch (InterruptedException e) {
			}
		} 
	}

	public void stopThread() {
		this.running = false; 
	}

}
