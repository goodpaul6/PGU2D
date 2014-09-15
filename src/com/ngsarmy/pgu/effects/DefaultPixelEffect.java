package com.ngsarmy.pgu.effects;

import com.ngsarmy.pgu.core.IPixelEffect;
import com.ngsarmy.pgu.core.PixelBuffer;

public class DefaultPixelEffect implements IPixelEffect
{
	private static DefaultPixelEffect instance;
	
	public DefaultPixelEffect()
	{
	}
	
	public static DefaultPixelEffect get()
	{
		if(instance == null)
			instance = new DefaultPixelEffect();
		
		return instance;
	}
	
	public int postProcessColor(int bufX, int bufY, int actualX, int actualY, int calcR, int calcG, int calcB, int col, int smpX, int smpY, PixelBuffer input, PixelBuffer output)
	{
		output.setPixel(100, 100, 0xFF000);
		return col;
	}
}
