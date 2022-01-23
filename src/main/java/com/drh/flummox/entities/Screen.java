package com.drh.flummox.entities;

import java.awt.Graphics;
import java.awt.Point;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drh.flummox.GameObject;
import com.drh.flummox.assets.AssetLoader;
import com.drh.flummox.assets.Tile;
import com.drh.flummox.utilities.game.GameUtils;

//TODO: this will probably be abstract eventually
public class Screen implements GameObject {
	private static final Logger LOGGER = LoggerFactory.getLogger(Screen.class);
	private List<Tile[][]> tileLayers;
	private Map<String, List<BufferedImage>> tileAssets;
//	private List<Creature> creatures;
	private List<Bear> bears;
	private List<Fox> foxes;
	
	private Random random = new Random();
	
	public Screen() {
		try {
			loadAssets();
		} catch (IOException e) {
			LOGGER.error("Error loading assets", e);
		}
		
		tileLayers = new ArrayList<>();
		
		URI uri;
		try {
			uri = ClassLoader.getSystemResource("maps/overworld/baseLayer.flmx").toURI();
			tileLayers.add(AssetLoader.buildTileSet(uri, tileAssets));
			uri = ClassLoader.getSystemResource("maps/overworld/decorativeLayer.flmx").toURI();
			tileLayers.add(AssetLoader.buildTileSet(uri, tileAssets));
		} catch (URISyntaxException e) {
			LOGGER.error("Error building tiles from map: {}", e.getMessage(), e);
		}
		//TODO: try building this using the creature map or something
	
//		bears.add(new BlackBear(140, 140));
//		bears.add(new BlackBear(580, 350));
//		bears.add(new BrownBear(575, 450));
//		bears.add(new BlackBear(900, 250));
//		bears.add(new PaleBear(975, 520));
		
		spawnCreatures();
		System.out.println("foxes = " + foxes);
		System.out.println("bears = " + bears);
	}
	

	@Override
	public void update() {
		//TODO: bears and foxes (and other creatures) should share the same class, and should be in one loop
		for(Fox fox : foxes) {
			for(Tile[][] tileLayer : tileLayers) {
				if(GameUtils.checkCollision(fox, tileLayer)) {
					fox.reverseDirection();
				}
				fox.update();
			}
		}
		//TODO: set this back to creatures
		for(Bear bear : bears) {
			for(Tile[][] tileLayer : tileLayers) {
				if(GameUtils.checkCollision(bear, tileLayer)) {
					bear.reverseState();
				}
				bear.update();
			}
		}		
		
	}

	@Override
	public void draw(Graphics g) {
		for(Tile[][] tileLayer : tileLayers) {
			for(int i = 1; i < tileLayer.length; i++) {
				Tile[] line = tileLayer[i];			
				for(int j = 0; j < line.length; j++) {
					Tile tile = line[j];
					if(tile != null) {
						List<BufferedImage> possibleTiles = tileAssets.get(tile.getName());
						if(possibleTiles == null) {
							continue;
						}
						g.drawImage(tile.getBufferedImage(), tile.getxLocation(), tile.getyLocation(), Tile.WIDTH, Tile.HEIGHT, null);
						if(!tile.isWalkable()) {
//							g.drawRect(tile.getBounds().x, tile.getBounds().y, tile.getBounds().width, tile.getBounds().height);
						}
					}
				}			
			}
		}

//		for(Creature creature : creatures) {
//			creature.draw(g);
//		}
		for(Fox fox : foxes) {
			fox.draw(g);
		}
		
		for(Bear bear : bears) {
			bear.draw(g);
		}
	}

	//TODO: make this an interface method?
	//TODO: this should all probably go in the AssetLoader or something
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

	//This can surely be improved...
	private void spawnCreatures() {
		bears = new ArrayList<Bear>();
		foxes = new ArrayList<Fox>();	
		
		//save all of the collidable indexes.  An index set will be collidable if any of the layers with those indexes are collidable.
		Map<Point, Boolean> collidableIndexes = new HashMap<Point, Boolean>();
		for(Tile[][] tileLayer : tileLayers) {
			for(int i = 1; i < tileLayer.length; i++) {
				Tile[] line = tileLayer[i];			
				for(int j = 0; j < line.length; j++) {
					Tile tile = line[j];
					if(tile != null && !tile.isWalkable()) {
						collidableIndexes.put(new Point(i,  j), true);
					}
				}			
			}
		}
		
		System.out.println("collidable indexes = " + collidableIndexes);
		//just grab the base tile layer to get coordinates
		Tile[][] tileLayer = tileLayers.get(0);
		for(int i = 1; i < tileLayer.length; i++) {
			Tile[] line = tileLayer[i];	
			for(int j = 0; j < line.length; j++) {
				boolean isCollidable = collidableIndexes.getOrDefault(new Point(i, j), false);
				if(!isCollidable) {
					int xLocation = j * Tile.WIDTH;
					int yLocation = i * Tile.HEIGHT - Tile.HEIGHT;
					//if the tile is not collidable, spawn a creature 10% of the time
					if(random.nextInt(101) <= 10) {
						if(random.nextInt(100) < 20) {
							foxes.add(new Fox(xLocation, yLocation));
						} else {
							bears.add(spawnBear(i, j, xLocation, yLocation));
						}
					}
				}
			}
		}
	}
	
	private Bear spawnBear(int i, int j, int xLocation, int yLocation) {
		int randomNumber = random.nextInt(101);
		if(randomNumber < 15) {
			return new PaleBear(xLocation, yLocation);
		} else if(randomNumber < 60) {
			return new BrownBear(xLocation, yLocation);
		} else {
			return new BlackBear(xLocation, yLocation);
		}
	}
	
}
