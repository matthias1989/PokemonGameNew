package info.androidhive.gametest.pokemons;

/**
 * Created by matthias on 3/15/2016.
 */
public class Stat {
    private int hp;
    private int attack;
    private int defense;
    private int sAttack;
    private int sDefense;
    private int speed;
    private int accuracy;
    private int evasion;

    public Stat(){

    }
    public Stat(int hp, int attack, int defense, int sAttack, int sDefense, int speed, int accuracy, int evasion) {
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.sAttack = sAttack;
        this.sDefense = sDefense;
        this.speed = speed;
        this.accuracy = accuracy;
        this.evasion = evasion;
    }


    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getsAttack() {
        return sAttack;
    }

    public void setsAttack(int sAttack) {
        this.sAttack = sAttack;
    }

    public int getsDefense() {
        return sDefense;
    }

    public void setsDefense(int sDefense) {
        this.sDefense = sDefense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getEvasion() {
        return evasion;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }
}
