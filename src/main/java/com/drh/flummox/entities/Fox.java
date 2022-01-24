package com.drh.flummox.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drh.flummox.utilities.game.ImageUtils;

public class Fox extends AnimatedCreature {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Fox.class);
	
	private enum State {
		IDLE1,
		IDLE2,
		WALKING,		
		CATCHING,
		SLEEPING
	}
	
	private List<State> movementStates; 
	
	//TODO: alot of this stuff should be able to go in a base class
	private static final int MIN_FRAMES_IN_GIVEN_STATE = 60 * 5;// 5 seconds
	private static final int MAX_FRAMES_IN_GIVEN_STATE = 60 * 10;// 10 seconds		
	protected static final int WIDTH = 35;
	protected static final int HEIGHT = 35;
	
	//key = state_direction
	private Map<String, AnimatedCreature> animations;
	private AnimatedCreature activeAnimation;
	private State currentState;
	private Direction currentDirection;
	private int framesInCurrentState;
	private int framesToRemainInCurrentState;
	private int xLocation;
	private int yLocation;
	private float velocity;

	public Fox(int xLocation, int yLocation) {
		super(xLocation, yLocation);
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		framesInCurrentState = 0;
		velocity = 1;
		buildAnimations();
		framesToRemainInCurrentState = calculateNumberOfFramesToRemainInState();
		
		movementStates = new ArrayList<State>();
		movementStates.add(State.WALKING);
		movementStates.add(State.CATCHING);
		
		updateStateAndDirection();
		
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
		activeAnimation = animations.get(buildAnimationKey());
		activeAnimation.update();
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
		animations = new HashMap<String, AnimatedCreature>();
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
			
			List<BufferedImage> idle1Left = ImageUtils.mirrorImages(idle1Right);
			List<BufferedImage> idle2Left = ImageUtils.mirrorImages(idle2Right);
			List<BufferedImage> walkingLeft = ImageUtils.mirrorImages(walkingRight);
			List<BufferedImage> catchingLeft = ImageUtils.mirrorImages(catchingRight);
			List<BufferedImage> sleepingLeft = ImageUtils.mirrorImages(sleepingRight);
			
			animations.put(buildAnimationKey(State.IDLE1, Direction.LEFT), new AnimatedCreature(idle1Left));
			animations.put(buildAnimationKey(State.IDLE2, Direction.LEFT), new AnimatedCreature(idle2Left));
			animations.put(buildAnimationKey(State.WALKING, Direction.LEFT), new AnimatedCreature(walkingLeft));
			animations.put(buildAnimationKey(State.CATCHING, Direction.LEFT), new AnimatedCreature(catchingLeft));
			animations.put(buildAnimationKey(State.SLEEPING, Direction.LEFT), new AnimatedCreature(sleepingLeft));
			
			animations.put(buildAnimationKey(State.IDLE1, Direction.RIGHT), new AnimatedCreature(idle1Right));
			animations.put(buildAnimationKey(State.IDLE2, Direction.RIGHT), new AnimatedCreature(idle2Right));
			animations.put(buildAnimationKey(State.WALKING, Direction.RIGHT), new AnimatedCreature(walkingRight));
			animations.put(buildAnimationKey(State.CATCHING, Direction.RIGHT), new AnimatedCreature(catchingRight));
			animations.put(buildAnimationKey(State.SLEEPING, Direction.RIGHT), new AnimatedCreature(sleepingRight));
			
		} catch (IOException e) {
			LOGGER.error("Error animating bear. Exception: {}" , e.getMessage(), e);
		}
	}
	
	private List<BufferedImage> getSubImages(BufferedImage image, int numberOfFrames, int spriteHeight, List<Integer> spriteWidths) {
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
	
	private void updateStateAndDirection() {
		currentState = chooseRandomState();
		currentDirection = chooseRandomDirection();
	}
	
	//TODO: this can probably go in a parent class
	private int calculateNumberOfFramesToRemainInState() {
		return new Random().nextInt(MAX_FRAMES_IN_GIVEN_STATE - MIN_FRAMES_IN_GIVEN_STATE) + MIN_FRAMES_IN_GIVEN_STATE;
	}
	
	private State chooseRandomState() {
		return State.values()[new Random().nextInt(State.values().length)];
	}

	public Direction chooseRandomDirection() {
		//for now, use a copy of the directions that excludes "up" and "down".  I know this 
		List<Direction> remainingDirections = Arrays.asList(Direction.values()).stream().filter(d -> d == Direction.LEFT || d == Direction.RIGHT).collect(Collectors.toList());
		return remainingDirections.get(new Random().nextInt(remainingDirections.size()));
	}
	private String buildAnimationKey() {
		return currentState.toString() + "_" + currentDirection.toString();
	}
	
	private String buildAnimationKey(State state, Direction direction) {
		return state.toString() + "_" + direction.toString();
	}
	
	@Override
	public void reverseDirection() {
		switch (currentDirection) {
		case LEFT: {
			currentDirection = Direction.RIGHT;
			break;
		}
		case RIGHT: {
			currentDirection = Direction.LEFT;
			break;
		}
		case UP: {
			currentDirection = Direction.DOWN;
			break;
		}
		case DOWN: {
			currentDirection = Direction.UP;
			break;
		}
		default:
			LOGGER.error("Unknown direction");
		}
	}
	
	//TODO: I'm sure there's a way better way to do this..also, I think they can all be lists instead of maps
	private static List<Integer> getIdle1FrameWidths() {
		List<Integer> widths = new ArrayList<Integer>();
		widths.add(21);
		widths.add(19);
		widths.add(20);
		widths.add(22);
		widths.add(20);
		return widths;
	}
	
	private static List<Integer> getIdle2FrameWidths() {
		List<Integer> widths = new ArrayList<Integer>();
		widths.add(21);
		widths.add(19);
		widths.add(20);
		widths.add(24);
		widths.add(22);
		widths.add(23);
		widths.add(19);
		widths.add(20);
		widths.add(20);
		widths.add(21);
		widths.add(19);
		widths.add(20);
		widths.add(22);
		widths.add(20);
		return widths;
	}
	
	private static List<Integer> getWalkingFrameWidths() {
		List<Integer> widths = new ArrayList<Integer>();
		widths.add(21);
		widths.add(19);
		widths.add(21);
		widths.add(22);
		widths.add(23);
		widths.add(23);
		widths.add(19);
		widths.add(20);
		return widths;
	}
	
	private static List<Integer> getCatchingFrameWidths() {
		List<Integer> widths = new ArrayList<Integer>();
		widths.add(21);
		widths.add(19);
		widths.add(20);
		widths.add(22);
		widths.add(23);
		widths.add(24);
		widths.add(20);
		widths.add(19);
		widths.add(20);
		widths.add(20);
		widths.add(19);
		return widths;
	}
	
	private static List<Integer> getSleepingFrameWidths() {
		List<Integer> widths = new ArrayList<Integer>();
		widths.add(20);
		widths.add(20);
		widths.add(22);
		widths.add(22);
		widths.add(22);
		widths.add(18);
		return widths;
	}

}
