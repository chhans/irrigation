package humidity.impl; 

import humidity.HumiditySensore;

public class HumiditySensoreImpl implements HumiditySensore { 
	HumiditySensoreThread thread;
	public HumiditySensoreImpl(){
		 this.thread = new HumiditySensoreThread();
		 this.thread.start();
	}
	
	public int getHumidity(){
		return this.thread.humidity;
	}
} 