package com.ngsarmy.pgu.core;

/* GameRect class:
 * a storage class for dirty rectangles
 * i.e areas of the screen which must 
 * be cleared (much more efficient than
 * clearing all areas [when unnecessary])
 */
public class GameRect 
{
	public int x;
	public int y;
	public int w;
	public int h;
}
