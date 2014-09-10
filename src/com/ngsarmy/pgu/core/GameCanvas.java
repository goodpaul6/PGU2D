package com.ngsarmy.pgu.core;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.ngsarmy.pgu.input.GameKeyboard;
import com.ngsarmy.pgu.input.GameMouse;

/* GameCanvas Class:
 * houses the backbuffer for the screen and provides the methods
 * for rendering various entities onto the screen. The rasterize 
 * of this canvas is passed in as an argument to a GameState's 
 * draw method.
 */
public class GameCanvas extends Canvas
{	
	private static final long serialVersionUID = 1L;
	
	private int width;
	private int height;
	private int scale;
	
	// this is our raster buffer (final rendered view)
	private BufferedImage image; 
	// this is our raw raster data (an array of pixels)
	private int[] pixels;
	
	// this is our rasterizer
	GameRasterizer rst;
	
	public GameCanvas(int _width, int _height, int _scale)
	{
		width = _width; 
		height = _height; 
		scale = _scale;
		
		setPreferredSize(new Dimension(width * scale, height * scale));
		addKeyListener(new GameKeyboard());
		GameMouse m = new GameMouse();
		addMouseListener(m);
		addMouseMotionListener(m);
		addMouseWheelListener(m);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// retrieve writable raster
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		// create the rasterizer
		rst = new GameRasterizer(width, height, pixels);
	}
	
	public GameCanvas(int _width, int _height)
	{
		this(_width, _height, 2);
	}
	
	public GameCanvas(int _scale)
	{
		this(320, 240, _scale);
	}
	
	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	// gets the rasterizer in order to allow rendering
	public GameRasterizer getRasterizer()
	{
		return rst;
	}
	
	// CAUTION: NOT MEANT TO BE USED DIRECTLY
	// render contents of the buffer to the screen
	public void renderToScreen()
	{
		BufferStrategy bs = getBufferStrategy();
		// if no buffer strategy is created for this canvas
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
}
