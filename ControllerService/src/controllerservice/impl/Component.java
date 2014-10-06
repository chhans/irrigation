package controllerservice.impl;

import common.ServiceUserThread;

import humidity.HumiditySensore;

public class Component {


	HumiditySensore humiditySensore;
	ServiceUserThread thread;

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

	private void log(String message) { 
		System.out.println("dateservice component: " + message); 
	} 

}
