package info.androidhive.gametest.abstractClasses;

import info.androidhive.gametest.sprites.Sprite;

/**
 * Created by matthias on 3/20/2016.
 */
abstract public class Foreground extends info.androidhive.gametest.abstractClasses.Renderable {

    abstract public boolean isActionStopped();
    abstract public void setActionStopped(boolean actionStopped);


    abstract public  void goUp();
    abstract public  void goDown();
    abstract public  void goLeft();
    abstract public  void goRight();


    abstract public boolean stepAllowed(int row,int col);

    abstract public Sprite getSprite();

    abstract public void setStopMovement();

    abstract public String checkInteractionPossible();

    abstract public void doInteraction();

}
