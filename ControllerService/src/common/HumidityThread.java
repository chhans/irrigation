package common;

import humidity.HumiditySensor;

public class HumidityThread extends Thread {
	protected static final String deviceName = "HUMIDITY_SENSOR";
	private HumiditySensor sensor = null; 
	private boolean running = true; 

	public HumidityThread(HumiditySensor sensor, String threadName) { 
		super(threadName);
		this.sensor = sensor; 
	}

	public void run() {
		DeviceStatus.humidityStatus = randomHumidity();
		while (running) {
			int humidity = DeviceStatus.humidityStatus;
			
			if (DeviceStatus.sprinklerStatus != DeviceStatus.SprinklerStatus.ON) {
				int rd = randomDecrease();
				if (humidity - rd > 0) {
					humidity -= rd;
				} else {
					humidity = 0;
				}
			}
			
			if (DeviceStatus.humidityStatus >= 20 && humidity < 20) {
				Log.log("Humidity low", deviceName);
			} else if (DeviceStatus.humidityStatus >= 10 && humidity < 10) {
				Log.log("Humidity very low", deviceName);
			}
			DeviceStatus.humidityStatus = humidity;
			
			//TODO: Increase humidity if raining
			
			try {
				Thread.sleep(200);
				if (!running) break;
			} catch (Exception e) {}
		}
	}

	public void stopThread() {
		this.running = false;
	}
	
	private int randomDecrease() {
		return (new java.util.Random().nextInt(4));
	}
	
	private int randomHumidity() {
		return (new java.util.Random().nextInt(40)+30);
	}
	
}
