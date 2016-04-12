package info.androidhive.gametest.buildinginside;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceView;

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.Utils;
import info.androidhive.gametest.abstractClasses.Foreground;
import info.androidhive.gametest.sprites.Sprite;
import info.androidhive.gametest.sprites.TrainerSprite;

/**
 * Created by matthias on 3/20/2016.
 */

public class BuildingInsideForeground extends Foreground
{
    private GameSurfaceView mParent;
    private String buildingName;
    private String status;



    private boolean actionStopped = true;

    public BuildingInsideForeground(int speed, SurfaceView view, String status, String buildingName)
    {
        this.buildingName = buildingName;
        mParent = (GameSurfaceView) view;
        init();
        this.status = status;
        if(buildingName.equals("pokecenter"))
            Utils.currentEnvironment = "inside_pokecenter";
        else if(buildingName.equals("pokemarkt"))
            Utils.currentEnvironment = "inside_pokemarkt";
        //Utils.currentEnvironment = "inside_pokecenter";
    }

    public boolean isActionStopped() {
        return actionStopped;
    }

    public void setActionStopped(boolean actionStopped) {
        this.actionStopped = actionStopped;
    }



    @Override
    public void playfield(int width, int height)
    {

    }

    @Override
    public void update(RectF dirty, double timeDelta)
    {
        setStopMovement();
        Utils.mySprite.update();

    }

    private void init(){

        if(Utils.mySprite == null || Utils.mySprite.getStatus().startsWith("back")) {
            Utils.mySprite.setStatus("backstanding");
        }
        else{
            Utils.mySprite.setStatus(status);
            if(Utils.mySprite.getStatus().startsWith("right")) Utils.mySprite.setStatus("rightstanding");
            else{
                if(Utils.mySprite.getStatus().startsWith("left")) Utils.mySprite.setStatus("leftstanding");
                else Utils.mySprite.setStatus("backstanding");
            }
            setStopMovement();
            Utils.mySprite.update();

        }
    }



    @Override
    public void draw(Canvas c)
    {
        Utils.mySprite.draw(c);


    }
    public  void goUp(){
        // save current data first
        String status = Utils.mySprite.getStatus();

        if(!status.startsWith("back")) {
            Utils.mySprite.setStatus("backstanding");
        }
        else {
            // now load the correct sprite
            if (!status.equals("backwalking1")) {
                Utils.mySprite.setStatus("backwalking1");
            } else {
                Utils.mySprite.setStatus("backwalking2");
            }

            BuildingInsideBackground pokecenterBackground = (BuildingInsideBackground) mParent.getmThread().getBackground();
            BuildingInsideFirstLayer pokecenterFirstLayer = (BuildingInsideFirstLayer) mParent.getmThread().getFirstLayer();


            if (stepAllowed(0, -Utils.tileSize)) {
                pokecenterBackground.setScrollY(pokecenterBackground.getScrollY() - Utils.steps);
                pokecenterFirstLayer.setScrollY(pokecenterFirstLayer.getScrollY() - Utils.steps);
            }
        }
    }

    public  void goDown(){
        String status = Utils.mySprite.getStatus();

        if(!status.startsWith("front")) {
            Utils.mySprite.setStatus("frontstanding");
        }
        else {
            // now load the correct sprite
            if (!status.equals("frontwalking1")) {
                Utils.mySprite.setStatus("frontwalking1");
            } else {
                Utils.mySprite.setStatus("frontwalking2");
            }

            BuildingInsideBackground pokecenterBackground = (BuildingInsideBackground) mParent.getmThread().getBackground();
            BuildingInsideFirstLayer pokecenterFirstLayer = (BuildingInsideFirstLayer) mParent.getmThread().getFirstLayer();

            boolean beforeDoor = false;
            if (stepAllowed(0, +Utils.tileSize)) {
                beforeDoor = isBeforeDoor(0, +Utils.tileSize);
                pokecenterBackground.setScrollY(pokecenterBackground.getScrollY() + Utils.steps);
                pokecenterFirstLayer.setScrollY(pokecenterFirstLayer.getScrollY() + Utils.steps);
            }
            if (beforeDoor) {
                exitPokemoncenter();
            }

        }
    }

    public  void goLeft(){
        String status = Utils.mySprite.getStatus();

        if(!status.startsWith("left")) {
            Utils.mySprite.setStatus("leftstanding");
        }
        else {
            // now load the correct sprite
            if (!status.equals("leftwalking1")) {
                Utils.mySprite.setStatus("leftwalking1");
            } else {
                Utils.mySprite.setStatus("leftwalking2");
            }

            BuildingInsideBackground pokecenterBackground = (BuildingInsideBackground) mParent.getmThread().getBackground();
            BuildingInsideFirstLayer pokecenterFirstLayer = (BuildingInsideFirstLayer) mParent.getmThread().getFirstLayer();


            if (stepAllowed(-Utils.tileSize, 0)) {
                pokecenterBackground.setScrollX(pokecenterBackground.getScrollX() - Utils.steps);
                pokecenterFirstLayer.setScrollX(pokecenterFirstLayer.getScrollX() - Utils.steps);
            }

        }

    }

    public  void goRight(){
        // save current data first
        int x = Utils.mySprite.getX();
        int y = Utils.mySprite.getY();
        String status = Utils.mySprite.getStatus();

        if(!status.startsWith("right")) {
            Utils.mySprite.setStatus("rightstanding");
        }
        else {
            // now load the correct sprite
            if (!status.equals("rightwalking1")) {
                Utils.mySprite.setStatus("rightwalking1");
            } else {
                Utils.mySprite.setStatus("rightwalking2");
            }


            BuildingInsideBackground pokecenterBackground = (BuildingInsideBackground) mParent.getmThread().getBackground();
            BuildingInsideFirstLayer pokecenterFirstLayer = (BuildingInsideFirstLayer) mParent.getmThread().getFirstLayer();


            if (stepAllowed(Utils.steps, 0)) {
                pokecenterBackground.setScrollX(pokecenterBackground.getScrollX() + Utils.steps);
                pokecenterFirstLayer.setScrollX(pokecenterFirstLayer.getScrollX() + Utils.steps);
            }

        }


    }

    private boolean isBeforeDoor(int moveX, int moveY){
        BuildingInsideBackground pokecenterBackground= (BuildingInsideBackground) mParent.getmThread().getBackground();
        int doorPosInPokemonCenterX=Utils.tileSize*8;
        int doorPosInPokemonCenterY=Utils.tileSize*9;

        int x1 = -pokecenterBackground.getScrollX() - moveX+doorPosInPokemonCenterX;
        int x2 = x1 + Utils.tileSize;
        int y =  -pokecenterBackground.getScrollY() - moveY+doorPosInPokemonCenterY;

        Log.d("Coords",x1+","+Utils.mySprite.getX());
        if( ((x1==Utils.mySprite.getX()) || (x2==Utils.mySprite.getX())) && (y==Utils.mySprite.getY())){
            return true;
        }
        return false;
    }

    private void exitPokemoncenter(){
        mParent.pokemoncenterLeft(mParent.getHolder());
   }

    public boolean stepAllowed(int moveX, int moveY){
        BuildingInsideBackground buidingInsideBackground= (BuildingInsideBackground) mParent.getmThread().getBackground();
        int x1 = -buidingInsideBackground.getScrollX() - moveX +buidingInsideBackground.getStartPosX();
        int y1 = -buidingInsideBackground.getScrollY() - moveY+buidingInsideBackground.getStartPosY();
        int x2 = x1 + (int) buidingInsideBackground.getmFieldWidth()+Utils.tileSize;
        int y2 = y1 + (int) buidingInsideBackground.getmFieldHeight();
        Rect rect = new Rect(x1,y1,x2,y2);
        if(!rect.intersect(Utils.mySprite.getX(),Utils.mySprite.getY(),Utils.mySprite.getX(),Utils.mySprite.getY())){
            if (isBeforeDoor(moveX,moveY))
                return true;
            else
                return false;
        }
        else{               // between the borders of the pokecenter
            x1 =  -buidingInsideBackground.getScrollX() - moveX + 5 *Utils.tileSize;               // balie
            y1 = -buidingInsideBackground.getScrollY() - moveY + 2* Utils.tileSize;
            x2 = x1 +  6 * Utils.tileSize;
            y2 = y1 + 3 * Utils.tileSize;
            rect = new Rect(x1,y1,x2,y2);
            if(rect.intersect(Utils.mySprite.getX(),Utils.mySprite.getY(),Utils.mySprite.getX(),Utils.mySprite.getY()))
                return false;
        }

        return true;

    }

    public Sprite getSprite(){
        return Utils.mySprite;
    }

    public void setStopMovement(){
        if(actionStopped) {         // als er niet meer op een knop gedrukt wordt, doe dan dit
            int x = Utils.mySprite.getX();
            int y = Utils.mySprite.getY();
            if (Utils.mySprite.getStatus().startsWith("back")) {
                Utils.mySprite.setStatus("backstanding");
            } else if (Utils.mySprite.getStatus().startsWith("front")) {
                Utils.mySprite.setStatus("frontstanding");
            } else if (Utils.mySprite.getStatus().startsWith("left")) {
                Utils.mySprite.setStatus("leftstanding");
            } else if (Utils.mySprite.getStatus().startsWith("right")) {
                Utils.mySprite.setStatus("rightstanding");
            }
            Utils.mySprite.setX(x);
            Utils.mySprite.setY(y);
        }
    }
    @Override
    public String checkInteractionPossible() {
        Sprite seller = null;
        for(Sprite sprite: Utils.allSprites){
            if(sprite.getName().equals("seller"))
                seller = sprite;
        }
        int helperPosX = seller.getX() - seller.getScrollX();
        int helperPosY = seller.getY() - seller.getScrollY();

        if((helperPosX==Utils.mySprite.getX()) && ((helperPosY+Utils.tileSize*2)==Utils.mySprite.getY()) && ((TrainerSprite)getSprite()).getStatus().startsWith("back")){
            return buildingName;
        }
        return "";
    }

    @Override
    public void doInteraction() {
    }

    @Override
    public void checkIfSeenByNPC() {

    }

}


