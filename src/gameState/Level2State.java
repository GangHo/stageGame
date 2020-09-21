package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import Entity.*;
import main.GamePanel;
import tileMap.*;
import main.GamePanel;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import Entity.Enemies.*;
import Audio.AudioPlayer;

public class Level2State  extends GameState {

	private TileMap tileMap;
	private Background bg;
	private MapObject player;
	private Teleport teleport;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private HUD hud;
	private AudioPlayer bgMusic;
	
	public Level2State(GameStateManager gsm, MapObject job){
		this.gsm = gsm;
		this.player = job;
		//this.tileMap = gsm.getTileMap();
		init();
	}
	
	public void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0,0);
		tileMap.setTween(0.07);
		
		bg = new Background("/Backgrounds/background1.png",0.1);
		bgMusic = new AudioPlayer("/BGM/stage1.mp3");
		bgMusic.play();
		
		player = new Wizard(tileMap);
		teleport = new Teleport(tileMap);

	
		player.setPosition(100,100); // 처음 위치
		teleport.setPosition(3150, 193);
		
		populateEnemies();		
		
		explosions = new ArrayList<Explosion>();
		hud = new HUD((Wizard) player);
		
	}
	
//	public void init2() {
//		bg = new Background("/Backgrounds/background.png",0.1);
//	}
		
	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		
		Snail s;
		Point[] points = new Point[] {
				new Point(200,100),
				new Point(300,100),
				new Point(860,100),
				new Point(1325,100),
				new Point(1800,100)
		};
		
		for( int i=0; i< points.length; i++) {
			s = new Snail(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		//Shroom test
		Shroom sr = new Shroom(tileMap);
		sr.setPosition(230, 100);
		enemies.add(sr);
		//darkgolem
		DarkGolem dg = new DarkGolem(tileMap);
		dg.setPosition(3100, 100);
		enemies.add(dg);
		
	}
	
	public void update(){

		((Wizard) player).update();
		if(teleport.intersects(player)) {
			bgMusic.close();
			System.out.println("go to the next");
			//gsm.setState(gsm.getCurrentState() + 1);
			System.exit(0);
		}
		
		teleport.update();
		
		tileMap.setPosition( // 캐릭터 가운데로 tilemap이동
				GamePanel.WIDTH/2 - player.getx(),
				GamePanel.HEIGHT/2 - player.gety()
		);
		
		//set background (캐릭터 움직임에 따라오게 )
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		//attack enemies
		((Wizard) player).checkAttack(enemies);
		
		//update all enemies
		for (int i=0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()){ //죽으면
				enemies.remove(i);
				i--;
				
				explosions.add(
						new Explosion(e.getx(), e.gety()));
			}
		}
		
		//update explosions
		for(int i=0; i< explosions.size(); i++) {
			explosions.get(i).update();
			
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
	}
	
	public void draw(Graphics2D g){
	
	//draw background
		bg.draw(g);
		
	//draw tileMap
		tileMap.draw(g);
		
	//draw player
		player.draw(g);
	
	//draw teleport
		teleport.draw(g);
		
	//draw enemies
		for(int i=0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
	//draw explosion
		for(int i=0; i< explosions.size(); i++) {
			explosions.get(i).setMapPosition(
					(int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
	//draw HUD
		hud.draw(g);
	}
	public void keyPressed(int k){
		if( k == KeyEvent.VK_LEFT)
			player.setLeft(true);
		if( k == KeyEvent.VK_RIGHT)
			player.setRight(true);
		if( k == KeyEvent.VK_UP)
			player.setUp(true);
		if( k == KeyEvent.VK_DOWN)
			player.setDown(true);
		if( k == KeyEvent.VK_W)
			player.setJumping(true);
		if( k == KeyEvent.VK_E)
			((Wizard) player).setGliding(true);
		if( k == KeyEvent.VK_CONTROL)
			((Wizard) player).setScratching();
		if( k == KeyEvent.VK_F){
			((Wizard) player).setFiring();
		}
	}
	 
	public void keyReleased(int k){
		if( k == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if( k == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if( k == KeyEvent.VK_UP)
			player.setUp(false);
		if( k == KeyEvent.VK_DOWN)
			player.setDown(false);
		if( k == KeyEvent.VK_W)
			player.setJumping(false);
		if( k == KeyEvent.VK_E)
			((Wizard) player).setGliding(false);
	
	}
}
