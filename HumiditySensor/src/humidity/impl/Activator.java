package humidity.impl;

import java.util.Hashtable;
import humidity.HumiditySensor;
import org.osgi.framework.BundleActivator; 
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator { 

	  public static BundleContext bc = null; 

	  public void start(BundleContext bc) throws Exception { 
	    Activator.bc = bc; 

	    HumiditySensor sensore = new HumiditySensorImpl(); 
	    ServiceRegistration registration = bc.registerService(HumiditySensor.class.getName(), sensore, new Hashtable()); 
	    
	  } 
	  public void stop(BundleContext bc) throws Exception { 
	    Activator.bc = null; 
	  } 


} 
