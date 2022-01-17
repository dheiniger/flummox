package com.drh.flummox.assets;

import java.awt.image.BufferedImage;

public abstract class Tile {

	//TODO: can tiles have different heights?
	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	public BufferedImage bufferedImage;
//	private int width;
//	private int height;
	
	public Tile() {
		
	}
	
	public abstract String getName();

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}
	
	/*
	 * public int getWidth() { return width; }
	 * 
	 * public void setWidth(int width) { this.width = width; }
	 * 
	 * public int getHeight() { return height; }
	 * 
	 * public void setHeight(int height) { this.height = height; }
	 */

	/*
	 * @Override public String toString() { return "Tile [width=" + width +
	 * ", height=" + height + "]"; }
	 */
	
	
	
}
