package controllerservice.impl;

import java.util.ArrayList;
import java.util.List;

import sprinkler.Sprinkler;
import common.Log;
import common.ServiceUserThread;
import common.SprinklerThread;
import common.TimeThread;
import humidity.HumiditySensore;

public class Component {
	//TODO: Stop time thread when stopping last service
	
	private List<Sprinkler> sprinklers = new ArrayList<Sprinkler>();
	HumiditySensore humiditySensore;
	ServiceUserThread thread;
	SprinklerThread sprinklerThread;
	private TimeThread time = new TimeThread("TIME");

	public Component() {
		if (Log.time == null) {
			Log.time = time;
			time.start();
		}
	}
	
	/**
	 * Called by the Declarative Service component finds
	 * a registered DateService as specified in the component.xml
	 */
	protected void setHumiditySensore(HumiditySensore humiditySensore) {
		Log.log("Humidity sensor registered");
		this.humiditySensore= humiditySensore;

		if(thread == null) {
			thread = new ServiceUserThread(humiditySensore, "declarative example"); 
			thread.start(); 
		}
	}

	/**
	 * Called by the Declarative Service component notices an
	 * unregistered DateService as specified in the component.xml
	 */
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
		
		//TODO: Remove when system is working:
		time.stopThread();
	}

}
