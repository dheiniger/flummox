package com.drh.flummox.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fox extends Animation implements Creature  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Fox.class);
	
	private enum State {
		IDLE1,
		IDLE2,
		WALKING,		
		CATCHING,
		SLEEPING
	}
	
	private enum Direction {
		LEFT,
		RIGHT/*,
		UP,
		DOWN*/
	}
	
	private List<State> movementStates; 
	
	//TODO: alot of this stuff should be able to go in a base class
	private static final int MIN_FRAMES_IN_GIVEN_STATE = 60 * 5;// 5 seconds
	private static final int MAX_FRAMES_IN_GIVEN_STATE = 60 * 10;// 10 seconds		
	protected static final int WIDTH = 35;
	protected static final int HEIGHT = 35;
	
	//key = state_direction
	private Map<String, Animation> animations;
	private Animation activeAnimation;
	private State currentState;
	private Direction currentDirection;
	private int framesInCurrentState;
	private int framesToRemainInCurrentState;
	private int xLocation;
	private int yLocation;
	private float velocity;

	public Fox(int xLocation, int yLocation) {
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		framesInCurrentState = 0;
		velocity = 1;
		buildAnimations();
		currentState = chooseRandomState();
		currentDirection = chooseRandomDirection();
		framesToRemainInCurrentState = calculateNumberOfFramesToRemainInState();
		
		movementStates = new ArrayList<State>();
		movementStates.add(State.WALKING);
		movementStates.add(State.CATCHING);
		
		activeAnimation = animations.get(buildAnimationKey());		
	}
	
	@Override
	public void update() {
		if(framesInCurrentState++ > framesToRemainInCurrentState) {
			currentState = chooseRandomState();
			framesToRemainInCurrentState = calculateNumberOfFramesToRemainInState();
			framesInCurrentState = 0;
		}			
		
		switch (currentDirection) {
		case LEFT: {
			if(movementStates.contains(currentState)) {
				xLocation -= velocity;
			}
			break;
		}
		case RIGHT: {
			if(movementStates.contains(currentState)) {
				xLocation += velocity;
			}
		}
		default:
			
		}
		LOGGER.info("current state = " + currentState);
		activeAnimation.update();
		activeAnimation = animations.get(buildAnimationKey());
	}
	
	@Override
	public void draw(Graphics g) {
//		for debugging...
//		int index = 0;
//		for (BufferedImage image : animations.get(State.IDLE1.toString() + "_" + "RIGHT").getImages()) {
//			g.drawImage(image, 50, index++ * HEIGHT, WIDTH, HEIGHT, null);
//		}
		 
		g.drawImage(activeAnimation.getCurrentImage(), xLocation, yLocation, WIDTH, HEIGHT, null);
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(xLocation, yLocation, Fox.WIDTH, Fox.HEIGHT);
	}
	
	protected void buildAnimations() {
		animations = new HashMap<String, Animation>();
		try {
			BufferedImage allAnimations = ImageIO.read(getClass().getResource("/creatures/fox/fox.png"));
			BufferedImage idle1 = allAnimations.getSubimage(0, 0, 104, 15);		
			BufferedImage idle2 = allAnimations.getSubimage(0, 15, 290, 15);		
			BufferedImage walking = allAnimations.getSubimage(0, 30, 173, 16);
			BufferedImage catching = allAnimations.getSubimage(0, 46, 227, 18);
			BufferedImage sleeping = allAnimations.getSubimage(0, 80, 126, 16);
			
			List<BufferedImage> idle1Right = getSubImages(idle1, 5, 15, getIdle1FrameWidths());
			List<BufferedImage> idle2Right = getSubImages(idle2, 14, 14, getIdle2FrameWidths());
			List<BufferedImage> walkingRight = getSubImages(walking, 8, 16, getWalkingFrameWidths());
			List<BufferedImage> catchingRight = getSubImages(catching, 11, 18, getCatchingFrameWidths());
			List<BufferedImage> sleepingRight = getSubImages(sleeping, 6, 15, getSleepingFrameWidths());
			
			List<BufferedImage> idle1Left = mirrorImages(idle1Right);
			List<BufferedImage> idle2Left = mirrorImages(idle2Right);
			List<BufferedImage> walkingLeft = mirrorImages(walkingRight);
			List<BufferedImage> catchingLeft = mirrorImages(catchingRight);
			List<BufferedImage> sleepingLeft = mirrorImages(sleepingRight);
			
			animations.put(State.IDLE1.toString() + "_" + Direction.LEFT.toString(), new Animation(idle1Left));
			animations.put(State.IDLE2.toString() + "_" + Direction.LEFT.toString(), new Animation(idle2Left));
			animations.put(State.WALKING.toString() + "_" + Direction.LEFT.toString(), new Animation(walkingLeft));
			animations.put(State.CATCHING.toString() + "_" + Direction.LEFT.toString(), new Animation(catchingLeft));
			animations.put(State.SLEEPING.toString() + "_" + Direction.LEFT.toString(), new Animation(sleepingLeft));
			
			animations.put(State.IDLE1.toString() + "_" + Direction.RIGHT.toString(), new Animation(idle1Right));
			animations.put(State.IDLE2.toString() + "_" + Direction.RIGHT.toString(), new Animation(idle2Right));
			animations.put(State.WALKING.toString() + "_" + Direction.RIGHT.toString(), new Animation(walkingRight));
			animations.put(State.CATCHING.toString() + "_" + Direction.RIGHT.toString(), new Animation(catchingRight));
			animations.put(State.SLEEPING.toString() + "_" + Direction.RIGHT.toString(), new Animation(sleepingRight));
			
		} catch (IOException e) {
			LOGGER.error("Error animating bear. Exception: {}" , e.getMessage(), e);
		}
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
	
	private List<BufferedImage> getSubImages(BufferedImage image, int numberOfFrames, int spriteHeight, Map<Integer, Integer> spriteWidths) {
		List<BufferedImage> images = new ArrayList<BufferedImage>(); 
		int imageX = 0;
		int imageY = 0;
		//TODO: this is hacky, but the spritesheet wasn't consistent.  This is (hopefully) very temporary
		for(int i = 0; i < numberOfFrames; i++) {			
			int width = spriteWidths.get(i);					
			images.add(image.getSubimage(imageX, imageY, width, spriteHeight));
			imageX += width;
		}
		return images;
	}
	
	protected void reverseDirection() {
		switch (currentDirection) {
			case LEFT: {
				currentDirection = Direction.RIGHT;
				break;
			}
			case RIGHT: {
				currentDirection = Direction.LEFT;
				break;
			}
		}
	}
	
	private int calculateNumberOfFramesToRemainInState() {
		return new Random().nextInt(MAX_FRAMES_IN_GIVEN_STATE - MIN_FRAMES_IN_GIVEN_STATE) + MIN_FRAMES_IN_GIVEN_STATE;
	}
	
	private State chooseRandomState() {
		return State.values()[new Random().nextInt(State.values().length)];
	}
	
	private Direction chooseRandomDirection() {
		return Direction.values()[new Random().nextInt(Direction.values().length)];
	}
	
	private String buildAnimationKey() {
		return currentState.toString() + "_" + currentDirection.toString();
	}
	
	//TODO: I'm sure there's a way better way to do this..also, I think they can all be lists instead of maps
	private static Map<Integer, Integer> getIdle1FrameWidths() {
		Map<Integer, Integer> widths = new HashMap<Integer,Integer>();
		widths.put(0, 21);
		widths.put(1, 19);
		widths.put(2, 20);
		widths.put(3, 22);
		widths.put(4, 20);
		return widths;
	}
	
	private static Map<Integer, Integer> getIdle2FrameWidths() {
		Map<Integer, Integer> widths = new HashMap<Integer,Integer>();
		widths.put(0, 21);
		widths.put(1, 19);
		widths.put(2, 20);
		widths.put(3, 24);
		widths.put(4, 22);
		widths.put(5, 23);
		widths.put(6, 19);
		widths.put(7, 20);
		widths.put(8, 20);
		widths.put(9, 21);
		widths.put(10, 19);
		widths.put(11, 20);
		widths.put(12, 22);
		widths.put(13, 20);
		return widths;
	}
	
	private static Map<Integer, Integer> getWalkingFrameWidths() {
		Map<Integer, Integer> widths = new HashMap<Integer,Integer>();
		widths.put(0, 21);
		widths.put(1, 19);
		widths.put(2, 21);
		widths.put(3, 22);
		widths.put(4, 23);
		widths.put(5, 23);
		widths.put(6, 19);
		widths.put(7, 20);
		return widths;
	}
	
	private static Map<Integer, Integer> getCatchingFrameWidths() {
		Map<Integer, Integer> widths = new HashMap<Integer,Integer>();
		widths.put(0, 21);
		widths.put(1, 19);
		widths.put(2, 20);
		widths.put(3, 22);
		widths.put(4, 23);
		widths.put(5, 24);
		widths.put(6, 20);
		widths.put(7, 19);
		widths.put(8, 20);
		widths.put(9, 20);
		widths.put(10, 19);
		return widths;
	}
	
	private static Map<Integer, Integer> getSleepingFrameWidths() {
		Map<Integer, Integer> widths = new HashMap<Integer,Integer>();
		widths.put(0, 20);
		widths.put(1, 20);
		widths.put(2, 22);
		widths.put(3, 22);
		widths.put(4, 22);
		widths.put(5, 18);
		return widths;
	}

}
