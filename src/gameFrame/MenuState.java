package gameFrame;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.sound.sampled.Clip;

import audio.AudioPlayer;
import map.Background;

public class MenuState extends GameState
{
	private Background bg;
	private AudioPlayer bgMusic;
	
	private String [] choice = { "Play", "Help", "Exit"};
	
	private int currentChoice = 0;
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
/*////////////////////////////////////////////////
	 
////////////////////////////////////////////////*/
	//every state must refer to gameStateManager
	public MenuState (GameStateManager gsm)		
	{
		this.gsm = gsm;
		
		try
		{
			
			bg = new Background("/Backgrounds/forestbg_1.png",1);
			
			titleColor = new Color(128,0, 0);
			titleFont = new Font ("Garamond", font.ITALIC, 60);
			
			font = new Font("Garamond", font.BOLD, 28);
		}catch(Exception e) {e.printStackTrace();}
		
		init();
	}

/*////////////////////////////////////////////////
	 
////////////////////////////////////////////////*/
	public void init()
	{
		bgMusic = new AudioPlayer("/Music/NinjaWayMenu.mp3");
		bgMusic.play();
		
	}
	public void update()
	{
		//bg.update(g);
	}
/*////////////////////////////////////////////////
 
////////////////////////////////////////////////*/
	public void  draw (Graphics2D g)
	{
		bg.draw(g);
		g.setColor(titleColor);;
		g.setFont(titleFont);
		g.drawString("Ninja Way", 130, 70);
		
		//menu
		g.setFont(font);
		for (int i = 0; i <choice.length; i++)
		{
			if (i == currentChoice)
			{
				g.setColor(Color.RED);
			}
			else 
			{
				g.setColor(Color.BLUE);
			}									
			g.drawString(choice[i], 220,  140 + i * 50);		//choice, x coords of choices, how spaced they are
		}
	}
	
/*////////////////////////////////////////////////
Method that responds when I press enter on a choice
////////////////////////////////////////////////*/
	private void select()
	{
		if (currentChoice == 0)										//if choice is 0
		{
			bgMusic.close();
			gsm.setState(GameStateManager.Level1State);	//lets tell the GSM to draw level 1
			//gsm.setState(GameStateManager.Level1p2State);
		}
		
		if (currentChoice == 1){	}
		
		if (currentChoice ==2) System.exit(0);
		
	}	

/*////////////////////////////////////////////////
	 
////////////////////////////////////////////////*/
	public void keyPressed(int k) 
	{
		if (k == KeyEvent.VK_ENTER) select();
		
		if (k == KeyEvent.VK_UP)
		{
			currentChoice--;
			if(currentChoice == -1) currentChoice = choice.length -1;
		}
		
		if (k == KeyEvent.VK_DOWN)
		{
			currentChoice++;
			if(currentChoice == choice.length) currentChoice = 0;		//checks if it is out of bounds
		}
		
		
	
	}
	
	public void keyReleased(int k) {}

}
