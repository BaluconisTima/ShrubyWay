package com.shrubyway.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		System.out.println("DesktopLauncher.main: ");
		config.setForegroundFPS(60);
		config.setTitle("Shruby Way - beta");

		config.setWindowIcon("SWicon.png");
		config.setWindowedMode(1920, 1080);
		config.setFullscreenMode(config.getDisplayMode());
		new Lwjgl3Application(new ShrubyWay(), config);

	}
}
