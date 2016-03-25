package info.androidhive.gametest.pokemons;

/**
 * Created by matthias on 3/18/2016.
 */
public class TypeEfficacy {
    private int damageTypeId;
    private int targetTypeId;
    private int damageFactor;

    public TypeEfficacy(int damageTypeId, int targetTypeId, int damageFactor) {
        this.damageTypeId = damageTypeId;
        this.targetTypeId = targetTypeId;
        this.damageFactor = damageFactor;
    }

    public int getDamageTypeId() {
        return damageTypeId;
    }

    public void setDamageTypeId(int damageTypeId) {
        this.damageTypeId = damageTypeId;
    }

    public int getTargetTypeId() {
        return targetTypeId;
    }

    public void setTargetTypeId(int targetTypeId) {
        this.targetTypeId = targetTypeId;
    }

    public int getDamageFactor() {
        return damageFactor;
    }

    public void setDamageFactor(int damageFactor) {
        this.damageFactor = damageFactor;
    }
}
