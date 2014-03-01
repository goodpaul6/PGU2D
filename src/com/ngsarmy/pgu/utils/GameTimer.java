package com.ngsarmy.pgu.utils;

/* GameTimer class:
 * this class provides a simple interface
 * for basic timing (i.e getting elapsed time
 * since last call to run) NOTE: this relies
 * on system time so it will time regardless
 * of blocking code.
 */
public class GameTimer 
{
	// time during which this started
	private long startTime;
	// time during which this was paused
	private long pauseTime;
	
	public GameTimer()
	{
		startTime = System.nanoTime();
		pauseTime = 0;
	}
	
	public double pause()
	{
		pauseTime = System.nanoTime();
		return (pauseTime - startTime) / GameConsts.NSF;
	}
	
	public void unpause()
	{
		startTime = pauseTime;
		pauseTime = 0;
	}
	
	public double restart()
	{
		double delta = (System.nanoTime() - startTime) / GameConsts.NSF;
		startTime = System.nanoTime();
		pauseTime = 0;
		return delta;
	}
	
	public double getElapsed()
	{
		long now = System.nanoTime();
		return (now - startTime) / GameConsts.NSF;
	}
}
