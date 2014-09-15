package com.ngsarmy.pgu.blenders;

import com.ngsarmy.pgu.core.IPixelBlender;

public class DefaultPixelBlender implements IPixelBlender
{
	private static DefaultPixelBlender instance;
	
	public DefaultPixelBlender()
	{
	}
	
	public static DefaultPixelBlender get()
	{
		if(instance == null)
			instance = new DefaultPixelBlender();
		
		return instance;
	}
	
	public int blendR(int src, int dst, int srcA, int tint)
	{
		return (int)(dst * (1 - srcA / 255f) + (src * (tint / 255f)) * (srcA / 255f));
	}

	public int blendG(int src, int dst, int srcA, int tint)
	{
		return (int)(dst * (1 - srcA / 255f) + (src * (tint / 255f)) * (srcA / 255f));
	}
	
	public int blendB(int src, int dst, int srcA, int tint)
	{
		return (int)(dst * (1 - srcA / 255f) + (src * (tint / 255f)) * (srcA / 255f));	
	}
}
