package sprinkler.impl;

import sprinkler.Sprinkler;

public class SprinklerImpl implements Sprinkler {
	private boolean on = false;
		
	@Override
	public void startSprinkler() {
		on = true;
	}

	@Override
	public void stopSprinkler() {
		on = false;
	}
	
}