package weather.impl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import weather.Weather;


public class Activator implements BundleActivator {
	public static BundleContext bc = null; 

	public void start(BundleContext bc) throws Exception { 
	    Activator.bc = bc;

	    Weather weather = new WeatherImpl();
	    ServiceRegistration registration = bc.registerService(Weather.class.getName(), weather, new Hashtable());
	}

	public void stop(BundleContext bc) throws Exception { 
	    Activator.bc = null; 
	}
}
