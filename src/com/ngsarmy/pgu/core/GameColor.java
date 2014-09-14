package com.ngsarmy.pgu.core;

import java.util.ArrayList;

/* GameColor class:
 * this is a data storage class with no methods, it just stores
 * public r, g, and b integers
 */
public class GameColor 
{
	private static ArrayList<GameColor> inUse = new ArrayList<GameColor>();
	private static ArrayList<GameColor> disposed = new ArrayList<GameColor>();
	
	public int r;
	public int g;
	public int b;
	
	public static GameColor get()
	{
		if(disposed.size() > 0)
		{
			GameColor c =  disposed.remove(0);
			c.r = c.g = c.b = 0;
			return c;
		}
		GameColor c = new GameColor(0, 0, 0);
		inUse.add(c);
		return c;
	}
	
	public GameColor(int r, int g, int b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void add(int r, int g, int b)
	{
		this.r += r;
		this.g += g;
		this.b += b;
	}
	
	public void div(int v)
	{
		this.r /= v;
		this.g /= v;
		this.b /= v;
	}
	
	public void dispose()
	{
		inUse.remove(this);
		disposed.add(this);
	}
}
