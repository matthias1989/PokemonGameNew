package info.androidhive.gametest.pokemons;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by matthias on 3/16/2016.
 */
public class PokemonSprite extends Pokemon implements Serializable  {
    private static final String ATT = "attack";
    private static final String HP = "hitpoints";
    private static final String DEF = "defense";
    private static final String SATT = "special attack";
    private static final String SDEF = "special defense";
    private static final String SPE = "speed";
    private static final String ACCU = "accuracy";
    private static final String EVA = "evasion";

    private String UID;         // ofwel via timestamp, ofwel zelf opbouwen
    private int level=0;
    private int currentExperience;
    private int currentHP;
    private int experienceNeededForNextLevel;
    private int experienceNeededForThisLevel;
    private ArrayList<Move> learnedMoves = new ArrayList<>();
    private PokemonDataSource ds;


    private Map<String,Integer> individualValues = new HashMap<>();
    // 0 - 31 (value) for each stat (key), random number that doesnt change
    private Map<String,Integer> effortValues = new HashMap<>();
    // 0-252 in one stat, max of 510 in total, gains each battle 1-3 (depending on evolution stage)
    private Map<String,Float> nMod = new HashMap<>();
    // one stat has 0.9, one has 1.1. The rest has 1, this also stays the same
    // stat = ((base*2 + IV + (EV/4)) * level / 100 + 5) * Nmod  => round down
    // HP = (Base * 2 + IV + (EV/4)) * level / 100 + 10 + level => round down


    public PokemonSprite(String name, PokemonDataSource ds) {
        super(name);
        this.ds = ds;
        setId(ds.getPokemonByName(getName()).getId());
        setnMod();
        setEffortValues();      // init
        setIndividualValues();
        setType(ds.getPokemonByName(getName()).getType());
        setCaptureRate(ds.getPokemonByName(getName()).getCaptureRate());

    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getLevel(){
        return level;
    }
    public void setLevel(int level){
        this.level = level;
        List<Move> allMoves = ds.getPokemonByName(getName()).getMoves();
        for(Move allMove : allMoves){
            if(allMove.getLearnedLevel()<= level && allMove.getLearnedLevel()>0){
                addLearnedMove(allMove);            // map with order
            }
        }
        Map<Integer,Integer> experienceTable = ds.getPokemonByName(getName()).getExperienceTable();
        int min = experienceTable.get(level);
        int max = experienceTable.get(level+1);
        setExperienceNeededForNextLevel(max);
        setExperienceNeededForThisLevel(min);


        // stat = ((base*2 + IV + (EV/4)) * level / 100 + 5) * Nmod  => round down
        // HP = (Base * 2 + IV + (EV/4)) * level / 100 + 10 + level => round down
        Stat baseStats = ds.getPokemonByName(getName()).getStats();
        Stat statCurrent = new Stat();
        statCurrent.setHp((baseStats.getHp() * 2 + individualValues.get(HP) + (effortValues.get(HP) / 4)) * level / 100 + 10 + level);
        //Math.round(((baseStats.getAttack() * 2 + individualValues.get(ATT) + (effortValues.get(ATT) / 4)) * level / 100 + 5) *nMod.get(ATT));
        statCurrent.setAttack(Math.round(((baseStats.getAttack() * 2 + individualValues.get(ATT) + (effortValues.get(ATT) / 4)) * level / 100 + 5) *nMod.get(ATT)));
        statCurrent.setDefense(Math.round(((baseStats.getDefense() * 2 + individualValues.get(DEF) + (effortValues.get(DEF) / 4)) * level / 100 + 5) * nMod.get(DEF)));
        statCurrent.setsAttack(Math.round(((baseStats.getsAttack() * 2 + individualValues.get(SATT) + (effortValues.get(SATT) / 4)) * level / 100 + 5) * nMod.get(SATT)));
        statCurrent.setsDefense(Math.round(((baseStats.getsDefense() * 2 + individualValues.get(SDEF) + (effortValues.get(SDEF) / 4)) * level / 100 + 5) * nMod.get(SDEF)));
        statCurrent.setEvasion(Math.round(((baseStats.getEvasion() * 2 + individualValues.get(EVA) + (effortValues.get(EVA) / 4)) * level / 100 + 5) * nMod.get(EVA)));
        statCurrent.setAccuracy(Math.round(((baseStats.getAccuracy() * 2 + individualValues.get(ACCU) + (effortValues.get(ACCU) / 4)) * level / 100 + 5) * nMod.get(ACCU)));
        statCurrent.setSpeed(Math.round(((baseStats.getSpeed() * 2 + individualValues.get(SPE) + (effortValues.get(SPE) / 4)) * level / 100 + 5) * nMod.get(SPE)));

        setStats(statCurrent);
    }

    public int getCurrentExperience() {
        return currentExperience;
    }

    public void setCurrentExperience(int currentExperience) {
        this.currentExperience = currentExperience;
        Pokemon poke = ds.getPokemonByName(getName());

        int maxLevel = 0;
        Map<Integer,Integer> experienceTable = ds.getPokemonByName(getName()).getExperienceTable();
        Iterator it = experienceTable.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (currentExperience >= (Integer) pair.getValue()) {   // take all levels you can have (the ones you passed and the current one)
                if ((Integer) pair.getKey() > maxLevel) {
                    maxLevel = (Integer) pair.getKey();             // take only the max level
                    setExperienceNeededForNextLevel((Integer) pair.getValue());
                }
            }
        }
        setLevel(maxLevel);

    }

    public int addExperience(PokemonSprite wildPokemon,boolean hasOwner, boolean isTraded, boolean hasLuckyEgg, boolean hasBigAffection, boolean isPassedEvolveStage){
        // if wild pokemon => 1 , if trainer: 1.5
        float a = hasOwner ? 1.5f: 1.0f;
        float t = isTraded ? 1.5f: 1.0f;
        float e = hasLuckyEgg? 1.5f: 1.0f;
        float p = 1.0f;
        // if exp point power is active, p can be less or more: // http://bulbapedia.bulbagarden.net/wiki/Experience
        float f = hasBigAffection ? 1.2f : 1.0f;
        float v = isPassedEvolveStage? 1.2f : 1.0f;
        float s = 1.0f;  // 2.0f for pokemon with xp share

        int b = ds.getPokemonByName(wildPokemon.getName()).getBaseExp();

        int gainedExperience= (int) ((a * t * b * e * wildPokemon.getLevel() * p * f *v)/ (7 * s));
        setCurrentExperience(getCurrentExperience()+gainedExperience);

        Log.d("MAX_EXP",gainedExperience+"");
        return gainedExperience;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public ArrayList<Move> getLearnedMoves() {
        return learnedMoves;
    }

    public void setLearnedMoves(ArrayList<Move> learnedMoves) {
        this.learnedMoves = learnedMoves;
    }

    public void addLearnedMove(Move move){
        boolean allreadyLearned = false;
        for(Move learnedMove : learnedMoves){
            if(learnedMove.getName().equals(move.getName())){
                allreadyLearned = true;
            }
        }
        if(!allreadyLearned)
            this.learnedMoves.add(move);
    }

    public int getExperienceNeededForNextLevel() {
        return experienceNeededForNextLevel;
    }

    public void setExperienceNeededForNextLevel(int experienceNeededForNextLevel) {

        this.experienceNeededForNextLevel = experienceNeededForNextLevel;
    }

    public int getExperienceNeededForThisLevel() {
        return experienceNeededForThisLevel;
    }

    public void setExperienceNeededForThisLevel(int experienceNeededForThisLevel) {
        this.experienceNeededForThisLevel = experienceNeededForThisLevel;
    }

    public PokemonDataSource getDs() {
        return ds;
    }

    public void setDs(PokemonDataSource ds) {
        this.ds = ds;
    }

    public Map<String, Integer> getIndividualValues() {
        return individualValues;
    }

    public void setIndividualValues() {
        //Map<String, Integer> individualValues = new HashMap<>();
        Random random = new Random();
        individualValues.put(ATT,random.nextInt(31) + 1);
        individualValues.put(DEF,random.nextInt(31) + 1);
        individualValues.put(HP,random.nextInt(31) + 1);
        individualValues.put(SATT,random.nextInt(31) + 1);
        individualValues.put(SDEF,random.nextInt(31) + 1);
        individualValues.put(SPE,random.nextInt(31) + 1);
        individualValues.put(ACCU,random.nextInt(31) + 1);
        individualValues.put(EVA,random.nextInt(31) + 1);
        //this.individualValues = individualValues;
    }

    public Map<String, Integer> getEffortValues() {
        return effortValues;
    }

    public void setEffortValues() {
        effortValues.put(ATT,0);
        effortValues.put(DEF,0);
        effortValues.put(HP,0);
        effortValues.put(SATT,0);
        effortValues.put(SDEF,0);
        effortValues.put(SPE,0);
        effortValues.put(EVA,0);
        effortValues.put(ACCU,0);
    }

    public void editEffortValue(String key, Integer value){
        effortValues.put(key,effortValues.get(key)+value);      // take the current value of this key and add it with 1 to 3
    }

    public Map<String, Float> getnMod() {
        return nMod;
    }

    public void setnMod() {
        Random random = new Random();
        int pos = random.nextInt(7);
        int neg = random.nextInt(7);
        if(pos==neg){
            if(neg <7) neg++;
            else neg--;
        }
        for(int i=0;i<7;i++){
            if(i==pos)
                helpnMod(i,1.1f);
            else if(i==neg)
                helpnMod(i,0.9f);
            else
                helpnMod(i,1);
        }
    }

    private void helpnMod(int number, float value){
        switch (number){
            case 0:
                nMod.put(ATT,value);
                break;
            case 1:
                nMod.put(SATT,value);
                break;
            case 2:
                nMod.put(SDEF,value);
                break;
            case 3:
                nMod.put(DEF,value);
                break;
            case 4:
                nMod.put(SPE,value);
                break;
            case 5:
                nMod.put(ACCU,value);
                break;
            case 6:
                nMod.put(EVA,value);
                break;

        }
    }
}
