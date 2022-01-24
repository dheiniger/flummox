package com.drh.flummox.utilities.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drh.flummox.assets.Tile;
import com.drh.flummox.entities.Creature;

public class GameUtils {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(GameUtils.class);

	//TODO: need to make a more generic class.  Can't have this method specifically checking for bears and tiles
	//TODO: make a "collidable" interface?
	
	public static boolean checkCollision(Creature creature, Tile[][] tiles) {
		for(int i = 0; i < tiles.length; i++) {
			Tile[] tileRows = tiles[i];
			if(tileRows != null) {
				for(int j = 0; j < tileRows.length; j++) {
					Tile tile = tileRows[j];				
					if(tile != null && !tile.isWalkable() && tile.getBounds().intersects(creature.getBounds())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
