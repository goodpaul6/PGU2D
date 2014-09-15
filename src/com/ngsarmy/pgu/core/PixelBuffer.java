package com.ngsarmy.pgu.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.ngsarmy.pgu.utils.GameInstances;

/* PixelBuffer class:
 * a utility class for storing 
 * pixel data with a width 
 * and a height. It also has
 * methods for setting and 
 * getting pixel data, as
 * well as filling it. 
 * It also provides loading
 * and caching capabilities,
 * so you don't end up loading
 * the same image twice
 */
public class PixelBuffer 
{
	private static HashMap<String, PixelBuffer> bufferCache = new HashMap<String, PixelBuffer>();
	
	public int width;
	public int height;
	public int[] pixels;
	
	public static PixelBuffer fromFile(String path)
	{
		if(bufferCache.containsKey(path))
			return bufferCache.get(path);
		
		PixelBuffer buffer = new PixelBuffer();
		BufferedImage img;
		if(Game.inIde)
		{
			try
			{
				img = ImageIO.read(new File("res/" + path));
			}
			catch(IOException e)
			{
				e.printStackTrace();
				System.exit(-1);
				return null;
			}
		}
		else
		{
			try
			{
				img = ImageIO.read(PixelBuffer.class.getClassLoader().getResource("./" + path));
			}
			catch(IOException e)
			{
				e.printStackTrace();
				System.exit(-1);
				return null;
			}
		}

		buffer.resize(img.getWidth(), img.getHeight());
		img.getRGB(0, 0, buffer.width, buffer.height, buffer.pixels, 0, buffer.width);
		bufferCache.put(path, buffer);
		return buffer;
	}
	
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
	
	// USAGE:
	// scales buffer to given size using the (fastest) scaling algorithm (nearest neighbor)
	// it should be sufficient for pretty much all 2d texture scaling (but may cause jagged edges)
	public void scale(int nw, int nh)
	{
		int[] temp = GameInstances.allocateGeneralBuffer(nw * nh);
		
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
	// rotates buffer to desired rotation using the (fastest) rotation algorithm (nearest neighbor)
	public void rotate(double angle)
	{
		int[] npixels = new int[(width * 2) * (height * 2)];
 		rotateNearestNeigbour(angle, pixels, width, height, npixels, width * 2, height * 2);
 		pixels = new int[(width * 2) * (height * 2)];
 		width *= 2;
 		height *= 2;
 		for(int i = 0; i < pixels.length; i++)
 			pixels[i] = npixels[i];
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
