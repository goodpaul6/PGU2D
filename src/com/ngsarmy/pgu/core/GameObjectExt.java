package com.ngsarmy.pgu.core;

import com.ngsarmy.pgu.utils.Vector2;

// GameObjectExt class:
// an extended GameObject which 
// provides basic/semi-advanced physics 
// capabilities as well as collision callbacks
// and the like. This can be used in pretty
// much any game is makes life much easier
// for any top-down, sidescrolling, platforming,
// and actually any 2d game
// It is recommended that you use this over the 
// default GameObject class unless it is direly
// necessary.
public class GameObjectExt extends GameObject
{
	public String[] collisionTypes;
	
	public Vector2 velocity;
	public Vector2 maxVelocity;
	public Vector2 acceleration;
	
	public Vector2 friction;
	
	public float bounciness;
	
	public float angle;
	
	public GameObjectExt(Vector2 position, String type) 
	{
		super(position, type);
	}
	
}
