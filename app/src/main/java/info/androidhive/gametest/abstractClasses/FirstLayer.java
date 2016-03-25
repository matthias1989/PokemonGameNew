package info.androidhive.gametest.abstractClasses;

import java.util.List;

import info.androidhive.gametest.sprites.Sprite;

/**
 * Created by matthias on 3/20/2016.
 */
abstract public class FirstLayer extends info.androidhive.gametest.abstractClasses.Renderable {
    abstract public int getScrollX();

    abstract public void setScrollX(int scrollX);

    abstract public int getScrollY();

    abstract public void setScrollY(int scrollY);

    abstract public List<Sprite> getSprites();
    abstract public void addSprite(Sprite sprite);
    abstract public void setSprites(List<Sprite> sprites);
}
