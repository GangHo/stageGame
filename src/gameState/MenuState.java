package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import tileMap.Background;

public class MenuState extends GameState{
	
	private Background bg;
	private BufferedImage logo;
	
	private int currentChoice = 0;
	private String [] options = {
			"start", "help","exit"
	};
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public MenuState(GameStateManager gsm){
		
		this.gsm =gsm;
		
		try{
			bg = new Background("/Backgrounds/menubg2.jpg",1);
			logo = ImageIO.read(
					getClass().getResourceAsStream(
							"/Backgrounds/rsz_1maplestorylogo.png")
					);
			
			//bg.setVector(-0.5, 0); 배경 움직이게 할 경우
			
			titleColor = new Color(128,0,0);
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					16);
			
			font = new Font("Gothic", Font.BOLD,12);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void init(){}
	public void update(){
		bg.update();
	}
	public void draw(Graphics2D g){
		bg.draw(g);
		g.drawImage(logo,45,0,null);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Stage Game", 105, 130);
		
		g.setFont(font);
		
		for(int i=0; i<options.length; i++){
			if( i==currentChoice){
				g.setColor(Color.RED);
			}
			else{
				g.setColor(Color.BLUE);
			}
			g.drawString(options[i], 145, 140+i*15 + 25);
		}
	}
	public void keyPressed(int k){
		if( k == KeyEvent.VK_ENTER){
			select();
		}
		
		if(k == KeyEvent.VK_UP){
			currentChoice--;
			if(currentChoice == -1){
				currentChoice = options.length - 1;
			}
		}
		
		if(k == KeyEvent.VK_DOWN){
			currentChoice++;
			if(currentChoice == options.length){
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k){}
	
	private void select(){
		if(currentChoice == 0){
			System.out.println("게임 시작");
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1){
			gsm.setState(GameStateManager.SELECTJOB);
		}
		if(currentChoice == 2){
			System.exit(0);
		}
	}

}
