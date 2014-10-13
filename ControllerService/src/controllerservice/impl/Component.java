package controllerservice.impl;

import motion.MotionSensor;
import sprinkler.Sprinkler;
import common.DeviceStatus;
import common.Log;
import common.MotionThread;
import common.ServiceUserThread;
import common.SprinklerThread;
import common.TimeThread;
import humidity.HumiditySensore;

/**
 *	Will run the sprinkler periodically every 24h for 1/2 hour.
 *  If the humidity level (1-100) is above 80, no irrigation should be done.
 *  If the humidity level is below 10, irrigation should be initiated immediately.
 *  If the motion sensor is triggered between 00:00 and 06:00, the sprinkler should turn on for 5 minutes.
 *  If the weather service forecasts rain, no irrigation should be done (unless humidity is below 10).
 */

public class Component {
	//TODO: Stop time thread when stopping last service
	//TODO: Use single ControllerThread to control all threads?
	
	private TimeThread time = new TimeThread("TIME");
	
	private SprinklerThread sprinklerThread;
	private MotionThread motionThread;
	
	HumiditySensore humiditySensore;
	ServiceUserThread thread;
	
	//TODO: Stop timethread when stopping last service
	public Component() {
		if (Log.time == null) {
			Log.time = time;
			time.start();
		}
	}
	
	protected void setHumiditySensore(HumiditySensore humiditySensore) {
		Log.log("Humidity sensor registered");
		this.humiditySensore= humiditySensore;

		if(thread == null) {
			thread = new ServiceUserThread(humiditySensore, "declarative example"); 
			thread.start(); 
		}
	}

	protected void unsetHumiditySensore(HumiditySensore humiditySensore) { 
		Log.log("Humidity sensor unregistered");
		this.humiditySensore = null;
		if(thread != null) {
			thread.stopThread(); 
			try { 
				thread.join(); 
			} catch (InterruptedException e) { 
				e.printStackTrace(); 
			}
			thread = null;
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
