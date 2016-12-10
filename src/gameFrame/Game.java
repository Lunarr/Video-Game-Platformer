package gameFrame;

import javax.swing.JFrame;

public class Game 
{
	public static void main (String[] args)
	{
		JFrame window = new JFrame ("Ninja Way");
		window.setContentPane(new GameFrameWork());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Closes Jframe useing its method
		window.setResizable(false);// Person cannot resize it, it will work on all cpus
		window.pack(); 				//causes window to set the size to my preferred dimensions
		window.setVisible(true);  //shows/hides the window depending on if I click on it
		
	
	}
}

