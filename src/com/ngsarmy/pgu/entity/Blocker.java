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
		name = "pretty much a mario platform";
		setHitbox(GameAssets.PGULogoImage);
	}
	
	public void update(double delta)
	{
	}
	
	public void event(GameEvent ev)
	{
	}
	
	public void render(GameRasterizer g)
	{
		g.setColor(0xffffff);
		g.renderImage(GameAssets.PGULogoImage, (int)getLeft(), (int)getTop());
	}
}
