package controllerservice.impl;

import java.util.ArrayList;
import java.util.List;

import motion.MotionSensor;
import sprinkler.Sprinkler;
import common.Log;
import common.MotionSensorThread;
import common.ServiceUserThread;
import common.SprinklerThread;
import common.TimeThread;
import humidity.HumiditySensore;

public class Component {
	//TODO: Stop time thread when stopping last service
	
	private List<Sprinkler> sprinklers = new ArrayList<Sprinkler>();
	private List<MotionSensor> motionSensors = new ArrayList<MotionSensor>();
	
	private TimeThread time = new TimeThread("TIME");
	private SprinklerThread sprinklerThread;
	private MotionSensorThread motionSensorThread;
	
	HumiditySensore humiditySensore;
	ServiceUserThread thread;
	
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
		this.sprinklers.add(sprinkler);
		if (sprinklerThread == null) {
			sprinklerThread = new SprinklerThread(sprinkler, "SprinklerThread");
			sprinklerThread.start();
		}
	}
	
	protected void unsetSprinkler(Sprinkler sprinkler) {
		Log.log("Sprinkler unregistered");
		this.sprinklers.remove(sprinkler);
		if (sprinklerThread != null) {
			sprinklerThread.interrupt();
			sprinklerThread.stopThread();
			try {
				sprinklerThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//TODO: Remove when system is working: OBS! When you stop the sprinkler, time stops working for now
		time.stopThread();
	}
	
	protected void setMotionSensor(MotionSensor motionSensor) {
		Log.log("Motion sensor registered");
		this.motionSensors.add(motionSensor);
		//TODO: Add motion sensor thread polling the motionSensor once every 200ms
	}
	
	protected void unsetMotionSensor(MotionSensor motionSensor) {
		Log.log("Motion sensor unregistered");
		this.motionSensors.remove(motionSensor);
	}
}
