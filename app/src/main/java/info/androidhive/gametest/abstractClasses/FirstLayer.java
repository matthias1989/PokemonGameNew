package info.androidhive.gametest.abstractClasses;

import java.util.List;

import info.androidhive.gametest.InteractionListener;
import info.androidhive.gametest.sprites.Sprite;

/**
 * Created by matthias on 3/20/2016.
 */
abstract public class FirstLayer extends info.androidhive.gametest.abstractClasses.Renderable {
    public InteractionListener interactionListener;
    abstract public int getScrollX();

    abstract public void setScrollX(int scrollX);

    abstract public int getScrollY();

    abstract public void setScrollY(int scrollY);


    public void setInteractionListener(InteractionListener interactionListener){
        this.interactionListener = interactionListener;
    }
}
