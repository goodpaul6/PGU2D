package com.ngsarmy.pgu.core;

import com.ngsarmy.pgu.blenders.DefaultPixelBlender;
import com.ngsarmy.pgu.effects.DefaultPixelEffect;
import com.ngsarmy.pgu.graphicutils.Animation;
import com.ngsarmy.pgu.graphicutils.Animator;
import com.ngsarmy.pgu.utils.GameUtils;

/* GameRasterizer Class:
 * an internal class used by game canvas to
 * draw to its raw buffer. You should not have
 * to use this unless you want to add some 3d
 * functionality.
 */
public class GameRasterizer 
{
	// internal pixel buffer
	private PixelBuffer buffer;
	
	// current color
	private int color;
	
	// current effect
	public IPixelBlender blender;
	public IPixelEffect effect;
	
	// internal temp pixel buffer
	private PixelBuffer tempBuffer;
	
	// internal temp GameColor
	private GameColor tempColor;
	
	public GameRasterizer(int _width, int _height, int[] _pixels)
	{
		buffer = new PixelBuffer();
		buffer.resize(_width, _height);
		color = 0;
		blender = DefaultPixelBlender.get();
		effect = DefaultPixelEffect.get();
		tempBuffer = new PixelBuffer();
		tempColor = new GameColor(0, 0, 0);
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
		buffer.fill(_color);
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
		if(_x >= (int)Game.cameraX + buffer.width) return;
		if(_y >= (int)Game.cameraY + buffer.height) return;
		
		renderBuffer(img.getBuffer(), _sx, _sy, _x, _y, _w, _h, img.getAlpha(), img.maskR, img.maskG, img.maskB);
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
		if(_x >= (int)Game.cameraX + buffer.width) return;
		if(_y >= (int)Game.cameraY + buffer.height) return;
		
		angle = ((int)angle % 360);
		
		if(angle != 0 && angle != 360)
		{
			double sin = Math.abs(Math.sin(angle));
			double cos = Math.abs(Math.cos(angle));
			int nw, nh;
			int w = _w, h = _h;
			nw = (int)Math.floor(cos * w + sin * h);
			nh = (int)Math.floor(sin * w + cos * h);
			tempBuffer.resize(nw, nh);
			PixelBuffer.rotateNearestNeigbour(angle, img.getRegionData(_sx, _sy, _w, _h), _w, _h, tempBuffer.pixels, nw, nh);
			renderBuffer(tempBuffer, _sx, _sy, _x, _y, _w, _h, img.getAlpha(), img.maskR, img.maskG, img.maskB);
		}
		else
			renderBuffer(img.getBuffer(), _sx, _sy, _x, _y, _w, _h, img.getAlpha(), img.maskR, img.maskG, img.maskB);
	}
	
	// USAGE:
	// render an image to a location
	public void renderImage(GameImage img, int _x, int _y)
	{	
		renderBuffer(img.getBuffer(), 0, 0, _x, _y, img.getWidth(), img.getHeight(), img.getAlpha(), img.maskR, img.maskG, img.maskB);
	}
	
	// USAGE:
	// render an image to a location with the specified rotation
	public void renderImage(GameImage img, int _x, int _y, double angle)
	{	
		if(_x + img.getWidth() < (int)Game.cameraX) return;
		if(_y + img.getHeight() < (int)Game.cameraY) return;
		if(_x >= (int)Game.cameraX + buffer.width) return;
		if(_y >= (int)Game.cameraY + buffer.height) return;
		
		angle = ((int)angle % 360);
		
		if(angle != 0 && angle != 360)
		{
			double sin = Math.abs(Math.sin(angle));
			double cos = Math.abs(Math.cos(angle));
			int nw, nh;
			int w = img.getWidth(), h = img.getHeight();
			nw = (int)Math.floor(cos * w + sin * h);
			nh = (int)Math.floor(sin * w + cos * h);
			tempBuffer.resize(nw, nh);
			
			PixelBuffer.rotateNearestNeigbour(angle, img.getBuffer().pixels, img.getWidth(), img.getHeight(), tempBuffer.pixels, nw, nh);
			renderBuffer(tempBuffer, 0, 0, _x, _y, nw, nh, img.getAlpha(), img.maskR, img.maskB, img.maskG);
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
		if (_x > (int)Game.cameraX + buffer.width) return;
		if (_y > (int)Game.cameraY + buffer.height) return;
		
		tempBuffer.resize(w, h);
		
		int rX = 0, rY = 0;
		
		for(int x = 0; x < w; x++) 
		{
			rX = x;
			rY = 0;
			tempBuffer.pixels[rX + rY * w] = color;
		}
		for(int y = 0; y < h; y++)
		{
			rX = 0;
			rY = y;
			tempBuffer.pixels[rX + rY * w] = color;
		}
		for(int x = 0; x < w; x++)
		{
			rX = x;
			rY = h - 1;
			tempBuffer.pixels[rX + rY * w] = color;
		}
		for(int y = 0; y < h; y++)
		{
			rX = w - 1;
			rY = y;
			tempBuffer.pixels[rX + rY * w] = color;
		}
		
		renderBuffer(tempBuffer, 0, 0, _x, _y, w, h, 255, 0, 0, 0);
	}
	
	// USAGE:
	// give x, y, width, and height value and it will render a filled rectangle with those dimensions
	// of the current color
	public void renderFilledRectangle(int _x, int _y, int _w, int _h)
	{
		if (_x + _w < (int)Game.cameraX) return;
		if (_y + _h < (int)Game.cameraY) return;
		if (_x > (int)Game.cameraX + buffer.width) return;
		if (_y > (int)Game.cameraY + buffer.height) return;
		tempBuffer.resize(_w, _h);
		tempBuffer.fill(color);
		renderBuffer(tempBuffer, 0, 0, _x, _y, _w, _h, 255, -1, -1, -1);
	}
	
	// USAGE:
	// give x and y value and it will set the pixel at the x and y coordinate to the color
	public void renderPixel(int x, int y, int color)
	{
		if(x >= buffer.width || x < 0) return;
		if(y >= buffer.height || y < 0) return;
		this.color = color;
		buffer.setPixel(x, y, color);
	}
	
	// USAGE:
	// this is the internal function used to render pixel buffers, but you may want to manipulate pixels directly
	// and this function allows you to blit them onto the screen at a specific location using the current blender
	// and effect
	public void renderBuffer(PixelBuffer _input, int ox, int oy, int _x, int _y, int w, int h, int alpha, int maskR, int maskG, int maskB)
	{
		int tX = _x - (int)Game.cameraX;
		int tY = _y - (int)Game.cameraY;
		
		for(int y = 0; y < h; y++)
		{
			if(y + oy >= _input.height) break;
			int rY = tY + y;
			if(rY >= buffer.height || rY < 0) continue;
			
			for(int x = 0; x < w; x++)
			{
				if(x + ox >= _input.width) break;
				int rX = tX + x;
				if(rX >= buffer.width || rX < 0) continue;

				int col = _input.getPixel(x + ox, y + oy);
				
				int cr = (col >> 16) & 0xff;
				int cg = (col >>  8) & 0xff;
				int cb = (col      ) & 0xff;
				
				if(cr == maskR && cg == maskG && cb == maskB) continue;
				
				int pcol = buffer.getPixel(rX, rY);
				int pr = (pcol >> 16) & 0xff;
				int pg = (pcol >>  8) & 0xff;
				int pb = (pcol      ) & 0xff; 
				
				int nr, ng, nb;
				nr = (blender.blendR(cr, pr, alpha, GameUtils.decomposeRgb(color, 0)));
				ng = (blender.blendG(cg, pg, alpha, GameUtils.decomposeRgb(color, 1)));
				nb = (blender.blendB(cb, pb, alpha, GameUtils.decomposeRgb(color, 2)));
				
				buffer.setPixel(rX, rY, effect.postProcessColor(rX, rY, _x + x, _y + y, nr, ng, nb, GameUtils.mapRgb(nr, ng, nb), x + ox, y + oy, _input, buffer));
			}
		}
	}
	
	// USAGE:
	// put as much of the screen data (or all of it)
 	// into the provided buffer
	public void getRaster(int[] pix)
	{
		for(int i = 0; i < (int)Math.min(pix.length, buffer.pixels.length); i++)
			pix[i] = buffer.pixels[i];
	}
	
	// USAGE:
	// This function can essentially copy a region of the screen into a buffer
	// which can be displayed afterwards or through GameImage, can be converted 
	// to an image and saved to a file
	public void getRasterRegion(int[] pix, int ox, int oy, int w, int h) throws Exception
	{
		if(ox + w < 0) return;
		if(ox + h < 0) return;
		if(ox >= buffer.width) return;
		if(oy >= buffer.height) return;
		if(pix.length < w * h) throw new Exception("Attempted to get raster region but length of container was not adequate!");
		
		for(int y = oy; y < oy + h; y++)
		{
			int iX = y - oy;
			if(y >= buffer.height || y < 0) continue;
			for(int x = ox; x < ox + w; x++)
			{
				if(x >= buffer.width || x < 0) continue;
				int iY = x - ox;
				pix[iX + iY * w] = buffer.pixels[x + y * buffer.width]; 
			}
		}
	}
	
	// WARNING: DO NOT CALL THIS DIRECTLY
	// CALLBACK:
	// This method will be called by the Game class after the screen is done
	// rendering. You can use this to perform post processing effects on the pixel buffer
	// (i.e blurring). Most of the times, you do not need to perform post processing here
	// and can simply do it by using an IPixelEffect, but if you need to do
	// a full scale screen effect, like a water mark, use this
	public void postProcess()
	{
	}
}
