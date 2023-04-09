package com.shrubyway.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Batch;
import jdk.incubator.vector.VectorOperators;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;


public class ShrubyWay extends ApplicationAdapter {
	private Shraby player;
	private Background background;
	SpriteBatch batch;
	OrthographicCamera Camera;
	Vector2 CameraPosition;
	Vector2 MousePosition;
	TreeSet<VisibleObject> renderingObjects;
	private BitmapFont font;

	private inputAdapter InputProcessor = new inputAdapter();

	@Override
	public void create () {
		font = new BitmapFont();
		font.getData().setScale(3);

		Gdx.graphics.setWindowedMode(1920, 1080);
		Graphics.DisplayMode currentDisplayMode = Gdx.graphics.getDisplayMode();
		Gdx.graphics.setFullscreenMode(currentDisplayMode);

		player = new Shraby(0, 0);
		CameraPosition = new Vector2(player.positionCenter());
		Camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Camera.position.set(CameraPosition.x,CameraPosition.y, 0);
		Camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background = new Background();
		Gdx.input.setInputProcessor(InputProcessor);
		batch = new SpriteBatch();
		batch.enableBlending();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderingObjects = new TreeSet<VisibleObject>();
		Pixmap iconPixmap = new Pixmap(Gdx.files.internal("SWicon.png"));
		AnimationGlobalTime.x = 0f;
		Gdx.graphics.setVSync(true);

	}

    public void correctPosition() {
		Vector2 temp = new Vector2(player.position());
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
		AnimationGlobalTime.x += Gdx.graphics.getDeltaTime();
		player.Running(InputProcessor.isRuning());
		Vector2 movingVector = InputProcessor.getMovementDirection();
		if(InputProcessor.isMouseLeft()) {
			Bullet temp = player.shoot(MousePosition);
			if(temp != null) renderingObjects.add(temp);
		}
		if(InputProcessor.isMouseRight()) {
			background.addDecoration(1, MousePosition, '2');
		}
		MousePosition = new Vector2(InputProcessor.MousePosition().x + CameraPosition.x - Gdx.graphics.getWidth()/2,
				InputProcessor.MousePosition().y + CameraPosition.y - Gdx.graphics.getHeight()/2);


		background.update(1, player.positionCenter(), renderingObjects);
		renderingObjects.add(player);
		for(VisibleObject obj : renderingObjects) {
			if(obj instanceof Bullet) {
				((Bullet) obj).TryMoveTo();
			}
		}
		player.TryMoveTo(movingVector, renderingObjects);

		player.LiquidStatus(background.checkLiquid(1,player.positionLegs()));
		correctPosition();
		CameraPosition.lerp(player.positionCenter(),
				0.1f);
		Camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Camera.position.set(Math.round(CameraPosition.x),Math.round(CameraPosition.y), 0);
		Camera.update();

		batch.setProjectionMatrix(Camera.combined);

		ScreenUtils.clear(1,1,1,1);
		batch.begin();

		background.render(batch,1,player.position());
		for (VisibleObject obj : renderingObjects) {
			obj.Render(batch);
		}
		batch.end();
		renderingObjects.clear();

		batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
		Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		batch.begin();
		font.draw(batch, ""+renderingObjects.size() + " " + player.position, 100, 100);
		batch.end();




	}

	@Override
	public void dispose () {
         // player.dispose();
	}
}
