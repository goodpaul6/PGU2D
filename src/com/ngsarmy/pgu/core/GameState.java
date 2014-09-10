package com.ngsarmy.pgu.core;


/* GameState class usage:
 * inherit from this class and 
 * overload the methods in order
 * to create an independent game
 * state which can be transtioned
 * to and from.
 */
public class GameState extends GameGroup
{
	protected Game game;
	
	// USAGE:
	// all resource creation and loading should be in the contstructor for best results
	public GameState(Game _game) { game = _game; }
	
	// USAGE:
	// this is called right after the state has been loaded and it should be used for any work done specifically at that point
	// this should be used for initialization, not creation (constructor)
	public void load() {}
	// USAGE:
	// this is called every frame therefore all logic should be in here
	// return true if you want to stop updating states after this one
	// for example, if you had a pause screen, but you don't want the previous 
	// state to update, return true
	public boolean update(float delta) {super.updateGroup(delta); return false;}
	// USAGE:
	// this is called every frame and all drawing code should be in here
	// return true if you want to stop updating states after this one
	// for example, if you had a screen that covers the entire window,
	// you wouldn't want the previous state to render since you're 
	// rendering over it anyways. Therefore, you can return true in that 
	// case to prevent unnecessary rendering.
	public boolean render(GameRasterizer g) {super.renderGroup(g); return false;}
	// USAGE:
	// this is called right at the end of the state (i.e transition) so it should be used for unloading and transitions.
	public void unload() {}
	// USAGE:
	// this is called when an event is detected (i.e mouse click, keypress)
	public void event(GameEvent ev) {super.eventGroup(ev);}
}
