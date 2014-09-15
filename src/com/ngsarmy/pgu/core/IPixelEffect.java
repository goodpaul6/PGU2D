package com.ngsarmy.pgu.core;

/* IPixelEffect interface:
 * This interface provides
 * methods which are meant
 * to modify color values
 * on the fly as they are being
 * rendered. These provide 
 * the ability to do fast
 * effects. the PixelBuffer
 * class 'put' method takes
 * an IPixelEffect as one of
 * it's arguments. You can pass in
 * the DefaultPixelEffect or one 
 * of your own.
 */
public interface IPixelEffect 
{
	// CALLBACK:
	// return the new color value (use GameUtils.mapRgb)
	// arguments:
	// bufX -> an integer representing the x position within the target buffer where this pixel is being rendered
	// bufY -> an integer representing the y position within the target buffer where this pixel is being rendered
	// actualX -> an integer representing the x position within the world of the pixel
	// actualY -> an integer representing the y position within the world of the pixel
	// calcR, calcG, calcB -> the blended r, g, and b color values
	// col -> the default blender-generated color value (RGB integer)
	// smpX -> the x position within the input buffer from which the color value is sampled
	// smpY -> the y position within the input buffer from which the color value is sampled
	// input -> the input pixel buffer being rendered onto the output buffer
	// output -> the buffer to which the pixel is being rendered
	int postProcessColor(int bufX, int bufY, int actualX, int actualY, int calcR, int calcG, int calcB, int col, int smpX, int smpY, PixelBuffer input, PixelBuffer output);
}
