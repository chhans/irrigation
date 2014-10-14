package common;

import weather.Weather;

public class WeatherThread extends Thread {
	private static final String deviceName = "WEATHER_SERVICE";
	private Weather weatherService = null;
	private boolean running = true;

	public WeatherThread(Weather weatherService, String threadName) {
		super(threadName);
		this.weatherService = weatherService;
	}
	
	public void run() {
		while (running) {
			Log.log(weatherService.getForecast().toString(), deviceName);
			try {
				Thread.sleep(Log.time.realMinutesToSystemMillis(60*24));
				if (!running) break;
			} catch (InterruptedException e) {
			}
		} 
	}

	public void stopThread() {
		this.running = false; 
	}
}