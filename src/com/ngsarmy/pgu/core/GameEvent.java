package com.ngsarmy.pgu.core;

/* GameEvent class:
 * a storage container for an event.
 * You must use event.type to verify the type of the
 * event before you access any values, as they could
 * be undefined.
 */
public class GameEvent 
{
	public int button;
	public int key;
	public int x;
	public int y;
	public int wheel;
	public GameEventType type;
}
