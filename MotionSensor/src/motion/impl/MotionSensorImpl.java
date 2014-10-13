package motion.impl;

import java.util.Random;
import motion.MotionSensor;

public class MotionSensorImpl implements MotionSensor {
	//private MotionSensorThread thread;
	
	public MotionSensorImpl() {
		//this.thread = new MotionSensorThread();
		//this.thread.start();
	}
	
	public boolean pollMotionSensor() {
		//TODO: Cheat to simulate easily
		return (new Random().nextInt(20) == 1);
		//return thread.motion;
	}
	
}