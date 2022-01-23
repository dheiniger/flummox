package com.drh.flummox.utilities.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drh.flummox.Game;
import com.drh.flummox.assets.Tile;
import com.drh.flummox.entities.Bear;
import com.drh.flummox.entities.Fox;

public class GameUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

	//TODO: need to make a more generic class.  Can't have this method specifically checking for bears and tiles
	//TODO: make a "collidable" interface?
	public static boolean checkCollision(Bear bear, Tile[][] tiles) {

		for(int i = 0; i < tiles.length; i++) {
			Tile[] tileRows = tiles[i];
			if(tileRows != null) {
				for(int j = 0; j < tileRows.length; j++) {
					Tile tile = tileRows[j];				
					if(tile != null && !tile.isWalkable() && tile.getBounds().intersects(bear.getBounds())) {
						LOGGER.info("Bear is colliding!");
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//TODO: should only need one method to check collision for all creatures
	public static boolean checkCollision(Fox fox, Tile[][] tiles) {

		for(int i = 0; i < tiles.length; i++) {
			Tile[] tileRows = tiles[i];
			if(tileRows != null) {
				for(int j = 0; j < tileRows.length; j++) {
					Tile tile = tileRows[j];				
					if(tile != null && !tile.isWalkable() && tile.getBounds().intersects(fox.getBounds())) {
						LOGGER.info("Fox is colliding!");
						return true;
					}
				}
			}
		}
		return false;
	}
}
