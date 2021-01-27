package com.maximgorshkov.reversi;

import java.util.List;
import java.util.Random;

import javax.persistence.Entity;

@Entity
public class AB_MinimaxAgent extends IAgent
{
    Random rand;
    long durationMS=0; // total cumulative duration of this agent in ms throughout the game
    
    public AB_MinimaxAgent() {
        rand = new Random();
    }

    @Override
    public long getDuration() {
        return durationMS;
    }
    
    
    public Action makeAMove(State currentState)
    {
        // return minimax(currentState, currentState.getCurrPlayer());
        long startTime = System.nanoTime();
        Action nextAction = minimax_decision(currentState);
        long endTime = System.nanoTime();
        long duration = (endTime-startTime)/1000000;
        durationMS =  durationMS + duration;

        return nextAction;

    }
    
    
    private Action minimax_decision(State currentState)
    {
        int player_color = currentState.getCurrPlayer();
        List<Action> possibleActions = currentState.getPossibleActions();
        int          max             = Integer.MIN_VALUE;
        Action       maxAction       = null;
        if (!possibleActions.isEmpty()) {
            maxAction  = possibleActions.get(rand.nextInt(possibleActions.size()));
        }
        for (Action action : possibleActions)
        {
            int result = min_value(State.applyAction(currentState, action), Integer.MIN_VALUE, Integer.MAX_VALUE, player_color);
            if (result > max)
            {
                max       = result;
                maxAction = action;
            }
        }
        return maxAction;
    }
    
    
    private int max_value(State currentState, int alpha, int beta, int player_color)
    {
        if (currentState.isTerminal())
        {
            return currentState.computeUtility(player_color);
        }
        
        int v = Integer.MIN_VALUE;
        for (Action action : currentState.getPossibleActions())
        {
            int result = min_value(State.applyAction(currentState, action), alpha, beta, player_color);
            if (result > v)
            {
                v = result;
            }
            
            if (v >= beta) {
                return v;
            }

            if (v > alpha) {
                alpha = v;
            }
        }
        return v;
    }
    
    
    private int min_value(State currentState, int alpha, int beta, int player_color)
    {
        if (currentState.isTerminal())
        {
            return currentState.computeUtility(player_color);
        }
        
        int v = Integer.MAX_VALUE;
        for (Action action : currentState.getPossibleActions())
        {
            int result = max_value(State.applyAction(currentState, action), alpha, beta, player_color);
            if (result < v)
            {
                v = result;
            }
            
            if (v <= alpha) {
                return v;
            }

            if (v < beta) {
                beta = v;
            }
        }
        return v;
    }
    
}
