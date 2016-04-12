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

import info.androidhive.gametest.GameSurfaceView;
import info.androidhive.gametest.Utils;
import info.androidhive.gametest.abstractClasses.Foreground;
import info.androidhive.gametest.R;
import info.androidhive.gametest.sprites.Sprite;
import info.androidhive.gametest.pokemons.PokemonSprite;
import info.androidhive.gametest.sprites.TrainerSprite;

/**
 * Created by matthias on 3/12/2016.
 */
public class MapForeground extends Foreground
{
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Random random = new Random();

    private GameSurfaceView mParent;
    private MapBackground background;
    private MapFirstLayer firstLayer;

    private int requiredBorderSpace=32;

    private float mFieldWidth;
    private float mFieldHeight;

    private HashMap<String,PokemonSprite> fight;

    private boolean noStep = false;

    private boolean beforeDoor;
    private String spriteForm="";




    private boolean actionStopped = true;

    public MapForeground(int speed, SurfaceView view)
    {
        mParent = (GameSurfaceView) view;
        init();
        Utils.currentEnvironment = "outside";
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
        Utils.mySprite.update();
        if(!Utils.searched){
            if(background != null) {
                int col = (background.getScrollX()+Utils.mySprite.getX()-Utils.steps)/Utils.tileSize;
                int row = (background.getScrollY()+Utils.mySprite.getY())/Utils.tileSize;
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


        if(Utils.mySprite.getStatus().startsWith("front")) {
            Utils.mySprite.setStatus("frontstanding");
        }
        else{
            //Utils.mySprite.setStatus(Utils.status);       // status appears to be null, sometimes, why?
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
            if (!status.equals("backwalking1")) {
                Utils.mySprite.setStatus("backwalking1");
            } else {
                Utils.mySprite.setStatus("backwalking2");
            }

            background = (MapBackground) mParent.getmThread().getBackground();
            firstLayer = (MapFirstLayer) mParent.getmThread().getFirstLayer();

            int col = (background.getScrollX() + Utils.mySprite.getX()) / Utils.tileSize;
            int row = (background.getScrollY() + Utils.mySprite.getY() - Utils.steps) / Utils.tileSize;
            beforeDoor = false;
            if (background.getBackgroundTile(row, col) != '-') {
                if (stepAllowed(0, -Utils.tileSize)) {
                    //beforeDoor = isBeforeDoor(firstLayer.getScrollX(), firstLayer.getScrollY()-tileSize);
                    background.setScrollY(background.getScrollY() - Utils.steps);
                    firstLayer.setScrollY(firstLayer.getScrollY() - Utils.steps);
                    Utils.searched = false;
                }
                if (beforeDoor) {
                    if (spriteForm.equals("pokecenter"))
                        enterPokemoncenter();
                    else if (spriteForm.equals("pokemarkt"))
                        enterPokemarkt();
                }
                if(Utils.currentEnvironment.equals("outside"))
                    checkIfSeenByNPC();
            }

        }
    }

    public  void goDown(){
        String status = Utils.mySprite.getStatus();

        if(!status.startsWith("front")) {
            Utils.mySprite.setStatus("frontstanding");
        }
        else {
            if (!status.equals("frontwalking1")) {
                Utils.mySprite.setStatus("frontwalking1");
            } else {
                Utils.mySprite.setStatus("frontwalking2");
            }
            background = (MapBackground) mParent.getmThread().getBackground();
            firstLayer = (MapFirstLayer) mParent.getmThread().getFirstLayer();

            int col = (background.getScrollX() + Utils.mySprite.getX()) / Utils.tileSize;
            int row = (background.getScrollY() + Utils.mySprite.getY() + Utils.steps) / Utils.tileSize;

            if (background.getBackgroundTile(row, col) != '-') {
                if (stepAllowed(0, Utils.tileSize)) {
                    background.setScrollY(background.getScrollY() + Utils.steps);
                    firstLayer.setScrollY(firstLayer.getScrollY() + Utils.steps);
                    Utils.searched = false;
                }
            }
            if(Utils.currentEnvironment.equals("outside"))
                checkIfSeenByNPC();

        }
    }

    public  void goLeft(){

        String status = Utils.mySprite.getStatus();

        if(!status.startsWith("left")) {
            Utils.mySprite.setStatus("leftstanding");
        }
        else {
            if (!status.equals("leftwalking1"))
                Utils.mySprite.setStatus("leftwalking1");
            else
                Utils.mySprite.setStatus("leftwalking2");

            background = (MapBackground) mParent.getmThread().getBackground();
            firstLayer = (MapFirstLayer) mParent.getmThread().getFirstLayer();
            int col = (background.getScrollX() + Utils.mySprite.getX() - Utils.steps) / Utils.tileSize;
            int row = (background.getScrollY() + Utils.mySprite.getY()) / Utils.tileSize;

            if (background.getBackgroundTile(row, col) != '-') {
                if (stepAllowed(-Utils.tileSize, 0)) {
                    background.setScrollX(background.getScrollX() - Utils.steps);
                    firstLayer.setScrollX(firstLayer.getScrollX() - Utils.steps);
                    Utils.searched = false;
                }
            }
            if(Utils.currentEnvironment.equals("outside"))
                checkIfSeenByNPC();

        }
    }

    public  void goRight(){
        // save current data first
//        int x = mySprite.getX();
//        int y = mySprite.getY();
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


            background = (MapBackground) mParent.getmThread().getBackground();
            firstLayer = (MapFirstLayer) mParent.getmThread().getFirstLayer();

            int col = (background.getScrollX() + Utils.mySprite.getX() + Utils.steps) / Utils.tileSize;
            int row = (background.getScrollY() + Utils.mySprite.getY()) / Utils.tileSize;


            if (background.getBackgroundTile(row, col) != '-') {
                if (stepAllowed(Utils.tileSize, 0)) {
                    background.setScrollX(background.getScrollX() + Utils.steps);
                    firstLayer.setScrollX(firstLayer.getScrollX() + Utils.steps);
                    Utils.searched = false;
                }
            }
            if(Utils.currentEnvironment.equals("outside"))
                checkIfSeenByNPC();
        }




    }

    private void isBeforeDoor(int newScrollX, int newScrollY,Sprite sprite,int doorPosInBuildingX, int doorPosInBuildingY){


        int x = (int) sprite.getX() - Utils.mySprite.getX() -doorPosInBuildingX;
        int y = (int) sprite.getY() - Utils.mySprite.getY() -doorPosInBuildingY;
        if((x==newScrollX) && (y==newScrollY)){
            beforeDoor = true;
            spriteForm = sprite.getName();
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
        for (Sprite sprite : Utils.allSprites)
        {
            int x1 = firstLayer.getScrollX() +moveX-Utils.tileSize/2;
            int y1 = firstLayer.getScrollY() +moveY;
            int x2 = x1 + (int) sprite.getWidth();
            int y2 = y1 + (int) sprite.getHeight()-Utils.tileSize;
            Rect rect = new Rect(x1,y1,x2,y2);


            // char
            int posX = (int) sprite.getX()-Utils.mySprite.getX();
            int posY = (int) sprite.getY()-Utils.mySprite.getY();
            /*if(sprite.getType().equals("NPC")) {
                Log.d("POS_NPC2", x1 + "-" + x2 + "," + y1 + "-" + y2);
                Log.d("POS_ME2", posX+ "," + posY);
            }*/
            if(rect.intersects(posX, posY, posX, posY)){
                int doorPosInBuildingX = 0;
                int doorPosInBuildingY = 0;
                if(sprite.getName().equals("pokecenter")) {
                    spriteForm="pokecenter";
                    doorPosInBuildingX = Utils.tileSize * 2 + Utils.tileSize / 2;
                    doorPosInBuildingY = Utils.tileSize;
                }
                if(sprite.getName().equals("pokemarkt")) {
                    doorPosInBuildingX = Utils.tileSize * 2 + Utils.tileSize / 2;
                    doorPosInBuildingY = Utils.tileSize;
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
                Utils.currentWildPokemon = new PokemonSprite("rattata", Utils.ds);
            } else {
                if (getal < 14) {
                    Utils.currentWildPokemon = new PokemonSprite("pidgey",Utils.ds);
                } else {
                    Utils.currentWildPokemon = new PokemonSprite("spearow",Utils.ds);
                }
            }
            Utils.currentWildPokemon.setCurrentExperience(810000);
            Utils.currentWildPokemon.setCurrentHP(Utils.currentWildPokemon.getStats().getHp());
            //pokemonSprite.setLevel(38);
        }
        else {
            Utils.pokemonFound = false;
        }
    }


    public HashMap<String,PokemonSprite> getFight(){
        return fight;
    }

    public void setFight(HashMap<String,PokemonSprite> fight){
        this.fight = fight;
    }

    public Sprite getSprite(){
        return Utils.mySprite;
    }

    public void setStopMovement(){
        if(actionStopped) {         // als er niet meer op een knop gedrukt wordt, doe dan dit
            int x = Utils.mySprite.getX();
            int y = Utils.mySprite.getY();
            //if(Utils.mySprite.getStatus()==null)

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
        return "";
    }

    @Override
    public void doInteraction() {

    }

    public void checkIfSeenByNPC(){

        for(Sprite sprite: Utils.allSprites){

            if(sprite.getName().equals("bugcatcher") && !((TrainerSprite)sprite).isDoneFighting()){
                TrainerSprite trainerSprite = (TrainerSprite) sprite;
                int x1 = sprite.getX() - sprite.getScrollX();
                int y1 = sprite.getY() - sprite.getScrollY();

                if(trainerSprite.getStatus().startsWith("front")){
                    checkIfSeenByNPCHelp(x1,y1,x1,y1+Utils.tileSize*5,"front",trainerSprite);
                } else if(trainerSprite.getStatus().startsWith("back")){
                    checkIfSeenByNPCHelp(x1,y1,x1,y1-Utils.tileSize*5,"back",trainerSprite);
                } else if(trainerSprite.getStatus().startsWith("left")){
                    checkIfSeenByNPCHelp(x1,y1,x1-Utils.tileSize*5,y1,"left",trainerSprite);
                } else{
                    checkIfSeenByNPCHelp(x1,y1,x1+Utils.tileSize*5,y1,"right", trainerSprite);
                }
            }
        }
    }

    private void checkIfSeenByNPCHelp(int x1,int y1, int x2, int y2,String status, TrainerSprite trainerSprite){
        MapFirstLayer firstLayer= (MapFirstLayer) mParent.getmThread().getFirstLayer();
        switch(status){
            case "front":
                if(Utils.mySprite.getX()==x1 && Utils.mySprite.getX()==x2 && Utils.mySprite.getY() >= y1 && Utils.mySprite.getY() <= y2){
                    Utils.trainerFoundMe = true;
                    firstLayer.discoveredByTrainer(x1,y1,x1,Utils.mySprite.getY()-Utils.tileSize,trainerSprite);
                    Log.d("FOUND","found!");
                }
                break;
            case "back":
                if(Utils.mySprite.getX()==x1 && Utils.mySprite.getX()==x2 && Utils.mySprite.getY() >= y2 && Utils.mySprite.getY() <= y1){
                    Utils.trainerFoundMe = true;
                    firstLayer.discoveredByTrainer(x1, y1,x1,Utils.mySprite.getY()+Utils.tileSize,trainerSprite);
                    Log.d("FOUND","found!");
                }
                break;
            case "left":
                if(Utils.mySprite.getX()>=x2 && Utils.mySprite.getX()<=x1 && Utils.mySprite.getY() == y1 && Utils.mySprite.getY() == y2){
                    Utils.trainerFoundMe = true;
                    firstLayer.discoveredByTrainer(x1,y1,Utils.mySprite.getX()+Utils.tileSize,y1,trainerSprite);
                    Log.d("FOUND","found!");
                }
                break;
            case "right":
                if(Utils.mySprite.getX()>=x1 && Utils.mySprite.getX()<=x2 && Utils.mySprite.getY() == y1 && Utils.mySprite.getY() == y2){
                    Utils.trainerFoundMe = true;
                    firstLayer.discoveredByTrainer(x1, y1,Utils.mySprite.getX()-Utils.tileSize,y1,trainerSprite);
                    Log.d("FOUND","found!");
                }
        }


    }

}

