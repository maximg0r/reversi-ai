package com.maximgorshkov.reversi;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Random;

import javax.persistence.Entity;

@Entity
public class AB_H_MinimaxAgent extends IAgent
{
    Random                 rand;
    int                    depth_limit;
    long durationMS=0; // total cumulative duration of this agent in ms throughout the game

    public AB_H_MinimaxAgent(int depth_limit) {
        rand = new Random();
        this.depth_limit = depth_limit;

        /*System.out.println("What is the fixed depth limit you want to set?");
        boolean bInputAccepted = false;
        while(bInputAccepted==false)
        {
            try{
                fixed_depth_limit = input.nextInt();
                bInputAccepted = true;
            }catch (Exception e)
            {
                System.out.println("Error with accepting input. Message: "+e.getMessage());

            }
        }*/
    }

    public AB_H_MinimaxAgent() {
        rand = new Random();
        this.depth_limit = 5;
    }
    
    
    public Action makeAMove(State currentState)
    {
        // return minimax(currentState, currentState.getCurrPlayer());
        long startTime = System.nanoTime();
        Action nextAction = H_minimax_decision(currentState);
        long endTime = System.nanoTime();
        long duration = (endTime-startTime)/1000000;
        durationMS =  durationMS + duration;

        return nextAction;
        
    }

    @Override
    public long getDuration() {
        return durationMS;
    }
    
    private Action H_minimax_decision(State currentState)
    {
        int player_color = currentState.getCurrPlayer();
        TreeMap<State, Action> actionsMap = currentState.getPossibleActionsMap(player_color);
        // List<Action> possibleActions = currentState.getPossibleActions();
        int          max             = Integer.MIN_VALUE;
        Action       maxAction       = null;
        if (!actionsMap.isEmpty()) {
            maxAction = (Action) actionsMap.values().toArray()[rand.nextInt(actionsMap.size())];
            // maxAction  = possibleActions.get(rand.nextInt(possibleActions.size()));
        }

        // implement something like a priority queue to consider best actions first
        /*
        for (Action action : possibleActions)
        {
            int result = min_value(State.applyAction(currentState, action), Integer.MIN_VALUE, Integer.MAX_VALUE, player_color, 1);
            if (result > max)
            {
                max       = result;
                maxAction = action;
            }
        }
        */

        for (Entry<State, Action> entry : actionsMap.entrySet()) {
            State s = entry.getKey();
            Action a = entry.getValue();
          
            int result = min_value(s, Integer.MIN_VALUE, Integer.MAX_VALUE, player_color, 1);
            if (result > max)
            {
                max       = result;
                maxAction = a;
            }
        }
        return maxAction;
    }
    
    
    private int max_value(State currentState, int alpha, int beta, int player_color, int current_depth)
    {
       

        if (currentState.isTerminal())
        {

            /*
                Explanation [Sid]:
                The reason that we are using this switch statement is because of how we are currently assigning value to different states.
                For situations where it is not a terminal state, the heurisic function returns a value such that v is a member of any Real number.
                However, for the compute utility, we only return values such that |v| <= 1
                Now since the objective is to find the shortest path to a winning terminal state, we want to ensure that we overvalue the nearest terminal states
                so that when compared in value to non-terminal states, we can prioritise a decision about the terminal state (i.e. if it is a positive terminal, then we want to head down that path
                because within this fixed limit on depth, we have already reached a winning terminal state).

                In the scenario that within set depth we recover more than one terminal state, then we want to ensure that we overvalue closer states as compared to states closer to the fixed depth limit.
                To do this, we subtract the current_depth from the Integer MAX OR add the current to Integer MIN.
                In the case where it is winning and so Integer Max, subtracting the depth will make nearer victories have a higher value
                In the case where it is losing and so Integer Min, adding that current depth, makes the value larger if it was further away (making it better because you delay the loss)
            */

            int returnedValue = 0;
            switch(currentState.computeUtility(player_color))
            {
                case 1:
                    returnedValue = Integer.MAX_VALUE - current_depth;
                    break;
                case -1:
                    returnedValue = Integer.MIN_VALUE + current_depth;
                case 0:
                    returnedValue = 0;
            }
            return returnedValue;
        } else if (cutoff_test(current_depth))
        {
            return evaluate_heuristic(currentState, player_color);
        }
        
        int v = Integer.MIN_VALUE;
        for (Entry<State, Action> entry : currentState.getPossibleActionsMap(player_color).entrySet()) {
            State s = entry.getKey();
            // Action a = entry.getValue();
          
            int result = min_value(s, alpha, beta, player_color, ++current_depth);
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
          
        /*
        for (Action action : currentState.getPossibleActions())
        {
            int result = min_value(State.applyAction(currentState, action), alpha, beta, player_color, ++current_depth);
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
        */
        
        return v;
    }
    
    
    private int min_value(State currentState, int alpha, int beta, int player_color, int current_depth)
    {
        if (currentState.isTerminal())
        {

            /*
                Explanation [Sid]:
                The reason that we are using this switch statement is because of how we are currently assigning value to different states.
                For situations where it is not a terminal state, the heurisic function returns a value such that v is a member of any Real number.
                However, for the compute utility, we only return values such that |v| <= 1
                Now since the objective is to find the shortest path to a winning terminal state, we want to ensure that we overvalue the nearest terminal states
                so that when compared in value to non-terminal states, we can prioritise a decision about the terminal state (i.e. if it is a positive terminal, then we want to head down that path
                because within this fixed limit on depth, we have already reached a winning terminal state).

                In the scenario that within set depth we recover more than one terminal state, then we want to ensure that we overvalue closer states as compared to states closer to the fixed depth limit.
                To do this, we subtract the current_depth from the Integer MAX OR add the current to Integer MIN.
                In the case where it is winning and so Integer Max, subtracting the depth will make nearer victories have a higher value
                In the case where it is losing and so Integer Min, adding that current depth, makes the value larger if it was further away (making it better because you delay the loss)

            */
            int returnedValue = 0;
            switch(currentState.computeUtility(player_color))
            {
                case 1:
                    returnedValue = Integer.MAX_VALUE - current_depth;
                    break;
                case -1:
                    returnedValue = Integer.MIN_VALUE + current_depth;
                case 0:
                    returnedValue = 0;
            }
            return returnedValue;
        } else if (cutoff_test(current_depth))
        {
            return evaluate_heuristic(currentState, player_color);
        }

        int v = Integer.MAX_VALUE;
        for (Entry<State, Action> entry : currentState.getPossibleActionsMap(player_color).entrySet()) {
            State s = entry.getKey();
            // Action a = entry.getValue();
          
            int result = max_value(s, alpha, beta, player_color, ++current_depth);
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

        /*
        for (Action action : currentState.getPossibleActions())
        {
            int result = max_value(State.applyAction(currentState, action), alpha, beta, player_color, ++current_depth);
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
        */

        return v;
    }
    
    
    static int evaluate_heuristic(State state, int currentPlayer)
    {
        if (currentPlayer == 1) {
            return state.getNumberOfBlackTiles() - state.getNumberOfWhiteTiles();
        } else {
            return state.getNumberOfWhiteTiles() - state.getNumberOfBlackTiles();
        }
    }


    boolean cutoff_test(int current_depth) {
        return current_depth > depth_limit;
    }

}
