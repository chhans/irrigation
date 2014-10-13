package common;

import humidity.HumiditySensor;

public class HumidityThread extends Thread { 
	private HumiditySensor sensor = null; 
	private boolean running = true; 

	public HumidityThread(HumiditySensor sensor, String threadName) { 
		super(threadName);
		this.sensor = sensor; 
	}

	public void run() {
		DeviceStatus.humidityStatus = randomHumidity();
		while (running) {
			//Cheating:
			int humidity = DeviceStatus.humidityStatus;
			
			if (DeviceStatus.sprinklerStatus != DeviceStatus.SprinklerStatus.ON) {
				int rd = randomDecrease();
				if (humidity - rd > 0) {
					humidity -= rd;
				} else {
					humidity = 0;
				}
			}
			
			System.out.println("Humi: "+humidity);
			DeviceStatus.humidityStatus = humidity;
//			if (DeviceStatus.sprinklerStatus == DeviceStatus.SprinklerStatus.ON) {
//				humidity += sprinklerIncrease();
//			} else {
//				humidity -= randomDecrease();
//			}
//			
//			if (humidity > 100) {
//				humidity = 100;
//			} else if (humidity < -1) {
//				humidity = 0;
//			}
//			System.out.println("Humidity: "+humidity);
			
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
