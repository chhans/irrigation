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
			
			if (DeviceStatus.weatherStatus == DeviceStatus.WeatherStatus.SUNNY) {
				int rd = randomDecrease();
				if (humidity - rd > 0) {
					humidity -= rd;
				} else {
					humidity = 0;
				}
			} else if (DeviceStatus.weatherStatus == DeviceStatus.WeatherStatus.LIGHT_RAIN) {
				int ri = randomRainIncrease();
				if (humidity + ri > 100) {
					humidity = 100;
				} else {
					humidity += ri;
				}
			} else if (DeviceStatus.weatherStatus == DeviceStatus.WeatherStatus.HEAVY_RAIN) {
				int ri = 2*randomRainIncrease();
				if (humidity + ri > 100) {
					humidity = 100;
				} else {
					humidity += ri;
				}
			}
			
			if (DeviceStatus.humidityStatus >= 20 && humidity < 20) {
				Log.log("Humidity low", deviceName);
			} else if (DeviceStatus.humidityStatus >= 10 && humidity < 10) {
				Log.log("Humidity very low", deviceName);
			}
			
			DeviceStatus.humidityStatus = humidity;
			
			try {
				Thread.sleep(200);
				if (!running) break;
			} catch (Exception e) {}
		}
	}

	public void stopThread() {
		this.running = false;
	}
	
	private int randomRainIncrease() {
		return (new java.util.Random().nextInt(10) + 5);
	}
	
	private int randomDecrease() {
		return (new java.util.Random().nextInt(4));
	}
	
	private int randomHumidity() {
		return (new java.util.Random().nextInt(40)+30);
	}
	
}
