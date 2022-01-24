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

public abstract class Bear extends AnimatedCreature {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Bear.class);
	
	protected enum State {
		WALKING_HORIZONTALLY,
		WALKING_VERTICALLY,		
		IDLE
	}	
	
	private static final int MIN_FRAMES_IN_GIVEN_STATE = 60 * 2;// 2 seconds
	private static final int MAX_FRAMES_IN_GIVEN_STATE = 60 * 5;// 5 seconds		
	protected static final int WIDTH = 48;
	protected static final int HEIGHT = 48;
	
	//key = state_direction
	protected Map<String, AnimatedCreature> animations;
	protected AnimatedCreature activeAnimation;
	protected List<State> movementStates; 
	protected State currentState;
	protected Direction currentDirection;
	
	private int framesInCurrentState;
	private int framesToRemainInCurrentState;
	private int velocity;

	protected abstract String getType();
	
	public Bear(int xLocation, int yLocation) {
		super(xLocation, yLocation);
		framesInCurrentState = 0;
		velocity = 1;
		buildAnimations();		
		
		framesToRemainInCurrentState = calculateNumberOfFramesToRemainInState();
		
		movementStates = new ArrayList<State>();
		movementStates.add(State.WALKING_HORIZONTALLY);
		movementStates.add(State.WALKING_VERTICALLY);

		updateStateAndDirection();
		
		activeAnimation = animations.get(buildAnimationKey());
	}
	
	@Override
	public void update() {
		if(framesInCurrentState++ > framesToRemainInCurrentState) {
			updateStateAndDirection();
			framesToRemainInCurrentState = calculateNumberOfFramesToRemainInState();
			framesInCurrentState = 0;
		}			
		
		switch (currentDirection) {
		case LEFT: {
			if(movementStates.contains(currentState)) {
				setXLocation(getXLocation() - velocity);
			}
			break;
		}
		case RIGHT: {
			if(movementStates.contains(currentState)) {
				setXLocation(getXLocation() + velocity);
			}
			break;
		}
		case UP: {
			if(movementStates.contains(currentState)) {
				setYLocation(getYLocation() - velocity);
			}
			break;
		}
		case DOWN: {
			if(movementStates.contains(currentState)) {
				setYLocation(getYLocation() + velocity);
			}
			break;
		}
		default:
			
		}
		System.out.println("key = " + buildAnimationKey());
		activeAnimation.update();
		//TODO: fix this.  Currently assuming if the key isn't found, the state is "IDLE"
		activeAnimation = animations.get(buildAnimationKey());
	}

	@Override
	public void draw(Graphics g) {
		System.out.println("activeAnimation = " + activeAnimation);
		g.drawImage(activeAnimation.getCurrentImage(), getXLocation(), getYLocation(), WIDTH, HEIGHT, null);
	}
	
	//TODO: this can probably be an interface method. Maybe animation can implement "Animatable"
	protected void buildAnimations() {
		animations = new HashMap<String, AnimatedCreature>();
		try {
			BufferedImage allBearAnimations = ImageIO.read(getClass().getResource(String.format("/creatures/bear/%s/bear.png", getType())));
			BufferedImage walkingRight = allBearAnimations.getSubimage(0, 0, 65, 16);//TODO: avoid hardcoding			
			BufferedImage walkingDown = allBearAnimations.getSubimage(0, 16, 65, 16);
			BufferedImage walkingUp = allBearAnimations.getSubimage(0, 32, 65, 16);
			
			List<BufferedImage> walkingRightImages = getSubImages(walkingRight);
			List<BufferedImage> walkingDownImages = getSubImages(walkingDown);
			
			animations.put(buildAnimationKey(State.WALKING_HORIZONTALLY, Direction.RIGHT), new AnimatedCreature(walkingRightImages));
			animations.put(buildAnimationKey(State.WALKING_VERTICALLY, Direction.UP), new AnimatedCreature(getSubImages(walkingUp)));
			animations.put(buildAnimationKey(State.WALKING_VERTICALLY, Direction.DOWN), new AnimatedCreature(walkingDownImages));
			animations.put(buildAnimationKey(State.WALKING_HORIZONTALLY, Direction.LEFT), new AnimatedCreature(ImageUtils.mirrorImages(walkingRightImages)));
			//for "idling", just use the first "walking down" image
			animations.put(buildAnimationKey(State.IDLE, Direction.DOWN), new AnimatedCreature(Arrays.asList(walkingDownImages.get(0))));
			
			
		} catch (IOException e) {
			LOGGER.error("Error animating bear. Exception: {}" , e.getMessage(), e);
		}
	}
	
	//TODO: this should probably be revisited.  The state should most likely determine the animation, and the draw should draw/mirror depending on the direction.  Then we don't have to keep track of as many animation combinations
	private void updateStateAndDirection() {
		currentState = chooseRandomState();
		currentDirection = chooseRandomDirection();
	}
	
	private List<BufferedImage> getSubImages(BufferedImage image) {
		List<BufferedImage> images = new ArrayList<BufferedImage>(); 
		int imageX = 0;
		int imageY = 0;
		//TODO: rework this with config files or something
		List<Integer> spriteWidths = new ArrayList<Integer>();
		spriteWidths.add(16);
		spriteWidths.add(16);
		spriteWidths.add(17);
		spriteWidths.add(16);
		for(int i = 0; i < 4; i++) {			
			int width = spriteWidths.get(i);					
			images.add(image.getSubimage(imageX, imageY, width, 16));
			imageX += width;
		}
		return images;
	}
	
	private int calculateNumberOfFramesToRemainInState() {
		return new Random().nextInt(MAX_FRAMES_IN_GIVEN_STATE - MIN_FRAMES_IN_GIVEN_STATE) + MIN_FRAMES_IN_GIVEN_STATE;
	}
	
	public State chooseRandomState() {
		return State.values()[new Random().nextInt(State.values().length)];
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(getXLocation(), getYLocation(), Fox.WIDTH, Fox.HEIGHT);
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
	
	public Direction chooseRandomDirection() {
		//determine the direction based on the state
		if(currentState == State.WALKING_HORIZONTALLY) {
			List<Direction> horizontalDirections = Arrays.asList(Direction.values()).stream().filter(d -> d == Direction.LEFT || d == Direction.RIGHT).collect(Collectors.toList());
			return horizontalDirections.get(new Random().nextInt(horizontalDirections.size()));
		} else if(currentState == State.WALKING_VERTICALLY) {
			List<Direction> verticalDirections = Arrays.asList(Direction.values()).stream().filter(d -> d == Direction.UP || d == Direction.DOWN).collect(Collectors.toList());
			return verticalDirections.get(new Random().nextInt(verticalDirections.size()));
		} else if(currentState == State.IDLE) {
			return Direction.DOWN;
		}
		return null;
	}
	
	private String buildAnimationKey() {
		return currentState.toString() + "_" + currentDirection.toString();
	}
	
	private String buildAnimationKey(State state, Direction direction) {
		return state.toString() + "_" + direction.toString();
	}
	
	@Override
	public String toString() {
		return "Bear [animations=" + animations + ", activeAnimation=" + activeAnimation + ", movementStates="
				+ movementStates + ", currentState=" + currentState + ", currentDirection=" + currentDirection
				+ ", framesInCurrentState=" + framesInCurrentState + ", framesToRemainInCurrentState="
				+ framesToRemainInCurrentState + ", velocity=" + velocity + "]";
	}
	
	
}
