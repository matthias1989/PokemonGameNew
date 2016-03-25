package info.androidhive.gametest.abstractClasses;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by matthias on 3/12/2016.
 */
public abstract class Renderable
{
    protected final RectF mRect = new RectF();
    public static final int steps=64;
    public static final int tileSize = 64;

    public abstract void playfield(int width, int height);
    public abstract void update(RectF dirty, double timeDelta);
    public abstract void draw(Canvas c);

    public final void unionRect(RectF dirty)
    {
        dirty.union(mRect);
    }



}
