package objectControl;

import map.TileMap;

public class Enemy extends MapObjects
{
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage; 			//when the player touches an enemy
	
	protected boolean flinching;
	protected long flinchTimer;
	
	
/*//////////////////////////////////////////////////
 Our typical constructor, every object needs the tilemap
///////////////////////////////////////*/
	public Enemy(TileMap tm)
	{
		super(tm);														//all enemies need a TileMap...
	}

	public boolean isDead() {return dead;}

	public int getDamage() 	{return damage;}

/*///////////////////////////////////////////
 Getting hit function, takes in how much damage
///////////////////////////////////////////*/
	public void hit(int damage)
	{
		if (dead || flinching) return;							//this means we can't get hit, so just return!
		health -= damage;
		if (health < 0) health = 0;								//I never want health to go under 0...
		if (health == 0) dead = true;
		
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void update(){};

}
