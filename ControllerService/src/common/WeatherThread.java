package common;

import weather.Forecast;
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
		Forecast forecast = null;
		while (running) {
			if (Log.time.getCurrentHour() == 0) {
				forecast = weatherService.getForecast();
				Log.log(forecast.toString(), deviceName);
			} 
			
			if (forecast != null) {
				int i = Log.time.getCurrentHour()/2;
				DeviceStatus.weatherStatus = DeviceStatus.WeatherStatus.valueOf(forecast.getForecastArray()[i].toString());
			}
			
			try {
				Thread.sleep(200);
				if (!running) break;
			} catch (InterruptedException e) {
			}
		} 
	}

	public void stopThread() {
		this.running = false; 
	}
}