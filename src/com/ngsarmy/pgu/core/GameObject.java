package com.ngsarmy.pgu.core;

import java.util.ArrayList;
import java.util.List;

import com.ngsarmy.pgu.utils.Rectangle;
import com.ngsarmy.pgu.utils.Vector2;

public class GameObject 
{
	public int collisionSamples = 1;
	public GameState state;
	public GameGroup parent;
	public Rectangle rectangle;
	public String name;
	public String type;
	public int layer;
	public boolean collidable;
	
	private ArrayList<GameObject> cachedObjectList;
	
	public GameObject(Vector2 position, String type)
	{
		rectangle = new Rectangle(position.x, position.y, 0, 0);
		name = "default";
		this.type = type;
		layer = 0;
		collidable = true;
		parent = null;
		cachedObjectList = new ArrayList<GameObject>();
	}
	
	public Vector2 getCenter()
	{
		return new Vector2(rectangle.position.x + rectangle.size.x / 2, rectangle.position.y + rectangle.size.y / 2);
	}

	public void event(GameEvent ev)
	{
	}
	
	public void update(float delta)
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
	
	public int getLeftI()
	{
		return (int)rectangle.position.x;
	}
	
	public int getTopI()
	{
		return (int)rectangle.position.y;
	}
	
	public int getRightI()
	{
		return (int)(rectangle.position.x + rectangle.size.x);
	}
	
	public int getBottomI()
	{
		return (int)(rectangle.position.y + rectangle.size.y);
	}
	
	public float getWidth()
	{
		return rectangle.size.x;
	}
	
	public float getHeight()
	{
		return rectangle.size.y;
	}
	
	public int getWidthI()
	{
		return (int)rectangle.size.x;
	}
	
	public int getHeightI()
	{
		return (int)rectangle.size.y;
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
	
	public void moveBy(float x, float y)
	{
		rectangle.position.x += x;
		rectangle.position.y += y;
	}
	
	private void sampledMove(float x, float y, List<GameObject> possible)
	{
		float xAmt = x / collisionSamples;
		float yAmt = y / collisionSamples;
		
		for(int i = 0; i < collisionSamples; i++)
		{
			GameObject go = collideList(rectangle.position.x + xAmt, rectangle.position.y, possible);
			
			if(go != null)
			{
				if(moveCollideX(go)) break;
				else rectangle.position.x += xAmt;
			}
			else
				rectangle.position.x += xAmt;
		}
		
		for(int i = 0; i < collisionSamples; i++)
		{	
			GameObject go = collideList(rectangle.position.x, rectangle.position.y + yAmt, possible);
			
			if(go != null)
			{
				if(moveCollideY(go)) break;
				else rectangle.position.y += yAmt;
			}
			else
				rectangle.position.y += yAmt;
		}
	}
	
	public void moveBy(float x, float y, String type)
	{
		if(collidable)
		{
			cachedObjectList.clear();
			state.getCollidableWithType(type, cachedObjectList);
		
			sampledMove(x, y, cachedObjectList);
		}
		else
			moveBy(x, y);
	}
	
	public void moveBy(float x, float y, String[] types)
	{	
		if(collidable && types.length > 0)
		{
			cachedObjectList.clear();
			for(int i = 0; i < types.length; i++)
				state.getCollidableWithType(types[i], cachedObjectList);	
			
			sampledMove(x, y, cachedObjectList);
		}
		else
			moveBy(x, y);
			
	}
	
	public void moveTo(float x, float y)
	{
		rectangle.position.x = x;
		rectangle.position.y = y;
	}
	
	public void moveTo(float x, float y, String type)
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
	
	public void added()
	{
	}
	
	public void removed()
	{
	}

	public GameObject collide(float x, float y, String type)
	{
		cachedObjectList.clear();
		state.getCollidableWithType(type, cachedObjectList);
		
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < cachedObjectList.size(); i++)
		{
			GameObject go = cachedObjectList.get(i);
			
			if(go != this && go.collidable && go.rectangle.collide(temp))
				return go;
		}
		
		return null;
	}
	
	public GameObject collideTypes(float x, float y, String[] types)
	{
		cachedObjectList.clear();
		for(int i = 0; i < types.length; i++)
			state.getCollidableWithType(types[i], cachedObjectList);
		
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < cachedObjectList.size(); i++)
		{
			GameObject go = cachedObjectList.get(i);
			
			if(go != this && go.collidable && go.rectangle.collide(temp))
				return go;
		}
		return null;
	}
	
	public void collideInto(float x, float y, String type, List<GameObject> objs)
	{
		cachedObjectList.clear();
		state.getCollidableWithType(type, cachedObjectList);
		
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < cachedObjectList.size(); i++)
		{
			GameObject go = cachedObjectList.get(i);
			
			if(go != this && go.collidable && go.rectangle.collide(temp))
				objs.add(go);
		}
	}
	
	public void collideTypesInto(float x, float y, String[] types, List<GameObject> objs)
	{
		cachedObjectList.clear();
		for(int i = 0; i < types.length; i++)
			state.getCollidableWithType(types[i], cachedObjectList);
		
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);
		
		for(int i = 0; i < cachedObjectList.size(); i++)
		{
			GameObject go = cachedObjectList.get(i);
			
			if(go != this && go.collidable && go.rectangle.collide(temp))
				objs.add(go);
		}
	}
	
	public boolean collideWith(float x, float y, GameObject go)
	{
		Rectangle temp = new Rectangle(x, y, rectangle.size.x, rectangle.size.y);
		
		if(go.collidable && go.rectangle.collide(temp))
			return true;
		return false;
	}
	
	public GameObject collideList(float x, float y, List<GameObject> objs)
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
