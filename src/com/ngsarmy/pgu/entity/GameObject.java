package com.ngsarmy.pgu.entity;

import java.util.ArrayList;
import java.util.List;

import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.core.GameState;
import com.ngsarmy.pgu.utils.Rectangle;
import com.ngsarmy.pgu.utils.Vector2;

public class GameObject 
{
	private final int GAMEOBJECT_COLLISION_SAMPLE_FACTOR = 5;
	private GameState state;
	public Rectangle rectangle;
	public String name;
	public String type;
	public int layer;
	public boolean collidable;
	
	public GameObject(Vector2 position, String type)
	{
		rectangle = new Rectangle(position.x, position.y, 0, 0);
		name = "default";
		this.type = type;
		layer = 0;
		collidable = true;
	}

	public void event(GameEvent ev)
	{
	}
	
	public void update(double delta)
	{
	}
	
	public void render(GameRasterizer g)
	{
	}
	
	public Vector2 getPos()
	{
		return rectangle.position;
	}
	
	public float getTop()
	{
		return rectangle.position.y;
	}
	
	public float getLeft()
	{
		return rectangle.position.x;
	}
	
	public float getBottom()
	{
		return rectangle.position.y + rectangle.size.y;
	}
	
	public float getRight()
	{
		return rectangle.position.x + rectangle.size.x;
	}
	
	protected void moveBy(float x, float y, String type)
	{
		for(int i = 0; i < GAMEOBJECT_COLLISION_SAMPLE_FACTOR; i++)
		{
			int divisor = GAMEOBJECT_COLLISION_SAMPLE_FACTOR - i;
			
			GameObject go = collide(rectangle.position.x + x / divisor, rectangle.position.y, type);
			
			if(go != null)
			{
				if(moveCollideX(go)) return;
				else rectangle.position.x += x / divisor;
			}
			
			go = collide(rectangle.position.x, rectangle.position.y + y / divisor, type);
			
			if(go != null)
			{
				if(moveCollideY(go)) return;
				else rectangle.position.y += y / divisor;
			}
		}
	}
	
	protected boolean moveCollideX(GameObject go)
	{
		return true;
	}
	
	protected boolean moveCollideY(GameObject go)
	{
		return true;
	}
	
	protected GameObject collide(float x, float y, String type)
	{
		List<GameObject> objs = new ArrayList<GameObject>();
		state.getObjectsWithType(type, objs);
		
		Rectangle temp = new Rectangle(rectangle.position.x + x, rectangle.position.y + y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < objs.size(); i++)
		{
			GameObject go = objs.get(i);
			
			if(go.collidable && go.rectangle.collide(temp))
				return go;
		}
		
		return null;
	}
	
	protected GameObject collideTypes(float x, float y, String[] types)
	{
		List<GameObject> objs = new ArrayList<GameObject>();
		for(int i = 0; i < types.length; i++)
			state.getObjectsWithType(types[i], objs);
		
		Rectangle temp = new Rectangle(rectangle.position.x + x, rectangle.position.y + y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < objs.size(); i++)
		{
			GameObject go = objs.get(i);
			
			if(go.collidable && go.rectangle.collide(temp))
				return go;
		}
		return null;
	}
	
	protected void collideInto(float x, float y, String type, List<GameObject> objs)
	{
		List<GameObject> possible = new ArrayList<GameObject>();
		state.getObjectsWithType(type, possible);
		
		Rectangle temp = new Rectangle(rectangle.position.x + x, rectangle.position.y + y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < possible.size(); i++)
		{
			GameObject go = possible.get(i);
			
			if(go.collidable && go.rectangle.collide(temp))
				objs.add(go);
		}
	}
	
	protected void collideTypesInto(float x, float y, String[] types, List<GameObject> objs)
	{
		List<GameObject> possible = new ArrayList<GameObject>();
		for(int i = 0; i < types.length; i++)
			state.getObjectsWithType(types[i], possible);
		
		Rectangle temp = new Rectangle(rectangle.position.x + x, rectangle.position.y + y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < possible.size(); i++)
		{
			GameObject go = possible.get(i);
			
			if(go.collidable && go.rectangle.collide(temp))
				objs.add(go);
		}
	}
}
