package com.shrubyway.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Batch;


public class ShrubyWay extends ApplicationAdapter {
	private Shraby player;
	private Background background;
	SpriteBatch batch;
	OrthographicCamera Camera;
	Vector2 CameraPosition;
	Texture TEST_OBJECT;

	private KeyboardAdapter InputProcessor = new KeyboardAdapter();

	@Override
	public void create () {
		TEST_OBJECT = new Texture(Gdx.files.internal("Test.png"));
		Gdx.graphics.setWindowedMode(1920, 1080);
		CameraPosition = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		Graphics.DisplayMode currentDisplayMode = Gdx.graphics.getDisplayMode();
		Gdx.graphics.setFullscreenMode(currentDisplayMode);
		Camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Camera.position.set(CameraPosition.x,CameraPosition.y, 0);
        background = new Background();

		Gdx.input.setInputProcessor(InputProcessor);
		batch = new SpriteBatch();

		player = new Shraby(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		//Gdx.graphics.setVSync(true);


	}

	@Override
	public void render () {
		ScreenUtils.clear(1,1,1,1);

		CameraPosition.lerp(new Vector2(player.Position().x, player.Position().y),
				0.08f);


		Camera.position.set(CameraPosition.x,CameraPosition.y, 0);
		Camera.update();
		batch.setProjectionMatrix(Camera.combined);
        player.moveTo(InputProcessor.getMovementDirection());
		player.LiquidStatus(background.checkLiquid(1,player.BottomPosition()));

		batch.begin();

		background.render(batch,1,player.Position());
		batch.draw(new Texture(Gdx.files.internal("TEST.png")), player.BottomPosition().x,
				player.BottomPosition().y);
		player.render(batch);
		batch.end();

	}

	@Override
	public void dispose () {
          player.dispose();
	}
}
