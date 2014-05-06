package com.ngsarmy.pgu.entity;

import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameImage;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.utils.Vector2;

public class Blocker extends GameObject
{
	private GameImage image;
	
	public Blocker(Vector2 pos)
	{
		super(pos, "test");
		image = new GameImage();
		
		if(!image.loadFromFile("res/PGULogo.png"))
			System.exit(-100);
		rectangle.size.x = image.getWidth();
		rectangle.size.y = image.getHeight();
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
