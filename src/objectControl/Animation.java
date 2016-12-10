package objectControl;

import java.awt.image.BufferedImage;

public class Animation 
{
	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;		//how long we switch between each frame
	
	private boolean playedOnce;		//useful for attack animations, where animations of attack only need to happen once
	
/*///////////////////////
Constructor 
///////////////////////*/
	public Animation()
	{
		playedOnce = false;
	}
/*//////////////////////////////////////////////
 SetFrames from an array of images
//////////////////////////////////////////////*/
	public void setFrames(BufferedImage[] frames)
	{
		this.frames = frames;
		currentFrame = 0;								//reset currentframe to 0
		startTime = System.nanoTime();		//lets get the start time
		playedOnce = false;						
	}
	
//setters
	public void setDelay (long d) {delay = d;}
	public void setFrame(int i)    {currentFrame = i;}
	
/*////////////////////////////////////////////////////////////////
 In charge for determining whether or not to move to next animation
////////////////////////////////////////////////////////////////*/
	public void update()
	{
		if (delay == -1) return;		//then there's no animation
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;	//to get it in miliseconds, divide by 1mil
		
		if(elapsed > delay)								//if that time is greater then delay...
		{
			currentFrame++;								//go to the next frame/image
			startTime = System.nanoTime();		//reset the timer
		}
		if(currentFrame == frames.length)		//don't go past the amount of frames we have(out of bounds)
		{
			currentFrame = 0;								//we'll just set it back to 0
			playedOnce = true;
		}
	}
	
/*	//////////////////////////////////////////
							getters
///////////////////////////////////////////*/
	public int getFrame() {return currentFrame;}										//gets the frame number.
	public BufferedImage getImage() {return frames[currentFrame];}		//gets image we need to draw
	public boolean hasPlayedOnce() {return playedOnce;}							//returns if animation has playedOnce already or not
}
