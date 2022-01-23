package com.drh.flummox.entities;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Bear extends Animation implements Creature {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Bear.class);
	
	protected enum State {
		WALKING_DOWN,
		WALKING_UP,
		WALKING_LEFT,
		WALKING_RIGHT,
		IDLING
	}	
	
	private static final int MIN_FRAMES_IN_GIVEN_STATE = 60 * 2;// 2 seconds
	private static final int MAX_FRAMES_IN_GIVEN_STATE = 60 * 5;// 5 seconds		
	protected static final int WIDTH = 48;
	protected static final int HEIGHT = 48;
	
	protected Map<State, Animation> animations;
	protected Animation activeAnimation;
	protected State currentState;
	
	private int framesInCurrentState;
	private int framesToRemainInCurrentState;
	private int xLocation;
	private int yLocation;
	private float velocity;

	public Bear(int xLocation, int yLocation) {
		this.xLocation = xLocation;
		this.yLocation = yLocation;		
		framesInCurrentState = 0;
		velocity = 1;
		buildAnimations();
		//initialize the bear to a random state
		currentState = chooseRandomState();
		framesToRemainInCurrentState = calculateNumberOfFramesToRemainInState();
		activeAnimation = animations.get(currentState);
	}
	
	@Override
	public void update() {
		if(framesInCurrentState++ > framesToRemainInCurrentState) {
			currentState = chooseRandomState();
			framesToRemainInCurrentState = calculateNumberOfFramesToRemainInState();
			framesInCurrentState = 0;
		}			
		
		switch (currentState) {
		case WALKING_DOWN: {
			yLocation += velocity;
			break;
		}
		case WALKING_UP: {
			yLocation -= velocity;
			break;
		}
		case WALKING_LEFT: {
			xLocation -= velocity;
			break;
		}
		case WALKING_RIGHT: {
			xLocation += velocity;
			break;
		}
		case IDLING: {
			break;
		}
		default:
			LOGGER.error("Bear in unknown state: {}", currentState);
		}
		
		activeAnimation.update();
		activeAnimation = animations.get(currentState);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(activeAnimation.getCurrentImage(), xLocation, yLocation, WIDTH, HEIGHT, null);
	}
	
	protected abstract String getType();
	
	//TODO: this can probably be an interface method. Maybe animation can implement "Animatable"
	protected void buildAnimations() {
		animations = new HashMap<State, Animation>();
		try {
			BufferedImage allBearAnimations = ImageIO.read(getClass().getResource(String.format("/creatures/bear/%s/bear.png", getType())));
			BufferedImage walkingRight = allBearAnimations.getSubimage(0, 0, 65, 16);//TODO: avoid hardcoding			
			BufferedImage walkingDown = allBearAnimations.getSubimage(0, 16, 65, 16);
			BufferedImage walkingUp = allBearAnimations.getSubimage(0, 32, 65, 16);
			
			List<BufferedImage> walkingRightImages = getSubImages(walkingRight);
			List<BufferedImage> walkingDownImages = getSubImages(walkingDown);
			
			animations.put(State.WALKING_RIGHT, new Animation(walkingRightImages));
			animations.put(State.WALKING_UP, new Animation(getSubImages(walkingUp)));
			animations.put(State.WALKING_DOWN, new Animation(walkingDownImages));
			//for "walking left", just flip the "walking right" animations
			animations.put(State.WALKING_LEFT, new Animation(mirrorImages(walkingRightImages)));
			//for "idling", just use the first "walking down" image
			animations.put(State.IDLING, new Animation(Arrays.asList(walkingDownImages.get(0))));
			
			
		} catch (IOException e) {
			LOGGER.error("Error animating bear. Exception: {}" , e.getMessage(), e);
		}
	}
	
	private List<BufferedImage> getSubImages(BufferedImage image) {
		List<BufferedImage> images = new ArrayList<BufferedImage>(); 
		int imageX = 0;
		int imageY = 0;
		//TODO: this is hacky, but the spritesheet wasn't consistent.  This is (hopefully) very temporary
		Map<Integer, Integer> spriteWidths = new HashMap<Integer, Integer>();
		spriteWidths.put(0, 16);
		spriteWidths.put(1, 16);
		spriteWidths.put(2, 17);
		spriteWidths.put(3, 16);
		for(int i = 0; i < 4; i++) {			
			int width = spriteWidths.get(i);					
			images.add(image.getSubimage(imageX, imageY, width, 16));
			imageX += width;
		}
		return images;
	}
	
	//TODO: this will probably be useful in a utility class or something
	private List<BufferedImage> mirrorImages(List<BufferedImage> images) {
		List<BufferedImage> mirroredImages = new ArrayList<>();
		for(BufferedImage image : images) {
			AffineTransform affineTransform = AffineTransform.getScaleInstance(-1, 1);
			affineTransform.translate(-image.getWidth(), 0);
			AffineTransformOp op = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			mirroredImages.add(op.filter(image, null));
		}
		return mirroredImages;
	}
	
	private int calculateNumberOfFramesToRemainInState() {
		return new Random().nextInt(MAX_FRAMES_IN_GIVEN_STATE - MIN_FRAMES_IN_GIVEN_STATE) + MIN_FRAMES_IN_GIVEN_STATE;
	}
	
	protected State chooseRandomState() {
		return State.values()[new Random().nextInt(State.values().length)];
	}
	
	//TODO: this should be "reverseDirection" and go in an abstract class or something
	protected void reverseState() {
		switch (currentState) {
		case WALKING_DOWN: {
			currentState = State.WALKING_UP;
			break;
		}
		case WALKING_UP: {
			currentState = State.WALKING_DOWN;
			break;
		}
		case WALKING_LEFT: {
			currentState = State.WALKING_RIGHT;
			break;
		}
		case WALKING_RIGHT: {
			currentState = State.WALKING_LEFT;
			break;
		}
		case IDLING: {
			break;
		}
		default:
			LOGGER.error("Bear in unknown state: {}", currentState);
		}
	}
	
	public int getxLocation() {
		return xLocation;
	}

	public int getyLocation() {
		return yLocation;
	}

	@Override
	public String toString() {
		return "Bear [animations=" + animations + ", activeAnimation=" + activeAnimation + ", currentState="
				+ currentState + ", framesInCurrentState=" + framesInCurrentState + ", framesToRemainInCurrentState="
				+ framesToRemainInCurrentState + ", xLocation=" + xLocation + ", yLocation=" + yLocation + "]";
	}
	
	
}
