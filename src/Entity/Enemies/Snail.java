package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.*;
import tileMap.TileMap;

public class Snail extends Enemy{

	//private BufferedImage[] sprites;
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
			1,1,1	
		}; //Æò»ó½Ã 1, È÷Æ®1, Á×À½1
	private static final int IDLE = 0;
	private static final int HIT = 1;
	private static final int DEAD = 2;
	
	public Snail(TileMap tm){
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.3;
		maxFallSpeed = 10.0;
		
		width = 27;
		height = 27;
		cwidth = 25;
		cheight = 25;
		
		health = maxHealth = 10;
		damage= 1;
		
		//load sprites 
		try{
			BufferedImage spritesheet = ImageIO.read(
					this.getClass().getResourceAsStream(
							"/Sprites/Enemies/rsz_redsnail.png")
				);
			//sprites = new BufferedImage[1];
			
			sprites = new ArrayList<BufferedImage[]>();
			
			for(int i=0; i< 3; i++){
				BufferedImage[] bi =
						new BufferedImage[numFrames[i]];
				for(int j=0; j<numFrames[i]; j++) {
					
					if( i == IDLE){
						bi[j] = spritesheet.getSubimage(
								139, //i*width
								2, //0
								width,
								height
							);
					}
					else if ( i == HIT) {
						bi[j] = spritesheet.getSubimage(
								135,
								67,
								32,
								34
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
		facingRight = true;
	}
	
	private void getNextPosition() {

		//´ÞÆØÀÌ¸÷Àº ¿ÞÂÊ,¿À¸¥ÂÊ,¶³¾îÁü
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
				width = 32;
				height = 34;
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
				width = 27;
				height = 27;
			}
		}
		
		// º®ÀÌ¸é ¹Ý´ë·Î
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0 ) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		//animation update;
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		super.draw(g); //mapObject ÀÇ draw
	}
}
