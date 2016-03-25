package info.androidhive.gametest.buildinginside;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.abstractClasses.RenderThread;
import info.androidhive.gametest.abstractClasses.Renderable;

/**
 * Created by matthias on 3/20/2016.
 */
public class BuildingInsideRenderThread extends RenderThread {
    private BuidingInsideBackground buidingInsideBackground;
    public BuildingInsideRenderThread(SurfaceHolder holder, SurfaceView view, String status,String buildingName) {
        super(holder);
        Random rand = new Random();
        Renderable[]  mRenderables = new Renderable[3];
        mRenderables[0] = new BuidingInsideBackground(view,buildingName);
        mRenderables[1] = new BuildingInsideFirstLayer(view,buildingName);
        mRenderables[2] = new BuildingInsideForeground(100 + rand.nextInt(1500),view,status,buildingName);

        buidingInsideBackground = (BuidingInsideBackground) mRenderables[0];
        setBackground((BuidingInsideBackground) mRenderables[0]);
        setFirstLayer((BuildingInsideFirstLayer) mRenderables[1]);
        setForeground((BuildingInsideForeground) mRenderables[2]);
        setmRenderables(mRenderables);


    }

    public BuidingInsideBackground getBuildingInsideBackground() {return buidingInsideBackground;}
    //public BuidingInsideBackground getPokecenterFirstLayer() {return pokecenterFirstLayer;}
}
