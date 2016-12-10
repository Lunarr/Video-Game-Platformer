package objectControl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import map.TileMap;

public class FireBall extends MapObjects		//all gameobjects extends MapObjects
{
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;	//fireball sprite
	private BufferedImage[] hitSprites;	//animation that plays AFTER you hit something
	
/*/////////////////////////////////////////
 Constructor
////////////////////////////////////////*/
	public FireBall (TileMap tm, boolean right)
	{
		super(tm);
		
		facingRight = right;
		
		moveSpeed = 3.8;
		
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 30;			//this is for reading the sprite sheets...
		height = 30;
		cwidth = 14;			//this is the actual width and height of
		cheight = 14;
		
		//load sprites
		try
		{
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/fireball.gif")); //gets the spriteSheet 
			
			sprites = new BufferedImage[4];		//there's 4 frames..
			for(int i = 0; i < sprites.length; i++)
			{
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);	//lets get each image of the animation into sprite[]
			}
					//do the same with the htisprites
			hitSprites = new BufferedImage[3];		//there's 3 frames
			for (int i = 0; i < hitSprites.length; i ++)
			{
				hitSprites[i] = spriteSheet.getSubimage(i*width, height,width, height);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
			
		}catch(Exception e){e.printStackTrace();}
	}

/*/////////////////////////////////////////
Calling this function will play the hit animation 
/////////////////////////////////////////*/
	public void setHit()
	{
		if(hit) return;		
		
		hit = true;
		animation.setFrames(hitSprites);	//set animations to hit sprites
		animation.setDelay(70);
		dx = 0;										//sprite will stop moving,
	}

/*//////////////////////////////////////////
 
//////////////////////////////////////////*/
	
	public boolean shouldRemove()
	{
		return remove;
	}
	
	public void update()
	{
		checkTileMapCollision();			//so we gotta check the collision
		setPosition(xtemp, ytemp);		//set poistion...
		
		if(dx == 0 && !hit)		//so any mapobject collides, dx goes to 0, we'll setHit to true
		{
			setHit();
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce())			//once it hits and the animation is played
		{
			remove = true;										//we'll just remove it from the map
		}
	}

/*//////////////////////////////
 Now lets draw to screen
///////////////////////////////*/
	public void draw(Graphics2D g)
	{
		setMapPosition();			//we must set map position first!
		
		super.draw(g);
	}


}
