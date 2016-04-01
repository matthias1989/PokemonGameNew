package info.androidhive.gametest.fight;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;

import java.util.Random;

import info.androidhive.gametest.abstractClasses.Background;
import info.androidhive.gametest.abstractClasses.FirstLayer;
import info.androidhive.gametest.abstractClasses.Foreground;
import info.androidhive.gametest.abstractClasses.RenderThread;
import info.androidhive.gametest.abstractClasses.Renderable;



/**
 * Created by matthias on 3/23/2016.
 */
public class FightRenderThread extends Thread{

    private SurfaceHolder mHolder;
    private boolean       mQuit;
    private int           mWidth;
    private int           mHeight;
    private Renderable[]  mRenderables;
    private FightRenderable fightRenderable;
    private FightBackground fightBackground;
    private boolean running;
    private Paint mPaint;
    private Paint mDebugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final boolean mDebugEnable = true; // flip me to see clip rect


    public FightRenderThread(SurfaceHolder holder,SurfaceView view) {
        mHolder = holder;
        Renderable[]  mRenderables = new Renderable[2];

        fightRenderable = new FightRenderable(view,holder);
        fightBackground = new FightBackground(view,holder);

        mRenderables[0] = fightBackground;
        mRenderables[1] = fightRenderable;

        setmRenderables(mRenderables);
    }

    public FightRenderable getFightRenderable(){
        return fightRenderable;
    }
    public FightBackground getFightBackground(){
        return fightBackground;
    }



    public void setSize(int width, int height)
    {
        mWidth  = width;
        mHeight = height;
        for(int i = 0; i < mRenderables.length; i++){
            mRenderables[i].playfield(width, height);
        }
    }

    public void setRunning(boolean run){
        running = run;
    }
    @Override
    public void run()
    {
        while(running){
            Canvas canvas = mHolder.lockCanvas();
            if(canvas != null){
                synchronized (mHolder){
                    for(int i = 0; i < mRenderables.length; i++){
                        mRenderables[i].draw(canvas);
                    }
                }
                mHolder.unlockCanvasAndPost(canvas);
            }
            //if(FightRenderable.wildPokemonLoaded==false) {
                try {
                    sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


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






}
