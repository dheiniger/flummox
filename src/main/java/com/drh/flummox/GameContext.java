package com.drh.flummox;

import com.drh.flummox.entities.Screen;

public class GameContext {

	public Screen activeScreen;
	
	public GameContext() {
		activeScreen = new Screen();
	}
}
