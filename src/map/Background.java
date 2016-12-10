package map;
 
import gameFrame.GameFrameWork;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Background 
{
	private BufferedImage image;				
	public double x,y, dx, dy;				//dx and dy for
	
//Parallax effect. 
	public double moveScale;			//For moving background. if scale is .1 , and we're moving right by 10, then background moves at 10% of normal speed..

/*///////////////////////////////////////////////
Always have a try catch when using bufferedImage 
///////////////////////////////////////////////*/
	public Background (String s, double MS)					//name of background, moveScale
	{
	
		try 
		{
			image = ImageIO.read(getClass().getResourceAsStream(s));			//reads/loads image from a source location.
			moveScale = MS;
		}catch (Exception e) { e.printStackTrace(); }
	
	}	
	
/*///////////////////////////////////////////
 Sets the
//////////////////////////////////////////*/
	public void setPosition (double x, double y)
	{
		this.x = (x * moveScale) % GameFrameWork.Width; 		//I really shouldn't hardCode the width, thats only for my computer screen.
		this.y = (y * moveScale) % GameFrameWork.Height;		//this is for a smooth scrolling, if it goes off the screen, we want it to reset.
	}

/*/////////////////////////////////////////////////////////////
 For the background to automatically scroll, do I really need this?
/////////////////////////////////////////////////////////////*/
	public void setVector (double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
/*//////////////////////////////////////////////
 Only if we have a automatically moving background
/////////////////////////////////////////////*/
	public void update()  
	{
		x += dx;
		y += dy;
	}

/*////////////////////////////////////////////////////
 Draws the background, make sure it keeps painting them 
////////////////////////////////////////////////////*/
	public void draw(Graphics g)
	{
		g.drawImage(image, (int) x, (int) y, null);
		
		//so if x is less then zero, we have to draw more images to the right
		if (x < 0)	g.drawImage(image, (int) x + GameFrameWork.Width, (int) y, null); 						
		
		
		//so if x is less then zero, we have to draw more images to the left
		if (x > 0)	g.drawImage(image, (int) x - GameFrameWork.Width, (int) y, null); 						
	
	}
	
}


