package com.drh.flummox.assets;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Tile {

	//TODO: can tiles have different heights?
	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	private BufferedImage bufferedImage;
	private int xLocation;
	private int yLocation;
	
	public Tile(Integer xLocation, Integer yLocation, BufferedImage bufferedImage) {
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.bufferedImage = bufferedImage;
	}
	
	public abstract String getName();
		
	public boolean isWalkable() {
		return false;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}
	
	public int getxLocation() {
		return xLocation;
	}

	public void setxLocation(int xLocation) {
		this.xLocation = xLocation;
	}

	public int getyLocation() {
		return yLocation;
	}

	public void setyLocation(int yLocation) {
		this.yLocation = yLocation;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(xLocation, yLocation, WIDTH, HEIGHT);
	}
}
