package com.drh.flummox;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int SCALE = 1;
	private static final int WIDTH = 1000 * SCALE;
	private static final int HEIGHT = WIDTH;
	private static final String TITLE = "Untitled";
	private static final double FPS = 60.0;
	
	private boolean running = false;
	private Thread thread;
	
	public static void main(String[] args) {
		createGame();
	}
	
	public Game() {
		//initialize stuff here
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
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " Ticks, FPS, " + frames);
				updates = 0;
				frames = 0;
			}
		}
	}
	
	private static void createGame() {
		Game game = new Game();
		game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		game.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		game.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		JFrame frame = new JFrame(TITLE);
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
	}
	
	private void render() {
		BufferStrategy bufferStrategy = this.getBufferStrategy();
		if(bufferStrategy == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bufferStrategy.getDrawGraphics();
		draw(g);
		g.dispose();
		bufferStrategy.show();
	}
	
	private void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH , HEIGHT);
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
	}

}
