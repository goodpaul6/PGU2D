package com.ngsarmy.pgu.core;

import java.util.ArrayList;
import java.util.List;

import com.ngsarmy.pgu.utils.Rectangle;

// GameGroup class:
// This class is a parent class
// of a game state. It provides 
// utilities for managing game 
// objects, such as rendering,
// finding, and deleting them
public class GameGroup 
{
	public static final int MAX_LAYER = 5;
	private GameText debugText;
	private Rectangle collisionBounds;
	
	static enum Action
	{
		ADD_GO,
		REM_GO
	}
	
	static class PendingChange
	{
		Action action;
		GameObject ent;
		
		public PendingChange(Action action, GameObject ent) 
		{
			this.action = action;
			this.ent = ent;
		}
	}
	
	private List<GameObject> objectList = new ArrayList<GameObject>();
	private List<PendingChange> objectChangeList = new ArrayList<PendingChange>();
	
	public GameGroup()
	{
		objectList = new ArrayList<GameObject>();
		debugText = new GameText();
		
		if(!debugText.loadFont("res/PGUFont.png", 8, 8))
			System.exit(-1);
		
		collisionBounds = null;
	}
	
	public void setCollisionBounds(float x, float y, float w, float h)
	{
		if(collisionBounds == null)
			collisionBounds = new Rectangle(x, y, w, h);
		else
		{
			collisionBounds.position.x = x;
			collisionBounds.position.y = y;
			collisionBounds.size.x = w;
			collisionBounds.size.y = h;
		}
	}
	
	public void removeCollisionBounds()
	{
		collisionBounds = null;
	}
	
	public void eventGroup(GameEvent ev)
	{
		for(int i = 0; i < objectList.size(); i++)
			objectList.get(i).event(ev);
		
		applyPendingChanges();
	}
	
	public void updateGroup(float delta)
	{
		for(int i = 0; i < objectList.size(); i++)
			objectList.get(i).update(delta);
		
		applyPendingChanges();
	}
	
	public void renderGroup(GameRasterizer g)
	{
		for(int l = 0; l <= MAX_LAYER; l++)
		{	
			for(int i = 0; i < objectList.size(); i++)
			{
				GameObject go = objectList.get(i);
				
				if(go.layer == l)
				{	
					if(Game.getViewRect().collide(go.rectangle))
					{
						GameRasterizer.currentGo = go;
						go.render(g);
					}
				}
			}
		}
	}
	
	protected void debug(GameRasterizer g)
	{
		for(int i = 0; i < objectList.size(); i++)
		{
			GameObject go = objectList.get(i);
			g.setColor(0xff0000);
			g.renderLineRectangle((int)go.getLeft(), (int)go.getTop(), (int)go.getWidth(), (int)go.getHeight());
			g.setColor(0xffffff);
			debugText.setText("name: " + go.name);
			g.renderText(debugText, (int)go.getLeft(), (int)go.getTop() - 10);
			debugText.setText("type: " + go.type);
			g.renderText(debugText, (int)go.getLeft(), (int)go.getTop() - 20);
			debugText.setText("layer: " + go.layer);
			g.renderText(debugText, (int)go.getLeft(), (int)go.getTop() - 30);
			debugText.setText("collidable: " + go.collidable);
			g.renderText(debugText, (int)go.getLeft(), (int)go.getTop() - 40);
		}
	}
	
	public GameObject add(GameObject object)
	{
		object.state = (GameState)this;
		objectChangeList.add(new PendingChange(Action.ADD_GO, object));
		return object;
	}
	
	public void remove(GameObject object)
	{
		objectChangeList.add(new PendingChange(Action.REM_GO, object));
	}
	
	public GameObject getObjectWithName(String name)
	{
		for(int i = 0; i < objectList.size(); i++)
		{
			GameObject go = objectList.get(i);
			
			if(go.name.equals(name))
				return go;
		}
		return null;
	}
	
	public void getObjectsWithType(String type, List<GameObject> objs)
	{
		for(int i = 0; i < objectList.size(); i++)
		{
			GameObject go = objectList.get(i);
			if(go.type.equals(type))
				objs.add(go);
		}
	}
	
	public void getObjectsWithLayer(int layer, List<GameObject> objs)
	{
		for(int i = 0; i < objectList.size(); i++)
		{
			GameObject go = objectList.get(i);
			if(go.layer == layer)
				objs.add(go);
		}
	}
	
	public GameObject getCollidableWithName(String name)
	{
		for(int i = 0; i < objectList.size(); i++)
		{
			GameObject go = objectList.get(i);
			
			if(collisionBounds != null)
			{
				if(collisionBounds.collide(go.rectangle))
				{
					if(go.collidable && go.name.equals(name))
						return go;
				}
			}
			else
			{
				if(go.collidable && go.name.equals(name))
					return go;
			}
		}
		return null;
	}
	
	public void getCollidableWithType(String type, List<GameObject> objs)
	{
		for(int i = 0; i < objectList.size(); i++)
		{
			GameObject go = objectList.get(i);
			
			if(collisionBounds != null)
			{
				if(collisionBounds.collide(go.rectangle))
				{
					if(go.collidable && go.type.equals(type))
						objs.add(go);
				}
			}
			else
			{
				if(go.collidable && go.type.equals(type))
					objs.add(go);
			}
				
		}
	}
	
	public void getCollidableWithLayer(int layer, List<GameObject> objs)
	{
		for(int i = 0; i < objectList.size(); i++)
		{
			GameObject go = objectList.get(i);
			if(collisionBounds != null)
			{
				if(collisionBounds.collide(go.rectangle))
				{
					if(go.collidable && go.layer == layer)
						objs.add(go);
				}
			}
			else
			{
				if(go.collidable && go.layer == layer)
					objs.add(go);
			}
		}
	}
	
	private void applyPendingChanges()
	{
		for(int i = 0; i < objectChangeList.size(); i++)
		{
			PendingChange change = objectChangeList.get(i);
			
			switch(change.action)
			{
			case ADD_GO:
				change.ent.parent = this;
				objectList.add(change.ent);
				change.ent.added();
				break;
			case REM_GO:
				objectList.remove(change.ent);
				change.ent.removed();
				break;
			default:
			}
		}
		objectChangeList.clear();
	}
}
