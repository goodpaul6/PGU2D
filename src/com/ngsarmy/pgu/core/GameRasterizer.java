package com.ngsarmy.pgu.core;

import java.util.Arrays;

import com.ngsarmy.pgu.graphicutils.Animation;
import com.ngsarmy.pgu.graphicutils.Animator;
import com.ngsarmy.pgu.input.GameMouse;
import com.ngsarmy.pgu.utils.GameInstances;
import com.ngsarmy.pgu.utils.GameUtils;
import com.ngsarmy.pgu.utils.Vector2;

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
	
	// publicly accessible pixel array
	public int[] pixels;
	
	// current color
	private int color;
	
	// gameobject currently being drawn (for gameobject based effects)
	public static GameObject currentGo;
	
	// FX VARIABLES:
	// You may place globals here which
	// the postRasterPixel can use for it's
	// effects, such as the players position
	// or the position of multiple lights.
	// You must set these yourself.
	// It is recommended that you prefix the variables
	// with fx.
	// END OF FX VARIABLES
	
	public GameRasterizer(int _width, int _height, int[] _pixels)
	{
		width = _width;
		height = _height;
		
		pixels = _pixels;
		color = 0;
		
		currentGo = null;
	}
	
	// USAGE:
	// set the color used to render primitives (rect, line)
	// this is also the screen clear color
	public void setColor(int _color)
	{
		color = _color;
	}
	
	// USAGE:
	// clear the screen
	public void clear()
	{
		clearToColor(color);
	}
	
	// USAGE:
	// clear the screen to the color passed in
	public void clearToColor(int _color)
	{
		Arrays.fill(pixels, _color);
	}
	
	// USAGE:
	// pass in a text object as well as x and y position
	// and this will render a text object onto the screen
	// in the location provided
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
	
	// USAGE:
	// see the other overload of renderSubImage
	// omits rotation for faster render
	public void renderSubImage(GameImage img, int _x, int _y, int _sx, int _sy, int _w, int _h)
	{
		if(_x + _w < (int)Game.cameraX) return;
		if(_y + _h < (int)Game.cameraY) return;
		if(_x >= (int)Game.cameraX + width) return;
		if(_y >= (int)Game.cameraY + height) return;
		
		renderBuffer(img.getData(), _sx, _sy, _x, _y, _w, _h, img.getAlpha(), GameUtils.mapHex(img.getMask()), img.getWidth(), img.getHeight(), 0);
	}
	
	// USAGE:
	// although this is often called internally through animators and such,
	// you can use this by passing in an image, as well as the position
	// where you wish to render the image, followed by the position
	// WITHIN the image from which you want it to paste the data.
	// w and h are the width and height of this region
	// following that, is the angle parameter, which you can use to rotate the data
	public void renderSubImage(GameImage img, int _x, int _y, int _sx, int _sy, int _w, int _h, double angle)
	{
		if(_x + _w < (int)Game.cameraX) return;
		if(_y + _h < (int)Game.cameraY) return;
		if(_x >= (int)Game.cameraX + width) return;
		if(_y >= (int)Game.cameraY + height) return;
		
		angle = ((int)angle % 360);
		
		if(angle != 0 && angle != 360)
		{
			double sin = Math.abs(Math.sin(angle));
			double cos = Math.abs(Math.cos(angle));
			int nw, nh;
			int w = _w, h = _h;
			nw = (int)Math.floor(cos * w + sin * h);
			nh = (int)Math.floor(sin * w + cos * h);
			int[] data = GameInstances.allocateGeneralBuffer(nw * nh);
			
			GameImage.rotateNearestNeigbour(angle, img.getRegionData(_sx, _sy, _w, _h), _w, _h, data, nw, nh);
			renderBuffer(data, 0, 0, _x, _y, nw, nh, img.getAlpha(), GameUtils.mapHex(img.getMask()), nw, nh, angle);
		}
		else
			renderBuffer(img.getData(), _sx, _sy, _x, _y, _w, _h, img.getAlpha(), GameUtils.mapHex(img.getMask()), img.getWidth(), img.getHeight(), 0);
	}
	
	// USAGE:
	// render an image to a location
	public void renderImage(GameImage img, int _x, int _y)
	{	
		renderBuffer(img.getData(), 0, 0, _x, _y, img.getWidth(), img.getHeight(), img.getAlpha(), GameUtils.mapHex(img.getMask()), img.getWidth(), img.getHeight(), 0);
	}
	
	// USAGE:
	// render an image to a location with the specified rotation
	public void renderImage(GameImage img, int _x, int _y, double angle)
	{	
		if(_x + img.getWidth() < (int)Game.cameraX) return;
		if(_y + img.getHeight() < (int)Game.cameraY) return;
		if(_x >= (int)Game.cameraX + width) return;
		if(_y >= (int)Game.cameraY + height) return;
		
		angle = ((int)angle % 360);
		
		if(angle != 0 && angle != 360)
		{
			double sin = Math.abs(Math.sin(angle));
			double cos = Math.abs(Math.cos(angle));
			int nw, nh;
			int w = img.getWidth(), h = img.getHeight();
			nw = (int)Math.floor(cos * w + sin * h);
			nh = (int)Math.floor(sin * w + cos * h);
			int[] data = GameInstances.allocateGeneralBuffer(nw * nh);
			
			GameImage.rotateNearestNeigbour(angle, img.getData(), img.getWidth(), img.getHeight(), data, nw, nh);
			renderBuffer(data, 0, 0, _x, _y, nw, nh, img.getAlpha(), GameUtils.mapHex(img.getMask()), nw, nh, angle);
		}
		else
			renderImage(img, _x, _y);
	}
	
	// USAGE:
	// call every frame passing in an animator instance to render an animator onto the screen
	public void renderAnimator(Animator animator, int _x, int _y)
	{
		Animation current = animator.getAnimation();
		renderSubImage(current.getImage(), _x, _y, (int)current.getOffset().x, (int)current.getOffset().y, current.getWidth(), current.getHeight());
	}
	
	// USAGE:
	// call every frame passing in an animator instance to render an animator onto the screen
	// this overload allows rotation
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
		if (_x + w < (int)Game.cameraX) return;
		if (_y + h < (int)Game.cameraY) return;
		if (_x > (int)Game.cameraX + width) return;
		if (_y > (int)Game.cameraY + height) return;
		int[] data = GameInstances.allocateGeneralBuffer(w * h);
		Arrays.fill(data, color - 1);
		int rX = 0, rY = 0;
		
		for(int x = 0; x < w; x++) 
		{
			rX = x;
			rY = 0;
			data[rX + rY * w] = color;
		}
		for(int y = 0; y < h; y++)
		{
			rX = 0;
			rY = y;
			data[rX + rY * w] = color;
		}
		for(int x = 0; x < w; x++)
		{
			rX = x;
			rY = h - 1;
			data[rX + rY * w] = color;
		}
		for(int y = 0; y < h; y++)
		{
			rX = w - 1;
			rY = y;
			data[rX + rY * w] = color;
		}
		
		renderBuffer(data, 0, 0, _x, _y, w, h, 255, GameUtils.mapHex(color - 1), w, h, 0);
	}
	
	// USAGE:
	// give x, y, width, and height value and it will render a filled rectangle with those dimensions
	// of the current color
	public void renderFilledRectangle(int _x, int _y, int _w, int _h)
	{
		if (_x + _w < (int)Game.cameraX) return;
		if (_y + _h < (int)Game.cameraY) return;
		if (_x > (int)Game.cameraX + width) return;
		if (_y > (int)Game.cameraY + height) return;
		int[] data = GameInstances.allocateGeneralBuffer(_w * _h);
		Arrays.fill(data, 0xffffff);
		renderBuffer(data, 0, 0, _x, _y, _w, _h, 255, new GameColor(-1, -1, -1), _w, _h, 0);
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
		int tX = _x - (int)Game.cameraX;
		int tY = _y - (int)Game.cameraY;
		
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
				
				pixels[rX + rY * width] = postRasterPixel(rX, rY, _x + x, _y + y, nr, ng, nb, GameUtils.mapRgb(nr, ng, nb), x + ox, y + oy, w, h, scanWidth, scanHeight, _pixels);
			}
		}
	}
	
	// USAGE:
	// put as much of the screen data (or all of it)
 	// into the provided buffer
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
	// CALLBACK:
	// This method will be called by the rasterizer after it draws a pixel to a location
	// for post processing of the pixels on the screen (i.e vignette, etc)
	// this is much faster than using the postProcess method as it is called
	// as the rendering goes along 
	// you can also use the currentGo variable of this class to change effects 
	// for different game objects (but you must first check if a game object is being drawn by
	// checking if currentGo is null)
	// return the calculated color value (as an integer color [use GameUtils.mapRgb])
	public int postRasterPixel(int screenX, int screenY, int actualX, int actualY, int calcR, int calcG, int calcB, int col, int smpX, int smpY, int w, int h, int scanWidth, int scanHeight, int[] data)
	{	
		return col;
	}
	
	// WARNING: DO NOT CALL THIS DIRECTLY
	// CALLBACK:
	// This method will be called by the Game class after the screen is done
	// rendering. You can use this to perform post processing effects on the pixel buffer
	// (i.e blurring). Most of the times, you do not need to perform post processing here
	// and can simply do it in the post raster pixels method, but if you need to do
	// a full scale screen effect, like a water mark, use this
	public void postProcess()
	{
	}
}
