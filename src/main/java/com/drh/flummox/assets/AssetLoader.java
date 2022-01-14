package com.drh.flummox.assets;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AssetLoader {
	//This will obviously be improved...
	public static Tile[][] loadTilesFromFile(URI uri) throws IOException {
		Path path = Paths.get(uri);
		List<String> lines = Files.readAllLines(path);
		int numberOfLines = lines.size();
		Tile[][] tiles = new Tile[numberOfLines][];
		for (int i = 1; i < lines.size(); i++) {
			char[] line = lines.get(i).toCharArray();
			tiles[i] = new Tile[line.length];
			for (int j = 0; j < line.length; j++) {
				char character = line[j];
				if (character == '0') {
					tiles[i][j] = new RockTile();
				} else if(character == '1' ){
					tiles[i][j] = new WaterTile();
				} else {
					tiles[i][j] = null;
				}
			}
		}
		return tiles;
	}
}
