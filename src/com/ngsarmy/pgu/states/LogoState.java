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

		for(int i = 0; i < 20; i++)
		{	
			add(new Blocker(new Vector2(128 * (i / 2) % Game.width, 200)));
			add(new Blocker(new Vector2(128 * (i / 2) % Game.width, 400)));
		}
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
		g.clearToColor(0x222333);
		super.render(g);
		debug(g);
	}
	
	public void unload()
	{
	}
}
