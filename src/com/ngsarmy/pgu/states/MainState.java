package com.ngsarmy.pgu.states;

import java.awt.event.KeyEvent;

import com.ngsarmy.pgu.core.Game;
import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameEventType;
import com.ngsarmy.pgu.core.GameObject;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.core.GameState;
import com.ngsarmy.pgu.entity.Blocker;
import com.ngsarmy.pgu.entity.Player;
import com.ngsarmy.pgu.utils.Vector2;

public class MainState extends GameState
{
	private GameObject e;
	
	public MainState(Game _game)
	{
		super(_game);

		e = new Player();
		add(e);

		for(int i = 0; i < 20; i++)
		{	
			add(new Blocker(new Vector2(32 * i, 200 + i * 5)));
			add(new Blocker(new Vector2(32 * i, 400 + i * 5)));
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
	
	public boolean update(float delta)
	{
		super.update(delta);
		return false;
	}
	
	public boolean render(GameRasterizer g)
	{
		g.clearToColor(0x222333);
		super.render(g);
		return false;
	}
	
	public void unload()
	{
	}
}
