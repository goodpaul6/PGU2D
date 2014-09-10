package com.ngsarmy.pgu.core;

import java.util.ArrayList;
import java.util.List;

// GameObjectManager class:
// This class is a parent class
// of a game state. It provides 
// utilities for managing game 
// objects, such as rendering,
// finding, and deleting them
public class GameGroup 
{
	public static final int MAX_LAYER = 5;
	private GameText debugText;
	
	static enum Action
	{
		ADD_GO,
		REM_GO,
		ADD_COLLIDABLE,
		REM_COLLIDABLE
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
	private List<GameObject> collidableObjectList = new ArrayList<GameObject>();
	private List<PendingChange> objectChangeList = new ArrayList<PendingChange>();
	private List<PendingChange> collidableChangeList = new ArrayList<PendingChange>();
	
	public GameGroup()
	{
		objectList = new ArrayList<GameObject>();
		debugText = new GameText();
		
		if(!debugText.loadFont("res/PGUFont.png", 8, 8))
			System.exit(-1);
	}
	
	public void eventGroup(GameEvent ev)
	{
		for(int i = 0; i < objectList.size(); i++)
			objectList.get(i).event(ev);
		
		applyPendingChanges();
		applyCollidablePendingChanges();
	}
	
	public void updateGroup(float delta)
	{
		for(int i = 0; i < objectList.size(); i++)
			objectList.get(i).update(delta);
		
		applyPendingChanges();
		applyCollidablePendingChanges();
	}
	
	public void renderGroup(GameRasterizer g)
	{
		for(int l = 0; l <= MAX_LAYER; l++)
		{	
			for(int i = 0; i < objectList.size(); i++)
			{
				GameObject go = objectList.get(i);
				
				if(go.layer == l)
					go.render(g);
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
			debugText.setText("collidable: " + go.getCollidable());
			g.renderText(debugText, (int)go.getLeft(), (int)go.getTop() - 40);
		}
	}
	
	public void addAsCollidable(GameObject object)
	{
		collidableChangeList.add(new PendingChange(Action.ADD_COLLIDABLE, object));
	}
	
	public void removeAsCollidable(GameObject object)
	{
		collidableChangeList.add(new PendingChange(Action.REM_COLLIDABLE, object));
	}
	
	public GameObject add(GameObject object)
	{
		object.state = (GameState)this;
		collidableChangeList.add(new PendingChange(Action.ADD_COLLIDABLE, object));
		objectChangeList.add(new PendingChange(Action.ADD_GO, object));
		return object;
	}
	
	public void remove(GameObject object)
	{
		collidableChangeList.add(new PendingChange(Action.REM_COLLIDABLE, object));
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
		for(int i = 0; i < collidableObjectList.size(); i++)
		{
			GameObject go = collidableObjectList.get(i);
			if(go.name.equals(name))
				return go;
		}
		return null;
	}
	
	public void getCollidableWithType(String type, List<GameObject> objs)
	{
		for(int i = 0; i < collidableObjectList.size(); i++)
		{
			GameObject go = collidableObjectList.get(i);
			if(go.type.equals(type))
				objs.add(go);
		}
	}
	
	public void getCollidableWithLayer(int layer, List<GameObject> objs)
	{
		for(int i = 0; i < collidableObjectList.size(); i++)
		{
			GameObject go = collidableObjectList.get(i);
			if(go.layer == layer)
				objs.add(go);
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
	
	private void applyCollidablePendingChanges()
	{
		for(int i = 0; i < collidableChangeList.size(); i++)
		{
			PendingChange change = collidableChangeList.get(i);
			
			switch(change.action)
			{
			case ADD_COLLIDABLE:
				collidableObjectList.add(change.ent);
				break;
			case REM_COLLIDABLE:
				collidableObjectList.remove(change.ent);
				break;
			default:
			}
		}
		collidableChangeList.clear();
	}
}
