package com.ngsarmy.pgu.graphicutils;

import com.ngsarmy.pgu.core.GameImage;
import com.ngsarmy.pgu.utils.Vector2;

// Animation class:
// This class is a container
// for a single sprite sheet's
// animation. I.e walk animation
// which consists of frames 1, 2,
// 3, 4, etc
// this class is not meant to be
// used directly, instead, it is
// meant to be stored within an
// animator
public class Animation 
{
	// the game image within which the animation is contained
	private GameImage image;
	// the width of the individual frame
	private int width;
	// the height of the individual frame
	private int height;
	// the indices of the frames within this animation
	private int[] frames;
	// the amount of time per frame
	private float timePerFrame;
	// the amount of time passed playing the animation
	private float accum;
	// the current frame index
	private int curFrameIndex;
	// current frame offset position
	private Vector2 curFrameOffset;
	
	
	// Constructor:
	// takes a reference to the image of the sprite sheet
	// frame width
	// frame height
	// the array of indices representing the frame to be rendered
	// the amount of frames per second
	public Animation(GameImage image, int frameWidth, int frameHeight, int[] frames, int fps)
	{
		this.image = image;
		width = frameWidth;
		height = frameHeight;
		this.frames = frames;
		this.timePerFrame = 1.0f / fps;
		accum = 0;
		curFrameOffset = new Vector2(0, 0);
	}
	
	public void update(float delta)
	{
		accum += (float)delta;
		if(accum >= timePerFrame * frames.length)
			accum -= timePerFrame * frames.length;
		
		curFrameIndex = (int)(accum / timePerFrame);
		
		int tu = frames[curFrameIndex] % (image.getWidth() / width);
		int tv = frames[curFrameIndex] / (image.getWidth() / width);
		
		curFrameOffset.x = tu * width;
		curFrameOffset.y = tv * height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public Vector2 getOffset()
	{
		return curFrameOffset;
	}
	
	public GameImage getImage()
	{
		return image;
	}
}
