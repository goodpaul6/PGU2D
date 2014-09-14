package com.ngsarmy.pgu.core;

import java.util.Arrays;

/* PixelBuffer class:
 * a utility class for storing 
 * pixel data with a width 
 * and a height. It also has
 * methods for setting and 
 * getting pixel data, as
 * well as filling it
 */
public class PixelBuffer 
{
	public int width;
	public int height;
	public int[] pixels;
	
	public PixelBuffer()
	{
		this.width = 0;
		this.height = 0;
		this.pixels = new int[0];
	}
	
	public PixelBuffer(int[] pixels, int height, int width)
	{
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}
	
	public void setPixel(int x, int y, int color)
	{
		if(x < 0 || x >= width) return;
		if(y < 0 || y >= height) return;
		
		pixels[x + y * width] = color;
	}
	
	public int getPixel(int x, int y)
	{
		if(x < 0 || x >= width) return 0;
		if(y < 0 || y >= height) return 0;
		
		return pixels[x + y * width];
	}
	
	public void fill(int x, int y, int w, int h, int color)
	{
		if(x < 0 || x >= width) return;
		if(y < 0 || y >= height) return;
		if(x + w >= width || y + h >= height) return;
		
		for(int yy = 0; yy < h; yy++)
		{
			for(int xx = 0; xx < w; xx++)
				pixels[(x + xx) + (y + yy) * width] = color;
		}
	}
	
	public void fill(int color)
	{
		Arrays.fill(pixels, color);
	}
	
	public void resize(int width, int height)
	{
		if(this.width < width || this.height < height)
		{
			this.pixels = new int[width * height];
			this.width = width;
			this.height = height;
		}
	}
}
