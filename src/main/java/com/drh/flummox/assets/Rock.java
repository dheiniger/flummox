package com.drh.flummox.assets;

import java.awt.image.BufferedImage;

public class Rock extends Tile {
	
	public Rock(Integer xLocation, Integer yLocation, BufferedImage bufferedImage) {
		super(xLocation, yLocation, bufferedImage);
	}

	@Override
	public String getName() {
		return "rock";
	}

}
