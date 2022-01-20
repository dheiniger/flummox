package com.drh.flummox.assets;

import java.awt.image.BufferedImage;

public class Water extends Tile {

	public Water(Integer xLocation, Integer yLocation, BufferedImage bufferedImage) {
		super(xLocation, yLocation, bufferedImage);
	}

	@Override
	public String getName() {
		return "water";
	}

}
