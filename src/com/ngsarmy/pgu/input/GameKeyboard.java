package com.ngsarmy.pgu.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.ngsarmy.pgu.core.GameEventManager;

public class GameKeyboard implements KeyListener
{
	private static boolean[] keys = new boolean[256];

	// CAUTION: Internal function
	// called automatically
	public void keyPressed(KeyEvent e) 
	{
		int kcode = e.getKeyCode();	
		if(kcode < keys.length)
			keys[e.getKeyCode()] = true;
		GameEventManager.pushKeyDownEvent(kcode);
	}

	// CAUTION: Internal function
	// called automatically
	public void keyReleased(KeyEvent e) 
	{
		int kcode = e.getKeyCode();
		if(kcode < keys.length)
			keys[e.getKeyCode()] = false;
		GameEventManager.pushKeyUpEvent(kcode);
	}

	// CAUTION: Internal function
	// called automatically
	public void keyTyped(KeyEvent e) 
	{
		int kcode = e.getKeyCode();
		if(kcode < keys.length)
			keys[e.getKeyCode()] = true;
		GameEventManager.pushKeyDownEvent(kcode);
	}
	
	// USAGE:
	// use this to check if a key is pressed down
	// the parameter passed in is expected to be
	// from the KeyEvent enum
	public static boolean isKeyDown(int key)
	{
		return keys[key];
	}
	
	// USAGE:
	// use this to check if a key is not pressed
	// the parameter passed in is expected to be
	// from the KeyEvent enum
	public static boolean isKeyUp(int key)
	{
		return !keys[key];
	}
}
