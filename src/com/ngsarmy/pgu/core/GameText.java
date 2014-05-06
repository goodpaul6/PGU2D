package com.ngsarmy.pgu.core;

/* GameText class:
 * loads a bitmap font (i.e text spritesheet)
 * and allows for you to write text to the 
 * screen.
 */
public class GameText 
{
	// image which stores the font
	private GameImage fontImage;
	// width and height of each character
	private int width, height;
	// raw text data
	private String text;
	// supported characters:
	// you can and should change this depending on the bitmap font file
	// this represents the layout of the image font file and is the reference
	// for the renderer to find the appropriate image for each letter.
	public String charSet =	"abcdefghijklmnopqrstuvwxyz .:,1234567890";
	// this will store the character indexes (relative to bitmap) for faster rendering
	private int[] charPos;

	public GameText()
	{
		fontImage = new GameImage();
	}
	
	// USAGE:
	// loads the bitmap font from the filename supplied, takes in the width and height of each individual
	// character within the file
	public boolean loadFont(String filePath, int charWidth, int charHeight)
	{
		if(!fontImage.loadFromFile(filePath))
			return false;
		width = charWidth;
		height = charHeight;
		return true;
	}
	
	// USAGE:
	// sets the charset of the text (i.e index data)
	public void setCharset(String set)
	{
		charSet = set;
		
		if(charPos != null)
		for(int i = 0; i < charPos.length; i++)
			charPos[i] = charSet.indexOf(text.charAt(i));
	}
	
	// USAGE:
	// sets the text to be rendered onto the screen
	public void setText(String text)
	{
		this.text = text;
		charPos = new int[text.length()];
		for(int i = 0; i < text.length(); i++)
			charPos[i] = charSet.indexOf(text.charAt(i));
	}
	
	// USAGE:
	// gives character width
	public int getCharWidth()
	{
		return width;
	}
	
	// USAGE:
	// gives character height
	public int getCharHeight()
	{
		return height;
	}
	
	// USAGE:
	// gives font image
	public GameImage getFontImage()
	{
		return fontImage;
	}
	
	// USAGE:
	// gives character data as UV index
	public int[] getCharData()
	{
		return charPos;
	}
	
	// USAGE:
	// gives expected width of text
	public int getWidth()
	{
		return charPos.length * width;
	}
	
	// USAGE:
	// gives expected height of text
	public int getHeight()
	{
		return height;
	}
}
