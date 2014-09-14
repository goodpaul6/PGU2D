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
	
	// mask (color which will not show up)
	private int mask;
	
	// raw pixel data
	private int[] pixels;
	
	// width and height
	int width;
	int height;
	
	public GameImage()
	{
		alpha = 255;		// by default, the alpha will be 255
		mask = 0xff00ff;	// by default magenta will be the mask color
	}
	
	// create a game image from pixel data
	public GameImage(int[] data, int w, int h)
	{
		alpha = 255;
		mask = 0xff00ff;
		pixels = data;
		width = w;
		height = h;
	}
	
	// USAGE:
	// loads image data from filename denoted by filepath, returns false if unsuccessful
	public boolean loadFromFile(String filePath)
	{
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(new File(filePath));

			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// USAGE:
	// loads image data from a stream which is what GameAssets uses in it's underlying system
	public boolean loadFromStream(InputStream stream)
	{
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(stream);

			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// USAGE:
	// loads image data from a url stream which is what GameAssets uses in it's underlying system
	public boolean loadFromStream(URL stream)
	{
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(stream);

			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// USAGE: 
	// saves the data of an image to a file (i.e for screenshots), returns false if unsuccessful
	public boolean saveToFile(String filePath)
	{
		BufferedImage image = null;
		
		try
		{
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			image.setRGB(0, 0, width, height, pixels, 0, width);
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
			if(iY >= width || iY < 0) continue;
			
			for(int x = 0; x < sw; x++)
			{
				int iX = sx + x;
				if(iX >= height || iX < 0) continue;
				
				int col = pixels[iX + iY * width];
				spixels[x + y * sw] = col;
			}
		}
		
		return spixels;
	}
	
	// USAGE:
	// get the data buffer for the image
	public int[] getData()
	{
		return pixels;
	}

	// USAGE:
	// get the alpha color of the image
	public int getAlpha() 
	{
		return alpha;
	}
	
	// USAGE:
	// scales image to given size using the (fastest) scaling algorithm (nearest neighbor)
	// it should be sufficient for pretty much all 2d texture scaling (but may cause jagged edges)
	// NOTE: returns an instance of itself for chaining image changes
	public GameImage scale(int nw, int nh)
	{
		int[] temp = new int[nw * nh];
		
	    double x_ratio = width/(double)nw;
	    double y_ratio = height/(double)nh;
	    
	    double px, py;
	    
	    for (int i = 0; i < nh; i++) 
	    {
	        for (int j = 0; j < nw; j++) 
	        {
	            px = Math.floor(j * x_ratio);
	            py = Math.floor(i * y_ratio);
	            temp[(i * nw) + j] = pixels[(int)((py * width) + px)] ;
	        }
	    }
	    pixels = new int[nw * nh];
	    for(int i = 0; i < temp.length; i++)
	    	pixels[i] = temp[i];
	    width = nw;
	    height = nh;
	    
	    return this;
	}
	
	// INTERNAL FUNCTION:
	// implementation of the rotation algorithm
	public static void rotateNearestNeigbour(double angle, int srcPixels[], int srcWidth, int srcHeight, int dstPixels[], int dstWidth, int dstHeight) 
	{
       for(int y_dst_pos=0; y_dst_pos<dstHeight; y_dst_pos++)
       {
           for(int x_dst_pos=0; x_dst_pos<dstWidth; x_dst_pos++)
           {
               int x_src = (int)(  (x_dst_pos - dstWidth/2) * Math.cos(Math.PI/180*angle) + (y_dst_pos - dstHeight/2) * Math.sin(Math.PI/180*angle) + srcWidth/2);
               int y_src = (int)( -(x_dst_pos - dstWidth/2) * Math.sin(Math.PI/180*angle) + (y_dst_pos - dstHeight/2) * Math.cos(Math.PI/180*angle) + srcHeight/2);
 
               if(x_src < srcWidth && x_src > 0 && y_src < srcHeight && y_src > 0)
               {
                   dstPixels[y_dst_pos*dstWidth+x_dst_pos] = srcPixels[y_src*srcWidth+x_src];
               }
           }
       }
	}

	// USAGE:
	// rotates image to desired rotation using the (fastest) rotation algorithm (nearest neighbor)
	// NOTE: returns an instance of this for chaining operations
	public GameImage rotate(double angle)
	{
		int[] npixels = new int[(width * 2) * (height * 2)];
 		rotateNearestNeigbour(angle, pixels, width, height, npixels, width * 2, height * 2);
 		pixels = new int[(width * 2) * (height * 2)];
 		width *= 2;
 		height *= 2;
 		for(int i = 0; i < pixels.length; i++)
 			pixels[i] = npixels[i];
 		
 		return this;
	}
	
	public void setAlpha(int alpha) 
	{
		this.alpha = alpha;
	}
	
	public int getMask()
	{
		return mask;
	}
	
	public void setMask(int mask)
	{
		this.mask = mask;
	}
	
	public int getWidth() 
	{
		return width;
	}

	public int getHeight() 
	{
		return height;
	}
}
