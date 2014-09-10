package com.ngsarmy.pgu.utils;

import java.util.HashMap;

// GameInstances class:
// This class holds commonly used
// data structures such as a Vector2
// so that said Vector2 is not 
// created over an over again
// unnecessarily
public class GameInstances 
{
	// global pixel buffer hash
	// USAGE:
	// for buffering misc. integer data (usually pixels)
	// could be music data, file data, etc
	// use the provided methods to use this
	private static HashMap<String, int[]> buffers = new HashMap<String, int[]>();
	
	// default buffer owner
	// used by allocateGeneralBuffer to allocate a buffer
	// which is ownership agnostic - this is the id for a 
	// general buffer which could have been used by anyone
	// at any point.
	private static final String DEFAULT_BUFFER_OWNER = "_default_buffer_owner_";
	
	// USAGE:
	// use this function to allocate a buffer which you wish to maintain
	// or prevent write access to by certain functions. I.e you wish to have
	// a buffer for level data and you dont' want it to be written over
	// unless the level class writes over it, you simply allocate the buffer
	// passing a unique id such as "level data" in, followed by the length
	// and receive a new buffer if no previously allocated one existed, 
	// or an old one (reallocated if the length required was larger than
	// was previously accommodated)
	public static int[] allocateOwnedBuffer(String owner, int len)
	{
		if(buffers.containsKey(owner))
		{
			int[] prevBuf = buffers.get(owner);
			if(prevBuf.length >= len)
				return prevBuf;
			else
			{
				int[] extBuf = new int[len];
				buffers.put(owner, extBuf);
				return extBuf;
			}
		}
		
		int[] buf = new int[len];
		buffers.put(owner, buf);
		return buf;
	}
	
	// USAGE:
	// use this to allocate a buffer for general use
	// for example, just to buffer some data for image
	// rotation or other such expensive procedures
	public static int[] allocateGeneralBuffer(int len)
	{
		return allocateOwnedBuffer(DEFAULT_BUFFER_OWNER, len);
	}
}
