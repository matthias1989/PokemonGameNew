package info.androidhive.gametest.pokemons;

/**
 * Created by matthias on 4/16/2016.
 */
public class Effect {
    private int moveId;
    private String effectProse;
    private int statId;
    private int statChange;

    public Effect(int moveId, String effectProse) {
        this.effectProse = effectProse;
        this.moveId = moveId;
    }

    public String getEffectProse() {
        return effectProse;
    }

    public void setEffectProse(String effectProse) {
        this.effectProse = effectProse;
    }

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }

    public int getStatChange() {
        return statChange;
    }

    public void setStatChange(int statChange) {
        this.statChange = statChange;
    }

    public int getMoveId() {
        return moveId;
    }

    public void setMoveId(int moveId) {
        this.moveId = moveId;
    }
}
