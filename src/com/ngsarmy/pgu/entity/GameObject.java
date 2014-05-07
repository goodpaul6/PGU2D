package com.ngsarmy.pgu.entity;

import java.util.ArrayList;
import java.util.List;

import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameImage;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.core.GameState;
import com.ngsarmy.pgu.utils.Rectangle;
import com.ngsarmy.pgu.utils.Vector2;

public class GameObject 
{
	private final int GAMEOBJECT_COLLISION_SAMPLE_FACTOR = 5;
	public GameState state;
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
	
	public Vector2 getSize()
	{
		return rectangle.size;
	}
	
	public void setPos(Vector2 pos)
	{
		rectangle.position = pos;
	}
	
	public void setPos(float x, float y)
	{
		rectangle.position.x = x;
		rectangle.position.y = y;
	}
	
	public void setHitbox(Vector2 size)
	{
		rectangle.size = size;
	}
	
	public void setHitbox(float w, float h)
	{
		rectangle.size.x = w;
		rectangle.size.y = h;
	}
	
	public void setHitbox(GameImage image)
	{
		rectangle.size.x = image.getWidth();
		rectangle.size.y = image.getHeight();
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
	
	public float getWidth()
	{
		return rectangle.size.x;
	}
	
	public float getHeight()
	{
		return rectangle.size.y;
	}
	
	public void setRight(float v)
	{
		rectangle.position.x = v - rectangle.size.x;
	}
	
	public void setLeft(float v)
	{
		rectangle.position.x = v;
	}
	
	public void setBottom(float v)
	{
		rectangle.position.y = v - rectangle.size.y;
	}
	
	public void setTop(float v)
	{
		rectangle.position.y = v;
	}
	
	protected void moveBy(float x, float y)
	{
		rectangle.position.x += x;
		rectangle.position.y += y;
	}
	
	protected void moveBy(float x, float y, String type)
	{
		List<GameObject> possible = new ArrayList<GameObject>();
		state.getObjectsWithType(type, possible);
		
		for(int i = 0; i < GAMEOBJECT_COLLISION_SAMPLE_FACTOR; i++)
		{
			int divisor = GAMEOBJECT_COLLISION_SAMPLE_FACTOR - i;
			
			GameObject go = collideList(rectangle.position.x + x / divisor, rectangle.position.y, possible);
			
			if(go != null)
			{
				if(moveCollideX(go)) break;
				else rectangle.position.x += x / divisor;
			}
			else
				rectangle.position.x += x / divisor;
		}
		
		for(int i = 0; i < GAMEOBJECT_COLLISION_SAMPLE_FACTOR; i++)
		{
			int divisor = GAMEOBJECT_COLLISION_SAMPLE_FACTOR - i;
			
			GameObject go = collideList(rectangle.position.x, rectangle.position.y + y / divisor, possible);
			
			if(go != null)
			{
				if(moveCollideY(go)) break;
				else rectangle.position.y += y / divisor;
			}
			else
				rectangle.position.y += y / divisor;
		}
	}
	
	protected void moveTo(float x, float y)
	{
		rectangle.position.x = x;
		rectangle.position.y = y;
	}
	
	protected void moveTo(float x, float y, String type)
	{
		float xDiff = x - rectangle.position.x;
		float yDiff = y - rectangle.position.y;
		
		moveBy(xDiff, yDiff, type);
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
		
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < objs.size(); i++)
		{
			GameObject go = objs.get(i);
			
			if(go != this && go.collidable && go.rectangle.collide(temp))
				return go;
		}
		
		return null;
	}
	
	protected GameObject collideTypes(float x, float y, String[] types)
	{
		List<GameObject> objs = new ArrayList<GameObject>();
		for(int i = 0; i < types.length; i++)
			state.getObjectsWithType(types[i], objs);
		
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < objs.size(); i++)
		{
			GameObject go = objs.get(i);
			
			if(go != this && go.collidable && go.rectangle.collide(temp))
				return go;
		}
		return null;
	}
	
	protected void collideInto(float x, float y, String type, List<GameObject> objs)
	{
		List<GameObject> possible = new ArrayList<GameObject>();
		state.getObjectsWithType(type, possible);
		
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < possible.size(); i++)
		{
			GameObject go = possible.get(i);
			
			if(go != this && go.collidable && go.rectangle.collide(temp))
				objs.add(go);
		}
	}
	
	protected void collideTypesInto(float x, float y, String[] types, List<GameObject> objs)
	{
		List<GameObject> possible = new ArrayList<GameObject>();
		for(int i = 0; i < types.length; i++)
			state.getObjectsWithType(types[i], possible);
		
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < possible.size(); i++)
		{
			GameObject go = possible.get(i);
			
			if(go != this && go.collidable && go.rectangle.collide(temp))
				objs.add(go);
		}
	}
	
	protected boolean collideWith(float x, float y, GameObject go)
	{
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);
		
		if(go.collidable && go.rectangle.collide(temp))
			return true;
		return false;
	}
	
	protected GameObject collideList(float x, float y, List<GameObject> objs)
	{
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);

		for(int i = 0; i < objs.size(); i++)
		{
			GameObject go = objs.get(i);
			
			if(go != this && go.collidable && go.rectangle.collide(temp))
				return go;
		}
		return null;
	}
}
