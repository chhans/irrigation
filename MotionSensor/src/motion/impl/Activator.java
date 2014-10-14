package motion.impl;

import motion.MotionSensor;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator; 
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
	public static BundleContext bc = null; 

	public void start(BundleContext bc) throws Exception { 
		Activator.bc = bc; 

		MotionSensor sensor = new MotionSensorImpl();
		ServiceRegistration registration = bc.registerService(MotionSensor.class.getName(), sensor, new Hashtable());
	} 
	
	public void stop(BundleContext bc) throws Exception {
		Activator.bc = null;
	}
}
