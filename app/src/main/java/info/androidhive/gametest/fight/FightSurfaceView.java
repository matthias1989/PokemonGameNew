package info.androidhive.gametest.fight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import info.androidhive.gametest.FightListener;
import info.androidhive.gametest.map.MapForeground;
import info.androidhive.gametest.MainActivity;
import info.androidhive.gametest.map.MapRenderThread;
import info.androidhive.gametest.pokemons.PokemonSprite;

/**
 * Created by matthias on 3/14/2016.
 */
public class FightSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Context context;
    private FightRenderThread mThread;

    private FightListener fightListener;
    public FightSurfaceView(Context context) {
        super(context);
    }

    public FightSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setKeepScreenOn(true);
        getHolder().addCallback(this);
        this.context = context;
        Log.d("HOLDER",getHolder()+"");

// TODO Auto-generated constructor stub
    }

    public void setFightListener(FightListener fightListener) {
        this.fightListener = fightListener;
    }

    public FightSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setKeepScreenOn(true);
        getHolder().addCallback(this);
// TODO Auto-generated constructor stub
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread = new FightRenderThread(holder,this,fightListener);
        mThread.setRunning(true);
        mThread.start();
        //mThread.getFightRenderable().setFightListener(fightListener);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //mThread.quit();
    }

    public FightRenderThread getmThread() {
        return mThread;
    }
}
