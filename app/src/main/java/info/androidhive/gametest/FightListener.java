package info.androidhive.gametest;

import info.androidhive.gametest.pokemons.Move;

/**
 * Created by matthias on 3/24/2016.
 */
public interface FightListener {
    void showActionButtons();
    void myAttackAnimationIsDone(Move myMove);
    void wildAttackAnimationIsDone(Move wildMove);
    void finishBattle(int gainedExperience);
    void pokemonCaptured();
}
