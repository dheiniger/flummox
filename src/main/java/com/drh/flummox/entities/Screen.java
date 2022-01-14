package com.drh.flummox.entities;

import java.awt.Graphics;

import com.drh.flummox.GameObject;
import com.drh.flummox.assets.Tile;

//TODO: this will probably be abstract eventually
public class Screen implements GameObject {

	private Tile[][] tiles;
	
	public Screen() {
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(Graphics g) {
		int tileX = 0;
		int tileY = 0;
		for(int i = 1; i < tiles.length; i++) {
			Tile[] line = tiles[i];			
			for(int j = 0; j < line.length; j++) {
				Tile tile = line[j];
				if(tile != null) {
					g.setColor(tile.getColor());
					g.fillRect(tileX, tileY, Tile.WIDTH, Tile.HEIGHT);
				}
				tileX += Tile.WIDTH;
			}			
			tileX = 0;
			tileY += Tile.HEIGHT;
		}
	}

}
