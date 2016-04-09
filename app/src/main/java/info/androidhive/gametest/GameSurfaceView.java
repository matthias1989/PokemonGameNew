package info.androidhive.gametest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import info.androidhive.gametest.abstractClasses.RenderThread;
import info.androidhive.gametest.abstractClasses.Renderable;
import info.androidhive.gametest.map.MapRenderThread;
import info.androidhive.gametest.buildinginside.BuildingInsideRenderThread;

import static info.androidhive.gametest.R.drawable.battle_bg1;

/**
 * Created by matthias on 3/12/2016.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    private InteractionListener interactionListener;
    private RenderThread mThread;
    private Context mParent;
    private SurfaceHolder mHolder;

    private String status;

    public GameSurfaceView(Context context){
        super(context);
        setKeepScreenOn(true);
        getHolder().addCallback(this);
        mParent = context;

    }
    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setKeepScreenOn(true);
        getHolder().addCallback(this);
        mParent = context;

// TODO Auto-generated constructor stub
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setKeepScreenOn(true);
        getHolder().addCallback(this);
        mParent = context;
// TODO Auto-generated constructor stub
    }

    public GameSurfaceView(Context context,String status) {
        super(context);
        setKeepScreenOn(true);
        getHolder().addCallback(this);
        mParent = context;
        this.status = status;
    }

    public RenderThread getmThread() {
        return mThread;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.d("SURFACE","gamesurfacecreated");
        mHolder = holder;
        openMapThread(holder);
    }

    public void openMapThread(SurfaceHolder holder){
//        if(mThread!=null)
//            mThread.quit();
        mThread = new MapRenderThread(holder,this,status);
        mThread.start();
        mThread.getFirstLayer().setInteractionListener(interactionListener);
    }

    public void pokemoncenterEntered(SurfaceHolder holder){
        Utils.scrollCoords.put("scrollX",mThread.getBackground().getScrollX());
        Utils.scrollCoords.put("scrollY",mThread.getBackground().getScrollY()+ Renderable.tileSize);
        mThread.quit();
        mThread = new BuildingInsideRenderThread(holder,this,status,"pokecenter");   // change this after
        mThread.start();
        mThread.getFirstLayer().setInteractionListener(interactionListener);
        mThread.setSize(getWidth(), getHeight());
    }

    public void pokemarktEntered(SurfaceHolder holder){
        Utils.scrollCoords.put("scrollX",mThread.getBackground().getScrollX());
        Utils.scrollCoords.put("scrollY",mThread.getBackground().getScrollY()+Renderable.tileSize);
        mThread.quit();
        mThread = new BuildingInsideRenderThread(holder,this,status,"pokemarkt");
        mThread.start();
        mThread.getFirstLayer().setInteractionListener(interactionListener);
        mThread.setSize(getWidth(), getHeight());
    }

    public void pokemoncenterLeft(SurfaceHolder holder){
        mThread.quit();
        mThread = new MapRenderThread(holder,this,status);
        mThread.start();
        mThread.getFirstLayer().setInteractionListener(interactionListener);
        //mThread.setSize(getWidth(),getHeight());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        //holder.setFixedSize(2000,2000);
        //mThread.setSize(2000,2000);
        mThread.setSize(20000, 20000);
        Log.d("MAX_SIZE", width + "");
    }

    public void setInteractionListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public SurfaceHolder getmHolder(){
        return mHolder;
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mThread.quit();
    }


}