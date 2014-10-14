package weather;

import java.util.ArrayList;
import java.util.Random;

/**
 * 24h forecast with details every 2h.
 */

public class Forecast {

	//Array has 12 entries, from 02:00-24:00
	private WeatherEnum[] forecastArray = new WeatherEnum[12];
	
	public Forecast() {
		int roll = new Random().nextInt(100);
		//Sunny and no clouds are the same, but in the first and last 3 entries, no clouds should replace sunny
		
		//First entry is random, 1% chance of heavy rain, 4% chance of light rain, 8% chance of cloudy and 87% chance of no clouds
		forecastArray[0] = roll < 87 ? WeatherEnum.NO_CLOUDS : (roll < 95 ? WeatherEnum.CLOUDY : (roll < 99 ? WeatherEnum.LIGHT_RAIN : WeatherEnum.HEAVY_RAIN)); 
		
		//Next entries are based on the previous one. 50% chance of staying in the same state, 30% chance of going to a state of better weather, 20% chance of going to a state of worse weather. Using wraparond to simplify shit
		for (int i = 1; i < 12; ++i) {
			boolean day = i > 2 && i < 9;
			int roll2 = new Random().nextInt(10);
			if (roll2 < 5) {
				if (i == 3 && forecastArray[i-1] == WeatherEnum.NO_CLOUDS) {
					forecastArray[i] = WeatherEnum.SUNNY;
				} else if (i == 9 && forecastArray[i-1] == WeatherEnum.SUNNY) {
					forecastArray[i] = WeatherEnum.NO_CLOUDS;
				} else {
					forecastArray[i] = forecastArray[i-1];					
				}
			} else if (roll2 < 8) {
				ArrayList<WeatherEnum> list = WeatherEnum.order(day);
				int index = list.indexOf(forecastArray[i-1]);
				if (index == -1) {
					forecastArray[i] = day? WeatherEnum.SUNNY : WeatherEnum.NO_CLOUDS;
					continue;
				}
				forecastArray[i] = index == 0 ? forecastArray[i-1] : list.get(index - 1);
			} else {
				ArrayList<WeatherEnum> list = WeatherEnum.order(day);
				int index = list.indexOf(forecastArray[i-1]);
				if (index == -1) index = 0;
				forecastArray[i] = index == (list.size() - 1) ? forecastArray[i-1] : list.get(index + 1);
			}
		}
	}
	
	private enum WeatherEnum {
		SUNNY, NO_CLOUDS, CLOUDY, LIGHT_RAIN, HEAVY_RAIN;
		
		private static ArrayList<WeatherEnum> order(boolean day) {
			ArrayList<WeatherEnum> list = new ArrayList<WeatherEnum>(4);
			list.add(day ? SUNNY : NO_CLOUDS);
			list.add(CLOUDY);
			list.add(LIGHT_RAIN);
			list.add(HEAVY_RAIN);
			return list;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < forecastArray.length; ++i) {
			sb.append(String.format("(%d:00, %s), ", i*2 + 2, forecastArray[i].toString()));
		}
		sb.replace(sb.length() - 2, sb.length() - 1, "]");
		return sb.toString();
	}
}
