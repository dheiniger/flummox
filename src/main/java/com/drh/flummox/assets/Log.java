package com.drh.flummox.assets;

import java.awt.image.BufferedImage;

public class Log extends Tile {

	public Log(Integer xLocation, Integer yLocation, BufferedImage bufferedImage) {
		super(xLocation, yLocation, bufferedImage);
	}

	@Override
	public String getName() {
		return "log";
	}

}
