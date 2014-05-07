package com.ngsarmy.pgu.entity;

import com.ngsarmy.pgu.core.GameAssets;
import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.utils.Vector2;

public class Blocker extends GameObject
{
	private int moveDown;
	private GameObject player = null;
	
	public Blocker(Vector2 pos, int down)
	{
		super(pos, "test");
		moveDown = down;
		layer = 0;
		setHitbox(GameAssets.PGULogoImage);
	}
	
	public void update(double delta)
	{
		if(player == null)
			player = state.getObjectWithName("player");
		
		moveBy(0, 10 * (float)delta * moveDown);
		
		if(moveDown == -1 && collideWith((int)getLeft(), (int)getTop() + (int)(10 * delta * moveDown), player))
			player.moveBy(0, 10 * (float)delta * moveDown);
	}
	
	public void event(GameEvent ev)
	{
	}
	
	public void render(GameRasterizer g)
	{
		g.setColor(0x345678);
		g.renderFilledRectangle((int)getLeft(), (int)getTop(), (int)getWidth(), (int)getHeight());
	}
}
