package info.androidhive.gametest.buildinginside;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import info.androidhive.gametest.abstractClasses.RenderThread;
import info.androidhive.gametest.abstractClasses.Renderable;

/**
 * Created by matthias on 3/20/2016.
 */
public class BuildingInsideRenderThread extends RenderThread {
    private BuildingInsideBackground buidingInsideBackground;
    public BuildingInsideRenderThread(SurfaceHolder holder, SurfaceView view, String status,String buildingName) {
        super(holder);
        Random rand = new Random();
        Renderable[]  mRenderables = new Renderable[3];
        mRenderables[0] = new BuildingInsideBackground(view,buildingName);
        mRenderables[1] = new BuildingInsideFirstLayer(view,buildingName);
        mRenderables[2] = new BuildingInsideForeground(100 + rand.nextInt(1500),view,status,buildingName);

        buidingInsideBackground = (BuildingInsideBackground) mRenderables[0];
        setBackground((BuildingInsideBackground) mRenderables[0]);
        setFirstLayer((BuildingInsideFirstLayer) mRenderables[1]);
        setForeground((BuildingInsideForeground) mRenderables[2]);
        setmRenderables(mRenderables);


    }

    public BuildingInsideBackground getBuildingInsideBackground() {return buidingInsideBackground;}
    //public BuildingInsideBackground getPokecenterFirstLayer() {return pokecenterFirstLayer;}
}
