package com.ngsarmy.pgu.core;

import java.util.Stack;

/* GameStateStack class:
 * stores various game states in a stack fashion
 * and it allows for layering of multiple states
 * with transition callbacks
 */
public class GameStateStack 
{
	// state list
	private Stack<GameState> gsList;
	
	public GameStateStack()
	{
		gsList = new Stack<GameState>();
	}
	
	// USAGE:
	// use this to add a state onto the stack without transition
	public void pushState(GameState state)
	{
		gsList.push(state);
		state.load();
	}
	
	// USAGE:
	// call this to stop the current state and transtion into another stage
	public void transState(GameState state)
	{
		stopState();
		pushState(state);
	}
	
	// USAGE:
	// call this to stop the current state without transitioning into another
	public void stopState()
	{
		gsList.pop().unload();
	}
	
	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	// the game class calls this to allow the state to poll events
	public void event(GameEvent ev)	
	{
		GameState state = gsList.peek();
		state.event(ev);
	}
	
	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	// the game class calls this to update the current state
	public void update(float delta)
	{
		for(int i = 0; i < gsList.size(); i++)
		{
			if(gsList.get(i).update(delta))
				return;
		}
	}
	
	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	// the game class calls this to render all states
	public void render(GameRasterizer g)
	{
		for(int i = 0; i < gsList.size(); i++)
		{	
			if(gsList.get(i).render(g))
				return;
		}
	}
}
