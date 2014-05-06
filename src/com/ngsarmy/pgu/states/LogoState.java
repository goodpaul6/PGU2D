package com.ngsarmy.pgu.states;

import java.awt.event.KeyEvent;

import com.ngsarmy.pgu.core.Game;
import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameEventType;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.core.GameState;
import com.ngsarmy.pgu.entity.Blocker;
import com.ngsarmy.pgu.entity.GameObject;
import com.ngsarmy.pgu.entity.Test;
import com.ngsarmy.pgu.utils.Vector2;

public class LogoState extends GameState
{
	private GameObject e;
	
	public LogoState(Game _game)
	{
		super(_game);

		e = new Test();
		add(e);
		add(new Blocker(new Vector2(100, 50)));
		add(new Blocker(new Vector2(100, 300)));
		add(new Blocker(new Vector2(400, 200)));
		add(new Blocker(new Vector2(200, 200)));
	}

	public void event(GameEvent ev)
	{
		super.event(ev);
		
		if(ev.type == GameEventType.E_KEY_PRESSED)
		{
			if(ev.key == KeyEvent.VK_R)
				remove(e);
		}
	}
	
	public void load()
	{
	}
	
	public void update(double delta)
	{
		super.update(delta);
	}
	
	public void render(GameRasterizer g)
	{
		super.render(g);
		debug(g);
	}
	
	public void unload()
	{
	}
}
