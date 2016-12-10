package gameFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import map.Background;

public class WinState extends GameState
{
private Background bg;
	
	private String [] choice = { "Continue", "Exit"};
	
	private int currentChoice = 0;
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
/*////////////////////////////////////////////////
	 
////////////////////////////////////////////////*/
	//every state must refer to gameStateManager
	public WinState (GameStateManager gsm)		
	{
		this.gsm = gsm;
		
		try
		{
			
			bg = new Background("/Backgrounds/Ninjas_BG.png",1);
			
			titleColor = new Color(128,0, 0);
			titleFont = new Font ("Garamond", font.ITALIC, 60);
			
			font = new Font("Garamond", font.BOLD, 28);
		}catch(Exception e) {e.printStackTrace();}
	}

/*////////////////////////////////////////////////
	 
////////////////////////////////////////////////*/
	public void init(){}
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
		g.drawString("YOU WIN :D", 130, 70);
		
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
			gsm.setState(GameStateManager.Level1p2State);	//lets tell the GSM to draw level 1
		}
		
		if (currentChoice == 1){System.exit(0);}

		
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
