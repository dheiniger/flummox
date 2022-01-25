package com.drh.flummox.assets;

import java.awt.image.BufferedImage;

public class Grass extends Tile {

	public Grass(Integer xLocation, Integer yLocation, BufferedImage bufferedImage) {
		super(xLocation, yLocation, bufferedImage);
	}

	@Override
	public String getName() {
		return "grass";
	}

	@Override
	public boolean isWalkable() {
		return true;
	}

}
