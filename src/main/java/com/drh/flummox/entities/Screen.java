package com.drh.flummox.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import com.drh.flummox.Game;
import com.drh.flummox.GameObject;
import com.drh.flummox.assets.AssetLoader;
import com.drh.flummox.assets.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: this will probably be abstract eventually
public class Screen implements GameObject {
	private static final Logger LOGGER = LoggerFactory.getLogger(Screen.class);
	private List<Tile[][]> tileLayer;
	private Map<String, List<BufferedImage>> tileAssets;
	private List<Creature> creatures;
	
	public Screen() {
		try {
			loadAssets();
		} catch (IOException e) {
			LOGGER.error("Error loading assets", e);
		}
		
		tileLayer = new ArrayList<>();
		
		URI uri;
		try {
			uri = ClassLoader.getSystemResource("maps/overworld/baseLayer.flmx").toURI();
			tileLayer.add(buildTileSet(AssetLoader.loadTilesFromFile(uri)));
			uri = ClassLoader.getSystemResource("maps/overworld/decorativeLayer.flmx").toURI();
			tileLayer.add(buildTileSet(AssetLoader.loadTilesFromFile(uri)));
		} catch (URISyntaxException | IOException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		//TODO: try building this using the creature map or something
		creatures = new ArrayList<Creature>();
		creatures.add(new BlackBear(80, 80));
		creatures.add(new BlackBear(144, 64));
		creatures.add(new BrownBear(700, 400));
		creatures.add(new BlackBear(640, 560));
		creatures.add(new PaleBear(96, 420));
	}
	
	private Tile[][] buildTileSet(Tile[][] tileSet) {
		for(int i = 0; i < tileSet.length; i++) {
			if(tileSet[i] == null) {
				continue;
			}
			for(int j = 0; j < tileSet[i].length; j++) {
				Tile tile = tileSet[i][j];
				if(tile == null) {
					continue;
				}
				List<BufferedImage> possibleTiles = tileAssets.get(tile.getName());
				if(possibleTiles == null) {
					continue;
				}
			    Random rand = new Random(); 
			    BufferedImage randomImage = possibleTiles.get(rand.nextInt(possibleTiles.size()));
			    tileSet[i][j].setBufferedImage(randomImage);
			}
		}
		return tileSet;
	}

	@Override
	public void render() {
		for(Creature creature : creatures) {
			creature.render();
		}
	}

	@Override
	public void draw(Graphics g) {
		for(Tile[][] tileLayer : tileLayer) {
			int tileX = 0;
			int tileY = 0;
			for(int i = 1; i < tileLayer.length; i++) {
				Tile[] line = tileLayer[i];			
				for(int j = 0; j < line.length; j++) {
					Tile tile = line[j];
					if(tile != null) {
						List<BufferedImage> possibleTiles = tileAssets.get(tile.getName());
						if(possibleTiles == null) {
							continue;
						}
						g.drawImage(tile.getBufferedImage(), tileX, tileY, Tile.WIDTH, Tile.HEIGHT, null);
					}
					tileX += Tile.WIDTH;
				}			
				tileX = 0;
				tileY += Tile.HEIGHT;
			}
		}

		for(Creature creature : creatures) {
			creature.draw(g);
		}
	}

	//TODO: make this an interface method?
	private void loadAssets() throws IOException {
		tileAssets = new HashMap<String, List<BufferedImage>>();
		List<BufferedImage> grassTiles = new ArrayList<>();
		BufferedImage allGrassTiles = ImageIO.read(getClass().getResource("/tiles/forest/grass.png"));
		int grassTileWidth = 16;
		int grassTileHeight = 16;
		int grassTileXLocation = 0;
		int grassTileYLocation = 0;
		
		List<BufferedImage> rockTiles = new ArrayList<>();
		BufferedImage allRockTiles = ImageIO.read(getClass().getResource("/tiles/forest/rock.png"));
		int rockTileWidth = 16;
		int rockTileHeight = 16;
		int rockTileXLocation = 0;
		int rockTileYLocation = 0;
		
		
		for(int i = 0; i < 4; i++) {
			grassTiles.add(allGrassTiles.getSubimage(grassTileXLocation, grassTileYLocation, grassTileWidth, grassTileHeight));
			grassTileXLocation += grassTileWidth;
			rockTiles.add(allRockTiles.getSubimage(rockTileXLocation, rockTileYLocation, rockTileWidth, rockTileHeight));
			rockTileXLocation += grassTileWidth;
		}
		//TODO: constants or something
		tileAssets.put("grass", grassTiles);
		tileAssets.put("rock", rockTiles);
		tileAssets.put("water", Arrays.asList(ImageIO.read(getClass().getResource("/tiles/forest/water.png"))));
		tileAssets.put("bush", Arrays.asList(ImageIO.read(getClass().getResource("/tiles/forest/bush.png"))));
		tileAssets.put("log", Arrays.asList(ImageIO.read(getClass().getResource("/tiles/forest/log.png"))));

	}
	
}
