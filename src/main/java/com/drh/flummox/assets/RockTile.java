package com.drh.flummox.assets;

import java.awt.Color;

public class RockTile extends Tile {
	
	public RockTile() {
		super();
	}

	public RockTile(int width, int height) {
		super(width, height);
	}

	@Override
	public Color getColor() {
		return Color.gray;
	}

}
