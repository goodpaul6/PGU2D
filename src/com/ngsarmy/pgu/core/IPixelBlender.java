package com.ngsarmy.pgu.core;

/* IPixelBlender interface:
 * This is the base interface
 * for pixel blending callbacks.
 * PixelBuffer takes an IPixelBlender
 * as an argument. The purpose of
 * this interface is to provide 
 * function for blending pixels 
 * when they are being rendered
 * onto a buffer. 
 */
public interface IPixelBlender 
{
	// CALLBACK:
	// return the blended r value
	int blendR(int src, int dst, int srcA, int tint);
	
	// CALLBACK:
	// return the blended g value
	int blendG(int src, int dst, int srcA, int tint);
	
	// CALLBACK:
	// return the blended b value
	int blendB(int src, int dst, int srcA, int tint);
}
