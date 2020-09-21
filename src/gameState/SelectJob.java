package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import tileMap.Background;

public class SelectJob extends GameState {

private Background bg;
	
	private int currentChoice = 0;
	private String [] options = {
			"Warrior", "Wizard","Bow"
	};
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	@Override
	public void init() {
		titleColor = new Color(128,0,0);
		titleFont = new Font(
				"Century Gothic",
				Font.PLAIN,
				32);
		
		font = new Font("Arial", Font.PLAIN,12);
	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g) {
		for(int i=0; i<options.length; i++){
			if( i==currentChoice){
				g.setColor(Color.RED);
			}
			else{
				g.setColor(Color.BLUE);
			}
			g.drawString(options[i], 145, 140+i*15);
		}
	}

	@Override
	public void keyPressed(int k) {
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

	private void select(){
		if(currentChoice == 0){
			gsm.setJob(currentChoice);;
		}
		if(currentChoice == 1){
			gsm.setState(currentChoice);
		}
		if(currentChoice == 2){
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(int k) {
		
	}
	

}
