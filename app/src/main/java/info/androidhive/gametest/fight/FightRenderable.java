package info.androidhive.gametest.fight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.Random;

import info.androidhive.gametest.FightListener;
import info.androidhive.gametest.R;
import info.androidhive.gametest.Utils;
import info.androidhive.gametest.abstractClasses.Renderable;
import info.androidhive.gametest.items.CustomItem;
import info.androidhive.gametest.map.MapForeground;
import info.androidhive.gametest.pokemons.Move;
import info.androidhive.gametest.pokemons.PokemonSprite;

import static info.androidhive.gametest.R.drawable.battle_bg1_part2;
import static info.androidhive.gametest.R.drawable.battle_bg1_part3;
import static info.androidhive.gametest.R.drawable.mainchar_animation1;
import static info.androidhive.gametest.R.drawable.mainchar_animation2;
import static info.androidhive.gametest.R.drawable.mainchar_animation3;
import static info.androidhive.gametest.R.drawable.mainchar_animation4;
import static info.androidhive.gametest.R.drawable.mainchar_animation5;

/**
 * Created by matthias on 3/23/2016.
 */
public class FightRenderable extends Renderable {
    private static final int START_BALL_X=-40;
    private static final int START_BALL_Y=120;
    private static final int LENGTH_OF_ATTACK=20;
    private Paint paint;

    private SurfaceHolder mHolder;
    private SurfaceView mView;

    private FightListener fightListener;

    private boolean animationBusy = false;

    // ANIMATION BEGINS
    private int x1;
    private int x2;
    private int x3=0;
    private int ballMovementX;
    private int ballMovementY;
    private int extraTime = 0;
    private int strCounter =0;
    private int strCounter4 =0;
    private int timeCounter =0;
    private String strAppeared;
    private String enemyGoPokemon;
    private String goPokemon;
    private String strAction;
    private String enemyType = "";

    // ATTACKING
    private int strCounter2=0;
    private int timeCounter2 = 0;
    private int critHitStrCounter = 0;
    private String attackMessage1 = "";
    private String attackMessage2 = "";
    private PokemonSprite attacker1=null;
    private PokemonSprite attacker2=null;
    private PokemonSprite enemyPokemon;
    private PokemonSprite myPokemon;
    private PokemonSprite currentAttacker;
    private Move enemyMove;
    private Move myMove;
    private String firstCritHitMessage="";
    private String secondCritHitMessage="";

    private boolean myPokemonChanged = false;
    private boolean enemyPokemonChanged = false;

    private int strCounter3=0;
    private int timeCounter3 = 0;
    // CATCHING

    private int catchTimeCounter = 0;
    private String pokeballMessage = "";
    private boolean ballToCatchThrown = false;
    private String pokemonCapturingState = "";
    private String pokemonCapturingMessage = "";
    private int shakeCounter = 0;
    private boolean enemyInBall = false;

    // END OF BATTLE
    private int gainedExperience=0;
    private String endBattleMessage = "";
    private String endBattleStatus = "";
    private String continueFightingMessage = "";
    private boolean endOfEncounter = false;
    private int runAwayTimeCounter = 0;
    private int runAwayStringCounter = 0;
    private String runAwayMessage = "";

    public void setFightListener(FightListener fightListener) {
        this.fightListener = fightListener;
    }

    public FightRenderable(SurfaceView view,SurfaceHolder holder,FightListener fightListener){
        setFightListener(fightListener);
        mHolder = holder;
        mView = view;
        x1=-250;
        x2 = 700;
        fightListener.fightRenderableLoaded(this);
        currentTime = SystemClock.elapsedRealtime();

        strCounter = 0;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(35);

        timeCounter = 0;
        ballMovementX = 0;
        ballMovementY = 0;

        Utils.enemyPlatform= BitmapFactory.decodeResource(mView.getResources(), battle_bg1_part2);
        Utils.enemyPlatform = Bitmap.createScaledBitmap(Utils.enemyPlatform, 600, 200, false);

        Utils.myPlatform= BitmapFactory.decodeResource(mView.getResources(), battle_bg1_part3);
        Utils.myPlatform = Bitmap.createScaledBitmap(Utils.myPlatform, 600, 200, false);

        Utils.player1 = BitmapFactory.decodeResource(mView.getResources(), mainchar_animation1);
        Utils.player1 = Bitmap.createScaledBitmap(Utils.player1, 250, 310, false);
        Utils.player2 = BitmapFactory.decodeResource(mView.getResources(), mainchar_animation2);
        Utils.player2 = Bitmap.createScaledBitmap(Utils.player2, 250, 310, false);
        Utils.player3 = BitmapFactory.decodeResource(mView.getResources(), mainchar_animation3);
        Utils.player3 = Bitmap.createScaledBitmap(Utils.player3, 250, 310, false);
        Utils.player4 = BitmapFactory.decodeResource(mView.getResources(), mainchar_animation4);
        Utils.player4 = Bitmap.createScaledBitmap(Utils.player4, 250, 310, false);
        Utils.player5 = BitmapFactory.decodeResource(mView.getResources(), mainchar_animation5);
        Utils.player5 = Bitmap.createScaledBitmap(Utils.player5, 250, 310, false);

        Utils.pokeball1 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball1);
        Utils.pokeball1 = Bitmap.createScaledBitmap(Utils.pokeball1, 60, 60, false);
        Utils.pokeball2 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball2);
        Utils.pokeball2 = Bitmap.createScaledBitmap(Utils.pokeball2, 60, 60, false);
        Utils.pokeball3 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball3);
        Utils.pokeball3 = Bitmap.createScaledBitmap(Utils.pokeball3, 60, 60, false);
        Utils.pokeball4 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball4);
        Utils.pokeball4 = Bitmap.createScaledBitmap(Utils.pokeball4, 60, 60, false);
        Utils.pokeball5 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball5);
        Utils.pokeball5 = Bitmap.createScaledBitmap(Utils.pokeball5, 60, 60, false);
        Utils.pokeball6 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball6);
        Utils.pokeball6 = Bitmap.createScaledBitmap(Utils.pokeball6, 60, 60, false);
        Utils.pokeball7 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball7);
        Utils.pokeball7 = Bitmap.createScaledBitmap(Utils.pokeball7, 60, 60, false);
        Utils.pokeball8 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball8);
        Utils.pokeball8 = Bitmap.createScaledBitmap(Utils.pokeball8, 60, 60, false);
        Utils.pokeball9 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball9);
        Utils.pokeball9 = Bitmap.createScaledBitmap(Utils.pokeball9, 60, 60, false);
        Utils.pokeball10 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball10);
        Utils.pokeball10 = Bitmap.createScaledBitmap(Utils.pokeball10, 60, 120, false);
        Utils.pokeball11 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball11);
        Utils.pokeball11 = Bitmap.createScaledBitmap(Utils.pokeball11, 60, 60, false);
        Utils.pokeball12 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball12);
        Utils.pokeball12 = Bitmap.createScaledBitmap(Utils.pokeball12, 60, 60, false);
        Utils.pokeball13 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball13);
        Utils.pokeball13 = Bitmap.createScaledBitmap(Utils.pokeball13, 60, 60, false);
        Utils.pokeball14 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball14);
        Utils.pokeball14 = Bitmap.createScaledBitmap(Utils.pokeball14, 60, 60, false);
        Utils.pokeball15 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball15);
        Utils.pokeball15 = Bitmap.createScaledBitmap(Utils.pokeball15, 60, 60, false);
        Utils.pokeball16 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball16);
        Utils.pokeball16 = Bitmap.createScaledBitmap(Utils.pokeball16, 60, 60, false);
        Utils.pokeball17 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball17);
        Utils.pokeball17 = Bitmap.createScaledBitmap(Utils.pokeball17, 60, 60, false);
        Utils.pokeball18 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball18);
        Utils.pokeball18 = Bitmap.createScaledBitmap(Utils.pokeball18, 60, 60, false);
        Utils.pokeball19 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball19);
        Utils.pokeball19 = Bitmap.createScaledBitmap(Utils.pokeball19, 60, 60, false);
        Utils.pokeball20 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball20);
        Utils.pokeball20 = Bitmap.createScaledBitmap(Utils.pokeball20, 60, 60, false);
        Utils.pokeball21 = BitmapFactory.decodeResource(mView.getResources(), R.drawable.pokeball21);
        Utils.pokeball21 = Bitmap.createScaledBitmap(Utils.pokeball21, 60, 60, false);
    }

    public void setup(){


        PokemonSprite myPokemon = FightActivity.myCurrentPokemon;
        String id2 = "a"+myPokemon.getId()+"_back";
        int resID = mView.getResources().getIdentifier(id2, "drawable", mView.getContext().getPackageName());
        Utils.myPokemonBm = BitmapFactory.decodeResource(mView.getResources(), resID);
        Utils.myPokemonBm = Bitmap.createScaledBitmap(Utils.myPokemonBm, 350, 350, false);

        Log.d("ENEMYPOKE",FightActivity.enemyPokemon.getId()+"");
        PokemonSprite enemyPokemon = FightActivity.enemyPokemon;
        String id1 = "a"+enemyPokemon.getId();
        resID = mView.getResources().getIdentifier(id1, "drawable", mView.getContext().getPackageName());
        Utils.enemyPokemonBm = BitmapFactory.decodeResource(mView.getResources(), resID);
        Utils.enemyPokemonBm = Bitmap.createScaledBitmap(Utils.enemyPokemonBm, 350, 350, false);


        if(enemyType.equals("wild"))
            strAppeared = "A wild " + enemyPokemon.getName().toUpperCase() + " appeared !";
        else {
            strAppeared = "You are challenged by " + enemyType.toUpperCase();
            enemyGoPokemon = Utils.currentTrainer.getName().toUpperCase() + " choses " +FightActivity.enemyPokemon.getName().toUpperCase();
        }
        goPokemon = "Go! "+myPokemon.getName().toUpperCase()+"!";
        strAction = "What will "+myPokemon.getName().toUpperCase()+ " do?";


    }

    @Override
    public void playfield(int width, int height) {

    }

    @Override
    public void update(RectF dirty, double timeDelta) {

    }

    private long currentTime;

    @Override
    public void draw(Canvas c) {
        long newTime = SystemClock.elapsedRealtime();
        loadStartAnimation(c);
        loadAttackInfo(c);
        throwPokeballToCatchAnimation(c);
        loadEndAnimation(c);
        continueFightingAnimation(c);
        runAwayAnimation(c);


        //c.drawBitmap(myPokemonBm,x2+60,328,null);
    }

    public void loadStartAnimation(Canvas c){
        c.drawBitmap(Utils.enemyPlatform, x1, 180, null);



        if ((timeCounter2 == 0 || timeCounter2 > LENGTH_OF_ATTACK || currentAttacker == enemyPokemon) && !endBattleStatus.equals("WON") && !enemyInBall) {

            if (enemyType.equals("wild") || timeCounter >= extraTime || myPokemonChanged) {
                c.drawBitmap(Utils.enemyPokemonBm, x1 + 100, 40, null);
            } else if (!enemyType.equals("") && timeCounter == 0 && !enemyPokemonChanged) {
                c.drawBitmap(Utils.enemyTrainerFrontBm, x1 + 100, 40, null);
            }
        }
        c.drawBitmap(Utils.myPlatform, x2, 450, null);
        if (timeCounter < 10 + extraTime && !enemyPokemonChanged)
            c.drawBitmap(Utils.player1, x2 + 110, 328, null);

        if(enemyPokemonChanged)
            c.drawBitmap(Utils.myPokemonBm, x2 + 60, 290, null);

        if(x1 < 500){
            x1+=35;         // x1 goes from -250 to 500
            x2 -= 35;       // x2 goes from 700 to -50
        }
        else{
            if(timeCounter<=40 +extraTime) {
                if(!myPokemonChanged && !enemyPokemonChanged && (extraTime==0||timeCounter==0))
                    c.drawText(strAppeared, 0, strCounter, 70, 690, paint);

                if (strCounter < strAppeared.length() && !enemyPokemonChanged) {
                    strCounter++;
                }
                else{
                    if(extraTime!=0 && !myPokemonChanged) {
                        c.drawText(enemyGoPokemon, 0, strCounter4, 70, 690, paint);
                        if (strCounter4 < enemyGoPokemon.length())
                            strCounter4++;
                    }

                    if(timeCounter < extraTime && !myPokemonChanged) {       // after "a enemy ... has appeared"
                        enemyTrainerBeginMovement(c);
                        timeCounter++;
                    }
                    else{
                        if(!enemyPokemonChanged) {
                            throwPokeballWithMyPokemonAnimation(c);
                            timeCounter++;
                        }
                        else {
                            timeCounter = 63 + extraTime;
                            //strCounter=0;
                        }
                    }
                }

            }
            if(timeCounter>40+extraTime){

                if((timeCounter2==0 || timeCounter2>LENGTH_OF_ATTACK || currentAttacker==myPokemon) && !endBattleStatus.equals("LOST") && !enemyPokemonChanged){
                            c.drawBitmap(Utils.myPokemonBm, x2 + 60, 290, null);
                }

                if(timeCounter<63+extraTime) {
                    c.drawText(goPokemon, 0, strCounter, 70, 690, paint);
                    if (strCounter < goPokemon.length()) {
                        strCounter++;
                    }
                }
                else if(timeCounter==63+extraTime)
                    strCounter = 0;

                else{
                    if(!animationBusy && !ballToCatchThrown && !endOfEncounter && runAwayMessage.equals("")) {
                        c.drawText(strAction, 0, strCounter, 70, 690, paint);
                        if (strCounter < strAction.length()) {
                            strCounter++;
                        }
                    }

                    if(timeCounter==80+extraTime) {         // if animation is done
                        fightListener.showActionButtons();
                    }
                }
                timeCounter++;
            }




        }
    }

    public void enemyTrainerBeginMovement(Canvas c){


        if(timeCounter<15) {
            c.drawBitmap(Utils.enemyTrainerFrontBm, x1 + 100 + x3, 40, null);
            x3 += 20;

        }
        if(timeCounter<23 && timeCounter >5){
            c.drawBitmap(Utils.pokeball11, x1 + 250, 250, null);
        }
        else if (timeCounter>=23){
            c.drawBitmap(Utils.pokeball10, x1 + 250, 210, null);
            endOfEncounter = false;
        }

    }

    public void throwPokeballWithMyPokemonAnimation(Canvas c){
        if (timeCounter >= 10+extraTime && timeCounter < 12+extraTime) c.drawBitmap(Utils.player2, x2 + 80, 328, null);
        else if (timeCounter >= 12+extraTime && timeCounter < 14+extraTime) c.drawBitmap(Utils.player3, x2 + 50, 328, null);
        else if (timeCounter >= 14+extraTime && timeCounter < 16+extraTime) c.drawBitmap(Utils.player4, x2 + 20, 328, null);
        else if (timeCounter >= 16+extraTime && timeCounter < 18+extraTime) c.drawBitmap(Utils.player5, x2 - 10, 328, null);
        else if (timeCounter >= 18+extraTime && timeCounter < 20+extraTime) c.drawBitmap(Utils.pokeball1, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 20+extraTime && timeCounter < 22+extraTime) c.drawBitmap(Utils.pokeball2, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 22+extraTime && timeCounter < 24+extraTime) c.drawBitmap(Utils.pokeball3, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 24+extraTime && timeCounter < 26+extraTime) c.drawBitmap(Utils.pokeball4, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 26+extraTime && timeCounter < 28+extraTime) c.drawBitmap(Utils.pokeball5, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 28+extraTime && timeCounter < 30+extraTime) c.drawBitmap(Utils.pokeball6, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 30+extraTime && timeCounter < 32+extraTime) c.drawBitmap(Utils.pokeball7, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 32+extraTime && timeCounter < 34+extraTime) c.drawBitmap(Utils.pokeball8, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 34+extraTime && timeCounter < 36+extraTime) c.drawBitmap(Utils.pokeball9, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 36+extraTime && timeCounter < 38+extraTime) c.drawBitmap(Utils.pokeball9, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 38+extraTime && timeCounter < 40+extraTime) c.drawBitmap(Utils.pokeball10, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if(timeCounter==40+extraTime) strCounter = 0;

        ballMovementX += 4;
        ballMovementY += 9;
    }

    public void loadAttackInfo(Canvas c){
        if(!attackMessage1.equals("") && timeCounter>=95 && !ballToCatchThrown && !endOfEncounter){      // only to this if the animation is completely loaded (also when switch pokemon)
            firstAttack(c);
        }
        if(!attackMessage2.equals("") && attackMessage1.equals("")){ // only when attackMessage1 has finished
            secondAttack(c);
        }
    }

    private void firstAttack(Canvas c){

        currentAttacker = attacker1;
        timeCounter2++;
        if(timeCounter2<LENGTH_OF_ATTACK){
            if(attacker1==myPokemon)
                c.drawBitmap(Utils.enemyPokemonBm, x1+100-20, 40, null);    //500
            else
                c.drawBitmap(Utils.myPokemonBm,x2+60-20,290,null);       // -45
        }

        if(timeCounter2 <40) {
            c.drawText(attackMessage1, 0, strCounter2, 70, 690, paint);
            if (strCounter2 < attackMessage1.length()) {
                strCounter2++;
            }
        }
        else if(timeCounter2<70 && !firstCritHitMessage.equals("")){
            c.drawText(firstCritHitMessage, 0, critHitStrCounter, 70, 690, paint);
            if (critHitStrCounter < firstCritHitMessage.length()) {
                critHitStrCounter++;
            }
        }
        else{
            firstCritHitMessage = "";
            attackMessage1 = "";
            strCounter2 = 0;
            timeCounter2 = 0;
            critHitStrCounter = 0;

            if(attacker1==myPokemon)
                fightListener.myAttackAnimationIsDone(myMove);
            else
                fightListener.enemyAttackAnimationIsDone(enemyMove);

            if(attackMessage2.equals("")){
                animationBusy = false;
            }


        }
    }

    private void secondAttack(Canvas c){
        currentAttacker = attacker2;
        timeCounter2++;
        if(timeCounter2<LENGTH_OF_ATTACK){
            if(attacker2==myPokemon)
                c.drawBitmap(Utils.enemyPokemonBm, x1+100-20, 40, null);
            else
                c.drawBitmap(Utils.myPokemonBm,x2+60-20,290,null);
        }

       if(timeCounter2 <40) {
            c.drawText(attackMessage2, 0, strCounter2, 70, 690, paint);
            if (strCounter2 < attackMessage2.length()) {
                strCounter2++;
            }
        }

       else if(timeCounter2<70 && !secondCritHitMessage.equals("")){
           c.drawText(secondCritHitMessage, 0, critHitStrCounter, 70, 690, paint);
           if (critHitStrCounter < secondCritHitMessage.length()) {
               critHitStrCounter++;
           }
       }

        else {
           secondCritHitMessage = "";
            attackMessage2 = "";
            strCounter2 = 0;
            timeCounter2 = 0;
            critHitStrCounter = 0;

            if(attacker2==myPokemon)
                fightListener.myAttackAnimationIsDone(myMove);
            else
                fightListener.enemyAttackAnimationIsDone(enemyMove);

           animationBusy = false;
        }

    }

    private void throwPokeballToCatchAnimation(Canvas c){

        if(ballToCatchThrown){
            if(catchTimeCounter<200) {
                if(strCounter3==0) {       // only write this message if the pokemon capture message is not being written
                    c.drawText(pokeballMessage, 0, strCounter2, 70, 690, paint);
                    if (strCounter2 < pokeballMessage.length()) {
                        strCounter2++;
                    }
                }
                if(catchTimeCounter>=10 && catchTimeCounter<15){
                    ballMovementY -= 35;
                    c.drawBitmap(Utils.pokeball1, START_BALL_X + ballMovementX, START_BALL_Y + ballMovementY, null);
                }
                else if(catchTimeCounter>=15 && catchTimeCounter<20){
                    ballMovementY -= 12;
                    c.drawBitmap(Utils.pokeball4, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
                }

                else if(catchTimeCounter>=20 && catchTimeCounter<25){
                    ballMovementY += 12;
                    c.drawBitmap(Utils.pokeball7, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
                }

                else if(catchTimeCounter>=25 && catchTimeCounter<30){
                    ballMovementY += 25;
                    c.drawBitmap(Utils.pokeball8, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
                }

                else if(catchTimeCounter>30 && catchTimeCounter < 33){
                    c.drawBitmap(Utils.pokeball10, START_BALL_X+700, START_BALL_Y-20, null);
                }
                else if(catchTimeCounter==33){
                    enemyInBall = true;
                    c.drawBitmap(Utils.pokeball11, START_BALL_X+700, START_BALL_Y+20, null);
                }
                else if(catchTimeCounter==34) c.drawBitmap(Utils.pokeball12, START_BALL_X+700, START_BALL_Y+50, null);
                else if(catchTimeCounter == 35) c.drawBitmap(Utils.pokeball13, START_BALL_X+700, START_BALL_Y+80, null);
                else if (catchTimeCounter == 36) c.drawBitmap(Utils.pokeball14, START_BALL_X+700, START_BALL_Y+110, null);
                else if (catchTimeCounter == 37) c.drawBitmap(Utils.pokeball15, START_BALL_X+700, START_BALL_Y+140, null);
                else if(catchTimeCounter==38) c.drawBitmap(Utils.pokeball16, START_BALL_X+700, START_BALL_Y+140, null);
                else if(catchTimeCounter>38){
                    if(pokemonCapturingState.equals("instant_caught")){
                        if(catchTimeCounter==39 ||catchTimeCounter==40){
                            c.drawBitmap(Utils.pokeball20, START_BALL_X+700, START_BALL_Y+140, null);
                            strCounter3 = 0;
                        }
                        else if(catchTimeCounter>=41 && catchTimeCounter<75) {
                            c.drawBitmap(Utils.pokeball21, START_BALL_X + 700, START_BALL_Y + 140, null);
                            c.drawText(pokemonCapturingMessage, 0, strCounter3, 70, 690, paint);
                            if (strCounter3 < pokemonCapturingMessage.length()) {
                                strCounter3++;
                            }
                        }
                        else if(catchTimeCounter==75) {
                            fightListener.pokemonCaptured(true);
                            ballToCatchThrown = false;
                            endOfEncounter = true;
                        }
                    }
                    else if(pokemonCapturingState.equals("trainer_pokemon")){
                        if(catchTimeCounter==39){
                            strCounter3 = 0;
                            enemyInBall = false;
                        }
                        else if(catchTimeCounter>=40 && catchTimeCounter<85) {
                            c.drawText(pokemonCapturingMessage, 0, strCounter3, 70, 690, paint);
                            if (strCounter3 < pokemonCapturingMessage.length()) {
                                strCounter3++;
                            }
                        }
                        else if(catchTimeCounter==85) {
                            fightListener.pokemonCaptured(false);
                            ballToCatchThrown = false;
                            endOfEncounter = false;
                        }
                    }
                    else{
                        if(((catchTimeCounter==39 || catchTimeCounter==40) && shakeCounter>=1) || ((catchTimeCounter==53 || catchTimeCounter==54) && shakeCounter >=2) || ((catchTimeCounter==66 || catchTimeCounter==67) && shakeCounter>=3) || ((catchTimeCounter==80 || catchTimeCounter==81) && shakeCounter==4)   )
                            c.drawBitmap(Utils.pokeball17, START_BALL_X+680, START_BALL_Y+140, null);
                        else if(((catchTimeCounter==41 || catchTimeCounter==42) && shakeCounter>=1) || ((catchTimeCounter==55 || catchTimeCounter==56) && shakeCounter >=2) || ((catchTimeCounter==68 || catchTimeCounter==69) && shakeCounter>=3) || ((catchTimeCounter==82 || catchTimeCounter==83) && shakeCounter==4)   )
                            c.drawBitmap(Utils.pokeball18, START_BALL_X+700, START_BALL_Y+140, null);
                        else if(((catchTimeCounter==43 || catchTimeCounter==44) && shakeCounter>=1) || ((catchTimeCounter==57 || catchTimeCounter==58) && shakeCounter >=2) || ((catchTimeCounter==70 || catchTimeCounter==71) && shakeCounter>=3) || ((catchTimeCounter==84 || catchTimeCounter==85) && shakeCounter==4)   )
                            c.drawBitmap(Utils.pokeball17, START_BALL_X+720, START_BALL_Y+140, null);
                        else if(((catchTimeCounter>=45 && catchTimeCounter<53) && shakeCounter>=1) || ((catchTimeCounter>=58 && catchTimeCounter<66) && shakeCounter >=2) || ((catchTimeCounter>=72 && catchTimeCounter<80) && shakeCounter>=3) || ((catchTimeCounter>=86 && catchTimeCounter<94) && shakeCounter==4)   )
                            c.drawBitmap(Utils.pokeball19, START_BALL_X+700, START_BALL_Y+140, null);

                        if(pokemonCapturingState.equals("caught") && catchTimeCounter>=94){
                            if(catchTimeCounter>=94 && catchTimeCounter<96) {
                                c.drawBitmap(Utils.pokeball20, START_BALL_X + 700, START_BALL_Y + 140, null);
                                strCounter3 = 0;
                            }
                            else if(catchTimeCounter>=96 && catchTimeCounter<130) {
                                c.drawBitmap(Utils.pokeball21, START_BALL_X + 700, START_BALL_Y + 140, null);
                                c.drawText(pokemonCapturingMessage, 0, strCounter3, 70, 690, paint);
                                if (strCounter3 < pokemonCapturingMessage.length()) {
                                    strCounter3++;
                                }
                            }
                            else if(catchTimeCounter==130) {
                                fightListener.pokemonCaptured(true);
                                endOfEncounter = true;
                                ballToCatchThrown = false;
                            }
                        }
                        else if(pokemonCapturingState.equals("not_caught") && catchTimeCounter>=53){
                            if((catchTimeCounter==53 && shakeCounter==1) || (catchTimeCounter==66 && shakeCounter ==2) || (catchTimeCounter==80 && shakeCounter==3) || (catchTimeCounter==94 && shakeCounter==4)   ) {
                                strCounter3 = 0;
                                c.drawBitmap(Utils.pokeball10, START_BALL_X + 700, START_BALL_Y + 20, null);
                            }
                            if((catchTimeCounter>=54 && catchTimeCounter<88 && shakeCounter==1) || (catchTimeCounter >= 67 && catchTimeCounter < 101 && shakeCounter == 2) || (catchTimeCounter>=81 && catchTimeCounter<115 && shakeCounter==3) || (catchTimeCounter>=95 && catchTimeCounter < 129 && shakeCounter==4)   ) {
                                enemyInBall = false;
                                c.drawBitmap(Utils.pokeball10, START_BALL_X + 700, START_BALL_Y + 20, null);
                                c.drawText(pokemonCapturingMessage, 0, strCounter3, 70, 690, paint);
                                if (strCounter3 < pokemonCapturingMessage.length()) {
                                    strCounter3++;
                                }
                            }
                            else if((catchTimeCounter==88 && shakeCounter==1) || (catchTimeCounter==101 && shakeCounter ==2) || (catchTimeCounter==115 && shakeCounter==3) || (catchTimeCounter==129 && shakeCounter==4)   ) {
                                //fightListener.pokemonCaptured(false);
                                ballToCatchThrown = false;
                            }

                        }

                    }

                    //c.drawBitmap(Utils.pokeball17, START_BALL_X+700, START_BALL_Y+140, null);
                }




                ballMovementX+=23;
                catchTimeCounter++;
            }

        }

    }

    private void loadEndAnimation(Canvas c){
        if (!endBattleMessage.equals("") && attackMessage1.equals("") && attackMessage2.equals("")) {
            if(gainedExperience>0)
                endBattleStatus = "WON";
            else
                endBattleStatus = "LOST";

            if(timeCounter3<50) {
                animationBusy = true;
                c.drawText(endBattleMessage, 0, strCounter2, 70, 690, paint);
                if (strCounter2 < endBattleMessage.length()) {
                    strCounter2++;
                }
            }
            else if(timeCounter3==50){
                fightListener.finishBattle(gainedExperience);
                endOfEncounter = true;
                animationBusy = false;
            }
            timeCounter3++;
        }
    }

    public void continueFightingAnimation(Canvas c){

        if(!continueFightingMessage.equals("")) {
            c.drawText(continueFightingMessage, 0, strCounter2, 70, 690, paint);


            if (strCounter2 < continueFightingMessage.length()) {
                strCounter2++;
            }
            else{
                if(timeCounter3==50){   // do this only once
                    if(continueFightingMessage.startsWith("Do you")) {
                        enemyPokemonChanged=false;
                        fightListener.switchPokemonAfterFainted();
                    }
                    else {
                        myPokemonChanged = false;
                        fightListener.enemySwitchesPokemonAfterFainted();
                    }
                }
            }
            timeCounter3++;


        }
    }

    public void runAwayAnimation(Canvas c){
        if(!runAwayMessage.equals("")) {
            if(runAwayTimeCounter<40) {
                c.drawText(runAwayMessage, 0, runAwayStringCounter, 70, 690, paint);
                if (runAwayStringCounter < runAwayMessage.length()) {
                    runAwayStringCounter++;
                }
                runAwayTimeCounter++;
            }
            else{
                if(enemyType.equals("wild")) {
                    fightListener.runAway();
                }
                else{
                    runAwayMessage = "";
                    runAwayTimeCounter=0;
                    runAwayStringCounter=0;
                }
            }


        }
    }

    public void startBattle(String enemyType,Context context){
        this.enemyType = enemyType;
        if(!enemyType.equals("wild"))
            extraTime = 30;
        if(!enemyType.equals("wild")) {
            String id = enemyType + "_frontanimation";
            int resID = context.getResources().getIdentifier(id, "drawable", context.getPackageName());
            Bitmap a = BitmapFactory.decodeResource(context.getResources(), resID);
            Utils.enemyTrainerFrontBm = Bitmap.createScaledBitmap(a, 250, 310, false);
        }

    }

    public void changePokemon(){
        enemyPokemonChanged = false;
        myPokemonChanged = true;
        continueFightingMessage ="";
        endBattleStatus = "";
        endOfEncounter = false;
        timeCounter2 = 0;
        timeCounter3 = 0;
        strCounter2 = 0;

        PokemonSprite myPokemon = FightActivity.myCurrentPokemon;
        String id2 = "a" + myPokemon.getId()+"_back";
        int resID = mView.getResources().getIdentifier(id2, "drawable", mView.getContext().getPackageName());
        Utils.myPokemonBm = BitmapFactory.decodeResource(mView.getResources(), resID);
        Utils.myPokemonBm = Bitmap.createScaledBitmap(Utils.myPokemonBm, 350, 350, false);

        //fightListener.showActionButtons();
        goPokemon = "Go! "+myPokemon.getName().toUpperCase()+"!";
        strAction = "What will "+myPokemon.getName().toUpperCase()+ " do?";
        strCounter = 0;
        timeCounter = 18;
        ballMovementX =0;          // these 2 vars will be used to move the ball from a start loc to a next one
        ballMovementY =0;

        animationBusy = false;

    }

    public void enemyChangesPokemon(){
        enemyPokemonChanged = true;
        myPokemonChanged = false;
        continueFightingMessage ="";
        timeCounter2 = 0;
        strCounter2 = 0;
        timeCounter3 = 0;


        setup();
        strCounter = 0;
        timeCounter = 10;
        strCounter4 = 0;

        animationBusy = false;

        /*PokemonSprite myPokemon = FightActivity.myCurrentPokemon;
        String id2 = "a" + myPokemon.getId()+"_back";
        int resID = mView.getResources().getIdentifier(id2, "drawable", mView.getContext().getPackageName());
        Utils.myPokemonBm = BitmapFactory.decodeResource(mView.getResources(), resID);
        Utils.myPokemonBm = Bitmap.createScaledBitmap(Utils.myPokemonBm, 350, 350, false);*/
    }

    public void enemyAttacked(Move attack, PokemonSprite enemyPokemon, PokemonSprite myPokemon, boolean criticalHit){
        this.enemyPokemon = enemyPokemon;
        this.myPokemon = myPokemon;
        this.enemyMove = attack;
        if(attackMessage1.equals("")) {        // the enemy pokemon attacked first
            attackMessage1 = "The enemy " + enemyPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker1 = enemyPokemon;
            if(criticalHit) {
                firstCritHitMessage = "A critical hit!";
                strCounter3 =0;
            }
        }
        else {                                            // you attacked first
            attackMessage2 = "The enemy " + enemyPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker2 = enemyPokemon;
            if(criticalHit) {
                secondCritHitMessage = "A critical hit!";
                strCounter3 =0;
            }
        }
        animationBusy = true;

    }

    public void youAttacked(Move attack, PokemonSprite myPokemon, PokemonSprite enemyPokemon, boolean criticalHit){
        Log.d("ATTACKED","I attacked!");
        this.enemyPokemon = enemyPokemon;
        this.myPokemon = myPokemon;
        this.myMove = attack;
        if(attackMessage1.equals("")) {                   // you attacked first
            attackMessage1 = myPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker1 = myPokemon;
            if(criticalHit) {
                firstCritHitMessage = "A critical hit!";
                strCounter3 =0;
            }
        }
        else {                                            // the enemy pokemon attacked first
            attackMessage2 = myPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker2 = myPokemon;
            if(criticalHit) {
                secondCritHitMessage = "A critical hit!";
                strCounter3 = 0;
            }
        }
        animationBusy = true;

    }

    public void throwPokeballToCatch(CustomItem ball, PokemonSprite enemyPokemon){

        float bonusStatus = 1;      // 1.5 for paralyze, poison or burn; 2 for sleep or freeze; 1 normal
        float ballBonus = calculateBallBonus(ball);
        float rate = enemyPokemon.getCaptureRate();
        float HPmax = enemyPokemon.getStats().getHp();
        float HPcurrent = enemyPokemon.getCurrentHP();
        float a = ((3*HPmax - 2*HPcurrent) * rate * ballBonus * bonusStatus)/(3*HPmax);
        double b = 1048560 / Math.sqrt(Math.sqrt(16711680/a));

        pokeballMessage = "Go! "+ball.getName().replace("-"," ").toUpperCase()+"!";
        strCounter2 = 0;            // for pokeballMessage
        strCounter3 = 0;            // for capturingMessage
        catchTimeCounter=0;
        ballMovementX =0;
        ballMovementY =0;
        shakeCounter = 0;

        boolean ballFailed = false;

        if(!enemyType.equals("wild")) {
            pokemonCapturingState = "trainer_pokemon";
            pokemonCapturingMessage = "A pokemon from a trainer cannot be caught!";
        }
        else {
            if (a >= 255) {
                pokemonCapturingState = "instant_caught";
                pokemonCapturingMessage = "Pokemon caught!";
            } else {       // 4 shakes are necessary to capture the pokemon in this case
                Random r = new Random();
                for (int i = 0; i < 4; i++) {
                    if (!ballFailed)
                        shakeCounter++;

                    int randomNumber = r.nextInt(65536);
                    if (randomNumber >= b) {
                        ballFailed = true;
                    }
                }
                if (!ballFailed) {
                    pokemonCapturingState = "caught";
                    pokemonCapturingMessage = "Pokemon caught!";
                } else {
                    pokemonCapturingState = "not_caught";
                    if (shakeCounter == 3)
                        pokemonCapturingMessage = "ARG, almost had it!";
                    else
                        pokemonCapturingMessage = "Oh no! The enemy pokemon broke free!";
                }
            }
        }

        ballToCatchThrown = true;


    }

    public void youWon(int gainedExperience){
        this.gainedExperience = gainedExperience;
        endBattleStatus = "WON";
        endBattleMessage = "You won and gained "+gainedExperience +" XP";
        animationBusy = true;

    }

    public void youLost(){
        this.gainedExperience = 0;
        endBattleStatus = "LOST";
        endBattleMessage = FightActivity.myCurrentPokemon.getName().toUpperCase()+" fainted";
        animationBusy = true;

    }

    public void showContinueFightingMessage(String whoHasToSwitch){
        // "me" or "enemy"
        Log.d("END", "endofencounter" + endOfEncounter);
        endBattleMessage = "";
        endOfEncounter = false;
        if(whoHasToSwitch.equals("me")) {
            continueFightingMessage = "Do you want to select another pokémon?";
        }
        else{
           continueFightingMessage = "My enemy chooses another pokemon!";
            endBattleStatus="";
        }
        animationBusy = true;

        timeCounter3 = 0;
        timeCounter2 = 0;
        strCounter2 = 0;
    }

    private float calculateBallBonus(CustomItem ball){
        String catchRateStr;
        Float catchRate=0.0f;
        if(ball.getCategoryName().contains("balls")){         // do this only for balls
            if(ball.getShortDescription().indexOf("rate")>-1){
                catchRateStr = ball.getShortDescription().substring(ball.getShortDescription().indexOf("rate"));
                int start = catchRateStr.indexOf("is");
                int end = catchRateStr.indexOf("×");
                catchRateStr = catchRateStr.substring(start+3,end);
                catchRate = Float.parseFloat(catchRateStr);
            }
            else {                // if rate is not specified, it is either a master ball or the catch rate is just 1
                if (ball.getName().equals("master-ball"))
                    catchRate = 1000.0f;
                else
                    catchRate = 1.0f;
            }
        }
        return catchRate;
    }

    public void runAway(){
        runAwayStringCounter=0;
        runAwayTimeCounter=0;
        continueFightingMessage = "";

        if(enemyType.equals("wild"))
            runAwayMessage = "Ran away safely"; // except if wild pokemon has too high level, to be checked
        else
            runAwayMessage = "Can't run away from a trainer-fight!";

    }
}
