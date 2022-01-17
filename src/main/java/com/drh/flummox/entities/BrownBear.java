package com.drh.flummox.entities;

//having different bear types is probably overkill, but it could be useful later, if we want to have different bear behaviors or something
public class BrownBear extends Bear {

	public BrownBear(int xLocation, int yLocation) {
		super(xLocation, yLocation);
	}

	@Override
	protected String getType() {
		return "brown";
	}
	
}
