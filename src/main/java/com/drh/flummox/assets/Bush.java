package com.drh.flummox.assets;

import java.awt.image.BufferedImage;

public class Bush extends Tile {

	public Bush(Integer xLocation, Integer yLocation, BufferedImage bufferedImage) {
		super(xLocation, yLocation, bufferedImage);
	}

	@Override
	public String getName() {
		return "bush";
	}

}
