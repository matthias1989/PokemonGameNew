package info.androidhive.gametest.map;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.abstractClasses.RenderThread;
import info.androidhive.gametest.abstractClasses.Renderable;

/**
 * Created by matthias on 3/20/2016.
 */
public class MapRenderThread extends RenderThread {


    public MapRenderThread(SurfaceHolder holder, SurfaceView view,String status)
    {
        super(holder);

        Random rand = new Random();
        Renderable[]  mRenderables = new Renderable[3];
        mRenderables[0] = new MapBackground(view);
        mRenderables[1] = new MapFirstLayer(view);
        mRenderables[2] = new MapForeground(100 + rand.nextInt(1500),view,status);

        setBackground((MapBackground) mRenderables[0]);
        setFirstLayer((MapFirstLayer) mRenderables[1]);
        setForeground((MapForeground) mRenderables[2]);


        setmRenderables(mRenderables);
    }



}
