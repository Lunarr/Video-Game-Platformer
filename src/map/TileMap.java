package map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import gameFrame.GameFrameWork;

public class TileMap 
{

	//java io reads file
	//position of map
	private double x;
	private double y;
	
	//bounds. These are the minimum and maximum bounds of map
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;		//found this online, supposed to be smooth camera work
	
	//map
	private int[][] map;			//multidimentional map [col] [row]
	private int tileSize;			//size of tile in pixels
	
	private int numRows;		//numbers of rows
	private int numCols;
	private int width;		//dimensions of the map, width, height, pixals
	private int height;
	
	//tileset
	private BufferedImage tileset;		//images in tileset
	private int numTilesAcross;
	private Tile [][] tiles; 		//after we imported tileset, we put all tile images in here
	
	//drawing bounds
	private int rowOffset;							//we draw the tiles that are only on the screen, instead of all of them at the same time
	private int colOffset;							//so this tells us where we are
	private int numRowsToDraw;				//this tells us ok, we're going to draw this much rows/column
	private int numColsToDraw;
	
	public TileMap(int tileSize)
	{
		this.tileSize = tileSize;																	//sets tilesize
		numRowsToDraw = GameFrameWork.Height / tileSize	  +2;           //240 height tilesize 30, 8 tiles down? draws just enough tiles to fit... 
		numColsToDraw = GameFrameWork.Width / tileSize + 2;																									//...whats VISIBLE to us
		tween = 0.07;
	}
	
/*////////////////////////////////////////////
 Loads TileSet into memory
////////////////////////////////////////////*/
	
	public void loadTiles (String s)
	{
		try {
			
			tileset = ImageIO.read(getClass().getResourceAsStream(s));				//this is how you find resources
			numTilesAcross = tileset.getWidth() / tileSize;									//calculates how much tiles I have across in my Image in "MyStuff"
			tiles = new Tile[2][numTilesAcross];														//so I'm creating an array at each coulmn of the 2 rows in my image in "MyStuff" 
			
			BufferedImage subimage;																//lets get a PART, a sub of the image. We're breaking it up.	
			
			for(int col = 0; col < numTilesAcross; col++)										//lets import the tileset
			{
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);						//x,y, width, height. We don't want to overlap the tiles. 
				tiles[0] [col] = new Tile(subimage, Tile.normal);													//first row of tiles(look at picture in "MyStuff")
				
				subimage = tileset.getSubimage (col * tileSize, tileSize, tileSize, tileSize);			//ok lets get each subImage in the 2nd row...
				tiles[1] [col] = new Tile (subimage, Tile.blocked); 													//2nd row of tiles (look at the tile pictures, these will have collision
			}
			
			}catch(Exception e){e.printStackTrace();}
		
	}
	
/*///////////////////////////////////////////
 Loads Map into memory
////////////////////////////////////////////*/
	public void loadMap (String s)
	{
		try{
			
			InputStream in = getClass().getResourceAsStream(s);				//lets get our resource from "MyStuff"
			BufferedReader br = new BufferedReader (new InputStreamReader(in));	//bufferedReader to read text file
		
			numCols = Integer.parseInt(br.readLine());				//reads the first line (number of columns)
			numRows = Integer.parseInt(br.readLine());				//reads the next line (number of rows)
			
			map = new int [numRows] [numCols];		//lets create the map array
			width = numCols * tileSize;				//you can find out the # of pixels here for width..unnecessary?
			height = numRows * tileSize;
			
			xmin = GameFrameWork.Width - width;
			xmax = 0;
			ymin = GameFrameWork.Height - height;
			ymax = 0;
			
			
			//Now lets read our map...
			String delims = "\\s+";			//Space will be our delims
			for (int row = 0; row < numRows; row++)		//going through each character in the rows
			{
				String line =  br.readLine();					//reads the next line
				String[] tokens = line.split(delims);		//lets split it into tokens
				for (int col = 0; col < numCols; col++)		//lets go through this array, put it into map
				{
					map[row][col] = Integer.parseInt(tokens[col]);	
				}
			}
		
		}catch(Exception e) {e.printStackTrace();}
	}
/*//////////////////////////////////////////
 A Whole bunch of getters, could be useful I guess
//////////////////////////////////////////*/
	//getters
	public int getTileSize()
	{
		return tileSize;
	}
	
	public int getNumRows() { return numRows; }		//these 2 was a hotfix for an out of bounds error 	
	public int getNumCols() { return numCols; }			//which is found in MapObjects
	
	public double getx()
	{
		return x;
	}
	
	public double gety()
	{
		return  y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
//gets the tile
	public int getType(int row, int col)
	{
		int rc = map[row][col];				//so this will get us the value
		int r = rc / numTilesAcross;		//rows
		int c = rc % numTilesAcross;		//columns
		return tiles[r][c].getType();		//return
		
	}

/*///////////////////////////////////////////////////////////////
 Custom camera control that's suppose to follow the player smoothly
///////////////////////////////////////////////////////////////*/
	public void setTween(double d) {tween = d;}
	
	public void setPosition(double x, double y)
	{
		this.x += (x - this.x) * tween;			//this will gradually move the camera to the player position
		this.y += (y - this.y) * tween;			
	
		fixBounds();										//lots of help with the camera...
		
		colOffset = (int) - this.x / tileSize;
		rowOffset = (int) - this.y / tileSize;
	}
/*////////////////////////////////////////////
makes sure the bounds aren't being passed
////////////////////////////////////////////*/
	private void fixBounds()
	{
		if (x < xmin) x = xmin;
		if (y < ymin) y = ymin;
		if (x > xmax) x = xmax;
		if (y > ymax) y = ymax;
	}
	
/*////////////////////////////////
 Draw Function
/////////////////////////////////*/
	public void draw(Graphics2D g)
	{						//starting row(0)
		for(int row = rowOffset; row < rowOffset + numRowsToDraw;row++)
		{
			
			if (row >= numRows) break;		//so we can stop drawing, since really there's nothing else to draw
								//starting Col
			for (int col = colOffset; col < colOffset + numColsToDraw; col++)
			{
				if (col >= numCols) break;
				
				if (map[row] [col] == 0) continue;		//if there's no image there, don't draw it..thats how the tileset image is
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(tiles [r][c].getImage(), (int) x + col * tileSize, (int)y + row * tileSize, null);
				
				
				
			}
		}
	}
}



//JAVA FINAL

//questions about what we did in class
//Collision detection between 2 rectangles, between 2 circles, line or a circle CHECK
//how to use line for artificial
//formulas for 2d to 3d transformation CHECK
//formulas for rotation CHECK?
//maybe write a tiny bit of code, code for rotation? CHECK
//most questions is "what's formula for rotation, translating from 2d to 3d screen" CHECK...
//explain that triangle view, when you see through the camera
//how to do animation...CHECK
//how to draw a line?...
