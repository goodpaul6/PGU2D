package com.ngsarmy.pgu.entity;

import java.awt.event.KeyEvent;

import com.ngsarmy.pgu.core.Game;
import com.ngsarmy.pgu.core.GameAssets;
import com.ngsarmy.pgu.core.GameEvent;
import com.ngsarmy.pgu.core.GameEventType;
import com.ngsarmy.pgu.core.GameObjectExt;
import com.ngsarmy.pgu.core.GameRasterizer;
import com.ngsarmy.pgu.graphicutils.Animation;
import com.ngsarmy.pgu.graphicutils.Animator;
import com.ngsarmy.pgu.input.GameKeyboard;
import com.ngsarmy.pgu.utils.Vector2;

public class Player extends GameObjectExt
{
	private Animator anim;
	
	public Player()
	{
		super(new Vector2(100, 100), "test");
		anim = new Animator();
		anim.add("alphabet", new Animation(GameAssets.PGUFontText.getFontImage(), 8, 8, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25}, 2));
		name = "player";
		layer = 1;
		setHitbox(8, 8);
		collisionTypes = new String[]{"test"};
		frictionFactor.x = 0.9f;
		acceleration.y = 10.0f;
		collisionSamples = 5;
	}
	
	public void update(float delta)
	{
		acceleration.x = 0;
		anim.update(delta);
		if(GameKeyboard.isKeyDown(KeyEvent.VK_W))
			velocity.y -= 20 * delta;
		if(GameKeyboard.isKeyDown(KeyEvent.VK_S))
			velocity.y += 20 * delta;
		
		if(GameKeyboard.isKeyDown(KeyEvent.VK_A))
			acceleration.x = -40;
		if(GameKeyboard.isKeyDown(KeyEvent.VK_D))
			acceleration.x = 40;
		
		if(getBottom() >= 480)
		{
			setBottom(480);
			if(velocity.y > 0)
				velocity.y = 0;
		}
		
		super.update(delta);
	}
	
	public void event(GameEvent ev)
	{
		if(ev.type == GameEventType.E_KEY_PRESSED && ev.key == KeyEvent.VK_SPACE)
		{
			if(isTouchingBottom())
			{	
				impulse(0, -5);
				touching.moveBy(0, 5);
			}
		}
	}
	
	public void render(GameRasterizer g)
	{
		g.setColor(0xffffff);
		g.xOffset = (int)getLeft() - Game.width / 2;
		g.yOffset = (int)getTop() - Game.height / 2;
		g.renderAnimator(anim, (int)getLeft(), (int)getTop());
	}
}
