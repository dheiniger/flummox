package com.drh.flummox.assets;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This will obviously be improved...
public class AssetLoader {
	public static Map<Character, Class<?>> tileTypes; 
	static {
		tileTypes = new HashMap<Character, Class<?>>();
		tileTypes.put('0', GrassTile.class);
		tileTypes.put('1', RockTile.class);
		tileTypes.put('2', WaterTile.class);
		tileTypes.put('3', Bush.class);
		tileTypes.put('4', Log.class);	
	}
	
	@SuppressWarnings("deprecation")
	public static Tile[][] loadTilesFromFile(URI uri) throws IOException, InstantiationException, IllegalAccessException {
		
		Path path = Paths.get(uri);
		List<String> lines = Files.readAllLines(path);
		int numberOfLines = lines.size();
		Tile[][] tiles = new Tile[numberOfLines][];
		for (int i = 1; i < lines.size(); i++) {
			char[] line = lines.get(i).toCharArray();
			tiles[i] = new Tile[line.length];
			for (int j = 0; j < line.length; j++) {
				char character = line[j];
				Class<?> tileType = (Class<?>) tileTypes.get(character);
				if(tileType == null) {
					continue;
				}
				tiles[i][j] = (Tile) tileTypes.get(character).newInstance();//TODO: find out why this is deprecated
			}
		}
		return tiles;
	}
	
}
