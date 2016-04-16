package info.androidhive.gametest.fight;

import android.util.Log;

import java.util.List;
import java.util.Random;

import info.androidhive.gametest.Utils;
import info.androidhive.gametest.pokemons.Move;
import info.androidhive.gametest.pokemons.PokemonSprite;
import info.androidhive.gametest.pokemons.TypeEfficacy;

/**
 * Created by matthias on 4/16/2016.
 */
public class DamageDealing {

    private static Random r = new Random();

    public static int doDamage(Move move, PokemonSprite attacker, PokemonSprite target){
        //Log.d("DAMAGECLASSID",move.getName() + ": "+ move.getDamageClassId()+"");

        if(move.getDamageClassId()==1)
            doStatusChange(move,attacker,target);

        else if(move.getDamageClassId()==2)
            doPhysicalDamage(move,attacker,target);

        else
            doSpecialDamage(move,attacker,target);

        float damageFactor = calculateDamageFactor(move.getType(), target.getType());
        int myDamage = (int) ((move.getPower() / 100.0) * attacker.getStats().getAttack() * damageFactor);
        float critChance = move.getCriticalChance();
        int randomNumber = r.nextInt(10001);
        if(randomNumber<=critChance*100){
            myDamage *= 2;
            FightActivity.criticalHit = true;
        }
        else
            FightActivity.criticalHit = false;

        return myDamage;
    }

    public static float calculateDamageFactor(String damageTypeId,List<String> targetTypeIds){
        List<TypeEfficacy> typeEfficacies = Utils.ds.getTypeEfficacies();
        int damageFactor = 100;
        int previousDamageFactor=-1;
        for(TypeEfficacy typeEfficacy : typeEfficacies){
            for(String targetTypeId : targetTypeIds) {
                if (typeEfficacy.getDamageTypeId() == Integer.parseInt(damageTypeId) && typeEfficacy.getTargetTypeId() == Integer.parseInt(targetTypeId)){
                    if(previousDamageFactor==-1 || previousDamageFactor==100){
                        damageFactor = typeEfficacy.getDamageFactor();
                        previousDamageFactor = damageFactor;
                    }
                }
            }
        }
        return damageFactor/100.0f;
    }

    public static void doPhysicalDamage(Move move, PokemonSprite attacker, PokemonSprite target){

    }

    private static void doStatusChange(Move move, PokemonSprite attacker, PokemonSprite target){
        Log.d("BEFORE",target.getTempStats().getAttack()+"");
        switch(move.getTargetId()){
            case 7: // user
                doStatusChangeHelp(move, attacker);
                break;
            case 10: // selected pokemon
            case 11: // all-opponents
                doStatusChangeHelp(move, target);
                break;
        }
        Log.d("AFTER",target.getTempStats().getAttack()+"");
        //Log.d("STATUSCHANGE",move.getEffect().getStatId()+","+move.getEffect().getStatChange()+","+move.getEffectChance() +","+move.getTargetId());

    }

    private static void doStatusChangeHelp(Move move, PokemonSprite target){

        switch (move.getEffect().getStatId()){
            case 1:
                target.getTempStats().setHp(target.getTempStats().getHp() + move.getEffect().getStatChange());
                break;
            case 2:
                target.getTempStats().setAttack(target.getTempStats().getAttack() + move.getEffect().getStatChange());
                break;
            case 3:
                target.getTempStats().setDefense(target.getTempStats().getDefense() + move.getEffect().getStatChange());
                break;
            case 4:
                target.getTempStats().setsAttack(target.getTempStats().getsAttack() + move.getEffect().getStatChange());
                break;
            case 5:
                target.getTempStats().setsDefense(target.getTempStats().getsDefense() + move.getEffect().getStatChange());
                break;
            case 6:
                target.getTempStats().setSpeed(target.getTempStats().getSpeed() + move.getEffect().getStatChange());
                break;
            case 7:
                target.getTempStats().setAccuracy(target.getTempStats().getAccuracy() + move.getEffect().getStatChange());
                break;
            case 8:
                target.getTempStats().setEvasion(target.getTempStats().getEvasion() + move.getEffect().getStatChange());

        }
    }

    private static void doStatusChangeOpponent(Move move,PokemonSprite opponent){

    }

    private static void doSpecialDamage(Move move, PokemonSprite attacker, PokemonSprite target){

    }
}
