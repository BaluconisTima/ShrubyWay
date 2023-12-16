package com.shrubyway.game.layout;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.shrubyway.game.GlobalBatch;
import com.shrubyway.game.ShrubyWay;
import com.shrubyway.game.myinterface.Button;

public class SettingsLayout extends Layout {
    static private Texture settingsText;
    static private Button doneButton;

    public SettingsLayout() {
        super();
        if (settingsText == null) settingsText = ShrubyWay.assetManager.get("interface/settings_text.png", Texture.class);
        if (doneButton == null)
            doneButton = new Button(ShrubyWay.assetManager.get("interface/done.png", Texture.class),
                    ShrubyWay.assetManager.get("interface/done2.png", Texture.class),
                    GlobalBatch.centerX() - ShrubyWay.assetManager.get("interface/done.png", Texture.class).getWidth()/2,
                    GlobalBatch.centerY() - 390);
    }

    private void done() {
        close();
    }

    @Override public void update(Vector2 mousePos) {
        super.update(mousePos);
        if(ShrubyWay.inputProcessor.isMouseLeft()) {
            if(doneButton.rectangle.checkPoint(mousePos)) done();
        }
    }

    @Override public void render(Vector2 mousePos) {
        super.render(mousePos);
        GlobalBatch.render(settingsText, GlobalBatch.centerX() - settingsText.getWidth()/2,
                GlobalBatch.centerY() - settingsText.getHeight()/2 + 350);

        if(doneButton.rectangle.checkPoint(mousePos)) doneButton.renderSellected();
        else doneButton.render();
    }

}
