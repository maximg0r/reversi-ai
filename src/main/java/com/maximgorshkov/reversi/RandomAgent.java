package com.maximgorshkov.reversi;

import java.util.ArrayList;
import java.util.Random;

import javax.persistence.Entity;

@Entity
public class RandomAgent extends IAgent {
    Random rand;
    long durationMS=0; // total cumulative duration of this agent in ms throughout the game

    public RandomAgent() {
        rand = new Random();
    }

    @Override
    public long getDuration() {
        return durationMS;
    }

    @Override
    public Action makeAMove(State currentState) {
        System.out.println("I’m picking a move randomly...");
        long startTime = System.nanoTime();

        ArrayList<Action> actions = currentState.getPossibleActions();
        if (!actions.isEmpty()) {
            //Action nextAction = actions.get(0);
            //currentState = State.applyAction(currentState, nextAction);
            Action nextAction = actions.get(rand.nextInt(actions.size()));
            long endTime = System.nanoTime();
            long duration = (endTime-startTime)/1000000;
            durationMS =  durationMS + duration;

            return nextAction;
        } else {
            long endTime = System.nanoTime();
            long duration = (endTime-startTime)/1000000;
            durationMS =  durationMS + duration;

            //currentState.setCurrPlayer(currentState.getCurrPlayer() == 1 ? 2 : 1);
            return null;
        }
    }
}