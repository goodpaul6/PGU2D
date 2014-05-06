package com.ngsarmy.pgu.entity;

import java.awt.event.KeyEvent;

import com.ngsarmy.pgu.core.GameAssets;
import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameEventType;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.input.GameKeyboard;
import com.ngsarmy.pgu.utils.Vector2;

public class Test extends GameObject
{
	private Vector2 velocity;
	private boolean grounded = false;
	
	public Test()
	{
		super(new Vector2(100, 100), "test");
		velocity = new Vector2();
		
		name = "player";
		setHitbox(GameAssets.PGULogoImage);
	}
	
	public void update(double delta)
	{
		if(GameKeyboard.isKeyDown(KeyEvent.VK_W))
			velocity.y -= 20 * delta;
		if(GameKeyboard.isKeyDown(KeyEvent.VK_S))
			velocity.y += 20 * delta;
		if(GameKeyboard.isKeyDown(KeyEvent.VK_A))
			velocity.x -= 20 * delta;
		if(GameKeyboard.isKeyDown(KeyEvent.VK_D))
			velocity.x += 20 * delta;
		
		if(!grounded)
			velocity.y += 10 * delta;
		
		if(collide(getLeft(), getTop() + 1, "test") == null)
			grounded = false;
		
		if(getBottom() >= 480)
		{
			setBottom(480);
			if(velocity.y > 0)
				velocity.y = 0;
			grounded = true;
		}
		
		velocity.x *= 0.9f;
		
		moveBy(velocity.x, velocity.y, "test");
	}
	
	@Override
	public boolean moveCollideX(GameObject go)
	{
		if(go.getTop() < getBottom())
			return false;
		return true;
	}
	
	@Override
	public boolean moveCollideY(GameObject go)
	{
		if(go.getTop() < getBottom())
			return false;
		else if(go.getTop() + velocity.y > getBottom())
		{
			velocity.y = 0;
			grounded = true;
		}
		return true;
	}
	
	public void event(GameEvent ev)
	{
		if(grounded && ev.type == GameEventType.E_KEY_PRESSED && ev.key == KeyEvent.VK_SPACE)
			velocity.y -= 5;
	}
	
	public void render(GameRasterizer g)
	{
		g.setColor(0xffffff);
		g.renderImage(GameAssets.PGULogoImage, (int)getLeft(), (int)getTop());
	}
}
