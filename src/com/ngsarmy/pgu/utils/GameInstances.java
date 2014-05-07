package com.ngsarmy.pgu.utils;

// GameInstances class:
// This class holds commonly used
// data structures such as a Vector2
// so that said Vector2 is not 
// created over an over again
// unnecessarily
public class GameInstances 
{
	// global vec2 instance
	public static Vector2 vec2 = new Vector2();
	// global pixel buffer instance (initially 512x512)
	// usage:
	// for buffering rotation data
	private static int[] buffer = new int[512 * 512];
	// is the buffer in use right now? because if it is, throw exception if someone allocates it/gets it again
	private static boolean bufferInUse = false;
	// current width of the buffer 
	private static int bufferWidth = 512;
	// current height of the buffer
	private static int bufferHeight = 512;
	// current user of buffer
	private static String currentUser = "default";
	
	// USAGE:
	// resizes the buffer if necessary to accomodate new data
	// and then returns it
	public static int[] allocateBuffer(int nw, int nh, String userName)
	{
		if(bufferInUse && !currentUser.equals(userName))
		{
			System.out.println("ERROR: Attempted to allocate/access game instance buffer when it was already in use!");
			System.exit(-1);
		}
		
		currentUser = userName;
		
		if(bufferWidth * bufferHeight < nw * nh)
		{
			buffer = new int[nw * nh];
			bufferWidth = nw;
			bufferHeight = nh;
		}
		
		bufferInUse = true;
		return buffer;
	}
	
	// USAGE:
	// call this when you no longer need the buffer (i.e you don't want to own it anymore)
	// it will only free ownership if the correct user tells it to
	public static void freeBufferOwnership(String userName)
	{
		if(currentUser.equals(userName))
			bufferInUse = false;
	}
	
	// USAGE:
	// call this when you want to check if the buffer is available for use
	// it will be available to the user if the userName matches the current
	// user
	public static boolean isBufferAvailable(String userName)
	{
		return (!bufferInUse || userName.equals(currentUser));
	}
	
	// USAGE:
	// allocate a buffer if it is available, but if it isnt, allocate a normal buffer
	public static int[] allocateBufferIf(String userName, int nw, int nh)
	{
		return (isBufferAvailable(userName) ? allocateBuffer(nw, nh, userName) : new int[nw * nh]);
	}
}
