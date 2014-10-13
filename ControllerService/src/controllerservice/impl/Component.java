package controllerservice.impl;

import motion.MotionSensor;
import sprinkler.Sprinkler;
import common.DeviceStatus;
import common.Log;
import common.MotionThread;
import common.HumidityThread;
import common.SprinklerThread;
import common.TimeThread;
import humidity.HumiditySensor;

/**
 *	Will run the sprinkler periodically every 24h for 1/2 hour.
 *  If the humidity level (1-100) is above 80, no irrigation should be done.
 *  If the humidity level is below 10, irrigation should be initiated immediately.
 *  If the motion sensor is triggered between 00:00 and 06:00, the sprinkler should turn on for 5 minutes.
 *  If the weather service forecasts rain, no irrigation should be done (unless humidity is below 10).
 */

public class Component {
	//TODO: Stop time thread when stopping last service
	
	private TimeThread time = new TimeThread("TIME");
	
	private SprinklerThread sprinklerThread;
	private MotionThread motionThread;
	private HumidityThread humidityThread;
	
	public Component() {
		if (Log.time == null) {
			Log.time = time;
			time.start();
		}
	}
	
	protected void setHumiditySensor(HumiditySensor humiditySensor) {
		Log.log("Humidity sensor registered");
		DeviceStatus.humidityStatus = -1;
		if (humidityThread == null) {
			humidityThread = new HumidityThread(humiditySensor, "HUMIDITY");
			humidityThread.start();
		}
	}

	protected void unsetHumiditySensor(HumiditySensor humiditySensore) { 
		Log.log("Humidity sensor unregistered");
		DeviceStatus.humidityStatus = Integer.MIN_VALUE;
		if (humidityThread != null) {
			humidityThread.stopThread();
			try {
				humidityThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			humidityThread = null;
		}
	}
	
	protected void setSprinkler(Sprinkler sprinkler) {
		Log.log("Sprinkler registered");
		DeviceStatus.sprinklerStatus = DeviceStatus.SprinklerStatus.OFF;
		if (sprinklerThread == null) {
			sprinklerThread = new SprinklerThread(sprinkler, "SPRINKLER");
			sprinklerThread.start();
		}
	}
	
	protected void unsetSprinkler(Sprinkler sprinkler) {
		Log.log("Sprinkler unregistered");
		DeviceStatus.sprinklerStatus = DeviceStatus.SprinklerStatus.UNREGISTERED;
		sprinklerThread.stopThread();
	}
	
	protected void setMotionSensor(MotionSensor motionSensor) {
		Log.log("Motion sensor registered");
		DeviceStatus.motionStatus = DeviceStatus.MotionStatus.NO_MOTION;
		if (motionThread == null) {
			motionThread = new MotionThread(motionSensor, "MOTION");
			motionThread.start();
		}
	}
	
	protected void unsetMotionSensor(MotionSensor motionSensor) {
		Log.log("Motion sensor unregistered");
		DeviceStatus.motionStatus = DeviceStatus.MotionStatus.UNREGISTERED;
		motionThread.stopThread();
	}
}
