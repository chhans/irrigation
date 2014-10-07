package motion.impl;

import motion.MotionSensor;

public class MotionSensorImpl implements MotionSensor {
	private MotionSensorThread thread;
	
	public MotionSensorImpl() {
		this.thread = new MotionSensorThread();
		this.thread.start();
		//TODO: Stop thread if motion sensor is unregistered
	}
	
	public boolean pollMotionSensor() {
		return false;
	}

}
