package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
//Head-Up-Display ( ���� ���������� ��Ÿ�� )
public class HUD {

	private Wizard player;
	private BufferedImage image;
	private Font font;
	
	public HUD(Wizard p) {
		player = p;
	
		try {
			image = ImageIO.read(
					getClass().getResourceAsStream(
							"/HUD/hud.gif")
				);
			font = new Font("����", Font.BOLD,15);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, 0, 10, null);
		g.setFont(font);
		g.drawString(
				player.getHealth() +"/"+player.getMaxHealth(),
				20,
				25);
		g.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100,
				20,
				45);
		g.setColor(Color.WHITE);
		g.drawString(player.timeToString(), 280, 30);
	}
	
	
	
}
