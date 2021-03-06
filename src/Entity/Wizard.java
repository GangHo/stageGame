package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import tileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wizard extends MapObject {

	//player stuff
	
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean flinching;
	private boolean dead;
	private long flinchTimer;
	private long time;
	
	//fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	//gliding
	private boolean gliding;
	
	//animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		3, 4, 1, 1, 3, 1, 2	
	}; //대기 3, 걷기 4,점프 1,떨어짐 1, 날때 1, 파이어볼1, 찌르기2
	
	//animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	
	private HashMap<String, AudioPlayer> sfx;
	
	public Wizard(TileMap tm){
		super(tm);
		
		width = 75; //100
		height = 64; //75
		cwidth = 20;
		cheight = 35;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;
		fire = maxFire = 2500;
		
		fireCost = 500;
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();
		
		scratchDamage = 3;
		scratchRange = 70; //
		
		//load Sprites
		try{
			BufferedImage standsheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/rsz_stand1_sprites.png")
					);
			
			BufferedImage walksheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/rsz_walk1_sprites.png")
					);
			
			BufferedImage jumpsheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/rsz_jump.png")
					);
			
			BufferedImage swingsheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/rsz_swingo1_2.png")
					);
			
			BufferedImage stabosheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/rsz_stabo1_sprites.png")
					);
			
			sprites = new ArrayList<BufferedImage[]>();
			
			for(int i=0; i<7; i++){
				BufferedImage[] bi =
						new BufferedImage[numFrames[i]];
				
				for(int j=0; j<numFrames[i]; j++){
					
					if( i == IDLE){
						width = 75;
						height = 65;
						bi[j] = standsheet.getSubimage(
								j * width,
								0,
								width,
								height
								);
					}
					else if( i == WALKING){
						width = 87;
						height = 64; 
						bi[j] = walksheet.getSubimage(
								j * width,
								0,
								width,
								height
								);
					}
					else if(i == JUMPING || i == FALLING) {
						width = 56;
						height = 77;
						bi[j] = jumpsheet.getSubimage(
								j * width,
								0,
								width,
								height
								);
					}
					else if(i == FIREBALL) {
						width = 73;
						height = 90;

						bi[j] = swingsheet.getSubimage(
								j * width,
								0,
								width,
								height
								);
					}
					else if(i == SCRATCHING) {
						width = 105;
						height = 58;
						
						bi[j] = stabosheet.getSubimage(
								j * width,
								0,
								width,
								height
								);
					}
				}
				sprites.add(bi);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/attack.mp3"));
			
	}
	
	public int getHealth(){
		return health;
	}
	public int getMaxHealth(){
		return maxHealth;
	}
	public int getFire(){
		return fire;
	}
	public int getMaxFire(){
		return maxFire;
	}
	public void setFiring(){
		firing = true;
	}
	public void setScratching(){
		scratching = true;
	}
	public boolean getFiring(){
		return firing;
	}
	public boolean getScratching(){
		return scratching;
	}
	
	public void setGliding(boolean b){
		gliding = b;
	}
	
	public String timeToString() {
		int minutes = (int) ( time / 3600);
		int seconds = (int) ((time % 3600) /60);
		return seconds < 10 ? minutes + ":0"
				+ seconds : minutes + ":" + seconds;
	}
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i=0; i<enemies.size(); i++) {
			Enemy e = enemies.get(i);
		
			//scratch attack
			if(scratching) {
				if(facingRight) {
					if(
							e.getx() > x  &&
							e.getx() < x + scratchRange &&
							e.gety() > y - height  / 2 &&
							e.gety() < y + height / 2
					){
						e.hit(scratchDamage);
						}
					}
				else {
					if(
							e.getx() < x &&
							e.getx() > x - scratchRange &&
							e.gety() > y - height / 2 &&
							e.gety() < y + height / 2
					) {
						e.hit(scratchDamage);
					}
				}
			}
			
			//fireball attack
			for(int j=0; j < fireBalls.size(); j++){
				if(fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}
			
			//check enemy collision
			if(intersects(e)) {
				hit(e.getDamage());
			}
		}
	}
	
	public void hit(int damage) {
		if (flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	private void getNextPosition(){
		//movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx =- maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if( dx < 0) {
					dx = 0;
				}
			}
			else if (dx < 0){
				dx += stopSpeed;
				if(dx > 0){
					dx = 0;
				}
			}
		}
		
		// 움직일때는 공격 안되게
		
		if((currentAction == SCRATCHING
				|| currentAction == FIREBALL) &&
				!(jumping || falling)) {
			dx = 0;
		}
		
		//jumping
		if(jumping && !falling) {
			sfx.get("jump").play(); // sound effect 
			dy= jumpStart;
			falling = true;
		}
		
		//falling
		if(falling){
			if( dy>0 && gliding)
				dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if( dy > 0) jumping = false;
			if( dy < 0 && !jumping) dy += stopJumpSpeed;
			if( dy > maxFallSpeed) dy = maxFallSpeed;
		}
		
	}
	
	/* --------- update function ----------- */
	public void update(){
		
//테스트 해제 시 주석 제거		
//		if(dead == true) {
//			width = 75; //100
//			height = 64;
//			health = 5;
//			setPosition(100,100);
//		}
		
		// time
		time ++;
		
		//update position
		getNextPosition();
		
		//out of screen
		if(this.notOnScreen()) {
			System.out.println("out of screen");
			setPosition(100,100);
		}
		
		checkTileMapCollision();
		
		setPosition(xtemp, ytemp);
		
		//check attack has stopped
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
		
		//fireball attack
		fire += 1;
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL) {
			if(fire > fireCost) {
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x , y);
				fireBalls.add(fb);
			}
		}
		
		//update fireballs
		for(int i=0; i< fireBalls.size(); i++){
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		//check done flinching
		if(flinching) {
			long elapsed =
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		//set animation
		if(scratching){
			if(currentAction != SCRATCHING){
				sfx.get("scratch").play(); // sound effect 
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(100);
				width = 90;
				height = 58;
			}
		}
		else if(firing){
			if(currentAction != FIREBALL){
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(200);
				width = 79;
				height = 90;				
			}
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 65;
				}
			}
			else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(200);
				width = 65;
				height = 77;
			}
		}
		else if( dy < 0){
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 65;
				height = 77;
			}
		}
		else if( left || right){
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(300);
				width = 87;
				height = 64;
			}
		}
		else {
			if(currentAction != IDLE){
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 75;
				height = 64;
			}
		}
		
		animation.update();
		
		//set direction
		if(currentAction != SCRATCHING && currentAction != FIREBALL){
			if(right)
				facingRight = true;
			if(left)
				facingRight = false;
		}
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();// 항상먼저
		
		// draw fireBalls
		for(int i=0; i< fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		//draw player
		if(flinching) {
			long elapse = (System.nanoTime() - flinchTimer) / 2000000;
			
			if(elapse / 100 % 2 == 0){
				return;
			}
		}
		if ( currentAction == FIREBALL ) {
			if(facingRight) { //right
				g.drawImage(
						animation.getImage(),
						(int)(x + xmap - width / 2),
						(int)(y + ymap - height/ 2 - 12),
						null
				);
			}
			else { // left
				g.drawImage(
						animation.getImage(),
						(int)(x + xmap - width / 2 + width),
						(int)(y + ymap - height / 2 - 12),
						-width,
						height,
						null
				);
			}
		}
		else super.draw(g); //MapObject의 draw

	}
	
}
