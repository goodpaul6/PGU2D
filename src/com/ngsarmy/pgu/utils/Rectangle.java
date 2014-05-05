package com.ngsarmy.pgu.utils;

// Rectangle class:
// a helper class for representing
// hitboxes
public class Rectangle 
{
	public Vector2 position;
	public Vector2 size;
	
	public Rectangle()
	{
		position = new Vector2();
		size = new Vector2();
	}
	
	public Rectangle(float w, float h)
	{
		position = new Vector2();
		size = new Vector2(w, h);
	}
	
	public Rectangle(float x, float y, float w, float h)
	{
		position = new Vector2(x, y);
		size = new Vector2(w, h);
	}
	
	public boolean collide(Rectangle other)
	{
		if(position.x + size.x > other.position.x &&
			position.y + size.y > other.position.y &&
			other.position.x + other.size.x > position.x &&
			other.position.y + other.size.y > position.y)
			return true;
		return false;
	}
	
	public boolean contains(float x, float y)
	{
		if(x > position.x && y > position.y &&
			x < position.x + size.x && y > position.y + size.y)
			return true;
		return false;
	}
	
	public boolean contains(Vector2 point)
	{
		return contains(point.x, point.y);
	}
}
