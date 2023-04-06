import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Bush {
    private final Vector2 position = new Vector2();
    private Texture texture = new Texture(Gdx.files.internal("Decorations/BUSH.png"));
    public Bush(float x, float y) {
        position.set(new Vector2(x, y));
    }
    public void Render(Batch batch) {
        batch.draw(texture, position.x - texture.getWidth() / 2,
                position.y - texture.getHeight() / 2);
    }
}
