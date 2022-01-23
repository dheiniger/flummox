package com.drh.flummox.assets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//This will obviously be improved...
public class AssetLoader {
	private static Logger LOGGER = LoggerFactory.getLogger(AssetLoader.class);
	public static Map<Character, Class<?>> tileTypes; 
	static {
		tileTypes = new HashMap<Character, Class<?>>();
		tileTypes.put('0', Grass.class);
		tileTypes.put('1', Rock.class);
		tileTypes.put('2', Water.class);
		tileTypes.put('3', Bush.class);
		tileTypes.put('4', Log.class);	
	}
	
	public static Tile[][] buildTileSet(URI uri, Map<String, List<BufferedImage>> tileAssets) {
		Tile[][] tileSet;
		try {
			tileSet = loadTilesFromFile(uri, tileAssets);

			for (int i = 0; i < tileSet.length; i++) {
				if(tileSet[i] == null) {
					continue;
				}
				
				for (int j = 0; j < tileSet[i].length; j++) {
					Tile tile = tileSet[i][j];
					if (tile == null) {
						continue;
					}
					List<BufferedImage> possibleTiles = tileAssets.get(tile.getName());
					if (possibleTiles != null) {
						Random rand = new Random();
						BufferedImage randomImage = possibleTiles.get(rand.nextInt(possibleTiles.size()));
						tile.setBufferedImage(randomImage);
					}
				}
			}
			return tileSet;
		} catch (IOException e) {
			LOGGER.error("Error building tileSet: {}", e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static Tile[][] loadTilesFromFile(URI uri, Map<String, List<BufferedImage>> tileAssets) throws IOException {
		Path path = Paths.get(uri);
		List<String> lines = Files.readAllLines(path);
		int numberOfLines = lines.size();
		Tile[][] tiles = new Tile[numberOfLines][];
		int tileX = 0;
		int tileY = 0;
		for (int i = 1; i < lines.size(); i++) {
			char[] line = lines.get(i).toCharArray();
			tiles[i] = new Tile[line.length];
			for (int j = 0; j < line.length; j++) {
				char character = line[j];
				Class<?> tileType = (Class<?>) tileTypes.get(character);
				if(tileType != null) {
					//TODO: still need to get the bufferedImage parameter working.  
					try {
//						LOGGER.debug("tileX = {}, tileY = {}", i, j, tileX, tileY);
						Constructor<Tile> constructor = (Constructor<Tile>) tileTypes.get(character).getDeclaredConstructor(Integer.class, Integer.class, BufferedImage.class);
						tiles[i][j] = constructor.newInstance(tileX, tileY, null);
					} catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
						LOGGER.error("An error occured while trying to load Tiles from File", e);
						throw new RuntimeException(e);
					}
				}
				tileX += Tile.WIDTH;
			}
			tileX = 0;
			tileY += Tile.HEIGHT;
		}
		return tiles;
	}
}
