package com.ngsarmy.pgu.core;

import java.util.Random;

import javax.swing.JFrame;

import com.ngsarmy.pgu.states.MainState;
import com.ngsarmy.pgu.utils.GameConsts;
import com.ngsarmy.pgu.utils.GameTimer;
import com.ngsarmy.pgu.utils.Rectangle;

public class Game implements Runnable
{
	public static int width = 480;
	public static int height = 240;
	public static int scale = 2;
	public static String log = "";
	public static float delta = (float)(1 / GameConsts.NPF);
	public static final boolean inIde = true;
	
	public static float cameraX;
	public static float cameraY;
	
	private GameCanvas canvas;
	
	private static GameTimer timer;
	
	public GameStateStack states;
	
	private JFrame frame;
	
	private Thread thread;
	private boolean running = false;
	
	private static Rectangle viewRect;
	public static Random random;
	
	public static void log(String message)
	{
		log = message;
	}
	
	public static void cameraFollow(GameObject go, boolean center, float lerpFactor)
	{
		int finalPosX = go.getLeftI() + go.getWidthI() / 2;
		int finalPosY = go.getTopI() + go.getHeightI() / 2;
		
		if(center)
		{
			finalPosX -= Game.width / 2;
			finalPosY -= Game.height / 2;
		}
		
		if(lerpFactor > 0)
		{
			cameraX += ((finalPosX - cameraX) * delta * lerpFactor);
			cameraY += ((finalPosY - cameraY) * delta * lerpFactor);
		}
		else
		{
			cameraX = finalPosX;
			cameraY = finalPosY;
		}
	}
	
	public static Rectangle getViewRect()
	{
		viewRect.position.x = cameraX;
		viewRect.position.y = cameraY;
		return viewRect;
	}
	
	public Game()
	{
		canvas = new GameCanvas(width, height, scale);
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("FMM Game Development Club Game");
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		states = new GameStateStack();
		cameraX = 0;
		cameraY = 0;
		
		random = new Random();
		
		viewRect = new Rectangle(cameraX, cameraY, width, height);
	}
	
	
	public synchronized void start()
	{
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
		timer = new GameTimer();
	}
	
	public synchronized void stop()
	{
		running = false;
		try { thread.join(); }
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public void run() 
	{
		long lastTime = System.nanoTime();
		double deltaTime = 0;
		int frames = 0;
		int ups = 0;
		long timer = System.currentTimeMillis();
		
		while (running)
		{
			while(!GameEventManager.isEmpty())
				states.event(GameEventManager.latest());
			long now = System.nanoTime();
			deltaTime += (now - lastTime) / GameConsts.NPF;
			lastTime = now;
			
			while(deltaTime >= 1.0)
			{
				update((float)(1 / GameConsts.FPS));
				Game.delta = (float)(1 / GameConsts.FPS);
				deltaTime--;
				ups++;
			}
			frames++;
			if((System.currentTimeMillis() - timer) / 1000.0 >= 1)
			{
				System.out.println("FPS: " + frames + " UPS: " + ups + " LOG: " + log);
				frames = 0;
				ups = 0;
				timer = System.currentTimeMillis();
			}
			render();
		}
	}
	
	private void update(float delta)
	{
		states.update(delta);
	}
	
	private void render()
	{
		GameRasterizer rst = canvas.getRasterizer();
		states.render(rst);
		rst.postProcess();
		canvas.renderToScreen();
	}
	
	public static double getElapsedTime()
	{
		return timer.getElapsed();
	}
	
	public static void main(String[] args)
	{
		Game game = new Game();
		GameAssets.load(game);
		game.states.pushState(new MainState(game));
		game.start();
	}
}
