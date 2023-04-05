package com.shrubyway.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Batch;


public class ShrubyWay extends ApplicationAdapter {
	private Shraby player;
	SpriteBatch batch;
	OrthographicCamera Camera;

	private KeyboardAdapter InputProcessor = new KeyboardAdapter();

	@Override
	public void create () {
		Gdx.graphics.setWindowedMode(1920, 1080);
		Graphics.DisplayMode currentDisplayMode = Gdx.graphics.getDisplayMode();
		Gdx.graphics.setFullscreenMode(currentDisplayMode);
		Camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);


		Gdx.input.setInputProcessor(InputProcessor);

         player = new Shraby(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		 batch = new SpriteBatch();

	}

	@Override
	public void render () {
		ScreenUtils.clear(1,1,1,1);
		Camera.update();
		batch.setProjectionMatrix(Camera.combined);
        player.moveTo(InputProcessor.getMovementDirection());
		batch.begin();
		player.render(batch);
		batch.end();

	}

	@Override
	public void dispose () {
          player.dispose();
	}
}
