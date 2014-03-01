package com.ngsarmy.pgu.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.ngsarmy.pgu.core.GameEventManager;

/* GameMouse class:
 * a listener for mouse events. You can poll the device in realtime using the static methods in
 * this class, or you can poll them event by event by using the event queue.
 */
public class GameMouse implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private static boolean left = false;
	private static boolean right = false;
	private static int x = 0;
	private static int y = 0;
	private static boolean focused;
	private static int wheel = 0;
	
	// USAGE:
	// gives the x position of the mouse relative to the canvas
	public static int getX() { return x; }
	// USAGE:
	// gives the y position of the mouse relative to the canvas
	public static int getY() { return y; }
	// USAGE:
	// gives the offset of the mouse wheel
	public static int getWheel() { int w = wheel; wheel = 0; return w; }
	// USAGE:
	// returns true or false depending on whether the left mouse button is pressed
	public static boolean getLeftButton() { return left; }
	// USAGE:
	// returns true or false depending on whether the right mouse button is pressed
	public static boolean getRightButton() { return right; }
	// USAGE:
	// returns true or false depending on whether the mouse is within the canvas screen
	public static boolean getFocused() { return focused; }

	
	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	public void mouseClicked(MouseEvent e)
	{
		GameEventManager.pushButtonClickEvent(e.getButton());
	}

	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	public void mouseEntered(MouseEvent e) 
	{
		focused = true;
		x = e.getX();
		y = e.getY();
		GameEventManager.pushEnterEvent(x, y);
	}

	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	public void mouseExited(MouseEvent e) 
	{
		focused = false;
		left = false;
		right = false;
		x = e.getX();
		y = e.getY();
		GameEventManager.pushExitEvent(x, y);
	}

	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	public void mousePressed(MouseEvent e)
	{
		int button = e.getButton();
		
		if(button == MouseEvent.BUTTON1) left = true;
		if(button == MouseEvent.BUTTON2) right = true;
		x = e.getX();
		y = e.getY();
		GameEventManager.pushButtonPressEvent(button);
	}

	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	public void mouseReleased(MouseEvent e)
	{
		int button = e.getButton();
		
		if(button == MouseEvent.BUTTON1) left = false;
		if(button == MouseEvent.BUTTON2) right = false;
		x = e.getX();
		y = e.getY();
		GameEventManager.pushButtonReleaseEvent(button);
	}

	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	public void mouseDragged(MouseEvent e) 
	{
		x = e.getX();
		y = e.getY();
		GameEventManager.pushMoveEvent(x, y);
	}

	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	public void mouseMoved(MouseEvent e)
	{
		x = e.getX();
		y = e.getY();
		GameEventManager.pushMoveEvent(x, y);
	}

	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		wheel = e.getWheelRotation();
		GameEventManager.pushWheelEvent(wheel);
	}
}
