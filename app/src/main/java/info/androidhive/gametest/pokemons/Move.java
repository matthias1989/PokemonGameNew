package info.androidhive.gametest.pokemons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthias on 3/15/2016.
 */
public class Move {
    private String name;
    private int power;
    private int pp;
    private int currentPp;
    private String type;            // IDs
    private int learnedLevel;
    private int accuracy;
    private int priority;
    private float criticalChance;

//    private int genId;
    private int targetId;
    private int damageClassId;
    private int effectId;
    private int effectChance;
    private int id;




    private Effect effect;


    public Move(int id,String name, int power, int pp, int learnedLevel, int accuracy, int priority, int effectChance,String typeId) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.pp = pp;
        this.learnedLevel = learnedLevel;
        this.accuracy = accuracy;
        this.priority = priority;
        this.effectChance = effectChance;
        this.type = typeId;
        setCurrentPp(pp);
        setCriticalChance();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;

    }

    public int getCurrentPp() {
        return currentPp;
    }

    public void setCurrentPp(int currentPp) {
        this.currentPp = currentPp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLearnedLevel() {
        return learnedLevel;
    }

    public void setLearnedLevel(int learnedLevel) {
        this.learnedLevel = learnedLevel;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public float getCriticalChance() {
        return criticalChance;
    }

    public void setCriticalChance() {
        List<String> stage2 = new ArrayList<>();
        stage2.add("aeroblast");
        stage2.add("air-cutter");
        stage2.add("attack-order");
        stage2.add("crabhammer");
        stage2.add("cross-chop");
        stage2.add("cross-poison");
        stage2.add("drill-run");
        stage2.add("leaf-blade");
        stage2.add("night-slash");
        stage2.add("psycho-cut");
        stage2.add("razor-leaf");
        stage2.add("shadow-claw");
        stage2.add("slash");
        stage2.add("spacial-rend");
        stage2.add("stone-edge");

        criticalChance = 6.25f;
        for(String attack : stage2){
            if(attack.equals(getName())){
                criticalChance = 12.5f;
            }
        }
    }

    public int getDamageClassId() {
        return damageClassId;
    }

    public void setDamageClassId(int damageClassId) {
        this.damageClassId = damageClassId;
    }

    public int getEffectId() {
        return effectId;
    }

    public void setEffectId(int effectId) {
        this.effectId = effectId;
    }

    public int getEffectChance() {
        return effectChance;
    }

    public void setEffectChance(int effectChance) {
        this.effectChance = effectChance;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
