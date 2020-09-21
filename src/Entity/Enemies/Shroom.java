package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.*;
import tileMap.TileMap;

public class Shroom extends Enemy{

	//private BufferedImage[] sprites;
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
			4,1,1	
		}; //평상시 4, 히트1, 죽음1
	private static final int IDLE = 0;
	private static final int HIT = 1;
	private static final int DEAD = 2;
	
	public Shroom(TileMap tm){
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.3;
		maxFallSpeed = 10.0;
		
		width = 31;
		height = 30;
		cwidth = 25;
		cheight = 25;
		
		health = maxHealth = 10;
		damage= 1;
		
		//load sprites 
		try{
			BufferedImage spritesheet = ImageIO.read(
					this.getClass().getResourceAsStream(
							"/Sprites/Enemies/rsz_shroom.png")
				);
			//sprites = new BufferedImage[1];
			
			sprites = new ArrayList<BufferedImage[]>();
			
			for(int i=0; i< 3; i++){
				BufferedImage[] bi =
						new BufferedImage[numFrames[i]];
				for(int j=0; j<numFrames[i]; j++) {
					
					if( i == IDLE){
						bi[j] = spritesheet.getSubimage(
								0, //i*width
								height, //0
								width,
								height
							);
					}
					else if ( i == HIT) {
						bi[j] = spritesheet.getSubimage(
								0,
								2*height,
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
		animation.setDelay(300);
		
		right = true;
		facingRight = false; //(주의) Image 반대방향이라 Snail과 반대임
	}
	
	private void getNextPosition() {

		//달팽이몹은 왼쪽,오른쪽,떨어짐
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
		
		if(falling) {
			dy += fallSpeed;
		}
		
	}
	
	public void update(){
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check flinching
		
		if(flinching) {
			if(currentAction != HIT) {
				currentAction = HIT;
				animation.setFrames(sprites.get(HIT));
				width = 30;
				height = 30;
			}
			long elapsed =
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) { //4seconds
				flinching = false;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(50);
				width = 30;
				height = 30;
			}
		}
		
		// 벽이면 반대로
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = true;
		}
		else if(left && dx == 0 ) {
			right = true;
			left = false;
			facingRight = false;
		}
		
		//animation update;
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		super.draw(g); //mapObject 의 draw
	}
}
