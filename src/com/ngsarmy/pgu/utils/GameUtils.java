package com.ngsarmy.pgu.utils;

import com.ngsarmy.pgu.core.GameColor;

public class GameUtils 
{
	// USAGE:
	// encodes color within integer value
	public static int mapRgb(int Red, int Green, int Blue)
	{
		int rgb = Red;
		rgb = (rgb << 8) + Green;
		rgb = (rgb << 8) + Blue;
		
		return rgb;
	}
	
	// USAGE:
	// gives r,g,b components of a hex color value
	public static GameColor mapHex(int hex)
	{
		GameColor color = new GameColor();
		color.r = (hex >> 16) & 0xff;
		color.g = (hex >>  8) & 0xff;
		color.b = (hex      ) & 0xff;
		
		return color;
	}
	
	// USAGE:
	// encodes color within GameColor value
	public static int mapRgb(GameColor col)
	{
		int Red = col.r;
		int Green = col.g;
		int Blue = col.b;
		
		Red = (Red << 16) & 0x00FF00; //Shift red 16-bits and mask out other stuff
	    Green = (Green << 8) & 0x0000FF; //Shift Green 8-bits and mask out other stuff
	    Blue = Blue & 0x0000FF; //Mask out anything not blue.

	    return 0xFF0000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
	}
	
	// USAGE:
	// decomposes the (x) [where x = 0 is red, x = 1 is green, x = 2 is blue) color value out of a hex color encoded 0xffffff
	public static int decomposeRgb(int rgbHex, int x)
	{
		int actual = 2 - x;
		
		return (rgbHex >> actual * 8) & 0xff;
	}
}
