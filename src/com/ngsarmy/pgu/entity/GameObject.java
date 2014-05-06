package com.ngsarmy.pgu.entity;

import com.ngsarmy.pgu.core.GameState;
import com.ngsarmy.pgu.utils.Rectangle;

public class GameObject 
{
	private GameState state;
	protected Rectangle rectangle;
	protected String name;
	protected String type;
	protected int layer;
	protected boolean collidable;
	
	public GameObject()
	{
	}
}
