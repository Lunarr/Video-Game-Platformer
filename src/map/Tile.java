package map;

import java.awt.image.BufferedImage;

public class Tile 
{
	private BufferedImage image;
	private int type;
	
	//the type of tiles I have
	
	public static final int normal = 0;
	public static final int blocked = 1;
	
/*/////////////////////////////////////////////
 Constructor that tells us what image and what type
////////////////////////////////////////////*/
	
	public Tile (BufferedImage image, int type)		//we're putting images in the tile[] []
	{
		this.image = image;
		this.type = type;
	}

	public BufferedImage getImage()
	{
		return image; 
	}
	
	public int getType()
	{
		return type;
	}
}
