package com.ngsarmy.pgu.entity;

import com.ngsarmy.pgu.core.GameAssets;
import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.utils.Vector2;

public class Blocker extends GameObject
{
	public Blocker(Vector2 pos)
	{
		super(pos, "test");
	}
	
	@Override
	public void added()
	{
		layer = 0;
		
		if((int)(Math.random() * 2) == 1)
			setCollidable(false);
		
		setHitbox(GameAssets.brickTile);
	}
	
	public void update(double delta)
	{
	}
	
	public void event(GameEvent ev)
	{
	}
	
	public void render(GameRasterizer g)
	{
		g.setColor((int)(getCollidable() ? 0xffffff : 0xffffff * Math.random()));
		GameAssets.brickTile.setAlpha(getCollidable() ? 255 : (int)(Math.random() * 255));
		g.renderImage(GameAssets.brickTile, (int)getLeft(), (int)getTop());
	}
}
