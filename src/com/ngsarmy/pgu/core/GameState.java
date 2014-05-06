package com.ngsarmy.pgu.core;

import com.ngsarmy.pgu.entity.GameObjectManager;

/* GameState class usage:
 * inherit from this class and 
 * overload the methods in order
 * to create an independent game
 * state which can be transtioned
 * to and from.
 */
public class GameState extends GameObjectManager
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
	public void update(double delta) {super.update(delta);}
	// USAGE:
	// this is called every frame and all drawing code should be in here
	public void render(GameRasterizer g) {super.render(g);}
	// USAGE:
	// this is called right at the end of the state (i.e transition) so it should be used for unloading and transitions.
	public void unload() {}
	// USAGE:
	// this is called when an event is detected (i.e mouse click, keypress)
	public void event(GameEvent ev) {super.event(ev);}
}
