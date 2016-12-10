package gameFrame;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import map.Background;
import map.TileMap;
import objectControl.Enemy;
import objectControl.Interface;
import objectControl.Player;
import audio.AudioPlayer;
import enemyControl.Boss1;
import enemyControl.Slugger;

public class Level1p2State extends GameState					//gameState is abstact, so we must implement all its methods
{	
	
	private TileMap tileMap;
	Background bg;
	private AudioPlayer bgMusic;
	private Player player;
	private Interface hud;
	private boolean check;
	
	
	private ArrayList <Enemy> enemies;
/*//////////////////////////////////////////
 Our Contructor
//////////////////////////////////////////*/
	public Level1p2State(GameStateManager gsm)				//all states must have this manager! they are controlled from there
	{
		try{
			bg = new Background("/Backgrounds/Level1_BG2.png",0.1);	//moves 10% of our movement...
		}catch(Exception e){e.printStackTrace();}
		this.gsm = gsm;
		init();					//lets call init, where we initialize TileMap;
	}

/*///////////////////////////////////////////////////////////////
  Let's start up the Tilemap in init. initialize tilemap before we use it
//////////////////////////////////////////////////////////////*/
	public void init()
	{
				tileMap = new TileMap(30);					//size 30
				tileMap.loadTiles("/Tilesets/VideogameTiles.png");
				tileMap.loadMap("/Maps/Level1-2.tme");
				tileMap.setPosition(0,0);
				tileMap.setTween(1.0);						//camera, 0.07
				
				
				player = new Player(tileMap);
				player.setPosition(300,50); 				//2800
				
		
				
				populateEnemies();
				
				hud = new Interface (player);
				
				bgMusic = new AudioPlayer("/Music/LVL1_BG.mp3");
				bgMusic.play();
				
	}
	
	
	private void populateEnemies()
	{
		enemies = new ArrayList <Enemy> ();
		
		Slugger s;
		//place them from 90-450 in Xcoords, 			-100 -> 300 for ycorrds
		Point[] points = new Point []
				{
					new Point (90,300), new Point(110, 200), new Point(100, 50), new Point(110,50), new Point (120, 60), 
					new Point(120, 400), new Point(130, 320), new Point(110,-1000), new Point (150, -1100), new Point(135, -1200),
					new Point(125, - 1400), new Point (115, -1300), new Point (360, - 1500),new Point(440,120), new Point(430, 50), 
					new Point (435, -1700), new Point (300, -1900), new Point (250, - 2100), new Point (230, - 2300), new Point(430, 30),
					new Point(410,-200), new Point (390, -300), new Point(390, - 500), new Point(450, -700), new Point(420, -800),
					new Point (90,-2500), new Point(110, -2700), new Point(100, -2900), new Point(110,-3100), new Point (120, -3300),
					new Point(450,-3500), new Point(410, -3700), new Point(445, - 3900), new Point (445, -1100), new Point (100, -400),
					new Point(120, -400), new Point(130, -320), new Point(110,-1325), new Point (150, -600), new Point(135, -200),
					new Point (90,-4100), new Point(110, -4300), new Point(100, -4500), new Point(110,-4700), new Point (120, -4900), 
					new Point(120, -5100), new Point(130, -5200), new Point(110,-5400), new Point (150, -5600), new Point(135, -5800),
					new Point(125, - 1325), new Point (115, -1350), new Point (360, - 1520),new Point(445,-1420), new Point(420, -1460), 
					new Point (235, -2720), new Point (200, -1820), new Point (150, - 2250), new Point (130, - 2130), new Point(430, -3130),
			     	new Point(110,-5240), new Point (410, -5100), new Point(360, - 5330), new Point(435, -4100), new Point(420, -4200), 
					new Point( 410, -4100), new Point (100, -4240), new Point (90,-4200), new Point(110, -4100), new Point(100, -4760),
					new Point(110,-4260), new Point (120, -4920), new Point(450,-4100), new Point(440, -4260), new Point(445, - 4100),
					new Point (432, -3600), new Point (300, -3020),
				};
		
		for(int i = 0; i < points.length; i++)
		{
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y );
			enemies.add(s);
		}
		
		
		
	}
	
/*//////////////////////////
 
 /////////////////////////*/
	public void update()
	{
		//update player
		player.update();
		
		
		
		tileMap.setPosition(GameFrameWork.Width / 2 - player.getx(), GameFrameWork.Height / 2 - player.gety());
	
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		//System.out.println(player.gety());
		//System.out.println(boss.getHealth());
		
		if (player.gety() > 1000 || player.getHealth() == 0)
		{
			bgMusic.close();
			gsm.setState(GameStateManager.Level1p2State);
		}
		
		//if (boss.getHealth() == 0)
		//{
			//bgMusic.close();
			//gsm.setState(GameStateManager.WinState);
		//}
		
		//attacking enemies
		player.checkAttack(enemies);
		
		//System.out.println(enemies.size());
		
		//updating enemies...
		for(int i = 0; i < enemies.size(); i++)
		{
			enemies.get(i).update();
			
			if (enemies.get(i).isDead())
			{
				enemies.remove(i);
				i--;
			}
			if(enemies.size() == 0)
			{
				bgMusic.close();
				gsm.setState(GameStateManager.WinState);
			}
		}
		
	}
	
	public void draw(Graphics2D g)
	{	
		bg.draw(g);
		
		tileMap.draw(g);
		
		player.draw(g);
		
		for(int i = 0; i < enemies.size(); i++)
		{
			enemies.get(i).draw(g);
		}
	
		hud.draw(g);
	}
	
	public  void keyPressed(int k)
	{
		//if (k == KeyEvent.VK_0) player.setPosition(2800,100);
		if ( k == KeyEvent.VK_LEFT) player.setLeft(true);
		if ( k == KeyEvent.VK_RIGHT) player.setRight(true);
		if ( k == KeyEvent.VK_UP) player.setUp(true);
		if ( k == KeyEvent.VK_DOWN) player.setDown(true);
		if ( k == KeyEvent.VK_W) player.setJumping(true);
		if ( k == KeyEvent.VK_E) player.setGliding(true);
		if ( k == KeyEvent.VK_R) player.setScratching();
		if ( k == KeyEvent.VK_F) player.setFiring();
		
	}
	public void keyReleased(int k)
	{
		if ( k == KeyEvent.VK_LEFT) player.setLeft(false);
		if ( k == KeyEvent.VK_RIGHT) player.setRight(false);
		if ( k == KeyEvent.VK_UP) player.setUp(false);
		if ( k == KeyEvent.VK_DOWN) player.setDown(false);
		if ( k == KeyEvent.VK_W) player.setJumping(false);
		if ( k == KeyEvent.VK_E) player.setGliding(false);
	}
}

