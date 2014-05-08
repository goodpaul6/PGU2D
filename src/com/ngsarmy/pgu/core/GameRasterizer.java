package com.ngsarmy.pgu.core;

import java.util.Arrays;

import com.ngsarmy.pgu.graphicutils.Animation;
import com.ngsarmy.pgu.graphicutils.Animator;
import com.ngsarmy.pgu.utils.GameInstances;
import com.ngsarmy.pgu.utils.GameUtils;

/* GameRasterizer Class:
 * an internal class used by game canvas to
 * draw to its raw buffer. You should not have
 * to use this unless you want to add some 3d
 * functionality.
 */
public class GameRasterizer 
{
	// unscaled width and height
	private int width;
	private int height;
	
	// last angle passed in as an arugment to rotated render method
	private double lastAngle = 0;
	
	// publicly accessible pixel array
	public int[] pixels;
	
	// current color
	private int color;
	
	// x render offset
	public int xOffset = 0;
	// y render offset
	public int yOffset = 0;
	
	public GameRasterizer(int _width, int _height)
	{
		width = _width;
		height = _height;
		
		pixels = new int[width * height];
		color = 0;
	}
	
	public void setColor(int _color)
	{
		color = _color;
	}
	
	public void clear()
	{
		clearToColor(color);
	}
	
	public void clearToColor(int _color)
	{
		Arrays.fill(pixels, _color);
	}
	
	public void renderText(GameText text, int _x, int _y)
	{
		GameImage img = text.getFontImage();
		int[] charData = text.getCharData();
		
		for(int i = 0; i < charData.length; i++)
		{
			int idx = charData[i];
			
			int tu = idx % (img.getWidth() / text.getCharWidth());
			int tv = idx / (img.getWidth() / text.getCharWidth());
			
			renderSubImage(img, _x + i * text.getCharWidth(), _y, tu * text.getCharWidth(), tv * text.getCharHeight(), text.getCharWidth(), text.getCharHeight());
		}
	}
	
	public void renderSubImage(GameImage img, int _x, int _y, int _sx, int _sy, int _w, int _h)
	{
		if(_x + _w < xOffset) return;
		if(_y + _h < yOffset) return;
		if(_x >= xOffset + width) return;
		if(_y >= yOffset + height) return;
		
		renderBuffer(img.getData(), _sx, _sy, _x, _y, _w, _h, img.getAlpha(), GameUtils.mapHex(img.getMask()), img.getWidth(), img.getHeight(), 0);
	}
	
	public void renderSubImage(GameImage img, int _x, int _y, int _sx, int _sy, int _w, int _h, double angle)
	{
		if(_x + _w < xOffset) return;
		if(_y + _h < yOffset) return;
		if(_x >= xOffset + width) return;
		if(_y >= yOffset + height) return;
		
		if(lastAngle == angle && GameInstances.isBufferAvailable("renderSubImage"))
		{
			double sin = Math.abs(Math.sin(angle));
			double cos = Math.abs(Math.cos(angle));
			int nw = (int)Math.floor(cos * _w + sin * _h);
			int nh = (int)Math.floor(sin * _w + cos * _h);
			int[] data = GameInstances.allocateBufferIf("renderSubImage", nw, nh);
			renderBuffer(data, 0, 0, _x, _y, nw, nh, img.getAlpha(), GameUtils.mapHex(img.getMask()), nw, nh, angle);
		}
		
		if(angle != 0 && angle != 360)
		{
			lastAngle = angle;
			double sin = Math.abs(Math.sin(angle));
			double cos = Math.abs(Math.cos(angle));
			int nw, nh;
			int w = _w, h = _h;
			nw = (int)Math.floor(cos * w + sin * h);
			nh = (int)Math.floor(sin * w + cos * h);
			int[] data = GameInstances.allocateBufferIf("renderSubImage", nw, nh);
			
			GameImage.rotateNearestNeigbour(angle, img.getRegionData(_sx, _sy, _w, _h), _w, _h, data, nw, nh);
			renderBuffer(data, 0, 0, _x, _y, nw, nh, img.getAlpha(), GameUtils.mapHex(img.getMask()), nw, nh, angle);
			GameInstances.freeBufferOwnership("renderSubImage");
		}
		else
			renderBuffer(img.getData(), _sx, _sy, _x, _y, _w, _h, img.getAlpha(), GameUtils.mapHex(img.getMask()), img.getWidth(), img.getHeight(), 0);
	}
	
	public void renderImage(GameImage img, int _x, int _y)
	{	
		renderBuffer(img.getData(), 0, 0, _x, _y, img.getWidth(), img.getHeight(), img.getAlpha(), GameUtils.mapHex(img.getMask()), img.getWidth(), img.getHeight(), 0);
	}
	
	public void renderImage(GameImage img, int _x, int _y, double angle)
	{	
		if(_x + img.getWidth() < xOffset) return;
		if(_y + img.getHeight() < yOffset) return;
		if(_x >= xOffset + width) return;
		if(_y >= yOffset + height) return;
		
		if(lastAngle == angle && GameInstances.isBufferAvailable("renderImage"))
		{
			double sin = Math.abs(Math.sin(angle));
			double cos = Math.abs(Math.cos(angle));
			int nw = (int)Math.floor(cos * img.getWidth() + sin * img.getHeight());
			int nh = (int)Math.floor(sin * img.getWidth() + cos * img.getHeight());
			int[] data = GameInstances.allocateBufferIf("renderImage", nw, nh);
			renderBuffer(data, 0, 0, _x, _y, nw, nh, img.getAlpha(), GameUtils.mapHex(img.getMask()), nw, nh, angle);
		}
		
		if(angle != 0 && angle != 360)
		{
			lastAngle = angle;
			double sin = Math.abs(Math.sin(angle));
			double cos = Math.abs(Math.cos(angle));
			int nw, nh;
			int w = img.getWidth(), h = img.getHeight();
			nw = (int)Math.floor(cos * w + sin * h);
			nh = (int)Math.floor(sin * w + cos * h);
			int[] data = GameInstances.allocateBufferIf("renderImage", nw, nh);
			
			GameImage.rotateNearestNeigbour(angle, img.getData(), img.getWidth(), img.getHeight(), data, nw, nh);
			renderBuffer(data, 0, 0, _x, _y, nw, nh, img.getAlpha(), GameUtils.mapHex(img.getMask()), nw, nh, angle);
			GameInstances.freeBufferOwnership("renderImage");
		}
		else
			renderImage(img, _x, _y);
	}
	
	// USAGE:
	// call every frame passing in an animator instance to render an animator onto the screen
	// use other overload for rotation
	public void renderAnimator(Animator animator, int _x, int _y)
	{
		Animation current = animator.getAnimation();
		
		renderSubImage(current.getImage(), _x, _y, (int)current.getOffset().x, (int)current.getOffset().y, current.getWidth(), current.getHeight());
	}
	
	public void renderAnimator(Animator animator, int _x, int _y, double angle)
	{
		Animation current = animator.getAnimation();
		
		renderSubImage(current.getImage(), _x, _y, (int)current.getOffset().x, (int)current.getOffset().y, current.getWidth(), current.getHeight(), angle);
	}
	
	// USAGE:
	// give x, y, w, and height value and it will render an outline of a rectangle with those dimensions
	// of the current color
	public void renderLineRectangle(int _x, int _y, int w, int h)
	{
		int rX = 0, rY = 0;
		for(int x = 0; x < w; x++) 
		{
			rX = _x + x;
			rY = _y;
			renderPixel(rX, rY, color);
		}
		for(int y = 0; y < h; y++)
		{
			rX = _x;
			rY = _y + y;
			renderPixel(rX, rY, color);
		}
		for(int x = 0; x < w; x++)
		{
			rX = _x + x;
			rY = _y + h;
			renderPixel(rX, rY, color);
		}
		for(int y = 0; y < h; y++)
		{
			rX = _x + w;
			rY = _y + y;
			renderPixel(rX, rY, color);
		}
	}
	
	// USAGE:
	// give x, y, w, and height value and it will render a filled rectangle with those dimensions
	// of the current color
	public void renderFilledRectangle(int _x, int _y, int _w, int _h)
	{
		if (_x + _w < xOffset) return;
		if (_y + _h < yOffset) return;
		if (_x > xOffset + width) return;
		if (_y > yOffset + height) return;
		int[] data = GameInstances.allocateBufferIf("renderFilledRectangle", _w, _h);
		Arrays.fill(data, 0xffffff);
		renderBuffer(data, 0, 0, _x, _y, _w, _h, 255, new GameColor(-1, -1, -1), _w, _h, 0);
		GameInstances.freeBufferOwnership("renderFilledRectangle");
	}
	
	// USAGE:
	// give x and y value and it will set the pixel at the x and y coordinate to the color
	public void renderPixel(int x, int y, int color)
	{
		if(x >= width || x < 0) return;
		if(y >= height || y < 0) return;
		this.color = color;
		pixels[x + y * width] = this.color;
	}
	
	// USAGE:
	// this is the internal function used to render pixel buffers, but you may want to manipulate pixels directly
	// and this function allows you to blit them onto the screen at a specific location easily and with blending
	public void renderBuffer(int[] _pixels, int ox, int oy, int _x, int _y, int w, int h, int alpha, GameColor mask, int scanWidth, int scanHeight, double angle)
	{
		int sX = _x;
		int sY = _y;
		int tX = _x - xOffset;
		int tY = _y - yOffset;
		
		for(int y = 0; y < h; y++)
		{
			if(y + oy >= scanHeight) break;
			int rY = tY + y;
			if(rY >= height || rY < 0) continue;
			
			for(int x = 0; x < w; x++)
			{
				if(x + ox >= scanWidth) break;
				int rX = tX + x;
				if(rX >= width || rX < 0) continue;
				
				int col = _pixels[(x + ox) + (y + oy) * scanWidth];
				
				int cr = (col >> 16) & 0xff;
				int cg = (col >>  8) & 0xff;
				int cb = (col      ) & 0xff;
				
				if(cr == mask.r && cg == mask.g && cb == mask.b) continue;
				
				int pcol = pixels[rX + rY * width];
				int pr = (pcol >> 16) & 0xff;
				int pg = (pcol >>  8) & 0xff;
				int pb = (pcol      ) & 0xff; 
				
				int nr, ng, nb;
				nr = (int)(blend(cr, pr, alpha, GameUtils.decomposeRgb(color, 0)));
				ng = (int)(blend(cg, pg, alpha, GameUtils.decomposeRgb(color, 1)));
				nb = (int)(blend(cb, pb, alpha, GameUtils.decomposeRgb(color, 2)));
				
				pixels[rX + rY * width] = GameUtils.mapRgb(nr, ng, nb);
			}
		}
	}
	
	
	public void getRaster(int[] pix)
	{
		for(int i = 0; i < (int)Math.min(pix.length, pixels.length); i++)
			pix[i] = pixels[i];
	}
	
	// USAGE:
	// This function can essentially copy a region of the screen into a buffer
	// which can be displayed afterwards or through GameImage, can be converted 
	// to an image and saved to a file
	public void getRasterRegion(int[] pix, int ox, int oy, int w, int h) throws Exception
	{
		if(ox + w < 0) return;
		if(ox + h < 0) return;
		if(ox >= width) return;
		if(oy >= height) return;
		if(pix.length < w * h) throw new Exception("Attempted to get raster region but length of container was not adequate!");
		
		for(int y = oy; y < oy + h; y++)
		{
			int iX = y - oy;
			if(y >= height || y < 0) continue;
			for(int x = ox; x < ox + w; x++)
			{
				if(x >= width || x < 0) continue;
				int iY = x - ox;
				pix[iX + iY * w] = pixels[x + y * width]; 
			}
		}
	}
	
	// CALLBACK:
	// This is the function called for blending r,g,b given src color and dest color with src alpha
	// The default version is usually sutiable for most games
	private float blend(int src, int dst, int srcA, int tint)
	{
		return (dst * (1 - srcA / 255f) + (src * (tint / 255f)) * (srcA / 255f));
	}
	
	
	// WARNING: DO NOT CALL THIS DIRECTLY
	// This method will be called by the game class when all rendering is completed to allow
	// for post processing of the pixels on the screen (i.e blurring, bloom, etc)
	public void postProcess()
	{
	}
}
