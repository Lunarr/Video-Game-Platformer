package enemyControl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import map.TileMap;
import objectControl.Animation;
import objectControl.Enemy;



public class Slugger extends Enemy
{
	private BufferedImage[] sprites;
	
/*////////////////////////////////
 Constructor & 
/////////////////////////////////*/
	public Slugger(TileMap tm)
	{
		super(tm);
	
	//enemy characteristics 
	moveSpeed = 0.3;
	maxSpeed = 0.3;
	fallSpeed = 0.2;
	maxFallSpeed = 10.0;
	
	width = 30;		//for tile sheet
	height = 30;	
	cwidth = 20;		//actual width and height
	cheight = 20;
	
	health = maxHealth = 2;
	damage = 1;

	//load sprites
	try
	{
		BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/slugger.gif"));
	
		sprites = new BufferedImage[3];			//3 because there is 3 frames for slugger.
		
		for (int i = 0; i < sprites.length; i++)
		{
			sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);				//so I'm getting each animation of 1st row...no j loop needed
		}
	
	
	}catch(Exception e){e.printStackTrace();}
	
	animation = new Animation();
	animation.setFrames(sprites);
	animation.setDelay(300);
	
	right = true;
	facingRight = true;	
	
	
	
	}

	private void getNextPosition()
	{
		if (left)
		{
			dx -= moveSpeed;				//vector has to go negative, "moving left"
			if(dx < -maxSpeed)			
			{
				dx = -maxSpeed;
			}
		}
		
		else if(right)				
		{
			dx += moveSpeed;
			if(dx > maxSpeed)
			{
				dx = maxSpeed;			//we're not going any faster then maxSpeed
			}
		}
				
		if(falling)
		{
			dy += fallSpeed;
		}
		
	}

	public void update()
	{
		//update postion
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check to turn off the flinching (the blinking)
		if (flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer / 1000000);		//into milliseconds...
			if (elapsed > 400)										//.4 seconds
			{
				flinching = false;
			}
		}
		
		//if it hits a wall, go other direction
		if(right && dx == 0)					//remember, when I hit something, dx is set to 0
		{
			right = false;
			left = true;
			facingRight = false;
		}
	
		else if (left && dx == 0)
		{
			right = true;
			left = false;
			facingRight = true;
		}
		
		//update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g)
	{
		setMapPosition();			//bufferedImage doesnt draw image
		super.draw(g);				//using the draw function in map objects
	}

}
