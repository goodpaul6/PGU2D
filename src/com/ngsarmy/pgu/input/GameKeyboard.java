package com.ngsarmy.pgu.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.ngsarmy.pgu.core.GameEventManager;

public class GameKeyboard implements KeyListener
{
	private static boolean[] keys = new boolean[256];
	
	public void keyPressed(KeyEvent e) 
	{
		int kcode = e.getKeyCode();	
		if(kcode < keys.length)
			keys[e.getKeyCode()] = true;
		GameEventManager.pushKeyDownEvent(kcode);
	}

	public void keyReleased(KeyEvent e) 
	{
		int kcode = e.getKeyCode();
		if(kcode < keys.length)
			keys[e.getKeyCode()] = false;
		GameEventManager.pushKeyUpEvent(kcode);
	}

	public void keyTyped(KeyEvent e) 
	{
		int kcode = e.getKeyCode();
		if(kcode < keys.length)
			keys[e.getKeyCode()] = true;
		GameEventManager.pushKeyDownEvent(kcode);
	}
	
	public static boolean isKeyDown(int key)
	{
		return keys[key];
	}
	
	public static boolean isKeyUp(int key)
	{
		return !keys[key];
	}
}
