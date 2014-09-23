package controllerservice.impl;

import java.text.DateFormat;
import java.util.Date;

import controllerservice.ControllerService;

public class ControllerServiceImpl implements ControllerService {
	public String getFormattedDate(Date date) { 
		return DateFormat.getDateInstance(DateFormat.SHORT)
				.format(date); 
	} 
}
