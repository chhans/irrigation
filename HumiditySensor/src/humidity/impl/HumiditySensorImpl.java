package humidity.impl; 

import humidity.HumiditySensor;

public class HumiditySensorImpl implements HumiditySensor { 
	HumiditySensorThread thread;
	public HumiditySensorImpl(){
		 this.thread = new HumiditySensorThread();
		 this.thread.start();
	}
	
	public int getHumidity(){
		return this.thread.humidity;
	}
} 