package gameFrame;
import java.awt.Graphics2D;
import java.util.ArrayList;
public class GameStateManager 
{
	
	private GameState[]  gameStates;
	private int currentState;
	
	public static final int NumGameStates = 4;
	public static final int MenuState = 0;
	public static final int Level1State = 1;
	public static final int WinState = 2;
	public static final int Level1p2State = 3;
	
/*//////////////////////////////////////////
 Creation of States, holds references to states
/////////////////////////////////////////*/
	public GameStateManager()
	{
		gameStates = new GameState[NumGameStates];
		
		currentState = MenuState;
		
		loadState(currentState);
		
	}

	private void loadState(int state)
	{
		if (state == MenuState) gameStates[state] = new MenuState(this);
	
		if(state == Level1State) gameStates[state] = new Level1State(this);
		
		if(state == Level1p2State) gameStates[state] = new Level1p2State(this);
		
		if (state == WinState) gameStates[state] = new WinState(this);
	}
	
	public void unloadState (int state)
	{
		gameStates[state] = null;
	}
/*/////////////////////////////////////
Get's the state, sets it as current
////////////////////////////////////*/
	public void setState (int state)
	{
		unloadState(currentState);
		currentState= state;

		loadState(currentState);
		//gameStates[currentState].init();
		
	}
	
/*/////////////////////////////////////
	 
////////////////////////////////////*/
	public void update()
	{
		//System.out.println(gameStates[currentState]);
		try{
			for(int i = 0; i < gameStates.length; i++ )			//FOR AND IF STATEMENTS ARE HOT FIX FOR NULL POINTER ERROR
				if (gameStates[i] != null)									//CONSIDER FIXING THE PROBLEM ANOTHER WAY, same for draw() below
				{
					gameStates[currentState].update();
				}
		}catch(Exception e){e.printStackTrace();}
	}
	
/*/////////////////////////////////////
	 
////////////////////////////////////*/
	
	public void draw(Graphics2D g)
	{
		try{
			for(int i = 0; i < gameStates.length; i++ )
				if (gameStates[i] != null)
				{
					gameStates[currentState].draw(g);
					
				}
		}catch(Exception e){e.printStackTrace();}
	}

/*/////////////////////////////////////
	 
////////////////////////////////////*/
	public void keyPressed(int k)
	{
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k)
	{
		gameStates[currentState].keyReleased(k);
	}
}
