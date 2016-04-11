package info.androidhive.gametest;

import info.androidhive.gametest.sprites.TrainerSprite;

/**
 * Created by matthias on 4/7/2016.
 */
public interface InteractionListener {
    void welcomeScreenLoaded(String buildingName);
    void interactionFinished();
    void trainerFightStarted(TrainerSprite currentTrainer);
}
