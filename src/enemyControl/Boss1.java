package enemyControl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import map.TileMap;
import objectControl.Animation;
import objectControl.Enemy;
import objectControl.FireBall;
import objectControl.MapObjects;
import objectControl.Player;

public class Boss1 extends MapObjects
{
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	private int counter;
	
	//fireball, switch accordingly for ninja
	private boolean firing;			//boolean for the keyboard
	private int fireCost;
	private int fireBallDamage;
	private ArrayList <FireBall> fireballs;
	protected int damage = 2;   //how much damage boss does if you come in contact with it
	
	//scratch, switch accordingly for ninja
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	//gliding, change accordingly for ninja
	private boolean gliding;
	
	//animations
	private ArrayList<BufferedImage []> sprites;		//I'm keeping SPrites in a bufferedImage array 
	private final int[] numFrames = 
		{2,8,1,2,4,2,5};				//you can change these depending on how many animations you have for ninja
	
	//animation actions, these are the indices of the bufferedImage array list.
	private static final int Idle = 0;			//Idle has 2 frames
	private static final int Walking = 1;		//walking has 8 frames
	private static final int Jumping = 2;
	private static final int Falling = 3;
	private static final int Gliding = 4;
	private static final int Fireball = 5;
	private static int Scratching = 6;
	
/*////////////////////////
 		Constructor
////////////////////////*/
	public Boss1 (TileMap tm)
	{
		super(tm);				//we're calling the mapObject
		width = 30;			//width and height for reading in spriteSheets, thats the size of my sprites
		height = 30;
		cwidth = 20;			//actual width and height, used for collision
		cheight = 20;
		
//movement, don't change these anymore, these are just fine
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
	
		health = maxHealth = 60;
		fire = maxFire = 250;
		
		//change accordingly for ninja
		fireCost = 0;			
		fireBallDamage = 5;
		fireballs = new ArrayList<FireBall>();
		//change accordingly for ninja
		scratchDamage = 8;
		scratchRange = 40;
		
		//load sprites
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));	//k lets fetch these sprites
			
			sprites = new ArrayList < BufferedImage[] > ();		//we're gonna put images here
			for (int i = 0; i < 7; i++)													//7 because we have 7 animations actions (change for ninja ?) 
			{
				BufferedImage [] bi = new BufferedImage[numFrames[i]];	//trace this, the size of this array is i ..
	
				for (int j = 0; j < numFrames[i]; j++)		//so lets read in each frame for each sprite
				{
					
					if (i != 6) 
						{
						bi[j] = spritesheet.getSubimage(j* width, i * height, width, height);		//we're getting the subImages of the sprite sheet, for each sprite
						}
					
					else 
					{
						bi[j] = spritesheet.getSubimage(j* width * 2, i * height, width *2, height);
					}
				
				}
				
				sprites.add(bi);				//add each sprite into the bufferimage array
			
			}
		
			}catch(Exception e){e.printStackTrace();}
			
			animation = new Animation();
			currentAction = Idle;
			animation.setFrames(sprites.get(Idle));		//hot fix to a problem I was having
			animation.setDelay(100);
	}
	
	
	public int getHealth() 			{return health;}
	public int getMaxHealth() 	{return maxHealth;}
	public int getFire()				{return fire;}
	public int getMaxFire()		{return maxFire;}
	public int getDamage() 		{return damage;}
	
	//for keyboard inputs
	public void setFiring()			{	firing = true;	}
	
	public void setScratching()	{scratching = true;}

	public void setGliding (boolean b)		{gliding = b;}


	
	public void checkAttack(Player player)
	{
		
		
			//scratch attack
			if (scratching)
			{
				if(facingRight)
				{
					if(player.getx() > x && player.getx() < x + scratchRange && player.gety() > y - height / 2 && player.gety() < y + height / 2)
					{
						player.hit(scratchDamage);
					}
						
				}
			
				else
					if( player.getx() < x && player.getx() > x - scratchRange && player.gety() > y - height / 2 && player.gety() < y + height /2)
					{
						player.hit (scratchDamage);
					}
			}
			
			//fireballs
			for(int j = 0; j < fireballs.size(); j++)
			{
				if(fireballs.get(j).intersects(player))
						{
						player.hit(fireBallDamage);
						fireballs.get(j).setHit();
						break;
						}
			}
	}
	
	
/*///////////////////////////////////
 
///////////////////////////////////*/
		public void hit (int damage)
		{
			if(flinching) return;
			health -= damage;
			if (health < 0) health = 0;
			if (health == 0) dead = true;
			flinching = true;
			flinchTimer = System.nanoTime();
		}
	
	
/*////////////////////////////////////////////////
 determines where the player should be after inputs
///////////////////////////////////////////////*/
	private void getNextPosition()
	{
		//movement
		if(left)
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
	
		
		else
		{
			if (dx > 0)		// Ok so once we let go off the key..
			{
				dx -= stopSpeed;	//so lets subtract by stopspeed
				if(dx < 0)				//this means we stopped
				{
					dx = 0;
				}
			}
			
			else if (dx < 0)		//so this is the opposite direction...
			{
				dx += stopSpeed;
				if(dx > 0)
				{
					dx = 0;
				}
			}
		
		}
	
		//cannot move while attacking
		if (currentAction == Scratching || currentAction == Fireball && !(jumping || falling))
		{
			dx = 0;				//we won't move
		}
	
		//jumping
		if (jumping && !falling)
		{
			dy = jumpStart;		//if we're jumping, lets make sure the physics are right
			falling = true;			//jumping is true..
		}
	
		//falling
		if(falling)
		{
			if (dy > 0 && gliding)
			{
				dy += fallSpeed * 0.1;			//we fall slower when gliding
			}
			else dy += fallSpeed;
			
			if (dy > 0) jumping = false;
			if (dy < 0 && !jumping) dy += stopJumpSpeed;		//the longer you press jump, the higher you jump
			if (dy > maxFallSpeed) dy = maxFallSpeed;		//we're going to cap the fallspeed to maxfallspeed
		}
	
	}
	
/*//////////////////////////////
 Complicated stuff
/////////////////////////////*/
	public void update()
	{
		//update position
		getNextPosition();
		checkTileMapCollision();		//checking collision
		setPosition(xtemp, ytemp);	//has to do with collision,
		
		//check if attack has stopped
		if (currentAction == Scratching)	
		{
			if (animation.hasPlayedOnce()) scratching = false;		//once it has played once, we're going to stop the animation
			
		}
		
		if (currentAction == Fireball)
		{
			if(animation.hasPlayedOnce()) firing = false;
		}
		
		//fireball attack
		fire += 1;												//to continiously regenerate energy
		if(fire > maxFire) fire = maxFire;			//this will cap the regeneration
		
		if(firing && currentAction != Fireball)	
		{
			if(fire > fireCost)		//if we have enough energy to use the fire
			{
				fire -= fireCost;													//lower the power you have
				FireBall fb = new FireBall(tileMap, facingRight);	//calls the fireball method..
				fb.setPosition(x, y);												//set the fireball in the same position as the player
				fireballs.add(fb);
			}
		}
		
		//updaing fireball
		for (int i = 0; i < fireballs.size(); i++)
		{
				fireballs.get(i).update();				//updating each fireballs
				
				if(fireballs.get(i).shouldRemove())		//remove each frame...
				{
					fireballs.remove(i);
					i--;
				}
				
				
		}
		
		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			
			if(elapsed > 1000)
			{
				flinching = false;
			}
			
		}
		
		//setting animations...
		if (scratching)
		{
			if (currentAction != Scratching)
			{
				currentAction = Scratching;							
				animation.setFrames(sprites.get(Scratching));			//setting the frames to the scratching animation
				animation.setDelay(50);
				width = 60;													
			}
		}
		
		else if (firing)
		{
			if(currentAction != Fireball)
			{
				currentAction = Fireball;
				animation.setFrames(sprites.get(Fireball));
				animation.setDelay(100);
				width = 30;
			}
		}
		
		else if (dy > 0)			//this means we are falling
		{
			if (gliding)				
			{
				if(currentAction != Gliding)
				{
					currentAction = Gliding;					//lets set our action to gliding
					animation.setFrames(sprites.get(Gliding));
					animation.setDelay(100);
					width = 30;
				}
			}
			
			else if (currentAction != Falling)		//if we're not falling, set the falling animation.
			{
				currentAction = Falling;
				animation.setFrames(sprites.get(Falling));
				animation.setDelay(100);
				width = 30;
			}
		}
	
		else if (dy < 0)
		{
			if(currentAction != Jumping)
			{
				currentAction = Jumping;
				animation.setFrames(sprites.get(Jumping));
				animation.setDelay(-1);				//there's only 1 sprite for jumping, so we don't really need delay
				width = 30;
			}
		}
	
		else if(left || right)		//if we're moving left or right
		{
			if (currentAction != Walking)
			{
				currentAction = Walking;							//set action to walking
				animation.setFrames(sprites.get(Walking));	//set walking animation
				animation.setDelay(40);
				width = 30;
			}
		}
		
		else		//if we're not doing anything else, we're idle
		{
			if (currentAction != Idle)
			{
				currentAction = Idle;
				animation.setFrames(sprites.get(Idle));
				animation.setDelay(400);
				width = 30;			
			}
		}
		
		animation.update();			//update the animation.
		
		//setting direction, we don't want player moving when he's attacking
		if (currentAction != Scratching && currentAction != Fireball)
		{
			if(right) facingRight = true;
			if (left) facingRight = false;				//so he won't turn around when hes attacking
		}
	}
	
	
/////////////////////////////////////////
//most disorganized AI you have ever seen
/////////////////////////////////////////
	public void SetAI()
	{
		counter++;
		
		int pointL = 2970;
		int pointR = 3135;
		int pointTL = 3130;
		int pointTR = 2975;
		
		if (counter == 60)
		{	
			setLeft(true);
		}
		
		if (getx() <= pointL)
		{
			setLeft(false);
			setFiring();
				
		}
		
		if (counter == 240)
		{
			setRight(true);
			firing = false;
		}
		
		if (counter > 240 && getx() >= pointR)
		{
			setRight(false);
			setLeft(true);
		}
		
		if (counter > 240 && counter < 499 && getx() == pointTL)
		{
			
			setLeft(false);
			facingRight = false;
			setScratching();
			counter = 500;
		}
		
		if (counter > 505)
		{
			jumping = true;
			
			firing = false;
		}
		//read fintate statemachines 
		if (counter > 800 && counter < 1200)
		{
			setGliding(true);
			setRight(true);
		}
		
		if(counter > 505 && getx() >= pointR)
		{
			setRight(false);
			setLeft(true);
		}
	
		
		if (counter > 505 && getx() == pointTL)
		{
			
			setLeft(false);
			setScratching();
			counter = 1000;
		}
	
		if (counter == 1150)
		{
			setGliding(false);
			setFiring();
			
		}
		if (counter > 1201)
		{
			setLeft (false);
			setRight (false);
			facingRight = false;
			
			setGliding(false);
			jumping = true;
			setFiring();
			
		}
		
		if (counter == 1260)
		{
			firing =false;
			jumping = false;
			setRight(true);
			counter = 0;
		}
		
	}
	
/*///////////////////////////////////
 now lets draw the player
///////////////////////////////////*/
	public void draw(Graphics2D g)
	{
		setMapPosition();		//we must set map position
		
		//draws fireball
		for(int i = 0; i < fireballs.size(); i++)	//drawing each frame
		{
			fireballs.get(i).draw(g);
		}
		
		//draw player
		if (flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			
			if (elapsed / 100 % 2 == 0)			//when we get hurt, we blink every 100 milliseconds
			{
				return;
			}
		}
		super.draw(g);
		
	}
}
