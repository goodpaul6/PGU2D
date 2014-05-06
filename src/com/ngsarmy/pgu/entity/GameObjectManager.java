package com.ngsarmy.pgu.entity;

import java.util.ArrayList;
import java.util.List;

import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.core.GameState;
import com.ngsarmy.pgu.core.GameText;

public class GameObjectManager 
{
	public static final int MAX_LAYER = 5;
	private GameText debugText;
	
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
		debugText = new GameText();
		
		if(!debugText.loadFont("res/PGUFont.png", 8, 8))
			System.exit(-1);
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
		}
	}
	
	public GameObject add(GameObject object)
	{
		object.state = (GameState)this;
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
