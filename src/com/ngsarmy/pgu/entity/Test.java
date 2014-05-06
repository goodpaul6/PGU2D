package com.ngsarmy.pgu.entity;

import java.awt.event.KeyEvent;

import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameImage;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.input.GameKeyboard;
import com.ngsarmy.pgu.utils.Vector2;

public class Test extends GameObject
{
	private GameImage image;
	private Vector2 velocity;
	
	public Test()
	{
		super(new Vector2(100, 100), "test");
		image = new GameImage();
		velocity = new Vector2();
		
		if(!image.loadFromFile("res/PGULogo.png"))
			System.exit(-100);
		rectangle.size.x = image.getWidth();
		rectangle.size.y = image.getHeight();
	}
	
	public void update(double delta)
	{
		if(GameKeyboard.isKeyDown(KeyEvent.VK_W))
			velocity.y -= 20 * delta;
		if(GameKeyboard.isKeyDown(KeyEvent.VK_S))
			velocity.y += 20 * delta;
		
		velocity.x *= 0.9f;
		velocity.y *= 0.9f;
		
		moveBy(velocity.x, velocity.y, "test");
	}
	
	public void event(GameEvent ev)
	{
	}
	
	public void render(GameRasterizer g)
	{
		g.renderImage(image, (int)getLeft(), (int)getTop());
	}
}
