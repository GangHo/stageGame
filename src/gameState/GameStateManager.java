package gameState;


import java.util.ArrayList;

import Entity.*;
import tileMap.TileMap;


public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	private int currentJob;
	private ArrayList<MapObject> jobs;
	private TileMap tileMap;
	
	public static final int NUMGAMESTATES = 3;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int LEVEL2STATE = 2;
	public static final int SELECTJOB = 99;
	public static final int WIZARD = 0;
	
	public GameStateManager(){
		
		gameStates =  new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		currentJob = WIZARD;
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/stage2.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.07);
		
		jobs = new ArrayList<MapObject>();
		
		jobs.add(new Wizard(tileMap));
		
		loadState(currentState);
//
//		gameStates.add(new MenuState(this));
//		gameStates.add(new Level1State(this,jobs[currentJob)));
//		gameStates.add(new Level2State(this));
//		gameStates.add(new SelectJob());
	} 
	
	private void loadState(int state) {
		if (state == MENUSTATE){
			gameStates[state] = new MenuState(this);
		}
		if(state == LEVEL1STATE) {
			System.out.println("In "+ currentState +" stage");
			gameStates[state] = new Level1State(this,jobs.get(currentJob));
		}
		if(state == LEVEL2STATE) {
			System.out.println("In "+ currentState +" stage");
			gameStates[state] = new Level2State(this,jobs.get(currentJob));
		}
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public TileMap getTileMap() {
		return tileMap;
	}
	
	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
	}
	
	public void setState(int state){
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public int getCurrentState() {
		return currentState;
	}
	
	public void setJob(int job) {
		currentJob = job;
	}
	
	public void update(){
		if(gameStates[currentState] != null)
			gameStates[currentState].update();
	}
	
	public void draw(java.awt.Graphics2D g){
		if(gameStates[currentState] != null)
			gameStates[currentState].draw(g);
	}
	
	public void keyPressed(int k){
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k){
		gameStates[currentState].keyReleased(k);
	}
}
