package com.ngsarmy.pgu.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/* GameImage class:
 * used to provide easy image loading and processing.
 * Can be rendered onto screen with the rasterizer.
 */
public class GameImage 
{
	// alpha value
	private int alpha;
	
	// mask red (red color value which will not show up)
	public int maskR;
	public int maskG;
	public int maskB;
	
	// internal pixel buffer
	private PixelBuffer buffer;
	
	public GameImage()
	{
		alpha = 255;		// by default, the alpha will be 255
		// by default magenta will be the mask color
		maskR = 255; maskG = 0; maskB = 255;
		buffer = new PixelBuffer();
	}
	
	// create a game image from pixel data
	public GameImage(int[] data, int w, int h)
	{
		alpha = 255;
		maskR = 255; maskG = 0; maskB = 255;
		buffer = new PixelBuffer(data, w, h);
	}
	
	// USAGE:
	// loads image data from filename denoted by filePath, returns false if unsuccessful
	public boolean loadFromFile(String filePath)
	{
		buffer = PixelBuffer.fromFile(filePath);
		
		if(buffer != null)
			return true;
		return false;
	}
	
	// USAGE: 
	// saves the data of an image to a file (i.e for screenshots), returns false if unsuccessful
	public boolean saveToFile(String filePath)
	{
		BufferedImage image = null;
		
		try
		{
			image = new BufferedImage(buffer.width, buffer.height, BufferedImage.TYPE_INT_RGB);
			image.setRGB(0, 0, buffer.width, buffer.height, buffer.pixels, 0, buffer.width);
			ImageIO.write(image, "png", new File(filePath));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// USAGE:
	// get the data buffer for a sub region within the image
	public int[] getRegionData(int sx, int sy, int sw, int sh)
	{
		int[] spixels = new int[sw * sh];
		
		for(int y = 0; y < sh; y++)
		{
			int iY = sy + y;
			if(iY >= buffer.width || iY < 0) continue;
			
			for(int x = 0; x < sw; x++)
			{
				int iX = sx + x;
				if(iX >= buffer.height || iX < 0) continue;
				
				int col = buffer.pixels[iX + iY * buffer.width];
				spixels[x + y * sw] = col;
			}
		}
		
		return spixels;
	}
	
	// USAGE:
	// get the pixel buffer for the image
	public PixelBuffer getBuffer()
	{
		return buffer;
	}

	// USAGE:
	// get the alpha color of the image
	public int getAlpha() 
	{
		return alpha;
	}
	
	public void setAlpha(int alpha) 
	{
		this.alpha = alpha;
	}
	
	public int getWidth() 
	{
		return buffer.width;
	}

	public int getHeight() 
	{
		return buffer.height;
	}
}
