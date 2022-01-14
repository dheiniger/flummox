package com.drh.flummox.assets;

import java.awt.Color;

public abstract class Tile {

	//TODO: can tiles have different heights?
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
//	private int width;
//	private int height;
	
	public Tile() {
//		this.width = 50;
//		this.height = 50;
	}
	
	public Tile(int width, int height) {
//		this.width = width;
//		this.height = height;
	}

	public abstract Color getColor();
	
	
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
