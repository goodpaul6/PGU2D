package com.ngsarmy.pgu.graphicutils;

import java.util.HashMap;
import java.util.Map;

// Animator class:
// This class provides functionality
// for sprite sheet animation. 
// It can store and address various
// Animation objects and can be
// rendered onto the screen
public class Animator 
{
	// a dictionary used to reference animation objects by name
	private Map<String, Animation> animDict;
	// the animation currently being played
	private Animation currentAnim;
	// paused?
	private boolean paused;
	
	public Animator()
	{
		animDict = new HashMap<String, Animation>();
		paused = false;
	}
	
	// USAGE:
	// call this to set the current animation to the one with the name given
	public void play(String name)
	{
		currentAnim = animDict.get(name);
	}
	
	// USAGE:
	// call this in the update method of the gameobject or state to update the current 
	// animation (you must call play prior to this to set the current animation, other
	// wise, no animation will be updated (all of this considering whether the animator
	// is paused)
	public void update(float delta)
	{
		if(!paused && currentAnim != null)
			currentAnim.update((float)delta);
	}
	
	// USAGE:
	// give a name and an animation object, and it will 
	// be stored within this animator for later
	// playback
	public void add(String name, Animation anim)
	{
		if(currentAnim == null)
			currentAnim = anim;
		animDict.put(name, anim);
	}
	
	// USAGE:
	// give a name of a previously added animation
	// and it will be removed
	public void remove(String name)
	{
		animDict.remove(name);
	}
	
	// USAGE:
	// give a boolean true or false indicating whether the current animation 
	// should be updated
	public void setPaused(boolean v)
	{
		paused = v;
	}
	
	// CAUTION: USED INTERNALLY
	// you should not need to use this yourself, although it has no side effects
	public Animation getAnimation()
	{
		return currentAnim;
	}
}
