package controllerservice.impl;

import motion.MotionSensor;
import sprinkler.Sprinkler;
import weather.Weather;
import common.DeviceStatus;
import common.Log;
import common.MotionThread;
import common.HumidityThread;
import common.SprinklerThread;
import common.TimeThread;
import common.WeatherThread;
import humidity.HumiditySensor;

/**
 *	Will run the sprinkler periodically every 24h for 1/2 hour.
 *  If the humidity level (1-100) is above 80, no irrigation should be done.
 *  If the humidity level is below 10, irrigation should be initiated immediately.
 *  If the motion sensor is triggered between 00:00 and 06:00, the sprinkler should turn on for 5 minutes.
 *  If the weather service forecasts rain, no irrigation should be done unless humidity is below X??.
 */

public class Component {
	//TODO: Stop time thread when stopping last service
	//TODO: To make it more Knopflerfishy, the threads that actually do stuff should reside in the bundles, not the controller. However, it works now, so maybe just not do it...
	
	private static final String deviceName = "CONTROLLER";
	private TimeThread time = new TimeThread("TIME");
	
	private SprinklerThread sprinklerThread;
	private MotionThread motionThread;
	private HumidityThread humidityThread;
	private WeatherThread weatherThread;
	
	public Component() {
		if (Log.time == null) {
			Log.time = time;
			time.start();
		}
	}
	
	protected void setHumiditySensor(HumiditySensor humiditySensor) {
		Log.log("Humidity sensor registered", deviceName);
		DeviceStatus.humidityStatus = -1;
		if (humidityThread == null) {
			humidityThread = new HumidityThread(humiditySensor, "HUMIDITY");
			humidityThread.start();
		}
	}

	protected void unsetHumiditySensor(HumiditySensor humiditySensore) { 
		Log.log("Humidity sensor unregistered", deviceName);
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
		Log.log("Sprinkler registered", deviceName);
		DeviceStatus.sprinklerStatus = DeviceStatus.SprinklerStatus.OFF;
		if (sprinklerThread == null) {
			sprinklerThread = new SprinklerThread(sprinkler, "SPRINKLER");
			sprinklerThread.start();
		}
	}
	
	protected void unsetSprinkler(Sprinkler sprinkler) {
		Log.log("Sprinkler unregistered", deviceName);
		DeviceStatus.sprinklerStatus = DeviceStatus.SprinklerStatus.UNREGISTERED;
		if (sprinklerThread != null) {
			sprinklerThread.stopThread();
			try {
				sprinklerThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sprinklerThread = null;
		}
	}
	
	protected void setMotionSensor(MotionSensor motionSensor) {
		Log.log("Motion sensor registered", deviceName);
		DeviceStatus.motionStatus = DeviceStatus.MotionStatus.NO_MOTION;
		if (motionThread == null) {
			motionThread = new MotionThread(motionSensor, "MOTION");
			motionThread.start();
		}
	}
	
	protected void unsetMotionSensor(MotionSensor motionSensor) {
		Log.log("Motion sensor unregistered", deviceName);
		DeviceStatus.motionStatus = DeviceStatus.MotionStatus.UNREGISTERED;
		if (motionThread != null) {
			motionThread.stopThread();
			try {
				motionThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			motionThread = null;
		}
	}
	
	protected void setWeatherService(Weather weatherService) {
		Log.log("Weather service registered", deviceName);
		//TODO: Device status
		if (weatherThread == null) {
			weatherThread = new WeatherThread(weatherService, "WEATHER");
			weatherThread.start();
		}
	}
	
	protected void unsetWeatherService(Weather weatherService) {
		Log.log("Weather service unregistered", deviceName);
		//TODO: Device status
		if (weatherThread != null) {
			weatherThread.stopThread();
			try {
				weatherThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			weatherThread = null;
		}
	}
}
