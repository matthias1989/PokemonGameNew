package info.androidhive.gametest.abstractClasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.view.SurfaceHolder;

import info.androidhive.gametest.fight.FightRenderable;

/**
 * Created by matthias on 3/12/2016.
 */

// src: https://gist.github.com/slightfoot/a519bc3627f49c44a226
public abstract class RenderThread extends Thread
{
    private Foreground foreground;
    private Background background;
    private FirstLayer firstLayer;

    private SurfaceHolder mHolder;
    private boolean       mQuit;
    private int           mWidth;
    private int           mHeight;
    private Renderable[]  mRenderables;


    private Paint mDebugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final boolean mDebugEnable = true; // flip me to see clip rect

    public RenderThread(SurfaceHolder holder){
        mHolder = holder;

        mDebugPaint.setColor(Color.GREEN);
    }


    public void setSize(int width, int height)
    {
        mWidth  = width;
        mHeight = height;
        for(int i = 0; i < mRenderables.length; i++){
            mRenderables[i].playfield(width, height);
        }
    }

    @Override
    public void run()
    {
        mQuit = false;
        Rect dirty  = new Rect();
        RectF dirtyF = new RectF();
        double dt = 1/16.0f;
        double currentTime = SystemClock.elapsedRealtime();
        while(!mQuit){
            double newTime = SystemClock.elapsedRealtime();
            double frameTime = (newTime - currentTime) / 1000.0f;
            currentTime = newTime;
            dirtyF.setEmpty();
            while(frameTime > 0.0){
                double deltaTime = Math.min(frameTime, dt);
                integrate(dirtyF, 1.0f * deltaTime);
                frameTime -= deltaTime;
            }
            dirty.set((int)dirtyF.left, (int)dirtyF.top,
                    (int)Math.round(dirtyF.right),
                    (int)Math.round(dirtyF.bottom));
            render(dirty);
        }
    }

    private void integrate(RectF dirty, double timeDelta)
    {
        for(int i = 0; i < mRenderables.length; i++){
            final Renderable renderable = mRenderables[i];
            renderable.unionRect(dirty);
            renderable.update(dirty, timeDelta);
            renderable.unionRect(dirty);
        }
    }

    private void render(Rect dirty)
    {

        Canvas c = mHolder.lockCanvas(!mDebugEnable ? dirty : null);
        if(c != null){
            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            if(mDebugEnable){
                c.drawRect(dirty, mDebugPaint);
            }
            for(int i = 0; i < mRenderables.length; i++){
                mRenderables[i].draw(c);
            }
            mHolder.unlockCanvasAndPost(c);
        }
    }

    public void quit()
    {
        mQuit = true;
        try{
            this.join();
        }
        catch(InterruptedException e){
            //
        }
    }

    public SurfaceHolder getmHolder() {
        return mHolder;
    }

    public void setmHolder(SurfaceHolder mHolder) {
        this.mHolder = mHolder;
    }

    public Renderable[] getmRenderables() {
        return mRenderables;
    }

    public void setmRenderables(Renderable[] mRenderables) {
        this.mRenderables = mRenderables;
    }

    public Foreground getForeground() {return foreground;}

    public void setForeground(Foreground foreground){
        this.foreground = foreground;
    }

    public Background getBackground() {return background;}
    public FirstLayer getFirstLayer() {return firstLayer;}

    public void setBackground(Background background) {
        this.background = background;
    }

    public void setFirstLayer(FirstLayer firstLayer) {
        this.firstLayer = firstLayer;
    }


};
