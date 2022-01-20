package com.drh.flummox.entities;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Animation {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(Animation.class);

	private List<BufferedImage> images;
	private BufferedImage currentImage;
	private int imageNumber;
	private int framesBetweenImages;
	private int framesOnCurrentImage;
	
	public Animation() {
		images = new ArrayList<BufferedImage>();
		imageNumber = 0;		
		framesBetweenImages = 15;
		framesOnCurrentImage = 0;
	}
	
	public Animation(List<BufferedImage> images) {
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
	public String toString() {
		return "Animation [images=" + images + ", currentImage=" + currentImage + ", imageNumber=" + imageNumber
				+ ", framesBetweenImages=" + framesBetweenImages + "]";
	}
	
}
