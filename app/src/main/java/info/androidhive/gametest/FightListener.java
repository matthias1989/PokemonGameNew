package info.androidhive.gametest;

import info.androidhive.gametest.fight.FightRenderable;
import info.androidhive.gametest.pokemons.Move;

/**
 * Created by matthias on 3/24/2016.
 */
public interface FightListener {
    void fightRenderableLoaded(FightRenderable fightRenderable);
    void showActionButtons();
    void myAttackAnimationIsDone(Move myMove);
    void enemyAttackAnimationIsDone(Move wildMove);
    void finishBattle(int gainedExperience);
    void pokemonCaptured(boolean captured);
    void switchPokemonAfterFainted();
}
