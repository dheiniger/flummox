package com.drh.flummox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
	private static final long serialVersionUID = 1L;
	private static final float SCALE = .75f;
	private static final int SCREEN_WIDTH = (int) (1920 * SCALE);
	private static final int SCREEN_HEIGHT = (int) (1024 * SCALE);
	private static final String TITLE = "Untitled";
	private static final double FPS = 60.0;

//	private static int screenWidth;
//	private static int screenHeight;
	
	private boolean running = false;
	private Thread thread;
	
	private GameContext gameContext;
	
	public static void main(String[] args) throws IOException {
		try{
			createGame();
		}catch (RuntimeException e){
			LOGGER.error("An error occurred", e);
		}
	}
	
	public Game() {
		//initialize stuff here
		gameContext = new GameContext();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double ns = 1000000000 / FPS;//nanoseconds per frame
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while(running) {
			//game loop
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta > 0) {
				tick();
				updates++;
				delta--;//I don't think this accounts for a "catchup" feature
			}
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				LOGGER.info(updates + " Ticks, FPS, " + frames);
				updates = 0;
				frames = 0;
			}
		}
	}
	
	private static void createGame() throws IOException {
		Game game = new Game();
		Dimension screenSize = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
		game.setPreferredSize(screenSize);
		game.setMaximumSize(screenSize);
		game.setMinimumSize(screenSize);
		JFrame frame = new JFrame(TITLE);		
//		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
//		screenWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow().getWidth();
//		screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow().getHeight();
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}
	
	private synchronized void start() {
		if(running) {
			return;
		}
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	private void tick() {
		render();
	}
	
	private void render() {
		gameContext.activeScreen.render();
		
		BufferStrategy bufferStrategy = this.getBufferStrategy();
		if(bufferStrategy == null) {
			createBufferStrategy(3);
			return;
		}
				
		Graphics g = bufferStrategy.getDrawGraphics();
		//TODO: for each renderable object in the gamecontext, render 
		draw(g);
		g.dispose();
		bufferStrategy.show();
	}
	
	private void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		//TODO: for each drawable object in the gamecontext, draw
		gameContext.activeScreen.draw(g);
//		g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
//		g.setColor(Color.white);
//		g.drawString("hello", 50, 50);
	}

}
