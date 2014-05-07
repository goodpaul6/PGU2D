package com.ngsarmy.pgu.core;

// GameAssets class:
// this class stores all
// assets which are used 
// within the game statically
// in order to prevent reloading 
// of the same data. You 
// should edit this to your
// liking.
public class GameAssets 
{
	public static GameImage PGULogoImage = new GameImage();
	public static GameText PGUFontText = new GameText();
	public static GameImage brickTile = new GameImage();
	
	// WARNING:
	// Do not call this directly, it is called internally
	// Place your asset loading code in here
	public static void load()
	{
		if(!PGULogoImage.loadFromFile("res/PGULogo.png"))
			System.exit(-1);
		if(!PGUFontText.loadFont("res/PGUFont.png", 8, 8))
			System.exit(-1);
		if(!brickTile.loadFromFile("res/BrickTile.png"))
			System.exit(-1);

		PGUFontText.setCharset("abcdefghijklmnopqrstuvwxyz .:,1234567890");
	}
}
