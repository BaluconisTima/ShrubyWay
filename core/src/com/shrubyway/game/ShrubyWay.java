package com.shrubyway.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ShrubyWay extends ApplicationAdapter {
	private Shraby player;
	SpriteBatch batch;
	private KeyboardAdapter InputProcessor = new KeyboardAdapter();

	@Override
	public void create () {
		Gdx.input.setInputProcessor(InputProcessor);
         player = new Shraby(100, 200);
		 batch = new SpriteBatch();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1,1,1,1);
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
