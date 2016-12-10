package gameFrame;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;







import audio.AudioPlayer;
import enemyControl.Boss1;
import enemyControl.Slugger;
import objectControl.*;
import map.*;

public class Level1State extends GameState					//gameState is abstact, so we must implement all its methods
{	
	
	private TileMap tileMap;
	Background bg;
	private AudioPlayer bgMusic;
	private Player player;
	private Boss1 boss;
	private Interface hud;
	private boolean check;
	
	
	private ArrayList <Enemy> enemies;
/*//////////////////////////////////////////
 Our Contructor
//////////////////////////////////////////*/
	public Level1State(GameStateManager gsm)				//all states must have this manager! they are controlled from there
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
				tileMap.loadMap("/Maps/level1-1.map");
				tileMap.setPosition(0,0);
				tileMap.setTween(1.0);						//camera, 0.07
				
				
				player = new Player(tileMap);
				player.setPosition(100,100); 				//2800
				
				boss = new Boss1 (tileMap);
				boss.setPosition(3135,100);			//3135
				
				
				
				populateEnemies();
				
				hud = new Interface (player);
				
				bgMusic = new AudioPlayer("/Music/LVL1_BG.mp3");
				bgMusic.play();
				
	}
	
	
	private void populateEnemies()
	{
		enemies = new ArrayList <Enemy> ();
		
		Slugger s;
		Point[] points = new Point []
				{
					new Point (200,100), new Point(860, 200), new Point(1525, 200), new Point(1680, 200), new Point(1800,200)
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
		boss.update();
		
		//
		tileMap.setPosition(GameFrameWork.Width / 2 - player.getx(), GameFrameWork.Height / 2 - player.gety());
	
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		//System.out.println(player.getx());
		//System.out.println(boss.getHealth());
		
		if (player.gety() > 400 || player.getHealth() == 0)
		{
			bgMusic.close();
			gsm.setState(GameStateManager.Level1State);
		}
		
		if (boss.getHealth() == 0)
		{
			bgMusic.close();
			gsm.setState(GameStateManager.WinState);
		}
		
		//attacking enemies
		player.checkAttack(enemies);
		player.checkAttack(boss);
		boss.checkAttack(player);
		
		
		//BOSS AI
		boss.SetAI();
		
		
		
		
		//updating enemies...
		for(int i = 0; i < enemies.size(); i++)
		{
			enemies.get(i).update();
			
			if (enemies.get(i).isDead())
			{
				enemies.remove(i);
				i--;
			}
		}
		
	}
	
	public void draw(Graphics2D g)
	{	
		bg.draw(g);
		
		tileMap.draw(g);
		
		player.draw(g);
		
		boss.draw(g);
		
		for(int i = 0; i < enemies.size(); i++)
		{
			enemies.get(i).draw(g);
		}
	
		hud.draw(g);
	}
	
	public  void keyPressed(int k)
	{
		if (k == KeyEvent.VK_0) player.setPosition(2800,100);
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
