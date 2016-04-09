package info.androidhive.gametest.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import info.androidhive.gametest.DatabaseFileHandler;
import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.Utils;
import info.androidhive.gametest.abstractClasses.Foreground;
import info.androidhive.gametest.MainActivity;
import info.androidhive.gametest.sprites.PokecenterSprite;
import info.androidhive.gametest.sprites.PokemarktSprite;
import info.androidhive.gametest.R;
import info.androidhive.gametest.sprites.Sprite;
import info.androidhive.gametest.pokemons.PokemonSprite;

/**
 * Created by matthias on 3/12/2016.
 */
public class MapForeground extends Foreground
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
    private MapBackground background;
    private MapFirstLayer firstLayer;

    private int requiredBorderSpace=32;

    private int spriteHeight = 48;
    private int spriteWidth = 32;
    private int spritePosX = tileSize*7+spriteWidth; // X = 5
    private int spritePosY = tileSize *7; // Y  = 0

    private float mFieldWidth;
    private float mFieldHeight;

    private HashMap<String,PokemonSprite> fight;

    private String status;

    private boolean beforeDoor;
    private String spriteForm="";



    private boolean actionStopped = true;

    public MapForeground(int speed, SurfaceView view, String status)
    {
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
        if(!Utils.searched){
            if(background != null) {
                int col = (background.getScrollX()+spritePosX-steps)/tileSize;
                int row = (background.getScrollY()+spritePosY)/tileSize;
                if (background.getBackgroundTile(row,col) == 'g' && !Utils.pokemonFound) {
                    if(!Utils.pokemonFound) {
                        appearPokemon();
                    }
                }
                Utils.searched = true;
            }

        }
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

        if(mySprite == null || mySprite.getStatus().startsWith("front")) {
            mySprite = frontStanding;
            mySprite.setStatus("frontStanding");
        }
        else{
            mySprite.setStatus(status);
            if(mySprite.getStatus().startsWith("right")) mySprite = rightStanding;
            else{
                if(mySprite.getStatus().startsWith("left")) mySprite = leftStanding;
                else mySprite = backStanding;
            }
            setStopMovement();
            mySprite.update();

        }
    }

    public Sprite createSprite(int id){
        Bitmap a = BitmapFactory.decodeResource(mParent.getResources(), id);
        a = Bitmap.createScaledBitmap(a, tileSize, tileSize*3/2, false);
        return new Sprite(a, spritePosX, spritePosY,spriteWidth,spriteHeight);
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

        background = (MapBackground) mParent.getmThread().getBackground();
        firstLayer = (MapFirstLayer) mParent.getmThread().getFirstLayer();

        int col = (background.getScrollX() + spritePosX) / tileSize;
        int row = (background.getScrollY() + spritePosY - steps) / tileSize;

        beforeDoor = false;
        if(background.getBackgroundTile(row,col) != '-') {
            if (stepAllowed(0, -tileSize)) {
                //beforeDoor = isBeforeDoor(firstLayer.getScrollX(), firstLayer.getScrollY()-tileSize);
                background.setScrollY(background.getScrollY() - steps);
                firstLayer.setScrollY(firstLayer.getScrollY() - steps);
                Utils.searched = false;
            }
            if (beforeDoor) {
                if(spriteForm.equals("pokecenter"))
                    enterPokemoncenter();
                else if(spriteForm.equals("pokemarkt"))
                    enterPokemarkt();

            }
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
        background = (MapBackground) mParent.getmThread().getBackground();
        firstLayer = (MapFirstLayer) mParent.getmThread().getFirstLayer();

        int col = (background.getScrollX() + spritePosX) / tileSize;
        int row = (background.getScrollY() + spritePosY + steps) / tileSize;

        if(background.getBackgroundTile(row,col) != '-') {
            if (stepAllowed(0, tileSize)) {
                background.setScrollY(background.getScrollY() + steps);
                firstLayer.setScrollY(firstLayer.getScrollY() + steps);
                Utils.searched = false;
            }
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
        background = (MapBackground) mParent.getmThread().getBackground();
        firstLayer = (MapFirstLayer) mParent.getmThread().getFirstLayer();
        int col = (background.getScrollX() + spritePosX - steps) / tileSize;
        int row = (background.getScrollY() + spritePosY) / tileSize;

        if(background.getBackgroundTile(row,col) != '-') {
            if (stepAllowed(-tileSize, 0)) {
                background.setScrollX(background.getScrollX() - steps);
                firstLayer.setScrollX(firstLayer.getScrollX() - steps);
                Utils.searched = false;
            }
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


        background = (MapBackground) mParent.getmThread().getBackground();
        firstLayer = (MapFirstLayer) mParent.getmThread().getFirstLayer();

        int col = (background.getScrollX() + spritePosX + steps) / tileSize;
        int row = (background.getScrollY() + spritePosY) / tileSize;


        if(background.getBackgroundTile(row,col) != '-') {
            if (stepAllowed(tileSize, 0)) {
                background.setScrollX(background.getScrollX() + steps);
                firstLayer.setScrollX(firstLayer.getScrollX() + steps);
                Utils.searched = false;
            }
        }





    }

    private void isBeforeDoor(int newScrollX, int newScrollY,Sprite sprite,int doorPosInBuildingX, int doorPosInBuildingY){


        int x = sprite.getX() - spritePosX -doorPosInBuildingX;
        int y = sprite.getY() - spritePosY -doorPosInBuildingY;
        if((x==newScrollX) && (y==newScrollY)){
            beforeDoor = true;
            if(sprite instanceof PokecenterSprite)
                spriteForm = "pokecenter";
            else if(sprite instanceof PokemarktSprite)
                spriteForm = "pokemarkt";
        }
        else
            beforeDoor = false;
    }

    private void enterPokemoncenter(){
        mParent.pokemoncenterEntered(mParent.getHolder());
    }

    private void enterPokemarkt(){
        mParent.pokemarktEntered(mParent.getHolder());
    }

    public boolean stepAllowed(int moveX,int moveY){



        List<Sprite> buildings = mParent.getmThread().getFirstLayer().getSprites();

        for (Sprite sprite : buildings)
        {
            // pokecenter
            int x1 = firstLayer.getScrollX() +moveX;
            int y1 = firstLayer.getScrollY() +moveY;
            int x2 = x1 + (int) sprite.getWidth();
            int y2 = y1 + (int) sprite.getHeight();
            Rect rect = new Rect(x1,y1,x2,y2);

            // char
            int posX = sprite.getX()-spritePosX;
            int posY = sprite.getY()-spritePosY;
            if(rect.intersect(posX,posY,posX,posY)){
                int doorPosInBuildingX = 0;
                int doorPosInBuildingY = 0;
                if(sprite instanceof PokecenterSprite) {
                    spriteForm="pokecenter";
                    doorPosInBuildingX = tileSize * 2 + tileSize / 2;
                    doorPosInBuildingY = tileSize;
                }
                if(sprite instanceof PokemarktSprite) {
                    doorPosInBuildingX = tileSize * 2 + tileSize / 2;
                    doorPosInBuildingY = tileSize;
                }
                isBeforeDoor(x1, y1, sprite,doorPosInBuildingX,doorPosInBuildingY );
                if (beforeDoor)
                    return true;
                else
                    return false;
            }
        }


        return true;
    }
    private void appearPokemon() {
        int getal = random.nextInt(100);
        if (getal < 16) {
            Utils.pokemonFound = true;
            if (getal < 8) {
                pokemonSprite = new PokemonSprite("rattata", Utils.ds);
            } else {
                if (getal < 14) {
                    pokemonSprite = new PokemonSprite("pidgey",Utils.ds);
                } else {
                    pokemonSprite = new PokemonSprite("spearow",Utils.ds);
                }
            }
            pokemonSprite.setCurrentExperience(110);
            pokemonSprite.setCurrentHP(pokemonSprite.getStats().getHp());
            //pokemonSprite.setLevel(38);
        }
        else {
            Utils.pokemonFound = false;
            //pokemonSprite = null;
        }
    }


    public HashMap<String,PokemonSprite> getFight(){
        return fight;
    }

    public void setFight(HashMap<String,PokemonSprite> fight){
        this.fight = fight;
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
        return "";
    }

    @Override
    public void doInteraction() {

    }

}

