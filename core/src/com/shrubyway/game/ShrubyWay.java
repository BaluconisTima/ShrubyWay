package com.shrubyway.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Batch;
import jdk.incubator.vector.VectorOperators;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ShrubyWay extends ApplicationAdapter {
	private Shraby player;
	private Background background;
	SpriteBatch batch;
	OrthographicCamera Camera;
	Vector2 CameraPosition;
	List<VisibleObject> renderingObjects;
	Texture TEST_OBJECT;
	private BitmapFont font;

	private KeyboardAdapter InputProcessor = new KeyboardAdapter();

	@Override
	public void create () {
		font = new BitmapFont();
		font.getData().setScale(3);

		TEST_OBJECT = new Texture(Gdx.files.internal("Test.png"));
		Gdx.graphics.setWindowedMode(1920, 1080);
		CameraPosition = new Vector2(0, 0);
		Graphics.DisplayMode currentDisplayMode = Gdx.graphics.getDisplayMode();
		Gdx.graphics.setFullscreenMode(currentDisplayMode);
		Camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Camera.position.set(CameraPosition.x,CameraPosition.y, 0);
		Camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background = new Background();

		Gdx.input.setInputProcessor(InputProcessor);
		batch = new SpriteBatch();
		player = new Shraby(0, 0);

		Gdx.graphics.setVSync(true);


	}
    public void correctPosition() {
		Vector2 temp = new Vector2(player.Position());
		if(temp.x < 0) {
			temp.add(new Vector2(38400, 0));
			CameraPosition.add(new Vector2(38400, 0));
		}
		if(temp.y < 0) {
			temp.add(new Vector2(0, 38400));
			CameraPosition.add(new Vector2(0, 38400));
		}
		if(temp.x >= 38400) {
			temp.add(new Vector2(-38400, 0));
			CameraPosition.add(new Vector2(-38400, 0));
		}
		if(temp.y >= 38400) {
			temp.add(new Vector2(0, -38400));
			CameraPosition.add(new Vector2(0, -38400));
		}
		player.changePosition(temp);
	}
	@Override
	public void render () {
		ScreenUtils.clear(1,1,1,1);
		correctPosition();
		CameraPosition.lerp(new Vector2(player.Position().x, player.Position().y),
				0.1f);




		Camera.position.set(CameraPosition.x,CameraPosition.y, 0);
		Camera.update();
		batch.setProjectionMatrix(Camera.combined);
        player.moveTo(InputProcessor.getMovementDirection());
		player.LiquidStatus(background.checkLiquid(1,player.positionBottom()));

		batch.begin();
		background.render(batch,1,player.positionBottom());

		renderingObjects =
				background.decorationsList(1, player.Position());
		renderingObjects.add(player);
		Collections.sort(renderingObjects);

		System.out.println();
		for (VisibleObject obj : renderingObjects) {
			System.out.println(obj.position.y);
			obj.Render(batch);
		}
		batch.end();


		/*batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,

		Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		batch.begin();
		font.draw(batch, ""+player.Position(), 100, 100);
		batch.end(); */

	}

	@Override
	public void dispose () {
          player.dispose();
	}
}
