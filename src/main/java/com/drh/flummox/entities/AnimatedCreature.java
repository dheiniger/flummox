package com.drh.flummox.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimatedCreature implements Creature {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AnimatedCreature.class);

	private List<BufferedImage> images;
	private BufferedImage currentImage;
	private int imageNumber;
	private int framesBetweenImages;
	private int framesOnCurrentImage;
	
	private int xLocation;
	private int yLocation;
	
	private AnimatedCreature() {
		images = new ArrayList<BufferedImage>();
		imageNumber = 0;		
		framesBetweenImages = 15;
		framesOnCurrentImage = 0;
	}
	
	public AnimatedCreature(int xLocation, int yLocation) {
		this();
		this.xLocation = xLocation;
		this.yLocation = yLocation;
	}

	public AnimatedCreature(List<BufferedImage> images) {
		this();
		this.images = images;
		currentImage = images.get(0);
	}
	
	public List<BufferedImage> getImages() {
		return images;
	}

	public int getImageNumber() {
		return imageNumber;
	}

	public BufferedImage getCurrentImage() {
		return currentImage;
	}
	
	@Override
	public void update() {
		if(framesOnCurrentImage > framesBetweenImages) {
			if(imageNumber >= images.size()) {
				imageNumber = 0;				
			}			
			currentImage = images.get(imageNumber);
			imageNumber++;
			framesOnCurrentImage = 0;
		}
		framesOnCurrentImage++;
	}
	
	@Override
	public void draw(Graphics g) {
	}

	//TODO: move this to a different interface
	@Override
	public Rectangle getBounds() {
		//TODO: currently this is just being overridden.  Need to design this better...
		return null;
	}
	
	public void reverseDirection() {
		//TODO: currently this is just being overridden.  Need to design this better...
	}

	public int getXLocation() {
		return xLocation;
	}

	public void setXLocation(int xLocation) {
		this.xLocation = xLocation;
	}

	public int getYLocation() {
		return yLocation;
	}

	public void setYLocation(int yLocation) {
		this.yLocation = yLocation;
	}

}
