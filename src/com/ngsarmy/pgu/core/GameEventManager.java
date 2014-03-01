package com.ngsarmy.pgu.core;

import java.util.Stack;

/* GameEventManager class:
 * a class for storing static methods necessary
 * for event-queue based event polling. This class
 * is not meant to be used directly aside from special 
 * cases.
 */
public class GameEventManager 
{
	private static Stack<GameEvent> eventQueue = new Stack<GameEvent>();
	
	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY 
	public static void pushButtonPressEvent(int button)
	{
		GameEvent ev = new GameEvent();
		ev.button = button;
		ev.type = GameEventType.E_MOUSE_BUTTON_PRESSED;
		eventQueue.push(ev);
	}

	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY
	public static void pushButtonReleaseEvent(int button)
	{
		GameEvent ev = new GameEvent();
		ev.button = button;
		ev.type = GameEventType.E_MOUSE_BUTTON_RELEASED;
		eventQueue.push(ev);
	}

	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY
	public static void pushButtonClickEvent(int button)
	{
		GameEvent ev = new GameEvent();
		ev.button = button;
		ev.type = GameEventType.E_MOUSE_CLICK;
		eventQueue.push(ev);	
	}

	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY
	public static void pushWheelEvent(int wheel)
	{
		GameEvent ev = new GameEvent();
		ev.wheel = wheel;
		ev.type = GameEventType.E_MOUSE_WHEEL_MOVED;
		eventQueue.push(ev);
	}

	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY
	public static void pushKeyDownEvent(int key)
	{
		GameEvent ev = new GameEvent();
		ev.key = key;
		ev.type = GameEventType.E_KEY_PRESSED;
		eventQueue.push(ev);
	}

	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY
	public static void pushKeyUpEvent(int key)
	{
		GameEvent ev = new GameEvent();
		ev.key = key;
		ev.type = GameEventType.E_KEY_RELEASED;
		eventQueue.push(ev);
	}

	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY
	public static void pushMoveEvent(int x, int y)
	{
		GameEvent ev = new GameEvent();
		ev.x = x;
		ev.y = y;
		ev.type = GameEventType.E_MOUSE_MOVE;
		eventQueue.push(ev);
	}

	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY
	public static void pushEnterEvent(int x, int y)
	{
		GameEvent ev = new GameEvent();
		ev.x = x;
		ev.y = y;
		ev.type = GameEventType.E_MOUSE_ENTER;
		eventQueue.push(ev);
	}

	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY
	public static void pushExitEvent(int x, int y)
	{
		GameEvent ev = new GameEvent();
		ev.x = x;
		ev.y = y;
		ev.type = GameEventType.E_MOUSE_EXIT;
		eventQueue.push(ev);
	}
	
	// USAGE:
	// clear all events from queue, useful for negating any events which occurred prior to the call
	public static void flush()
	{
		eventQueue.clear();
	}
	
	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY
	public static GameEvent latest()
	{
		if(!eventQueue.empty())		
			return eventQueue.pop();
		return null;
	}
	
	// CAUTION: COULD BE USED DIRECTLY, BUT IS ALSO CALLED INTERNALLY
	// returns a boolean representing whether the event queue is empty
	public static boolean isEmpty()
	{
		return eventQueue.empty();
	}
}
