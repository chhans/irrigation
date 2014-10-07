package controllerservice.impl;

import java.util.ArrayList;
import java.util.List;

import sprinkler.Sprinkler;
import common.ServiceUserThread;
import common.SprinklerThread;
import humidity.HumiditySensore;

public class Component {
	
	private List<Sprinkler> sprinklers = new ArrayList<Sprinkler>();
	HumiditySensore humiditySensore;
	ServiceUserThread thread;
	SprinklerThread sprinklerThread;

	/**
	 * Called by the Declarative Service component finds
	 * a registered DateService as specified in the component.xml
	 */
	protected void setHumiditySensore(HumiditySensore humiditySensore) {
		log("setHumiditySensore");
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
		log("unsetHumiditySensore");
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
		log("setSprinkler");
		this.sprinklers.add(sprinkler);
		if (sprinklerThread == null) {
			sprinklerThread = new SprinklerThread(sprinkler, "SprinklerThread");
			sprinklerThread.start();
		}
	}
	
	protected void unsetSprinkler(Sprinkler sprinkler) {
		log("unsetSprinkler");
		this.sprinklers.remove(sprinkler);
		if (sprinklerThread != null) {
			sprinklerThread.stopThread();
			try {
				sprinklerThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void log(String message) { 
		System.out.println("dateservice component: " + message); 
	}

}
