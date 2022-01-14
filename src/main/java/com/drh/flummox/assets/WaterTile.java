package com.drh.flummox.assets;

import java.awt.Color;

public class WaterTile extends Tile {

	public WaterTile() {
		super();
	}
	
	public WaterTile(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Color getColor() {
		return Color.blue;
	}

}
