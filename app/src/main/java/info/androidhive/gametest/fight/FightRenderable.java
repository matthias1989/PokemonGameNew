package info.androidhive.gametest.fight;

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

    private boolean endOfEncounter = false;

    private int x1;
    private int x2;
    private int ballMovementX;
    private int ballMovementY;
    private String strAppeared;
    private String goPokemon;
    private String strAction;
    private int strCounter =0;
    private int strCounter2=0;
    private int timeCounter =0;           // loading start
    private int timeCounter2 = 0;         // attacking
    private int strCounter3=0;
    private int timeCounter3 = 0;       // pokecatching
    public static boolean wildPokemonLoaded = false;
    private String attackMessage1 = "";
    private String attackMessage2 = "";
    private PokemonSprite attacker1=null;
    private PokemonSprite attacker2=null;
    private PokemonSprite wildPokemon;
    private PokemonSprite myPokemon;
    private PokemonSprite currentAttacker;

    private Move wildMove;
    private Move myMove;
    private String firstCritHitMessage="";
    private String secondCritHitMessage="";

    private FightListener fightListener;
    private int gainedExperience=0;
    private String endBattleMessage = "";
    private String endBattleStatus = "";
    private String pokeballMessage = "";
    private String continueFightingMessage = "";
    private boolean pokemonChanged = false;
    private boolean animationBusy = false;


    private boolean ballToCatchThrown = false;
    private String pokemonCapturingState = "";
    private String pokemonCapturingMessage = "";
    private int shakeCounter = 0;
    private boolean wildInBall = false;

    public void setFightListener(FightListener fightListener) {
        this.fightListener = fightListener;
    }

    public FightRenderable(SurfaceView view,SurfaceHolder holder){

        mHolder = holder;
        mView = view;
        x1=-250;
        x2 = 700;

        currentTime = SystemClock.elapsedRealtime();

        PokemonSprite wildPokemon = MapForeground.pokemonSprite;
        String id1 = "a"+wildPokemon.getId();
        int resID = view.getResources().getIdentifier(id1, "drawable", view.getContext().getPackageName());
        Utils.wildPokemonBm = BitmapFactory.decodeResource(view.getResources(), resID);
        Utils.wildPokemonBm = Bitmap.createScaledBitmap(Utils.wildPokemonBm, 350, 350, false);

        PokemonSprite myPokemon = Utils.mySprite.getMyPokemons().getMyPokemonByOrderNr(0);
        String id2 = "a"+myPokemon.getId()+"_back";
        resID = view.getResources().getIdentifier(id2, "drawable", view.getContext().getPackageName());
        Utils.myPokemonBm = BitmapFactory.decodeResource(view.getResources(), resID);
        Utils.myPokemonBm = Bitmap.createScaledBitmap(Utils.myPokemonBm, 350, 350, false);

        Utils.wildPlatform= BitmapFactory.decodeResource(view.getResources(), battle_bg1_part2);
        Utils.wildPlatform = Bitmap.createScaledBitmap(Utils.wildPlatform, 600, 200, false);

        Utils.myPlatform= BitmapFactory.decodeResource(view.getResources(), battle_bg1_part3);
        Utils.myPlatform = Bitmap.createScaledBitmap(Utils.myPlatform, 600, 200, false);

        Utils.player1 = BitmapFactory.decodeResource(view.getResources(), mainchar_animation1);
        Utils.player1 = Bitmap.createScaledBitmap(Utils.player1, 250, 310, false);
        Utils.player2 = BitmapFactory.decodeResource(view.getResources(), mainchar_animation2);
        Utils.player2 = Bitmap.createScaledBitmap(Utils.player2, 250, 310, false);
        Utils.player3 = BitmapFactory.decodeResource(view.getResources(), mainchar_animation3);
        Utils.player3 = Bitmap.createScaledBitmap(Utils.player3, 250, 310, false);
        Utils.player4 = BitmapFactory.decodeResource(view.getResources(), mainchar_animation4);
        Utils.player4 = Bitmap.createScaledBitmap(Utils.player4, 250, 310, false);
        Utils.player5 = BitmapFactory.decodeResource(view.getResources(), mainchar_animation5);
        Utils.player5 = Bitmap.createScaledBitmap(Utils.player5, 250, 310, false);

        Utils.pokeball1 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball1);
        Utils.pokeball1 = Bitmap.createScaledBitmap(Utils.pokeball1, 60, 60, false);
        Utils.pokeball2 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball2);
        Utils.pokeball2 = Bitmap.createScaledBitmap(Utils.pokeball2, 60, 60, false);
        Utils.pokeball3 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball3);
        Utils.pokeball3 = Bitmap.createScaledBitmap(Utils.pokeball3, 60, 60, false);
        Utils.pokeball4 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball4);
        Utils.pokeball4 = Bitmap.createScaledBitmap(Utils.pokeball4, 60, 60, false);
        Utils.pokeball5 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball5);
        Utils.pokeball5 = Bitmap.createScaledBitmap(Utils.pokeball5, 60, 60, false);
        Utils.pokeball6 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball6);
        Utils.pokeball6 = Bitmap.createScaledBitmap(Utils.pokeball6, 60, 60, false);
        Utils.pokeball7 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball7);
        Utils.pokeball7 = Bitmap.createScaledBitmap(Utils.pokeball7, 60, 60, false);
        Utils.pokeball8 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball8);
        Utils.pokeball8 = Bitmap.createScaledBitmap(Utils.pokeball8, 60, 60, false);
        Utils.pokeball9 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball9);
        Utils.pokeball9 = Bitmap.createScaledBitmap(Utils.pokeball9, 60, 60, false);
        Utils.pokeball10 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball10);
        Utils.pokeball10 = Bitmap.createScaledBitmap(Utils.pokeball10, 60, 120, false);
        Utils.pokeball11 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball11);
        Utils.pokeball11 = Bitmap.createScaledBitmap(Utils.pokeball11, 60, 60, false);
        Utils.pokeball12 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball12);
        Utils.pokeball12 = Bitmap.createScaledBitmap(Utils.pokeball12, 60, 60, false);
        Utils.pokeball13 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball13);
        Utils.pokeball13 = Bitmap.createScaledBitmap(Utils.pokeball13, 60, 60, false);
        Utils.pokeball14 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball14);
        Utils.pokeball14 = Bitmap.createScaledBitmap(Utils.pokeball14, 60, 60, false);
        Utils.pokeball15 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball15);
        Utils.pokeball15 = Bitmap.createScaledBitmap(Utils.pokeball15, 60, 60, false);
        Utils.pokeball16 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball16);
        Utils.pokeball16 = Bitmap.createScaledBitmap(Utils.pokeball16, 60, 60, false);
        Utils.pokeball17 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball17);
        Utils.pokeball17 = Bitmap.createScaledBitmap(Utils.pokeball17, 60, 60, false);
        Utils.pokeball18 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball18);
        Utils.pokeball18 = Bitmap.createScaledBitmap(Utils.pokeball18, 60, 60, false);
        Utils.pokeball19 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball19);
        Utils.pokeball19 = Bitmap.createScaledBitmap(Utils.pokeball19, 60, 60, false);
        Utils.pokeball20 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball20);
        Utils.pokeball20 = Bitmap.createScaledBitmap(Utils.pokeball20, 60, 60, false);
        Utils.pokeball21 = BitmapFactory.decodeResource(view.getResources(), R.drawable.pokeball21);
        Utils.pokeball21 = Bitmap.createScaledBitmap(Utils.pokeball21, 60, 60, false);

        //fightInfoTxt = (TextView) mView.findViewById(R.id.fight_info_txt);
        //fightInfoTxt = new TextView(mView.getContext());
        strAppeared = "A wild " + wildPokemon.getName().toUpperCase() + " appeared !";
        goPokemon = "Go! "+myPokemon.getName().toUpperCase()+"!";
        strAction = "What will "+myPokemon.getName().toUpperCase()+ " do?";
        strCounter = 0;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(35);

        timeCounter = 0;
        ballMovementX = 0;
        ballMovementY = 0;



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
        //Log.d("TIME ", "" + (newTime - currentTime));
        loadStartAnimation(c);
        loadAttackInfo(c);
        throwPokeballToCatchAnimation(c);
        loadEndAnimation(c);
        continueFightingAnimation(c);



        //c.drawBitmap(myPokemonBm,x2+60,328,null);
    }

    public void loadStartAnimation(Canvas c){
        c.drawBitmap(Utils.wildPlatform, x1, 180, null);
        if((timeCounter2==0 || timeCounter2>LENGTH_OF_ATTACK || currentAttacker==wildPokemon)&& !endBattleStatus.equals("WON") && !wildInBall)
            c.drawBitmap(Utils.wildPokemonBm, x1 + 100, 40, null);
        c.drawBitmap(Utils.myPlatform, x2, 450, null);
        if(timeCounter<10)
            c.drawBitmap(Utils.player1,x2+110,328,null);


        if(x1 < 500){
            x1+=35;         // x1 goes from -250 to 500
            x2 -= 35;       // x2 goes from 700 to -50
        }
        else{
            if(timeCounter<=40) {
                if(!pokemonChanged)
                    c.drawText(strAppeared, 0, strCounter, 70, 690, paint);

                if (strCounter < strAppeared.length()) {
                    strCounter++;
                } else {       // after "a wild ... has appeared"
                    throwPokeballWithMyPokemonAnimation(c);
                    timeCounter++;
                }

            }
            if(timeCounter>40){
                if((timeCounter2==0 || timeCounter2>LENGTH_OF_ATTACK || currentAttacker==myPokemon) && !endBattleStatus.equals("LOST")){
                            c.drawBitmap(Utils.myPokemonBm, x2 + 60, 290, null);
                }

                if(timeCounter<63) {
                    c.drawText(goPokemon, 0, strCounter, 70, 690, paint);
                    if (strCounter < goPokemon.length()) {
                        strCounter++;
                    }
                }
                else if(timeCounter==63)
                    strCounter = 0;

                else{
                    if(!animationBusy && !ballToCatchThrown && !endOfEncounter) {
                        c.drawText(strAction, 0, strCounter, 70, 690, paint);
                        if (strCounter < strAction.length()) {
                            strCounter++;
                        }
                    }

                    if(timeCounter==80) {         // if animation is done
                        fightListener.showActionButtons();
                    }
                }
                timeCounter++;
            }




        }
    }

    public void throwPokeballWithMyPokemonAnimation(Canvas c){
        if (timeCounter >= 10 && timeCounter < 12) c.drawBitmap(Utils.player2, x2 + 80, 328, null);
        else if (timeCounter >= 12 && timeCounter < 14) c.drawBitmap(Utils.player3, x2 + 50, 328, null);
        else if (timeCounter >= 14 && timeCounter < 16) c.drawBitmap(Utils.player4, x2 + 20, 328, null);
        else if (timeCounter >= 16 && timeCounter < 18) c.drawBitmap(Utils.player5, x2 - 10, 328, null);
        else if (timeCounter >= 18 && timeCounter < 20) c.drawBitmap(Utils.pokeball1, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 20 && timeCounter < 22) c.drawBitmap(Utils.pokeball2, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 22 && timeCounter < 24) c.drawBitmap(Utils.pokeball3, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 24 && timeCounter < 26) c.drawBitmap(Utils.pokeball4, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 26 && timeCounter < 28) c.drawBitmap(Utils.pokeball5, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 28 && timeCounter < 30) c.drawBitmap(Utils.pokeball6, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 30 && timeCounter < 32) c.drawBitmap(Utils.pokeball7, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 32 && timeCounter < 34) c.drawBitmap(Utils.pokeball8, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 34 && timeCounter < 36) c.drawBitmap(Utils.pokeball9, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 36 && timeCounter < 38) c.drawBitmap(Utils.pokeball9, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 38 && timeCounter < 40) c.drawBitmap(Utils.pokeball10, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if(timeCounter==40) strCounter = 0;

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
                c.drawBitmap(Utils.wildPokemonBm, x1+100-20, 40, null);    //500
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
            c.drawText(firstCritHitMessage, 0, strCounter3, 70, 690, paint);
            if (strCounter3 < firstCritHitMessage.length()) {
                strCounter3++;
            }
        }
        else{
            firstCritHitMessage = "";
            attackMessage1 = "";
            strCounter2 = 0;
            timeCounter2 = 0;

            if(attacker1==myPokemon)
                fightListener.myAttackAnimationIsDone(myMove);
            else
                fightListener.wildAttackAnimationIsDone(wildMove);

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
                c.drawBitmap(Utils.wildPokemonBm, x1+100-20, 40, null);
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
           c.drawText(secondCritHitMessage, 0, strCounter3, 70, 690, paint);
           if (strCounter3 < secondCritHitMessage.length()) {
               strCounter3++;
           }
       }

        else {
           secondCritHitMessage = "";
            attackMessage2 = "";
            strCounter2 = 0;
            timeCounter2 = 0;

            if(attacker2==myPokemon)
                fightListener.myAttackAnimationIsDone(myMove);
            else
                fightListener.wildAttackAnimationIsDone(wildMove);

           animationBusy = false;
        }

    }

    private void throwPokeballToCatchAnimation(Canvas c){

        if(ballToCatchThrown){
            if(timeCounter3<200) {
                if(strCounter3==0) {       // only write this message if the pokemon capture message is not being written
                    c.drawText(pokeballMessage, 0, strCounter2, 70, 690, paint);
                    if (strCounter2 < pokeballMessage.length()) {
                        strCounter2++;
                    }
                }
                if(timeCounter3>=10 && timeCounter3<15){
                    ballMovementY -= 35;
                    c.drawBitmap(Utils.pokeball1, START_BALL_X + ballMovementX, START_BALL_Y + ballMovementY, null);
                }
                else if(timeCounter3>=15 && timeCounter3<20){
                    ballMovementY -= 12;
                    c.drawBitmap(Utils.pokeball4, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
                }

                else if(timeCounter3>=20 && timeCounter3<25){
                    ballMovementY += 12;
                    c.drawBitmap(Utils.pokeball7, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
                }

                else if(timeCounter3>=25 && timeCounter3<30){
                    ballMovementY += 25;
                    c.drawBitmap(Utils.pokeball8, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
                }

                else if(timeCounter3>30 && timeCounter3 < 33){
                    c.drawBitmap(Utils.pokeball10, START_BALL_X+700, START_BALL_Y-20, null);
                }
                else if(timeCounter3==33){
                    wildInBall = true;
                    c.drawBitmap(Utils.pokeball11, START_BALL_X+700, START_BALL_Y+20, null);
                }
                else if(timeCounter3==34) c.drawBitmap(Utils.pokeball12, START_BALL_X+700, START_BALL_Y+50, null);
                else if(timeCounter3 == 35) c.drawBitmap(Utils.pokeball13, START_BALL_X+700, START_BALL_Y+80, null);
                else if (timeCounter3 == 36) c.drawBitmap(Utils.pokeball14, START_BALL_X+700, START_BALL_Y+110, null);
                else if (timeCounter3 == 37) c.drawBitmap(Utils.pokeball15, START_BALL_X+700, START_BALL_Y+140, null);
                else if(timeCounter3==38) c.drawBitmap(Utils.pokeball16, START_BALL_X+700, START_BALL_Y+140, null);
                else if(timeCounter3>38){
                    if(pokemonCapturingState.equals("instant_caught")){
                        if(timeCounter3==39 ||timeCounter3==40){
                            c.drawBitmap(Utils.pokeball20, START_BALL_X+700, START_BALL_Y+140, null);
                            strCounter3 = 0;
                        }
                        else if(timeCounter3>=41 && timeCounter3<75) {
                            c.drawBitmap(Utils.pokeball21, START_BALL_X + 700, START_BALL_Y + 140, null);
                            c.drawText(pokemonCapturingMessage, 0, strCounter3, 70, 690, paint);
                            if (strCounter3 < pokemonCapturingMessage.length()) {
                                strCounter3++;
                            }
                        }
                        else if(timeCounter3==75) {
                            fightListener.pokemonCaptured(true);
                            ballToCatchThrown = false;
                            endOfEncounter = true;
                        }
                    }
                    else{
                        if(((timeCounter3==39 || timeCounter3==40) && shakeCounter>=1) || ((timeCounter3==53 || timeCounter3==54) && shakeCounter >=2) || ((timeCounter3==66 || timeCounter3==67) && shakeCounter>=3) || ((timeCounter3==80 || timeCounter3==81) && shakeCounter==4)   )
                            c.drawBitmap(Utils.pokeball17, START_BALL_X+680, START_BALL_Y+140, null);
                        else if(((timeCounter3==41 || timeCounter3==42) && shakeCounter>=1) || ((timeCounter3==55 || timeCounter3==56) && shakeCounter >=2) || ((timeCounter3==68 || timeCounter3==69) && shakeCounter>=3) || ((timeCounter3==82 || timeCounter3==83) && shakeCounter==4)   )
                            c.drawBitmap(Utils.pokeball18, START_BALL_X+700, START_BALL_Y+140, null);
                        else if(((timeCounter3==43 || timeCounter3==44) && shakeCounter>=1) || ((timeCounter3==57 || timeCounter3==58) && shakeCounter >=2) || ((timeCounter3==70 || timeCounter3==71) && shakeCounter>=3) || ((timeCounter3==84 || timeCounter3==85) && shakeCounter==4)   )
                            c.drawBitmap(Utils.pokeball17, START_BALL_X+720, START_BALL_Y+140, null);
                        else if(((timeCounter3>=45 && timeCounter3<53) && shakeCounter>=1) || ((timeCounter3>=58 && timeCounter3<66) && shakeCounter >=2) || ((timeCounter3>=72 && timeCounter3<80) && shakeCounter>=3) || ((timeCounter3>=86 && timeCounter3<94) && shakeCounter==4)   )
                            c.drawBitmap(Utils.pokeball19, START_BALL_X+700, START_BALL_Y+140, null);

                        if(pokemonCapturingState.equals("caught") && timeCounter3>=94){
                            if(timeCounter3>=94 && timeCounter3<96) {
                                c.drawBitmap(Utils.pokeball20, START_BALL_X + 700, START_BALL_Y + 140, null);
                                strCounter3 = 0;
                            }
                            else if(timeCounter3>=96 && timeCounter3<130) {
                                c.drawBitmap(Utils.pokeball21, START_BALL_X + 700, START_BALL_Y + 140, null);
                                c.drawText(pokemonCapturingMessage, 0, strCounter3, 70, 690, paint);
                                if (strCounter3 < pokemonCapturingMessage.length()) {
                                    strCounter3++;
                                }
                            }
                            else if(timeCounter3==130) {
                                fightListener.pokemonCaptured(true);
                                endOfEncounter = true;
                                ballToCatchThrown = false;
                            }
                        }
                        else if(pokemonCapturingState.equals("not_caught") && timeCounter3>=53){
                            if((timeCounter3==53 && shakeCounter==1) || (timeCounter3==66 && shakeCounter ==2) || (timeCounter3==80 && shakeCounter==3) || (timeCounter3==94 && shakeCounter==4)   ) {
                                strCounter3 = 0;
                                c.drawBitmap(Utils.pokeball10, START_BALL_X + 700, START_BALL_Y + 20, null);
                            }
                            if((timeCounter3>=54 && timeCounter3<88 && shakeCounter==1) || (timeCounter3 >= 67 && timeCounter3 < 101 && shakeCounter == 2) || (timeCounter3>=81 && timeCounter3<115 && shakeCounter==3) || (timeCounter3>=95 && timeCounter3 < 129 && shakeCounter==4)   ) {
                                wildInBall = false;
                                c.drawBitmap(Utils.pokeball10, START_BALL_X + 700, START_BALL_Y + 20, null);
                                c.drawText(pokemonCapturingMessage, 0, strCounter3, 70, 690, paint);
                                if (strCounter3 < pokemonCapturingMessage.length()) {
                                    strCounter3++;
                                }
                            }
                            else if((timeCounter3==88 && shakeCounter==1) || (timeCounter3==101 && shakeCounter ==2) || (timeCounter3==115 && shakeCounter==3) || (timeCounter3==129 && shakeCounter==4)   ) {
                                //fightListener.pokemonCaptured(false);
                                ballToCatchThrown = false;
                            }

                        }

                    }

                    //c.drawBitmap(Utils.pokeball17, START_BALL_X+700, START_BALL_Y+140, null);
                }




                ballMovementX+=23;
                timeCounter3++;
            }

        }

    }

    private void loadEndAnimation(Canvas c){
        if (!endBattleMessage.equals("") && attackMessage1.equals("") && attackMessage2.equals("")) {
            if(gainedExperience>0)
                endBattleStatus = "WON";
            else
                endBattleStatus = "LOST";

            if(timeCounter2<50) {
                animationBusy = true;
                c.drawText(endBattleMessage, 0, strCounter2, 70, 690, paint);
                if (strCounter2 < endBattleMessage.length()) {
                    strCounter2++;
                }
            }
            else if(timeCounter2==50){
                fightListener.finishBattle(gainedExperience);
                endOfEncounter = true;
                animationBusy = false;
            }
            timeCounter2++;
        }
    }

    public void continueFightingAnimation(Canvas c){

        if(!continueFightingMessage.equals("")) {
            c.drawText(continueFightingMessage, 0, strCounter2, 70, 690, paint);


            if (strCounter2 < continueFightingMessage.length()) {
                strCounter2++;
            }
            else{
                Log.d("COUNTERS",strCounter2+","+timeCounter2);
                if(timeCounter2==50){       // do this only once
                    fightListener.switchPokemonAfterFainted();


                }

            }
            timeCounter2++;


        }
    }

    public void changePokemon(){
        continueFightingMessage ="";
        endBattleStatus = "";
        endOfEncounter = false;
        timeCounter2 = 0;
        strCounter2 = 0;

        PokemonSprite myPokemon = Utils.mySprite.getMyPokemons().getMyPokemonByOrderNr(0);
        String id2 = "a" + myPokemon.getId()+"_back";

        int resID = mView.getResources().getIdentifier(id2, "drawable", mView.getContext().getPackageName());
        Utils.myPokemonBm = BitmapFactory.decodeResource(mView.getResources(), resID);
        Utils.myPokemonBm = Bitmap.createScaledBitmap(Utils.myPokemonBm, 350, 350, false);

        //fightListener.showActionButtons();
        goPokemon = "Go! "+myPokemon.getName().toUpperCase()+"!";
        strAction = "What will "+myPokemon.getName().toUpperCase()+ " do?";
        strCounter = 0;
        timeCounter = 18;
        pokemonChanged = true;
        ballMovementX =0;          // these 2 vars will be used to move the ball from a start loc to a next one
        ballMovementY =0;

        animationBusy = false;

    }

    public void wildAttacked(Move attack, PokemonSprite wildPokemon, PokemonSprite myPokemon, boolean criticalHit){
        this.wildPokemon = wildPokemon;
        this.myPokemon = myPokemon;
        this.wildMove = attack;
        if(attackMessage1.equals("")) {        // the wild pokemon attacked first
            attackMessage1 = "The wild " + wildPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker1 = wildPokemon;
            if(criticalHit) {
                firstCritHitMessage = "A critical hit!";
                strCounter3 =0;
            }
        }
        else {                                            // you attacked first
            attackMessage2 = "The wild " + wildPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker2 = wildPokemon;
            if(criticalHit) {
                secondCritHitMessage = "A critical hit!";
                strCounter3 =0;
            }
        }
        animationBusy = true;

    }

    public void youAttacked(Move attack, PokemonSprite myPokemon, PokemonSprite wildPokemon, boolean criticalHit){
        this.wildPokemon = wildPokemon;
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
        else {                                            // the wild pokemon attacked first
            attackMessage2 = myPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker2 = myPokemon;
            if(criticalHit) {
                secondCritHitMessage = "A critical hit!";
                strCounter3 = 0;
            }
        }
        animationBusy = true;

    }

    public void throwPokeballToCatch(CustomItem ball, PokemonSprite wildPokemon){

        float bonusStatus = 1;      // 1.5 for paralyze, poison or burn; 2 for sleep or freeze; 1 normal
        float ballBonus = calculateBallBonus(ball);
        float rate = wildPokemon.getCaptureRate();
        float HPmax = wildPokemon.getStats().getHp();
        float HPcurrent = wildPokemon.getCurrentHP();
        float a = ((3*HPmax - 2*HPcurrent) * rate * ballBonus * bonusStatus)/(3*HPmax);
        double b = 1048560 / Math.sqrt(Math.sqrt(16711680/a));

        pokeballMessage = "Go! "+ball.getName().replace("-"," ").toUpperCase()+"!";
        strCounter2 = 0;            // for pokeballMessage
        strCounter3 = 0;            // for capturingMessage
        timeCounter3 = 0;
        ballMovementX =0;
        ballMovementY =0;
        shakeCounter = 0;

        boolean ballFailed = false;
        if(a>=255){
            pokemonCapturingState = "instant_caught";
            pokemonCapturingMessage = "Pokemon caught!";
        }
        else{       // 4 shakes are necessary to capture the pokemon in this case
            Random r = new Random();
            for(int i=0;i<4;i++){
                if(!ballFailed)
                    shakeCounter++;

                int randomNumber = r.nextInt(65536);
                if(randomNumber>=b){
                   ballFailed = true;
                }
            }
            if(!ballFailed) {
                pokemonCapturingState="caught";
                pokemonCapturingMessage = "Pokemon caught!";
            }
            else {
                pokemonCapturingState="not_caught";
                if(shakeCounter==3)
                    pokemonCapturingMessage = "ARG, almost had it!";
                else
                    pokemonCapturingMessage = "Oh no! The wild pokemon broke free!";
            }
        }

        ballToCatchThrown = true;


    }

    public void youWon(int gainedExperience){
        this.gainedExperience = gainedExperience;
        endBattleMessage = "You won and gained "+gainedExperience +" XP";
        animationBusy = true;

    }

    public void youLost(){
        endBattleMessage = myPokemon.getName().toUpperCase()+" fainted";
        animationBusy = true;

    }

    public void showContinueFightingMessage(){
        //endBattleStatus = "";
        endBattleMessage = "";
        //endOfEncounter = false;
        continueFightingMessage = "Do you want to select another pokémon?";
        animationBusy = true;
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
}
