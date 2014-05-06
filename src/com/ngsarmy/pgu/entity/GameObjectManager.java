package com.ngsarmy.pgu.entity;

import java.util.ArrayList;
import java.util.List;

import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameRasterizer;

public class GameObjectManager 
{
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
	private List<PendingChange> pendingChangeList = new ArrayList<PendingChange>();
	
	public GameObjectManager()
	{
		objectList = new ArrayList<GameObject>();
	}
	
	public void event(GameEvent ev)
	{
		for(int i = 0; i < objectList.size(); i++)
			objectList.get(i).event(ev);
		
		applyPendingChanges();
	}
	
	public void update(double delta)
	{
		for(int i = 0; i < objectList.size(); i++)
			objectList.get(i).update(delta);
		
		applyPendingChanges();
	}
	
	public void render(GameRasterizer g)
	{
		for(int i = 0; i < objectList.size(); i++)
			objectList.get(i).render(g);
	}
	
	public GameObject add(GameObject object)
	{
		pendingChangeList.add(new PendingChange(Action.ADD_GO, object));
		return object;
	}
	
	public void remove(GameObject object)
	{
		pendingChangeList.add(new PendingChange(Action.REM_GO, object));
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
	
	private void applyPendingChanges()
	{
		for(int i = 0; i < pendingChangeList.size(); i++)
		{
			PendingChange change = pendingChangeList.get(i);
			
			switch(change.action)
			{
			case ADD_GO:
				objectList.add(change.ent);
				break;
			case REM_GO:
				objectList.remove(change.ent);
				break;
			}
		}
		pendingChangeList.clear();
	}
}
