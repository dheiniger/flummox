package com.drh.flummox.entities;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {

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
	}
	
	public void setCurrentImage(BufferedImage currentImage) {
		this.currentImage = currentImage;
	}

	public List<BufferedImage> getImages() {
		return images;
	}

	public void setImages(List<BufferedImage> images) {
		this.images = images;
	}

	public int getImageNumber() {
		return imageNumber;
	}

	public void setImageNumber(int imageNumber) {
		this.imageNumber = imageNumber;
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
