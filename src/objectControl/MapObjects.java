package objectControl;

import gameFrame.GameFrameWork;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import map.Tile;
import map.TileMap;

public abstract class MapObjects		//players enemies will all be objects, and extend this class
{
/*///////////////////////////////////////////////
 Variables are all protected so class that's extending
 this can see these
////////////////////////////////////////////////*/
	//tile variables
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	//position and vector
	protected double x;				//position of map object
	protected double y;
	protected double dx;			//dx dy, diretion object is going
	protected double dy;
	
	//dimensions
	protected int width;			//width and height for reading the sprite sheets
	protected int height;
	
	//collision box
	protected int cwidth;	//these are the dimensions that we use for finding collision of players, enimies, etc.
	protected int cheight;
	
	//collision
	protected int currRow;			//current row that we are in...
	protected int currCol;
	protected double xdest;		//the next position we're going to..
	protected double ydest;
	protected double xtemp;		//
	protected double ytemp;
	protected	 boolean topLeft;							//we're using the 4 courners to see whether or not
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	//animation
	protected Animation animation;
	protected int currentAction;		//to tell us what animation we're currently doing (EX: attack)
	protected int previousAction; 		//is it needed?
	protected boolean facingRight;		//if facing left, we'll flip sprite over, mirroring the image)
	
	//movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	//movement physics
	protected double moveSpeed;			//Acceleration
	protected double maxSpeed;			//how fast object can go
	protected double stopSpeed;			//decrements speed when you stop pressing right
	protected double fallSpeed;				//gravity stuff
	protected double maxFallSpeed;		//our velocity
	protected double jumpStart;			//how high I can jump
	protected double stopJumpSpeed;	//the longer you hold the button, the higher you go.
	
	
	//constructor
	public MapObjects(TileMap tm)
	{
		tileMap = tm;							//sets tilemap..
		tileSize = tm.getTileSize();		//gets the tilesize
		
	}
	
/*///////////////////////////////////////
 Lets check if 2 objects collide //part 2 find out what it does
///////////////////////////////////////*/
	public boolean intersects(MapObjects o)
	{
		Rectangle r1 = getRectangle();			
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	
	}

	public Rectangle getRectangle()
	{
		return new Rectangle( (int) x - cwidth, (int) y - cheight, cwidth, cheight);
	}
	
	public void calculateCorners(double x, double y)
	{
		int leftTile = (int) (x - cwidth / 2) / tileSize;		//Lets find out our leftTile, it's like i'm finding current column, but /2 to find whats left of it
		int rightTile = (int) (x + cwidth / 2 - 1) /tileSize;		//-1 so we don't accidently step over the next column
		int topTile = (int) (y - cheight / 2 ) / tileSize;			//same calculation, except in the y direction
		int bottomTile = (int) (y + cheight / 2 -1) / tileSize;		// - 1 so we won't go downward over the next tile.
		
		if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||			//this was a fix for an out of bounds error, is it needed?
				leftTile < 0 || rightTile >= tileMap.getNumCols())
		{
				topLeft = topRight = bottomLeft = bottomRight = false;
				return;
		}	
	
		//So we're going to check if these tiles are normal or block
		int tl = tileMap.getType(topTile, leftTile);				
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile,leftTile);
		int br = tileMap.getType(bottomTile,  rightTile);
		
		
		topLeft = tl == Tile.blocked;			//So if these tiles are blocked, we need collision with it
		topRight = tr == Tile.blocked;		//if we jumped on the right and we hit a wall, this should set it to true
		bottomLeft = bl == Tile.blocked;
		bottomRight = br == Tile.blocked;
		
	}
	
/*////////////////////////////////////////////////////
 Check whether or not we run into a block, or normal tile
////////////////////////////////////////////////////*/
	public void checkTileMapCollision()
	{
		currCol   = (int) x / tileSize;			//lets see what column tile we are on
		currRow = (int) y / tileSize;			//and row tile to see where we are
		
		xdest = x + dx;						
		ydest = y + dy;
	
		xtemp = x;				//we're going to make final changes to x and y when we collide(push the player back)
		ytemp = y;
		
		calculateCorners(x,ydest);	
		if (dy < 0)			//this means we're going upwards
		{
			if (topLeft || topRight)		//so lets check the top corners 
			{
				dy = 0;					//we must stop moving in the up direction
				ytemp = currRow * tileSize + cheight / 2;			//lets set the object just below the current tile we hit
			}
			else //else, we are free to keep going wherever we are going
			{
				ytemp += dy;
			}
		}
		
		if(dy > 0)		//this means we are on a tile, or landed on a tile..
		{
			if (bottomLeft || bottomRight)		//lets check bottom 2 corners
			{
				dy = 0;					//lets stop going in the downward direction
				falling = false;		//if we were falling, we must set falling to false
				ytemp = (currRow + 1) * tileSize - cheight / 2;	//lets set the y position 1 pixel above the tile we landed on
			}
			else { ytemp += dy;}		//else lets just keep going down
		}

		//lets check the x direction now
		calculateCorners(xdest, y);
		if (dx < 0)		//this means we are going left
		{
			if (topLeft || bottomLeft)		//so lets check the left courners, if we hit it its true
			{
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;		//lets set the xposition just to the right of the tile we hit
			}
			else{xtemp += dx;}					//else, lets just keep going
			
		}
		
		if (dx > 0)		//we're moving to the right...
		{
			if (topRight || bottomRight)		//so lets check if we collided with these 2 corners.
			{
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {xtemp += dx;}
		}
		
		if(!falling)		//so lets check if we are falling off the edge.
		{
			calculateCorners (x, ydest + 1);	//lets check the ground 1 pixal above our feet.
			if(!bottomLeft  && !bottomRight)	//this means we're not on the ground anymore, so we have to fall.
			{
				falling = true;
			}
		}
	}

/*/////////////////////////////////////////
Getters
/////////////////////////////////////////*/
	
	public int getx() 				{return (int) x;}
	public int gety() 				{return (int) y;}
	public int getWidth() 		{return width;}
	public int getHeight()		{return height;}
	public int getCWidth()		{return cwidth;}
	public int getCHeight()		{return cheight;}
	
/*///////////////////////////////////////////	
setters
///////////////////////////////////////////*/
	
public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	public void setvector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
/*
Ok, each object has
*/
	public void setMapPosition()
	{
		xmap = tileMap.getx();			//every object has 2 positions, their global positions(x and y) 
		ymap = tileMap.gety();			//and their local position, which is x and y plus the tilemap poisiton
	}
	
	public void setLeft(boolean b)				{ left = b;}
	public void setRight(boolean b)			{ right = b;}
	public void setUp(boolean b)				{up = b;}
	public void setDown(boolean b)				{down = b;}
	public void setJumping(boolean b)	{jumping = b;}

/*////////////////////////////////////////////////////////// 
We're not going to draw mapObjects that is not on the screen
//////////////////////////////////////////////////////////*/
	public boolean notOnScreen()
	{			//the final pos. of the player on the screen
		return x + xmap + width < 0 || x + xmap - width > GameFrameWork.Width || y + ymap + height < 0 || y + ymap - height > GameFrameWork.Height;
		//if object is beyond the left side of the screen, or the right					//or if object is above the screen, or below
	}

/*/////////////////////////////////////////////////
 (drawing animation for facing right and facing left)
 /////////////////////////////////////////////////*/
	public void draw (Graphics2D g)
	{
		if (facingRight)
		{
			g.drawImage(animation.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
		}
		else		//so when right isn't true, we'll be facing left, mirror image.
		{
			g.drawImage(animation.getImage(), (int) (x + xmap - width / 2 + width), (int) (y + ymap - height / 2), -width, height, null);
		}
	}

}
