package com.ngsarmy.pgu.core;

import java.net.URL;
import java.util.HashMap;

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
	public static GameImage PGULogoImage;
	public static GameText PGUFontText;
	private static HashMap<String, GameImage> images = new HashMap<String, GameImage>();
	private static HashMap<String, GameText> texts = new HashMap<String, GameText>();
	private static Game game;
	
	// WARNING:
	// Do not call this directly, it is called internally
	public static void load(Game _game)
	{
		game = _game;
		PGULogoImage = loadImage("PGULogo.png");
		if(PGULogoImage == null)
			System.exit(-1);
		
		PGUFontText = loadFont("PGUFont.png", 8, 8);
		if(PGUFontText == null)
			System.exit(-1);
		
		PGUFontText.setCharset("abcdefghijklmnopqrstuvwxyz .:,1234567890");
	}
	
	// USAGE:
	// loads (or returns a previously loaded) image file from a given path (relative to the res folder)
	// for example, if you have a file in the res folder called "spike.png", pass in "spike.png" as the 
	// argument to load it. This returns null if the loading failed
	public static GameImage loadImage(String path)
	{
		if(images.containsKey(path))
			return images.get(path);
		
		URL u = game.getClass().getResource("/" + path);
		
		if(u == null)
			return null;
		
		GameImage loaded = new GameImage();
		loaded.loadFromFile(path);
		images.put(path, loaded);
		return loaded;
	}
	
	// USAGE:
	// loads (or returns a previously loaded) game font from a given path (relative to the res folder)
	// for example, if you have a file in the res folder called "spikeFont.png", pass in "spikeFont.png" as the 
	// argument to load it. This returns null if the loading failed
	// also, supply the width of each character as well as the height of each character in to the function
	// as well
	public static GameText loadFont(String path, int charWidth, int charHeight)
	{
		if(texts.containsKey(path))
			return texts.get(path);
		
		GameText loaded = new GameText();
		if(!loaded.loadFont(path, charWidth, charHeight))
		{	
			System.err.println("Error: could not load font from path '" + path + "'");
			System.exit(-1);
		}
		texts.put(path, loaded);
		return loaded;
	}
}
