package com.ngsarmy.pgu.entity;

import java.awt.event.KeyEvent;

import com.ngsarmy.pgu.core.GameAssets;
import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameEventType;
import com.ngsarmy.pgu.core.GameObjectExt;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.utils.Vector2;

public class Blocker extends GameObjectExt
{
	public Blocker(Vector2 pos)
	{
		super(pos, "test");
		frictionFactor.x = 0.9f;
		frictionFactor.y = 0.9f;
		collisionTypes = new String[]{"test"};
		layer = 0;
		setHitbox(GameAssets.loadImage("BrickTile.png"));
	}
	
	public void update(float delta)
	{
		super.update(delta);
	}
	
	public void event(GameEvent ev)
	{
		if(ev.type == GameEventType.E_KEY_PRESSED && ev.key == KeyEvent.VK_Q)
			collidable = !collidable;
	}
	
	public void render(GameRasterizer g)
	{
		g.setColor((int)(collidable ? 0xffffff : 0xffffff * Math.random()));
		GameAssets.loadImage("BrickTile.png").setAlpha(collidable ? 255 : (int)(Math.random() * 255));
		g.renderImage(GameAssets.loadImage("BrickTile.png"), getLeftI(), getTopI());
	}
}
