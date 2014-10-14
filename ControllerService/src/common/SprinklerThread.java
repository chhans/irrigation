package common;

import java.util.Timer;
import java.util.TimerTask;

import sprinkler.Sprinkler;

public class SprinklerThread extends Thread {
	private static final String deviceName = "SPRINKLER";
	private Timer autoIrrigationTimer;
	private Timer motionTimer;
	private Timer humidityTimer;
	
	private Sprinkler sprinkler = null;
	private boolean running = true;
	private boolean humidityTimerRunning = false;
	
	public SprinklerThread(Sprinkler sprinkler, String threadName) {
		super(threadName);
		this.sprinkler = sprinkler;
	}
	
	public void run() {
		while (running) {
			//Periodic irrigation every 24h
			if (sprinkler != null && autoIrrigationTimer == null) {
				autoIrrigationTimer = new Timer();
				autoIrrigationTimer.scheduleAtFixedRate(new PeriodicIrrigation(), Log.time.realMinutesToSystemMillis((int) (60*23.5)), Log.time.realMinutesToSystemMillis(60*24));
			}
			
			//Motion sensor irrigation
			if (DeviceStatus.motionStatus == DeviceStatus.MotionStatus.MOTION && (Log.time.getCurrentTime().getTime()/(60*60*1000) % 24) <= 6) {
				motionTimer = new Timer();
				motionTimer.schedule(new MotionIrrigation(), 0);
			}
			
			//Humidity sensor irrigation
			if (DeviceStatus.humidityStatus < 10 && DeviceStatus.humidityStatus >= 0 && !humidityTimerRunning) {
				humidityTimerRunning = true;
				humidityTimer = new Timer();
				humidityTimer.schedule(new HumidityIrrigation(), 0);
			}
			
			try {
				Thread.sleep(200);
				if (!running) break;
			} catch (InterruptedException e) {
	
			}
		} 
	}

	public void stopThread() {
		autoIrrigationTimer.cancel();
		motionTimer.cancel();
		humidityTimer.cancel();
		autoIrrigationTimer = null;
		motionTimer = null;
		humidityTimer = null;
		this.running = false;
		sprinkler = null;
	}
	
	private boolean startSprinkler(StartReason reason) {
		if (DeviceStatus.humidityStatus >= 80 && reason != StartReason.MOTION) {
			Log.log(reason.toString() + ": humidity too high, sprinkler not started", deviceName);
			return false;
		} else if (DeviceStatus.weatherStatus == DeviceStatus.WeatherStatus.HEAVY_RAIN || DeviceStatus.weatherStatus == DeviceStatus.WeatherStatus.LIGHT_RAIN) {
			if (DeviceStatus.humidityStatus > 10) {
				Log.log(reason.toString() + ": it's going to rain, sprinkler not started", deviceName);
				return false;
			} else {
				Log.log("It's going to rain, but humidity is very low. Trying to start sprinkler.", deviceName);
			}
		}
		
		if (DeviceStatus.sprinklerStatus == DeviceStatus.SprinklerStatus.OFF) {
			DeviceStatus.sprinklerStatus = DeviceStatus.SprinklerStatus.ON;
			
			try {
				sprinkler.startSprinkler();
			} catch (Exception e) {
				Log.log("Exception occured during service usage: " + e, deviceName);
			}
			
			Log.log(reason.toString() + ": sprinkler started", deviceName);
			return true;
		} else if (DeviceStatus.sprinklerStatus == DeviceStatus.SprinklerStatus.ON) {
			Log.log(reason.toString() + ": failed to start sprinkler: already on", deviceName);
		} else {
			Log.log(reason.toString() + ": failed to start sprinkler: no sprinkler registered", deviceName);
		}
		return false;
	}
	
	private void stopSprinkler(StartReason reason, int sprinklerTime) {
		if (DeviceStatus.sprinklerStatus == DeviceStatus.SprinklerStatus.ON) {
			DeviceStatus.sprinklerStatus = DeviceStatus.SprinklerStatus.OFF;
			sprinkler.stopSprinkler();
			Log.log(reason.toString() + ": sprinkler stopped", deviceName);
			updateHumidity(sprinklerTime);
		} else if (DeviceStatus.sprinklerStatus == DeviceStatus.SprinklerStatus.OFF) {
			Log.log(reason.toString() + ": failed to stop sprinkler: already off", deviceName);
		} else {
			Log.log(reason.toString() + ": failed to stop sprinkler: no sprinkler registered", deviceName);
		}
	}
	
	private void updateHumidity(int sprinklerTime) {
		int curHum = DeviceStatus.humidityStatus;
		if (curHum > -1) {
			int randInc = sprinklerTime/3 * (new java.util.Random().nextInt(5) + 1);
			if (curHum + randInc <= 100) {
				DeviceStatus.humidityStatus = curHum + randInc;
			} else {
				DeviceStatus.humidityStatus = 100;
			}
			
			if (curHum < 90 && curHum + randInc > 90) {
				Log.log("Humidity very high", HumidityThread.deviceName);
			} else if (curHum < 80 && curHum + randInc > 80) {
				Log.log("Humidity high", HumidityThread.deviceName);
			}
		}
	}
	
	class PeriodicIrrigation extends TimerTask {
		@Override
		public void run() {
			if (!startSprinkler(StartReason.PERIODIC)) {
				this.cancel();
				return;
			}
			try {
				Thread.sleep(Log.time.realMinutesToSystemMillis(30));
				stopSprinkler(StartReason.PERIODIC, 30);
			} catch (Exception e) {
				stopSprinkler(StartReason.UNREGISTERED, 0);
			}
		}
	}
	
	class MotionIrrigation extends TimerTask {
		@Override
		public void run() {
			if (!startSprinkler(StartReason.MOTION)) {
				this.cancel();
				return;
			}
			try {
				Thread.sleep(Log.time.realMinutesToSystemMillis(5));
				stopSprinkler(StartReason.MOTION, 5);
			} catch (Exception e) {
				stopSprinkler(StartReason.UNREGISTERED, 0);
			}
		}
	}
	
	class HumidityIrrigation extends TimerTask {
		@Override
		public void run() {
			if (!startSprinkler(StartReason.HUMIDITY)) {
				this.cancel();
				return;
			}
			try {
				Thread.sleep(Log.time.realMinutesToSystemMillis(30));
				stopSprinkler(StartReason.HUMIDITY, 30);
			} catch (Exception e) {
				stopSprinkler(StartReason.UNREGISTERED, 0);
			}
			humidityTimerRunning = false;
		}
	}
	
	private enum StartReason {
		PERIODIC, MOTION, HUMIDITY, UNREGISTERED;
		
		public String toString() {
			switch (this) {
			case PERIODIC:
				return "Periodic irrigation";
			case MOTION:
				return "Punish the trespassers";
			case HUMIDITY:
				return "Humidity too low";
			case UNREGISTERED:
				return "Sprinkler was unregistered";
			default:
				return "";
			}
		};
	}
}
