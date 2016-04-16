package info.androidhive.gametest.fight;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import info.androidhive.gametest.FightListener;
import info.androidhive.gametest.R;
import info.androidhive.gametest.Utils;
import info.androidhive.gametest.items.CustomItem;
import info.androidhive.gametest.pokemons.Move;
import info.androidhive.gametest.pokemons.MyPokemons;
import info.androidhive.gametest.pokemons.PokemonSprite;
import info.androidhive.gametest.pokemons.TypeEfficacy;

/**
 * Created by matthias on 3/14/2016.
 */
public class FightActivity extends AppCompatActivity implements FightListener{
    private RelativeLayout fightButtonContainer;
    private FightRenderable fightRenderable;
    private LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    private static FightSurfaceView surfaceView;
    private FrameLayout viewFightFrame;
    private static LinearLayout myBars;
    private LinearLayout enemyBars;
    private RelativeLayout enemyInfoContainer;
    private RelativeLayout myInfoContainer;
    private TextView enemyInfo;
    private TextView enemyInfo2;
    private ProgressBar enemyHP;
    private TextView myInfo;
    private TextView myInfo2;
    private ProgressBar myHP;
    private ProgressBar myExp;
    private int myCurrentHPProgress;
    private int enemyCurrentHPProgress;
    private TextView lblMyCurrentHP;
    private TextView lblMyMaxHP;

    private static Random r = new Random();


    public static PokemonSprite myCurrentPokemon;
    private PokemonSprite myPokemonAtStart;
    public static PokemonSprite enemyPokemon;

    private int currentenemyHp;

    private boolean isTrainer;
    private String enemyType;
    private static int enemyDamage;
    private static int myDamage;
    public static boolean criticalHit = false;

    private boolean myAttackDone = false;
    private boolean enemyAttackDone = false;
    private ListView menuList;

    private boolean enemyIsAlive = true;
    private boolean youAreAlive = true;
    private static boolean myAnimationFinished = true;
    private static boolean enemyAnimationFinished = true;

    private static boolean switchedDuringFight = false;

    private int trainerPokemonCounter = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);


        isTrainer = getIntent().getBooleanExtra("isTrainer",false);
        

        myInfo2 = new TextView(this);

        myBars = (LinearLayout) findViewById(R.id.my_bars);
        myBars.setVisibility(View.INVISIBLE);
        enemyBars = (LinearLayout) findViewById(R.id.enemy_bars);
        enemyBars.setVisibility(View.INVISIBLE);

        fightButtonContainer = (RelativeLayout) findViewById(R.id.fight_button_container);
        fightButtonContainer.setVisibility(View.INVISIBLE);
        enemyInfoContainer = (RelativeLayout) findViewById(R.id.enemy_info_container);
        myInfoContainer = (RelativeLayout) findViewById(R.id.my_info_container);
        surfaceView = (FightSurfaceView) findViewById(R.id.surfaceView);
        surfaceView.setFightListener(this);

        int counter = 0;
        while(counter<6) {
            myCurrentPokemon = Utils.mySprite.getMyPokemons().getMyPokemonByOrderNr(counter);
            if(myCurrentPokemon.getCurrentHP()>0)
                break;
            else
                counter++;
        }
        myCurrentPokemon.setTempStats(myCurrentPokemon.getStats());
        myPokemonAtStart = Utils.mySprite.getMyPokemons().getMyPokemonByOrderNr(0);

    }

    @Override
    protected void onStart() {
        super.onStart();

        enemyInfo = (TextView) findViewById(R.id.enemy_info);
    }

    private void determineEnemy(FightRenderable fightRenderable){
        this.fightRenderable = fightRenderable;
        if(!isTrainer)
            enemyType = "wild";
        else
            enemyType = Utils.currentTrainer.getName();

            fightRenderable.startBattle(enemyType, getApplicationContext());
    }

    private void loadPokemons() {
        if (!isTrainer) {
            enemyPokemon = Utils.currentWildPokemon;
            enemyPokemon.setCurrentHP(enemyPokemon.getStats().getHp());
            setupEnemyBars();
        } else {
            enemyPokemon = Utils.currentTrainer.getMyPokemons().getMyPokemonByOrderNr(trainerPokemonCounter);
            if (enemyPokemon.getCurrentHP() <= 0) {
                trainerPokemonCounter++;
                enemyPokemon = Utils.currentTrainer.getMyPokemons().getMyPokemonByOrderNr(trainerPokemonCounter);
                //Log.d("POKE",enemyPokemon.getName());
            }
        }
        enemyPokemon.setTempStats(enemyPokemon.getStats());
        Log.d("ENEMY", enemyPokemon.getId() + "," + enemyPokemon.getEvolvedSpeciesId() + "");
//        MyPokemons myPokemons = Utils.mySprite.getMyPokemons();
//        myCurrentPokemon = myPokemons.getMyPokemonByOrderNr(0);

    }

    private void setupMyBars() {
        myBars.setVisibility(View.VISIBLE);


        myInfo = (TextView) findViewById(R.id.my_info);
        myInfo.setText(myCurrentPokemon.getName().toUpperCase());

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp3.leftMargin = 350;
        myInfoContainer.removeView(myInfo2);
        myInfo2.setText("Lv " + myCurrentPokemon.getLevel());
        myInfoContainer.addView(myInfo2, lp3);

        myHP = (ProgressBar) findViewById(R.id.my_hp);
        myHP.setMax(myCurrentPokemon.getStats().getHp());
        changeHPProgressBar(myCurrentPokemon.getCurrentHP(), myHP, true);

        lblMyCurrentHP = (TextView) findViewById(R.id.lbl_my_current_hp);
        lblMyCurrentHP.setText(myHP.getProgress() + " / " + myHP.getMax());

        myExp = (ProgressBar) findViewById(R.id.my_exp);
        myExp.setProgress(0);
        Rect bounds = myExp.getProgressDrawable().getBounds();
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.progressbar_full_experience);
        myExp.setProgressDrawable(drawable);
        int min = myCurrentPokemon.getExperienceNeededForThisLevel();
        int max = myCurrentPokemon.getExperienceNeededForNextLevel();
        int toGainThisLevel = max - min;
        int current = myCurrentPokemon.getCurrentExperience();
        int gainedThisLevel = current - min;
        int progress = (gainedThisLevel * 100) / toGainThisLevel;
        myExp.getProgressDrawable().setBounds(bounds);
        myExp.setProgress(progress);       // 60% van de bar

        TextView action = new TextView(this);
        action.setText("What will " + myCurrentPokemon.toString() + " do?");

    }

    private void setupEnemyBars(){

        enemyBars.setVisibility(View.VISIBLE);
        enemyInfo.setText(enemyPokemon.getName().toUpperCase());

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.leftMargin = 300;
        enemyInfoContainer.removeView(enemyInfo2);
        enemyInfo2 = new TextView(this);
        enemyInfo2.setText("Lv " + enemyPokemon.getLevel());
        enemyInfoContainer.addView(enemyInfo2, lp2);

        enemyHP = (ProgressBar) findViewById(R.id.enemy_hp);
        enemyHP.setMax(enemyPokemon.getStats().getHp());
        changeHPProgressBar(enemyPokemon.getCurrentHP(), enemyHP, true);
    }


    private void changeHPProgressBar(int newProgress, ProgressBar whoseHP, boolean firstTime){

        int currentProgress = whoseHP.getProgress();

        float progressPerc = newProgress*100/whoseHP.getMax()*1.0f;
        whoseHP.setProgress(0);
        Rect bounds = whoseHP.getProgressDrawable().getBounds();
        if(progressPerc>70)
            whoseHP.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_high_hp));
        else if(progressPerc>40)
            whoseHP.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_medium_hp));
        else
            whoseHP.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_low_hp));
        whoseHP.getProgressDrawable().setBounds(bounds);



        if(firstTime && whoseHP.getProgress()==0)
            whoseHP.setProgress(whoseHP.getMax());
        else {

            ObjectAnimator animation = ObjectAnimator.ofInt(whoseHP, "progress", currentProgress, newProgress);
            animation.setDuration(1000); // 0.5 seconds
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }
        //Log.d("BLA",whoseHP.getProgress()+"");
        whoseHP.setProgress(newProgress);
    }

    private void startActions(){

        fightButtonContainer.setVisibility(View.VISIBLE);
        fightButtonContainer.removeAllViewsInLayout();
        fightButtonContainer.setBackgroundResource(R.drawable.controls1);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp1.leftMargin = 85;
        lp1.topMargin = 142;
        lp1.width = 826;
        lp1.height = 300;
        Button fight = new Button(this);
        fight.setBackgroundResource(R.drawable.fight_button);
        fightButtonContainer.addView(fight, lp1);
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAttackMoves();
            }
        });


        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.leftMargin = 350;
        lp2.topMargin = 550;
        lp2.width = 293;
        lp2.height = 140;
        Button run = new Button(this);
        run.setBackgroundResource(R.drawable.run_button);
        fightButtonContainer.addView(run, lp2);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fightRenderable.runAway();
            }
        });

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp3.leftMargin = 693;
        lp3.topMargin = 525;
        lp3.width = 295;
        lp3.height = 140;
        Button btnSwitchPokemon = new Button(this);
        btnSwitchPokemon.setBackgroundResource(R.drawable.pokemon_button);
        fightButtonContainer.addView(btnSwitchPokemon, lp3);
        btnSwitchPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPokemon1(true);

            }
        });

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp4.leftMargin = 5;
        lp4.topMargin = 525;
        lp4.width = 293;
        lp4.height = 140;
        Button bag = new Button(this);
        bag.setBackgroundResource(R.drawable.bag_button);
        fightButtonContainer.addView(bag, lp4);
        bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bagMenu();

            }
        });

    }
    private void showAttackMoves(){
        fightButtonContainer.setBackgroundResource(R.drawable.controls2);
        fightButtonContainer.removeAllViewsInLayout();

        List<Move> moves = myCurrentPokemon.getLearnedMoves();

        for(int i=0;i<moves.size();i++){
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp1.width = 485;
            lp1.height = 198;

            if(i<4) {
                final RelativeLayout attack = new RelativeLayout(this);
                final Move current = moves.get(i);


                int ppExtraMargin=0;
                switch(i){
                    case 0:
                        lp1.leftMargin = 3;
                        lp1.topMargin = 75;
//                        ppExtraMargin = -5;
                        break;
                    case 1:
                        lp1.leftMargin = 510;
                        lp1.topMargin = 75;
//                        ppExtraMargin = 12;
                        break;
                    case 2:
                        lp1.leftMargin = 3;
                        lp1.topMargin = 290;
//                        ppExtraMargin = 5;
                        break;
                    case 3:
                        lp1.leftMargin = 510;
                        lp1.topMargin = 290;
//                        ppExtraMargin = 0;
                        break;
                }



                switch (current.getType()){
                    case "1":
                        attack.setBackgroundResource(R.drawable.normal_attack);
                        break;
                    case "2":
                        attack.setBackgroundResource(R.drawable.fight_attack);
                        break;
                    case "3":
                        attack.setBackgroundResource(R.drawable.flying_attack);
                        break;
                    case "4":
                        attack.setBackgroundResource(R.drawable.poison_attack);
                        break;
                    case "5":
                        attack.setBackgroundResource(R.drawable.ground_attack);
                        break;
                    case "6":
                        attack.setBackgroundResource(R.drawable.rock_attack);
                        break;
                    case "7":
                        attack.setBackgroundResource(R.drawable.bug_attack);
                        break;
                    case "8":
                        attack.setBackgroundResource(R.drawable.ghost_attack);
                        break;
                    case "9":
                        attack.setBackgroundResource(R.drawable.steel_attack);
                        break;
                    case "10":
                        attack.setBackgroundResource(R.drawable.fire_attack);
                        break;
                    case "11":
                        attack.setBackgroundResource(R.drawable.water_attack);
                        break;
                    case "12":
                        attack.setBackgroundResource(R.drawable.grass_attack);
                        break;
                    case "13":
                        attack.setBackgroundResource(R.drawable.electric_attack);
                        break;
                    case "14":
                        attack.setBackgroundResource(R.drawable.psychic_attack);
                        break;
                    case "15":
                        attack.setBackgroundResource(R.drawable.ice_attack);
                        break;
                    case "16":
                        attack.setBackgroundResource(R.drawable.dragon_attack);
                        break;
                    case "17":
                        attack.setBackgroundResource(R.drawable.dark_attack);
                        break;
                    case "10001":
                        attack.setBackgroundResource(R.drawable.unknown_attack);
                        break;
                }

                attack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startAttackSystem(current);

                    }
                });
                TextView moveName = new TextView(this);
                moveName.setText(current.getName());
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp2.leftMargin = 50;
                lp2.topMargin = 50;
                attack.addView(moveName, lp2);

                TextView currentPPTxt = new TextView(this);
                currentPPTxt.setText(current.getCurrentPp()+" / " + current.getPp());
                RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp3.topMargin = 100;
                lp3.leftMargin = 300;
                attack.addView(currentPPTxt, lp3);
//                //attack.animate();
                fightButtonContainer.addView(attack, lp1);
            }
        }

        Button cancelButton = new Button(this);
        RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp5.topMargin = 550;
        lp5.leftMargin = 45;
        lp5.width = 900;
        lp5.height = 145;
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActions();
            }
        });
        cancelButton.setBackgroundResource(R.drawable.cancel_button);
        fightButtonContainer.addView(cancelButton, lp5);

    }


    private void startAttackSystem(final Move currentMove){
        fightButtonContainer.removeAllViewsInLayout();
        ArrayList<Move> moves = enemyPokemon.getLearnedMoves();
        int randomMove = r.nextInt(moves.size());
        Move selectedMove = moves.get(randomMove);

        fightButtonContainer.setVisibility(View.INVISIBLE);
        myAnimationFinished = false;
        enemyAnimationFinished = false;


        // instead of doing the attacks here, start the animations, which when they will end come back here and do the real damage
        if (currentMove.getPriority() > selectedMove.getPriority()) {       // first check the priority of the moves
            myDamage = DamageDealing.doDamage(currentMove, myCurrentPokemon, enemyPokemon);
            surfaceView.getmThread().getFightRenderable().youAttacked(currentMove,myCurrentPokemon,enemyPokemon,criticalHit);
            enemyDamage = DamageDealing.doDamage(selectedMove, enemyPokemon, myCurrentPokemon);
            if(enemyHP.getProgress() - myDamage >0)
                surfaceView.getmThread().getFightRenderable().enemyAttacked(selectedMove, enemyPokemon, myCurrentPokemon, criticalHit);

        }
        else {
            if (currentMove.getPriority() < selectedMove.getPriority()) {
                enemyDamage = DamageDealing.doDamage(selectedMove, enemyPokemon, myCurrentPokemon);
                surfaceView.getmThread().getFightRenderable().enemyAttacked(selectedMove, enemyPokemon, myCurrentPokemon, criticalHit);
                myDamage = DamageDealing.doDamage(currentMove, myCurrentPokemon, enemyPokemon);
                if(myHP.getProgress() - enemyDamage >0)
                    surfaceView.getmThread().getFightRenderable().youAttacked(currentMove, myCurrentPokemon,enemyPokemon,criticalHit);
            }
            else {
                if (myCurrentPokemon.getStats().getSpeed() >= enemyPokemon.getStats().getSpeed()) {
                    myDamage = DamageDealing.doDamage(currentMove, myCurrentPokemon, enemyPokemon);
                    surfaceView.getmThread().getFightRenderable().youAttacked(currentMove, myCurrentPokemon,enemyPokemon,criticalHit);
                    enemyDamage = DamageDealing.doDamage(selectedMove, enemyPokemon, myCurrentPokemon);
                    if(enemyHP.getProgress() - myDamage >0)
                        surfaceView.getmThread().getFightRenderable().enemyAttacked(selectedMove, enemyPokemon, myCurrentPokemon, criticalHit);

                }
                else {
                    enemyDamage = DamageDealing.doDamage(selectedMove, enemyPokemon, myCurrentPokemon);
                    surfaceView.getmThread().getFightRenderable().enemyAttacked(selectedMove, enemyPokemon, myCurrentPokemon, criticalHit);
                    myDamage = DamageDealing.doDamage(currentMove, myCurrentPokemon, enemyPokemon);
                    if(myHP.getProgress() - enemyDamage >0)
                        surfaceView.getmThread().getFightRenderable().youAttacked(currentMove,myCurrentPokemon,enemyPokemon,criticalHit);
                }
            }
        }
    }

    private void youAttack(Move myMove){

        changeHPProgressBar(enemyHP.getProgress() - myDamage, enemyHP, false);
        myMove.setCurrentPp(myMove.getCurrentPp() - 1);
        enemyPokemon.setCurrentHP(enemyHP.getProgress());

        if(enemyHP.getProgress()<=0) {
            int gainedExperience = myCurrentPokemon.addExperience(enemyPokemon, isTrainer, false, false, false, false);
            surfaceView.getmThread().getFightRenderable().youWon(gainedExperience);
            enemyIsAlive = false;
        }

    }

    private void enemyAttacks(Move selectedMove) {
        changeHPProgressBar(myHP.getProgress() - enemyDamage, myHP,false);
        lblMyCurrentHP.setText(myHP.getProgress() + " / " + myHP.getMax());
        myCurrentPokemon.setCurrentHP(myHP.getProgress());

        if(myHP.getProgress()<=0){
            surfaceView.getmThread().getFightRenderable().youLost();
            youAreAlive = false;
        }
    }







    private void bagMenu(){
        fightButtonContainer.setBackgroundResource(R.drawable.bag_overview);
        fightButtonContainer.removeAllViewsInLayout();

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.leftMargin = 15;
        lp2.topMargin = 50;
        lp2.height = 235;
        lp2.width = 470;
        Button restoresBtn= new Button(this);
        restoresBtn.setBackgroundResource(R.drawable.bag_restores_1);
        restoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRestoresMenu();

            }
        });
        fightButtonContainer.addView(restoresBtn, lp2);

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp3.leftMargin = 515;
        lp3.topMargin = 50;
        lp3.height = 235;
        lp3.width = 460;
        Button pokeballsBtn= new Button(this);
        pokeballsBtn.setBackgroundResource(R.drawable.bag_pokeballs_1);
        pokeballsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPokeballsMenu();

            }
        });
        fightButtonContainer.addView(pokeballsBtn, lp3);

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp4.leftMargin = 15;
        lp4.topMargin = 320;
        lp4.height = 230;
        lp4.width = 465;
        Button statushealersBtn= new Button(this);
        statushealersBtn.setBackgroundResource(R.drawable.bag_statushealers_1);
        statushealersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStatusHealersMenu();

            }
        });
        fightButtonContainer.addView(statushealersBtn, lp4);

        RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp5.leftMargin = 515;
        lp5.topMargin = 320;
        lp5.height = 230;
        lp5.width = 460;
        Button battleItemsBtn= new Button(this);
        battleItemsBtn.setBackgroundResource(R.drawable.bag_battleitems_1);
        battleItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBattleItemsMenu();

            }
        });
        fightButtonContainer.addView(battleItemsBtn, lp5);

        RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp6.leftMargin = 850;
        lp6.topMargin = 570;
        lp6.height = 130;
        lp6.width = 130;
        Button backBtn= new Button(this);
        backBtn.setBackgroundResource(R.drawable.bag_return_1);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActions();
            }
        });
        fightButtonContainer.addView(backBtn,lp6);

    }

    private void openRestoresMenu(){
        Log.d("SUBMENU","Clicked on the restore button");
    }

    private void openPokeballsMenu(){

        fightButtonContainer.setBackgroundResource(R.drawable.bag_submenus);
        fightButtonContainer.removeAllViewsInLayout();

        int counter = 0;
        int x = 0;
        int y =0;
        for (final Map.Entry<CustomItem,Integer> entry : Utils.myItems.getMyItems().entrySet())
        {
            if(entry.getValue()>0) {
                final String itemName = entry.getKey().getName();
                int amount = entry.getValue();
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp2.leftMargin = 25 + x;    // add margin for the next item
                lp2.topMargin = 50 + y;     // add margin for the next item
                lp2.height = 140;
                lp2.width = 455;
                RelativeLayout ballsCell = new RelativeLayout(this);
                ballsCell.setBackgroundResource(R.drawable.bag_item_cell);
                setupItemCell(ballsCell, itemName, amount);
                ballsCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.myItems.setAmountByName(itemName, -1);
                        fightButtonContainer.setVisibility(View.INVISIBLE);
                        surfaceView.getmThread().getFightRenderable().throwPokeballToCatch(entry.getKey(), enemyPokemon);
                        ArrayList<Move> moves = enemyPokemon.getLearnedMoves();
                        int randomMove = r.nextInt(moves.size());
                        Move selectedMove = moves.get(randomMove);
                        enemyDamage = DamageDealing.doDamage(selectedMove, enemyPokemon, myCurrentPokemon);
                        surfaceView.getmThread().getFightRenderable().enemyAttacked(selectedMove, enemyPokemon, myCurrentPokemon, criticalHit);
                    }
                });
                fightButtonContainer.addView(ballsCell, lp2);
                counter++;
                if(counter%2==1) x= 490;
                else  x=0;
                y = counter/2*170;

            }
        }

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp3.leftMargin = 350;
        lp3.topMargin = 570;
        lp3.height = 135;
        lp3.width = 300;
        TextView itemsLabel= new TextView(this);
        itemsLabel.setBackgroundResource(R.drawable.bag_pokeballs_label);
        fightButtonContainer.addView(itemsLabel, lp3);

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp4.leftMargin = 855;
        lp4.topMargin = 580;
        lp4.height = 120;
        lp4.width = 130;
        Button backBtn= new Button(this);
        backBtn.setBackgroundResource(R.drawable.bag_return_1);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bagMenu();
            }
        });
        fightButtonContainer.addView(backBtn, lp4);

    }

    private void openStatusHealersMenu(){
        Log.d("SUBMENU","Clicked on the status healers button");
    }

    private void openBattleItemsMenu(){
        Log.d("SUBMENU","Clicked on the battle items button");
    }

    private void setupItemCell(RelativeLayout cell, String itemName, int amount){
        itemName = itemName.replace("-"," ");
        itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.leftMargin = 120;
        lp2.topMargin = 0;
        TextView itemNameLabel= new TextView(this);
        itemNameLabel.setText(itemName);
        itemNameLabel.setTextColor(Color.WHITE);
        itemNameLabel.setTextSize(18);
        cell.addView(itemNameLabel, lp2);

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp3.leftMargin = 110;
        lp3.topMargin = 55;
        lp3.height=95;
        lp3.width=85;
        TextView itemImage= new TextView(this);
        String id = itemName.replace(" ","_");
        int resID = getResources().getIdentifier(id, "drawable", getPackageName());
        //Utils.enemyPokemonBm = BitmapFactory.decodeResource(view.getResources(), resID);
        //itemImage.setBackgroundResource(getResources(),resID);

        itemImage.setBackgroundResource(R.drawable.poke_ball);
        cell.addView(itemImage, lp3);

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp4.leftMargin = 220;
        lp4.topMargin = 70;
        TextView itemAmountLabel= new TextView(this);
        itemAmountLabel.setText("x"+amount);
        itemAmountLabel.setTextColor(Color.WHITE);
        itemAmountLabel.setTextSize(18);
        cell.addView(itemAmountLabel, lp4);
    }

    private void switchPokemon1(boolean switchedDuringFight){
        Button btnCancel = Utils.pokemonMenu(fightButtonContainer, this);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActions();
            }
        });
        this.switchedDuringFight = switchedDuringFight;

    }
    public static void switchPokemon2(PokemonSprite currentPokemon,RelativeLayout container){
        if(currentPokemon.getCurrentHP()>0) {
            myBars.setVisibility(View.INVISIBLE);
            myCurrentPokemon = currentPokemon;
            myCurrentPokemon.setTempStats(myCurrentPokemon.getStats());
            container.removeAllViewsInLayout();
            container.setVisibility(View.INVISIBLE);
            myAnimationFinished = false;
            Utils.mySprite.getMyPokemons().setFirstPokemon(currentPokemon.getName());
            Utils.menuOpened = false;
            surfaceView.getmThread().getFightRenderable().changePokemon();

            // als geswitcht tijdens het gevecht, laat enemy aanvallen
            if (switchedDuringFight) {
                ArrayList<Move> moves = enemyPokemon.getLearnedMoves();
                int randomMove = r.nextInt(moves.size());
                Move selectedMove = moves.get(randomMove);
                enemyDamage = DamageDealing.doDamage(selectedMove, enemyPokemon, myCurrentPokemon);
                surfaceView.getmThread().getFightRenderable().enemyAttacked(selectedMove, enemyPokemon, myCurrentPokemon, criticalHit);
            }

            myCurrentPokemon = currentPokemon;
        }


    }

    @Override
    public void fightRenderableLoaded(final FightRenderable fightRenderable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                determineEnemy(fightRenderable);
                loadPokemons();
            }
        });
        fightRenderable.setup();
    }

    public void showActionButtons()
    {
        Log.d("SHOW","showActionButtons");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupMyBars();
                setupEnemyBars();
                startActions();
            }
        });
    }

    public void myAttackAnimationIsDone(final Move myMove){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                youAttack(myMove);
                myAnimationFinished = true;
                if (enemyAnimationFinished) {
                    startActions();
                }

            }
        });
    }

    public void enemyAttackAnimationIsDone(final Move enemyMove){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                enemyAttacks(enemyMove);
                enemyAnimationFinished = true;
                if (myAnimationFinished) {
                    startActions();
                }


            }
        });

    }

    public void finishBattle(int gainedExperience){
        Log.d("BLA","finishBattle "+gainedExperience);
        // also add experience to all the pokemon that fought, not only the one who won (TO DO)
        if(gainedExperience>0) {
            myPokemonWon(gainedExperience);
        }
        else if(gainedExperience==0) {
            myPokemonLost();
        } else {
            Utils.mySprite.getMyPokemons().setFirstPokemon(myPokemonAtStart.getName());
            setResult(RESULT_OK, getIntent().putExtra("FIGHT", "Pokemon caught"));
            finish();
        }

    }

    private void myPokemonWon(int gainedExperience){
        if(myCurrentPokemon.getLevel()==myCurrentPokemon.getNextEvolutionLevel()){
            PokemonSprite evolvedForm = new PokemonSprite(myCurrentPokemon.getNextEvolutionPokemonName(),Utils.ds);
            Log.d("EVOLVE", myCurrentPokemon.getName() + " evolves in a " + myCurrentPokemon.getNextEvolutionPokemonName());
            MyPokemons myPokemons = Utils.mySprite.getMyPokemons();
            myPokemons.evolve(myCurrentPokemon,evolvedForm);
            if(myPokemonAtStart==myCurrentPokemon)
                myPokemonAtStart = evolvedForm;

            myCurrentPokemon = evolvedForm;
        }
        myCurrentPokemon.setCurrentHP(myHP.getProgress());

        int counter = 0;
        if(isTrainer){
            for (Map.Entry<Integer, PokemonSprite> entry : Utils.currentTrainer.getMyPokemons().getMyPokemons().entrySet()) {
                if(entry.getValue().getCurrentHP()>0)
                    counter++;
            }
        }

        if(counter>0) {
            fightRenderable.showContinueFightingMessage("enemy");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fightButtonContainer.setVisibility(View.VISIBLE);
                    enemyBars.setVisibility(View.INVISIBLE);
                    enemyIsAlive = true;
                    loadPokemons();
                    setupMyBars();
                    setupEnemyBars();
                    startActions();
                }
            });
            fightRenderable.enemyChangesPokemon();
        }
        else {
            Utils.mySprite.getMyPokemons().setFirstPokemon(myPokemonAtStart.getName());
            setResult(RESULT_OK, getIntent().putExtra("FIGHT", "Fight won and " + gainedExperience + " XP gained"));
            finish();
        }

    }
    private void myPokemonLost(){
        myCurrentPokemon.setCurrentHP(myHP.getProgress());
        // myPokemon lost, possiblity to chose another pokemon (if there is still one alive
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myBars.setVisibility(View.INVISIBLE);
            }
        });
        int counter = 0;
        for (Map.Entry<Integer, PokemonSprite> entry : Utils.mySprite.getMyPokemons().getMyPokemons().entrySet()) {
            if(entry.getValue().getCurrentHP()>0)
                counter++;
        }
        if(counter>0) {
            Log.d("NEXT","My next pokemon");

            surfaceView.getmThread().getFightRenderable().showContinueFightingMessage("me");
        }
        else {
            Utils.mySprite.getMyPokemons().setFirstPokemon(myPokemonAtStart.getName());
            setResult(RESULT_OK, getIntent().putExtra("FIGHT", "Fight lost"));
            finish();
        }
    }
    public void pokemonCaptured(boolean captured){
        if(captured){
            Utils.mySprite.getMyPokemons().addPokemon(enemyPokemon);
            finishBattle(-1);
        }

        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActions();
                }
            });
        }
    }

    @Override
    public void switchPokemonAfterFainted() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fightButtonContainer.setVisibility(View.VISIBLE);
                youAreAlive = true;
                switchPokemon1(false);
            }
        });
    }

    @Override
    public void enemySwitchesPokemonAfterFainted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fightButtonContainer.setVisibility(View.VISIBLE);
                enemyIsAlive = true;
            }
        });
    }

    @Override
    public void runAway() {
        Utils.mySprite.getMyPokemons().setFirstPokemon(myPokemonAtStart.getName());
        setResult(RESULT_CANCELED, getIntent().putExtra("FIGHT", "Run away"));
        finish();
    }


}


/*
    TO DO:
        2) stat change attacks, make them work
        3) animation
        4) draw pokecenter

*/