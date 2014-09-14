package com.ngsarmy.pgu.core;


import com.ngsarmy.pgu.utils.GameUtils;
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
	public Vector2 frictionFactor;
	
	public float angularVelocity;
	public float angularFrictionFactor;
	public float angularAcceleration;
	public float maxAngularVelocity;
	
	public float collisionExtend;
	
	private boolean lastTouchingLeft;
	private boolean lastTouchingRight;
	private boolean lastTouchingTop;
	private boolean lastTouchingBottom;
	
	private boolean touchingLeft;
	private boolean touchingRight;
	private boolean touchingTop;
	private boolean touchingBottom;
	
	public GameObject touching;
	
	public boolean allowLeftCollision;
	public boolean allowRightCollision;
	public boolean allowBottomCollision;
	public boolean allowTopCollision;
	
	public float angle;
	
	public boolean immovable;
	
	public GameObjectExt(Vector2 position, String type) 
	{
		super(position, type);
		
		velocity = new Vector2();
		maxVelocity = new Vector2(100, 100);
		acceleration = new Vector2();
		frictionFactor = new Vector2(1, 1);
		
		angularVelocity = 0;
		angularFrictionFactor = 1;
		angularAcceleration = 0;
		maxAngularVelocity = 10;
		
		lastTouchingLeft = false;
		lastTouchingRight = false;
		lastTouchingTop = false;
		lastTouchingBottom = false;
		
		touchingLeft = false;
		touchingRight = false;
		touchingTop = false;
		touchingBottom = false;
		
		touching = null;
		
		allowLeftCollision = true;
		allowTopCollision = true;
		allowRightCollision = true;
		allowBottomCollision = true;
		
		immovable = false;
		
		collisionTypes = new String[0];
		
		collisionExtend = 1;
	}
	
	private void resetTouching()
	{
		lastTouchingLeft = touchingLeft;
		lastTouchingTop = touchingTop;
		lastTouchingRight = touchingRight;
		lastTouchingBottom = touchingBottom;
		
		touchingLeft = false;
		touchingRight = false;
		touchingTop = false;
		touchingBottom = false;
	}

	public void impulse(float x, float y)
	{
		resetTouching();
		
		velocity.x += x;
		velocity.y += y;
	}
	
	public void moveBy(float x, float y)
	{
		if(!immovable)
			super.moveBy(x, y);
	}
	
	public void moveBy(float x, float y, String type)
	{
		if(!immovable)
			super.moveBy(x, y, type);
	}
	
	public void moveBy(float x, float y, String[] types)
	{
		if(!immovable)
			super.moveBy(x, y, types);
	}
	
	public void applyPhysics(float delta)
	{
		resetTouching();

		touching = null;
		
		if(!immovable)
		{
			velocity.x += acceleration.x * delta;
			velocity.y += acceleration.y * delta;
		
			velocity.x = GameUtils.clamp(velocity.x, -maxVelocity.x, maxVelocity.x);
			velocity.y = GameUtils.clamp(velocity.y, -maxVelocity.y, maxVelocity.y);
			
			moveBy(velocity.x, velocity.y, collisionTypes);
			
			velocity.x *= frictionFactor.x;
			velocity.y *= frictionFactor.y;
			
			if(touchingBottom && lastTouchingBottom)
			{	
				if(velocity.y > 0)
					velocity.y = 0;
			}
			
			if(touchingLeft && lastTouchingLeft)
			{
				if(velocity.x < 0)
					velocity.x = 0;
			}
			
			if(touchingRight && lastTouchingRight)
			{
				if(velocity.x > 0)
					velocity.x = 0;
			}
			
			if(touchingTop && lastTouchingTop)
			{
				if(velocity.y < 0)
					velocity.y = 0;
			}
		}
		
		angularVelocity += angularAcceleration * delta;
		
		angularVelocity = GameUtils.clamp(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
		
		angle += angularVelocity * delta;
		
		angularVelocity *= angularFrictionFactor;
	}
	
	public void update(float delta)
	{
		applyPhysics(delta);
	}
	
	public void justTouchedLeft(GameObject go)
	{
		justTouched(go);
		if(!immovable)
		{	
			if(velocity.x < 0)
				velocity.x = 0;
		}
	}
	
	public void justTouchedRight(GameObject go)
	{	
		justTouched(go);
		if(!immovable)
		{	
			if(velocity.x > 0)
				velocity.x = 0;
		}
	}
	
	public void justTouchedTop(GameObject go)
	{
		justTouched(go);
		if(!immovable)
		{	
			if(velocity.y < 0)
				velocity.y = 0;
		}
	}
	
	public void justTouchedBottom(GameObject go)
	{
		justTouched(go);
		if(!immovable)
		{	
			if(velocity.y > 0)
				velocity.y = 0;
		}
	}
	
	public void justTouched(GameObject go)
	{
	}
	
	public boolean isTouchingLeft()
	{
		return touchingLeft;
	}
	
	public boolean isTouchingRight()
	{
		return touchingRight;
	}
	
	public boolean isTouchingTop()
	{
		return touchingTop;
	}
	
	public boolean isTouchingBottom()
	{
		return touchingBottom;
	}
	
	public boolean wasTouchingLeft()
	{
		return lastTouchingLeft && !touchingLeft;
	}
	
	public boolean wasTouchingRight()
	{
		return lastTouchingRight && !touchingRight;
	}
	
	public boolean wasTouchingTop()
	{
		return lastTouchingTop && !touchingTop;
	}
	
	public boolean wasTouchingBottom()
	{
		return lastTouchingBottom && !touchingBottom;
	}
	
	public boolean moveCollideX(GameObject go)
	{	
		// at this point, we know we hit something, so 
		// the idea behind finding out which side we hit is 
		// to add our velocity onto an object (moving it away 
		// such that we no longer collide) and then checking
		// whether that object lies on the left or the right 
		// side
		
		boolean hitSomething = false;
		
		// if we can hit things from the right
		if(allowRightCollision)
		{
			// touching right side of our rect with the left side of the other rect
			if(go.getLeft() + Math.abs(velocity.x) + collisionExtend > getRight())
			{
				touchingRight = true;
				touching = go;
				if(!lastTouchingRight)
					justTouchedRight(go);
				
				if(go instanceof GameObjectExt)
				{
					((GameObjectExt) go).touchingLeft = true;
					if(!((GameObjectExt) go).lastTouchingLeft) ((GameObjectExt) go).justTouchedLeft(this);
				}
				
				hitSomething = true;
			}
		}
		
		// if we can hit things from the left
		if(allowLeftCollision)
		{
			// touching left side of our rect with the right side of the other rect
			if(go.getRight() - Math.abs(velocity.x) - collisionExtend < getLeft())
			{
				touchingLeft = true;
				touching = go;
				if(!lastTouchingLeft)
					justTouchedLeft(go);
				
				if(go instanceof GameObjectExt)
				{
					((GameObjectExt) go).touchingRight = true;
					if(!((GameObjectExt) go).lastTouchingRight) ((GameObjectExt) go).justTouchedRight(this);
				}
				
				hitSomething = true;
			}
		}
		
		return hitSomething;
	}
	
	public boolean moveCollideY(GameObject go)
	{
		// at this point, we know we hit something, so 
		// the idea behind finding out which side we hit is 
		// to add our velocity onto an object (moving it away 
		// such that we no longer collide) and then checking
		// whether that object lies above or below this
		// object
		
		boolean hitSomething = false;
		
		// if we can hit things from the top
		if(allowTopCollision)
		{
			// touching the top side of our rect with the bottom side of the other rect
			if(go.getBottom() - Math.abs(velocity.y) - collisionExtend < getTop())
			{
				touchingTop = true;
				touching = go;
				if(!lastTouchingTop)
					justTouchedTop(go);
				
				if(go instanceof GameObjectExt)
				{
					((GameObjectExt) go).touchingBottom = true;
					if(!((GameObjectExt) go).lastTouchingBottom) ((GameObjectExt) go).justTouchedBottom(this);
				}
				
				hitSomething = true;
			}
		}
		
		// if we can hit things from the bottom
		if(allowBottomCollision)
		{
			// touching the bottom side of our rect with the top side of the other rect
			if(go.getBottom() + Math.abs(velocity.y) + collisionExtend > getBottom())
			{
				touchingBottom = true;
				touching = go;
				if(!lastTouchingBottom)
					justTouchedBottom(go);
				
				if(go instanceof GameObjectExt)
				{
					((GameObjectExt) go).touchingTop = true;
					if(!((GameObjectExt) go).lastTouchingTop) ((GameObjectExt) go).justTouchedTop(this);
				}

				hitSomething = true;
			}
		}
		
		return hitSomething;
	}
}
