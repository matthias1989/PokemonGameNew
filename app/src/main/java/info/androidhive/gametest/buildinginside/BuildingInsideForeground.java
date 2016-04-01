package info.androidhive.gametest.buildinginside;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceView;

import java.util.Random;

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.abstractClasses.Foreground;
import info.androidhive.gametest.R;
import info.androidhive.gametest.sprites.Sprite;
import info.androidhive.gametest.pokemons.PokemonSprite;

/**
 * Created by matthias on 3/20/2016.
 */

public class BuildingInsideForeground extends Foreground
{
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);




    private Random random = new Random();


    public static PokemonSprite pokemonSprite;

    private Sprite mySprite;
    private Sprite frontStanding;
    private Sprite frontWalkingRight;
    private Sprite frontWalkingLeft;
    private Sprite backStanding;
    private Sprite backWalkingRight;
    private Sprite backWalkingLeft;
    private Sprite rightStanding;
    private Sprite rightWalkingLeft;
    private Sprite rightWalkingRight;
    private Sprite leftStanding;
    private Sprite leftWalkingLeft;
    private Sprite leftWalkingRight;

    private GameSurfaceView mParent;

    private int requiredBorderSpace=32;

    private int spriteHeight = 48;
    private int spriteWidth = 32;
    private int spritePosX = steps*7+tileSize/2; // X = 5
    private int spritePosY = steps *7; // Y  = 0

    private float mFieldWidth;
    private float mFieldHeight;

    private String buildingName;


    private String status;



    private boolean actionStopped = true;

    public BuildingInsideForeground(int speed, SurfaceView view, String status, String buildingName)
    {
        this.buildingName = buildingName;
        mParent = (GameSurfaceView) view;
        //mySprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.front_standing), 100, 100);
        init();
        this.status = status;



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
        //mSize = width / 7.0f;
        mFieldWidth = width;
        mFieldHeight = height;
    }

    @Override
    public void update(RectF dirty, double timeDelta)
    {
        setStopMovement();
        mySprite.update();

    }

    private void init(){
        frontStanding = createSprite(R.drawable.front_standing);
        //frontStanding = new Sprite(BitmapFactory.decodeResource(mParent.getResources(), R.drawable.front_standing), spritePosX, spritePosY,spriteWidth,spriteHeight);

        frontWalkingRight = createSprite(R.drawable.front_right_walking);
        frontWalkingLeft = createSprite(R.drawable.front_left_walking);
        backStanding = createSprite(R.drawable.back_standing);
        backWalkingLeft = createSprite(R.drawable.back_walking_left);
        backWalkingRight = createSprite(R.drawable.back_walking_right);
        rightStanding = createSprite(R.drawable.right_standing);
        rightWalkingLeft = createSprite(R.drawable.right_walking_left);
        rightWalkingRight = createSprite(R.drawable.right_walking_right);
        leftStanding = createSprite(R.drawable.left_standing);
        leftWalkingLeft = createSprite(R.drawable.left_walking_left);
        leftWalkingRight = createSprite(R.drawable.left_walking_right);

        if(mySprite == null || mySprite.getStatus().startsWith("back")) {
            mySprite = backStanding;                    // first sprite is one with the back
            mySprite.setStatus("backStanding");
        }
        else{
            mySprite.setStatus(status);
            if(mySprite.getStatus().startsWith("right")) mySprite = rightStanding;
            else{
                if(mySprite.getStatus().startsWith("left")) mySprite = leftStanding;
                else mySprite = frontStanding;
            }
            setStopMovement();
            mySprite.update();

        }
    }

    public Sprite createSprite(int id){
        Bitmap a = BitmapFactory.decodeResource(mParent.getResources(), id);
        a = Bitmap.createScaledBitmap(a, tileSize, tileSize*3/2, false);
        return new Sprite(a, spritePosX, spritePosY,tileSize,tileSize);
        //return new Sprite(a, spritePosX, spritePosY,spriteWidth,spriteHeight);
    }

    @Override
    public void draw(Canvas c)
    {
        mySprite.draw(c);


    }
    public  void goUp(){
        // save current data first
        String status = mySprite.getStatus();

        // now load the correct sprite
        if(!status.equals("backWalkingLeft")){
            mySprite = backWalkingLeft;
            mySprite.setStatus("backWalkingLeft");
        }
        else{
            mySprite = backWalkingRight;
            mySprite.setStatus("backWalkingRight");
        }

        BuidingInsideBackground pokecenterBackground =  (BuidingInsideBackground) mParent.getmThread().getBackground();
        BuildingInsideFirstLayer pokecenterFirstLayer = (BuildingInsideFirstLayer) mParent.getmThread().getFirstLayer();

        int col = (pokecenterBackground.getScrollX() - pokecenterBackground.getStartPosX()+ spritePosX) / steps;
        int row = (pokecenterBackground.getScrollY() - pokecenterBackground.getStartPosY()+spritePosY - steps) / steps;


        if (stepAllowed(0,-tileSize)) {
            pokecenterBackground.setScrollY(pokecenterBackground.getScrollY() - steps);
            pokecenterFirstLayer.setScrollY(pokecenterFirstLayer.getScrollY() - steps);
        }


    }

    public  void goDown(){
        String status = mySprite.getStatus();

        // now load the correct sprite
        if(!status.equals("frontWalkingLeft")){
            mySprite = frontWalkingLeft;
            mySprite.setStatus("frontWalkingLeft");
        }
        else{
            mySprite = frontWalkingRight;
            mySprite.setStatus("frontWalkingRight");
        }

        BuidingInsideBackground pokecenterBackground =  (BuidingInsideBackground) mParent.getmThread().getBackground();
        BuildingInsideFirstLayer pokecenterFirstLayer = (BuildingInsideFirstLayer) mParent.getmThread().getFirstLayer();

        int col = (pokecenterBackground.getScrollX() - pokecenterBackground.getStartPosX() + spritePosX) / steps;
        int row = (pokecenterBackground.getScrollY() - pokecenterBackground.getStartPosY()+ spritePosY + steps) / steps;


        boolean beforeDoor = false;
        if (stepAllowed(0, +tileSize)) {
            beforeDoor = isBeforeDoor(0, +tileSize);
            pokecenterBackground.setScrollY(pokecenterBackground.getScrollY() + steps);
            pokecenterFirstLayer.setScrollY(pokecenterFirstLayer.getScrollY() + steps);
        }
        if (beforeDoor) {
            exitPokemoncenter();
        }


    }

    public  void goLeft(){
        String status = mySprite.getStatus();

        // now load the correct sprite
        if(!status.equals("leftWalkingLeft")){
            mySprite = leftWalkingLeft;
            mySprite.setStatus("leftWalkingLeft");
        }
        else{
            mySprite = leftWalkingRight;
            mySprite.setStatus("leftWalkingRight");
        }

        BuidingInsideBackground pokecenterBackground =  (BuidingInsideBackground) mParent.getmThread().getBackground();
        BuildingInsideFirstLayer pokecenterFirstLayer = (BuildingInsideFirstLayer) mParent.getmThread().getFirstLayer();

        int col = (pokecenterBackground.getScrollX() - pokecenterBackground.getStartPosX()+spritePosX - steps) / steps;
        int row = (pokecenterBackground.getScrollY() - pokecenterBackground.getStartPosY()+spritePosY) / steps;


        if (stepAllowed(-tileSize, 0)) {
            pokecenterBackground.setScrollX(pokecenterBackground.getScrollX() - steps);
            pokecenterFirstLayer.setScrollX(pokecenterFirstLayer.getScrollX() - steps);
        }



    }

    public  void goRight(){
        // save current data first
        int x = mySprite.getX();
        int y = mySprite.getY();
        String status = mySprite.getStatus();

        // now load the correct sprite
        if(!status.equals("rightWalkingLeft")){
            mySprite = rightWalkingLeft;
            mySprite.setStatus("rightWalkingLeft");
        }
        else{
            mySprite = rightWalkingRight;
            mySprite.setStatus("rightWalkingRight");
        }


        BuidingInsideBackground pokecenterBackground =  (BuidingInsideBackground) mParent.getmThread().getBackground();
        BuildingInsideFirstLayer pokecenterFirstLayer = (BuildingInsideFirstLayer) mParent.getmThread().getFirstLayer();

        int col = (pokecenterBackground.getScrollX() - pokecenterBackground.getStartPosX()+ spritePosX + steps) / steps;
        int row = (pokecenterBackground.getScrollY() - pokecenterBackground.getStartPosY() + spritePosY) / steps;


        if (stepAllowed(tileSize, 0)) {
            pokecenterBackground.setScrollX(pokecenterBackground.getScrollX() + steps);
            pokecenterFirstLayer.setScrollX(pokecenterFirstLayer.getScrollX() + steps);
        }




    }

    private boolean isBeforeDoor(int moveX, int moveY){
        BuidingInsideBackground pokecenterBackground= (BuidingInsideBackground) mParent.getmThread().getBackground();
        int doorPosInPokemonCenterX=tileSize*7+tileSize/2;
        int doorPosInPokemonCenterY=tileSize*9;

        int x1 = -pokecenterBackground.getScrollX() - moveX+doorPosInPokemonCenterX;
        int x2 = x1 + tileSize;
        int y =  -pokecenterBackground.getScrollY() - moveY+doorPosInPokemonCenterY;
        Log.d("coords",x1+"->"+x2+","+y);
        Log.d("coords2",spritePosX+","+spritePosY);
        if( ((x1==spritePosX) || (x2==spritePosX)) && (y==spritePosY)){
            Log.d("CENTER","Enter the door");
            return true;
        }
        return false;
    }

    private void exitPokemoncenter(){
        mParent.pokemoncenterLeft(mParent.getHolder());
        //Log.d("ENTER", "Leaving the pokemon center...");
   }

    public boolean stepAllowed(int moveX, int moveY){
        BuidingInsideBackground buidingInsideBackground= (BuidingInsideBackground) mParent.getmThread().getBackground();
        int x1 = -buidingInsideBackground.getScrollX() - moveX +buidingInsideBackground.getStartPosX();
        int y1 = -buidingInsideBackground.getScrollY() - moveY+buidingInsideBackground.getStartPosY()+tileSize;
        int x2 = x1 + (int) buidingInsideBackground.getmFieldWidth();
        int y2 = y1 + (int) buidingInsideBackground.getmFieldHeight()-tileSize;
       Log.d("RECT","["+x1+","+x2+","+y1+","+y2+"] <-> ["+spritePosX+","+spritePosY+"]");
        Rect rect = new Rect(x1,y1,x2,y2);
        if(!rect.intersect(spritePosX,spritePosY,spritePosX,spritePosY)){
            Log.d("CHECK","collision!");
            //return false;
            if (isBeforeDoor(moveX,moveY))
                return true;
            else
                return false;
        }
        else{               // between the borders of the pokecenter
            x1 =  -buidingInsideBackground.getScrollX() - moveX + 5 *tileSize;               // balie
            y1 = -buidingInsideBackground.getScrollY() - moveY + 2* tileSize;
            x2 = x1 +  6 * tileSize;
            y2 = y1 + 3 * tileSize;
//            Log.d("RECT","["+x1+","+x2+","+y1+","+y2+"] <-> ["+spritePosX+","+spritePosY+"]");
            rect = new Rect(x1,y1,x2,y2);
            if(rect.intersect(spritePosX,spritePosY,spritePosX+spriteWidth,spritePosY+spriteHeight))
                return false;
        }

        return true;

    }

    public Sprite getSprite(){
        return mySprite;
    }

    public void setStopMovement(){
        if(actionStopped) {         // als er niet meer op een knop gedrukt wordt, doe dan dit
            int x = mySprite.getX();
            int y = mySprite.getY();
            if (mySprite.getStatus().startsWith("back")) {
                mySprite = backStanding;
                mySprite.setStatus("backStanding");
            } else if (mySprite.getStatus().startsWith("front")) {
                mySprite = frontStanding;
                mySprite.setStatus("frontStanding");
            } else if (mySprite.getStatus().startsWith("left")) {
                mySprite = leftStanding;
                mySprite.setStatus("leftStanding");
            } else if (mySprite.getStatus().startsWith("right")) {
                mySprite = rightStanding;
                mySprite.setStatus("rightStanding");
            }
            mySprite.setX(x);
            mySprite.setY(y);
        }
    }
    @Override
    public String checkInteractionPossible() {

        Sprite helper = mParent.getmThread().getFirstLayer().getSprites().get(0);
        int helperPosX = helper.getX() - helper.getScrollX();
        int helperPosY = helper.getY() - helper.getScrollY();

        Log.d("INTERACTION","interaction? ["+helperPosX+","+helperPosY+"] => ["+spritePosX+","+spritePosY+"]");
        if((helperPosX==spritePosX) && ((helperPosY+tileSize*2)==spritePosY) && getSprite().getStatus().startsWith("back")){
            return buildingName;
        }
        return "";
    }

    @Override
    public void doInteraction() {
        //Log.d("INTERACTION","interaction!!!");
    }

}


