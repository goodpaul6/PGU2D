package com.ngsarmy.pgu.core;

import javax.swing.JFrame;

import com.ngsarmy.pgu.states.LogoState;
import com.ngsarmy.pgu.utils.GameConsts;
import com.ngsarmy.pgu.utils.GameTimer;

public class Game implements Runnable
{
	public static int width = 480;
	public static int height = 240;
	public static int scale = 1;
	
	private GameCanvas canvas;
	
	private static GameTimer timer;
	
	public GameStateStack states;
	
	private JFrame frame;
	
	private Thread thread;
	private boolean running = false;

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
				update(1 / GameConsts.FPS);
				deltaTime--;
				ups++;
			}
			frames++;
			if((System.currentTimeMillis() - timer) / 1000.0 >= 1)
			{
				System.out.println("FPS: " + frames + " UPS: " + ups);
				frames = 0;
				ups = 0;
				timer = System.currentTimeMillis();
			}
			render();
		}
	}
	
	private void update(double delta)
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
		Game.width = 640;
		Game.height = 480;
		Game.scale = 2;
		GameAssets.load();
		Game game = new Game();
		game.states.pushState(new LogoState(game));
		game.start();
	}
}
