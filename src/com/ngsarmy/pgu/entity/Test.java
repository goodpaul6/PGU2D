package com.ngsarmy.pgu.entity;

import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameImage;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.utils.Vector2;

public class Test extends GameObject
{
	private GameImage image;
	
	public Test()
	{
		super(new Vector2(100, 100), "test");
		image = new GameImage();
		
		if(!image.loadFromFile("res/PGULogo.png"))
			System.exit(-100);
	}
	
	public void update(double delta)
	{
	}
	
	public void event(GameEvent ev)
	{
	}
	
	public void render(GameRasterizer g)
	{
		g.renderImage(image, (int)getLeft(), (int)getTop());
	}
}
