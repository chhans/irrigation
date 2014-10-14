package weather.impl;

import weather.Forecast;
import weather.Weather;

public class WeatherImpl implements Weather {

	public Forecast getForecast() {
		return new Forecast();
	}

}
