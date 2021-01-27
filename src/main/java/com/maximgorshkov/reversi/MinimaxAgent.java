package com.maximgorshkov.reversi;

import java.util.List;
import java.util.Random;

import javax.persistence.Entity;

@Entity
public class MinimaxAgent extends IAgent
{
    Random rand;
    long durationMS=0; // total cumulative duration of this agent in ms throughout the game
    
    public MinimaxAgent() {
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
        Action currentMove = minimax_decision(currentState);
        long endTime = System.nanoTime();
        long duration = (endTime-startTime)/1000000;
        durationMS =  durationMS + duration;

        return currentMove;
        
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
            int result = min_value(State.applyAction(currentState, action), player_color);
            if (result > max)
            {
                max       = result;
                maxAction = action;
            }
        }
        return maxAction;
    }
    
    
    private int max_value(State currentState, int player_color)
    {
        if (currentState.isTerminal())
        {
            return currentState.computeUtility(player_color);
        }
        
        int v = Integer.MIN_VALUE;
        for (Action action : currentState.getPossibleActions())
        {
            int result = min_value(State.applyAction(currentState, action), player_color);
            if (result > v)
            {
                v = result;
            }
            
        }
        return v;
    }
    
    
    private int min_value(State currentState, int player_color)
    {
        if (currentState.isTerminal())
        {
            return currentState.computeUtility(player_color);
        }
        
        int v = Integer.MAX_VALUE;
        for (Action action : currentState.getPossibleActions())
        {
            int result = max_value(State.applyAction(currentState, action), player_color);
            if (result < v)
            {
                v = result;
            }
            
        }
        return v;
    }
    
    
    
    /*private State minimax(State currentState, int rootPlayer)
    {
        if (currentState.isTerminal())
        {
            currentState.setUtility(currentState.computeUtility());
            return currentState;
        } else if (currentState.getCurrPlayer() == rootPlayer)
        {
            // get the max as the agent's current player will be 2 always
            List<State> nextPossibleStates = currentState.getPossibleActions().stream().map(action -> State.applyAction(currentState, action)).collect(Collectors.toList());
            State       maxChild           = Collections.max(nextPossibleStates.stream().map(state -> minimax(state, rootPlayer)).collect(Collectors.toList()));
            currentState.setUtility(maxChild.getUtility());
            return currentState;
            
        } else if (currentState.getCurrPlayer() != rootPlayer)
        {
            List<State> nextPossibleStates = currentState.getPossibleActions().stream().map(action -> State.applyAction(currentState, action)).collect(Collectors.toList());
            State       minChild           = Collections.min(nextPossibleStates.stream().map(state -> minimax(state, rootPlayer)).collect(Collectors.toList()));
            currentState.setUtility(minChild.getUtility());
            return currentState;
        }
        return null;
        
        
    }*/
    
}
