package com.ngsarmy.pgu.states;

import java.awt.event.KeyEvent;

import com.ngsarmy.pgu.core.Game;
import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameEventType;
import com.ngsarmy.pgu.core.GameImage;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.core.GameState;
import com.ngsarmy.pgu.input.GameKeyboard;
import com.ngsarmy.pgu.utils.GameTimer;

public class LogoState extends GameState
{
	GameImage image;
	GameTimer timer;
	
	boolean visible = false;
	
	int x = 0;
	int y = 0;
	
	public LogoState(Game _game)
	{
		super(_game);
		timer = new GameTimer();
		image = new GameImage();
		if(!image.loadFromFile("res/PGULogo.png"))
			System.exit(-1);
		image.rotate(90);
		image.setAlpha(100);
		visible = true;
	}

	public void event(GameEvent ev)
	{
		if(ev.type == GameEventType.E_MOUSE_CLICK)
			visible = !visible;
	}
	
	public void load()
	{
	}
	
	public void update(double delta)
	{
		if(GameKeyboard.isKeyDown(KeyEvent.VK_A))
			x -= (int)(100 * delta);
		if(GameKeyboard.isKeyDown(KeyEvent.VK_S))
			y += (int)(100 * delta);
		if(GameKeyboard.isKeyDown(KeyEvent.VK_D))
			x += (int)(100 * delta);
		if(GameKeyboard.isKeyDown(KeyEvent.VK_W))
			y -= (int)(100 * delta);
	}
	
	public void render(GameRasterizer g)
	{
		if(visible)
			for(int i = 0; i < 20000; i++)
				g.renderImage(image, x + i, y);
	}
	
	public void unload()
	{
	}
}
