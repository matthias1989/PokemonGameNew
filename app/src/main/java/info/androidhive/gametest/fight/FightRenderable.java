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
    private TextView fightInfoTxt;

    private int x1;
    private int x2;
    private int ballMovementX;
    private int ballMovementY;
    private String strAppeared;
    private String goPokemon;
    private String strAction;
    private int strCounter =0;
    private int strCounter2=0;
    private int timeCounter =0;
    private int timeCounter2 = 0;
    private int strCounter3=0;
    private int timeCounter3 = 0;
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

    private FightListener fightListener;
    private int gainedExperience=0;
    String endBattleMessage = "";
    String endBattleStatus = "";

    private boolean pokemonChanged = false;

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

        PokemonSprite myPokemon = Utils.myPokemons.getMyPokemonByOrderNr(0);
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
        loadEndAnimation(c);

        //c.drawBitmap(myPokemonBm,x2+60,328,null);
    }

    public void loadStartAnimation(Canvas c){
        c.drawBitmap(Utils.wildPlatform, x1, 180, null);
        if((timeCounter2==0 || timeCounter2>LENGTH_OF_ATTACK || currentAttacker==wildPokemon)&& !endBattleStatus.equals("WON"))
            c.drawBitmap(Utils.wildPokemonBm, x1 + 100, 40, null);
        c.drawBitmap(Utils.myPlatform, x2, 450, null);
        if(timeCounter<10)
            c.drawBitmap(Utils.player1,x2+110,328,null);


        if(x1 < 500){
            x1+=35;         // x1 goes from -250 to 500
            x2 -= 35;       // x2 goes from 700 to -50
        }
        else{
            if(timeCounter<=51) {
                if(!pokemonChanged)
                    c.drawText(strAppeared, 0, strCounter, 70, 690, paint);

                if (strCounter < strAppeared.length()) {
                    strCounter++;
                } else {       // after "a wild ... has appeared"
                    throwPokeballWithMyPokemonAnimation(c);
                    timeCounter++;
                }

            }
            if(timeCounter>51){
                if(timeCounter2==0 || timeCounter2>LENGTH_OF_ATTACK || currentAttacker==myPokemon){
                            c.drawBitmap(Utils.myPokemonBm, x2 + 60, 290, null);
                }


                if(timeCounter<75) {
                    c.drawText(goPokemon, 0, strCounter, 70, 690, paint);
                    if (strCounter < goPokemon.length()) {
                        strCounter++;
                    }
                }
                else if(timeCounter==75)
                    strCounter = 0;

                else{
                    if(attackMessage1.equals("") && attackMessage2.equals("") && endBattleMessage.equals("")) {
                        c.drawText(strAction, 0, strCounter, 70, 690, paint);
                        if (strCounter < strAction.length()) {
                            strCounter++;
                        }
                    }

                    if(timeCounter==95) {         // if animation is done
                        fightListener.showActionButtons();
                    }
                }
                timeCounter++;
            }


        }
    }

    public void throwPokeballWithMyPokemonAnimation(Canvas c){
        if (timeCounter >= 10 && timeCounter < 12)
            c.drawBitmap(Utils.player2, x2 + 80, 328, null);
        else if (timeCounter >= 12 && timeCounter < 14)
            c.drawBitmap(Utils.player3, x2 + 50, 328, null);
        else if (timeCounter >= 14 && timeCounter < 16)
            c.drawBitmap(Utils.player4, x2 + 20, 328, null);
        else if (timeCounter >= 16 && timeCounter < 18)
            c.drawBitmap(Utils.player5, x2 - 10, 328, null);
        else if (timeCounter >= 18 && timeCounter < 21)
            c.drawBitmap(Utils.pokeball1, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 21 && timeCounter < 24)
            c.drawBitmap(Utils.pokeball2, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 24 && timeCounter < 27)
            c.drawBitmap(Utils.pokeball3, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 27 && timeCounter < 30)
            c.drawBitmap(Utils.pokeball4, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 30 && timeCounter < 33)
            c.drawBitmap(Utils.pokeball5, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 33 && timeCounter < 36)
            c.drawBitmap(Utils.pokeball6, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 36 && timeCounter < 39)
            c.drawBitmap(Utils.pokeball7, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 39 && timeCounter < 42)
            c.drawBitmap(Utils.pokeball8, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 42 && timeCounter < 45)
            c.drawBitmap(Utils.pokeball9, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 45 && timeCounter < 48)
            c.drawBitmap(Utils.pokeball9, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if (timeCounter >= 48 && timeCounter < 51)
            c.drawBitmap(Utils.pokeball10, START_BALL_X+ballMovementX, START_BALL_Y+ballMovementY, null);
        else if(timeCounter==51)
            strCounter = 0;

        ballMovementX += 4;
        ballMovementY += 9;
    }

    public void loadAttackInfo(Canvas c){
        if(!attackMessage1.equals("") && timeCounter>=95){      // only to this if the animation is completely loaded (also when switch pokemon)
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
        else {
            attackMessage1 = "";
            strCounter2 = 0;
            timeCounter2 = 0;

            if(attacker1==myPokemon)
                fightListener.myAttackAnimationIsDone(myMove);
            else
                fightListener.wildAttackAnimationIsDone(wildMove);

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

        else {
            attackMessage2 = "";
            strCounter2 = 0;
            timeCounter2 = 0;

            if(attacker2==myPokemon)
                fightListener.myAttackAnimationIsDone(myMove);
            else
                fightListener.wildAttackAnimationIsDone(wildMove);
        }
    }

    private void loadEndAnimation(Canvas c){
        if (!endBattleMessage.equals("") && attackMessage1.equals("") && attackMessage2.equals("")) {
            if(gainedExperience>0)
                endBattleStatus = "WON";
            else
                endBattleStatus = "LOST";

            if(timeCounter2<50) {
                c.drawText(endBattleMessage, 0, strCounter2, 70, 690, paint);
                if (strCounter2 < endBattleMessage.length()) {
                    strCounter2++;
                }
                timeCounter2++;
            }
            else{
                //Log.d("BATTLE","battle done");
                fightListener.finishBattle(gainedExperience);
            }
        }
    }



    public void changePokemon(){
        PokemonSprite myPokemon = Utils.myPokemons.getMyPokemonByOrderNr(0);
        String id2 = "a"+myPokemon.getId()+"_back";

        int resID = mView.getResources().getIdentifier(id2, "drawable", mView.getContext().getPackageName());
        Utils.myPokemonBm = BitmapFactory.decodeResource(mView.getResources(), resID);
        Utils.myPokemonBm = Bitmap.createScaledBitmap(Utils.myPokemonBm, 350, 350, false);

        fightListener.showActionButtons();
        goPokemon = "Go! "+myPokemon.getName().toUpperCase()+"!";
        strAction = "What will "+myPokemon.getName().toUpperCase()+ " do?";
        strCounter = 0;
        timeCounter = 18;
        pokemonChanged = true;
        ballMovementX =0;
        ballMovementY =0;

    }

    public void wildAttacked(Move attack, PokemonSprite wildPokemon){
        this.wildPokemon = wildPokemon;
        this.wildMove = attack;
        if(attackMessage1.equals("")) {        // the wild pokemon attacked first
            attackMessage1 = "The wild " + wildPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker1 = wildPokemon;
        }
        else {                                            // you attacked first
            attackMessage2 = "The wild " + wildPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker2 = wildPokemon;
        }
    }

    public void youAttacked(Move attack, PokemonSprite myPokemon){
        this.myPokemon = myPokemon;
        this.myMove = attack;
        if(attackMessage1.equals("")) {                   // you attacked first
            attackMessage1 = myPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker1 = myPokemon;
        }
        else {                                            // the wild pokemon attacked first
            attackMessage2 = myPokemon.getName().toUpperCase() + " used " + attack.getName().toUpperCase();
            attacker2 = myPokemon;
        }
    }

    public void throwPokeballToCatch(CustomItem ball, PokemonSprite wildPokemon){

        // this will start the animation

        float bonusStatus = 1;      // 1.5 for paralyze, poison or burn; 2 for sleep or freeze; 1 normal
        float ballBonus = calculateBallBonus(ball);
        float rate = wildPokemon.getCaptureRate();
        float HPmax = wildPokemon.getStats().getHp();
        float HPcurrent = wildPokemon.getCurrentHP();
        float a = ((3*HPmax - 2*HPcurrent) * rate * ballBonus * bonusStatus)/(3*HPmax);
        Log.d("A",a+"");
        double b = 1048560 / Math.sqrt(Math.sqrt(16711680/a));


        boolean ballFailed = false;
        if(a>=255){
            Log.d("BALL","Pokemon caught");
        }
        else{       // 4 shakes are necessary to capture the pokemon in this case
            Random r = new Random();
            for(int i=0;i<4;i++){
                int randomNumber = r.nextInt(65536);
                if(randomNumber>=b){
                   ballFailed = true;
                    break;
                }
                else{
                    // do shake animation
                }
            }
            if(ballFailed==false)
                Log.d("BALL","Pokemon caught");
            else
                Log.d("BALL","Pokemon escaped");
        }




    }

    public void youWon(int gainedExperience){
        this.gainedExperience = gainedExperience;
        endBattleMessage = "You won and gained "+gainedExperience +" XP";

    }

    public void youLost(){
        endBattleMessage = "You lost";

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
