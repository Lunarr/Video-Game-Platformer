package gameFrame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GameFrameWork extends JPanel implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	public static final int Width = 520;		//520     320 
	public static final int Height = 293;		//440      240
	public static final int Scale = 3;
	
	// game threads
	
	private Thread thread;
	private boolean running;		//all this is for the 60 fps frames calculation
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	//Image
	private BufferedImage image;		//
	private Graphics2D g;					//graphics2D is just like the graphics object, it extends it, and its newer
		
	//GSM(GameStateManager)
	private GameStateManager gsm;
	
/*//////////////////////////////////////
 
//////////////////////////////////////*/
	public GameFrameWork()
	{
		super();
		setPreferredSize(new Dimension(Width * Scale, Height * Scale));
		setFocusable(true);
		requestFocus();
	}

/*//////////////////////////////////////
	 
//////////////////////////////////////*/	

	public void addNotify()
	{
		super.addNotify();			//game panel done loading, then it goes adds a thread
		if (thread == null)
		{
			thread = new Thread (this);
			addKeyListener (this);
			thread.start();
		}
	}

/*//////////////////////////////////////
	 
//////////////////////////////////////*/	
	public void init()
	{
		image = new BufferedImage(Width,Height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
			
		gsm = new GameStateManager();
	}
	
/*//////////////////////////////////////
	 
//////////////////////////////////////*/
	public void run()
	{
		init();
		
		long start;
		long elapsed;
		long wait;
		
	while (running)
		{
			start = System.nanoTime();
			update();
			draw();
			drawToScreen();
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			if (wait < 0) wait = 5;
			try {
				Thread.sleep(wait);
			} catch(Exception e){e.printStackTrace();}
			
		}
	}
	
/*//////////////////////////////////////
	 
//////////////////////////////////////*/
	
	private void update()
	{
		gsm.update();   			//updates the gameState (pause, lvl 1, etc.)
	}

/*//////////////////////////////////////
	 
//////////////////////////////////////*/
	private void draw()
	{
		gsm.draw(g);				//draws whatever the state GSM sends us
	}
	
/*//////////////////////////////////////
	 
//////////////////////////////////////*/
	private void drawToScreen()
	{
		Graphics g2 = getGraphics();			//gameframe's drawing object
		g2.drawImage(image,0 ,0, Width * Scale, Height * Scale, null);		//draw the image
		g2.dispose();
	}

/*//////////////////////////////////////
	 
//////////////////////////////////////*/
	public void keyTyped(KeyEvent Key) 
	{
		
	}
	
	public void keyPressed(KeyEvent key) 
	{
		gsm.keyPressed(key.getKeyCode());
	}
	
	public void keyReleased (KeyEvent key)
	{
		gsm.keyReleased(key.getKeyCode());
	}

}
	
	

