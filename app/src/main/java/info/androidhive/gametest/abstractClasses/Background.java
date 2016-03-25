package info.androidhive.gametest.abstractClasses;

/**
 * Created by matthias on 3/20/2016.
 */
abstract public class Background extends info.androidhive.gametest.abstractClasses.Renderable {
    private float mFieldWidth;
    private float mFieldHeight;

    abstract public void getPlayField();
    abstract public int getScrollX();
    abstract public void setScrollX(int scrollX);
    abstract public int getScrollY();
    abstract public void setScrollY(int scrollY);

    abstract public int getStartPosX();
    abstract public void setStartPosX(int startPosX);
    abstract public int getStartPosY();
    abstract public void setStartPosY(int startPosY);

    public float getmFieldWidth() {
        return mFieldWidth;
    }
    public void setmFieldWidth(float mFieldWidth) {
        this.mFieldWidth = mFieldWidth;
    }
    public float getmFieldHeight() {
        return mFieldHeight;
    }
    public void setmFieldHeight(float mFieldHeight) {
        this.mFieldHeight = mFieldHeight;
    }

}
