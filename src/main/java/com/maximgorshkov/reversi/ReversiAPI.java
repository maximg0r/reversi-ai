package com.maximgorshkov.reversi;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class ReversiAPI {

    Random rand;
    GameSaveRepository repository;

    public ReversiAPI(GameSaveRepository repository) {
        rand = new Random();
        this.repository = repository;
    }

    public OnlineActionResponse makeMove(OnlineActionRequest request) {
        Long id = request.getGameId();
        Optional<GameSave> save_optional = repository.findById(id);
        GameSave save = save_optional.get();
        // .orElseThrow(() -> new GameSaveNotFoundException(id));

        State currentState = save.getCurrentState();
        IAgent computerAgent = save.getComputerAgent();

        System.out.println(computerAgent.getClass());

        // player's move

        Action playerAction = new Action(request.getRow(), request.getCol(), currentState.getCurrPlayer());
        System.out.println("playerAction: " + playerAction);

        ArrayList<Action> possibleActions = currentState.getPossibleActions();

        if (possibleActions.isEmpty()) { // pass move
            if (!(playerAction.row == -1 && playerAction.col == -1)) { // invalid move
                return new OnlineActionResponse(id, currentState.getBoard(), currentState.getCurrPlayer(),
                        currentState.getNumberOfBlackTiles(), currentState.getNumberOfWhiteTiles(), false, false, 0);
            } else {
                currentState.setCurrPlayer(currentState.getCurrPlayer() == 1 ? 2 : 1);
            }
        } else { // regular move
            if (!possibleActions.contains(playerAction)) { // invalid move
                return new OnlineActionResponse(id, currentState.getBoard(), currentState.getCurrPlayer(),
                        currentState.getNumberOfBlackTiles(), currentState.getNumberOfWhiteTiles(), false, false, 0);
            } else { // apply player's move
                    Action currentAction = possibleActions.get(possibleActions.indexOf(playerAction));
                    currentState = State.applyAction(currentState, currentAction);
            }
        }

        // check if player wins
        if (currentState.isTerminal()) {
            int util = currentState.computeUtility(save.getPlayerColor());

            save.setCurrentState(currentState);
            repository.saveAndFlush(save); // TODO: delete game from DB

            return new OnlineActionResponse(id, currentState.getBoard(), currentState.getCurrPlayer(),
                    currentState.getNumberOfBlackTiles(), currentState.getNumberOfWhiteTiles(), true, true, util);
        }

        // computer's move

        Action computerAction = computerAgent.makeAMove(currentState);
        System.out.println("computerAction: " + computerAction);

        if (computerAction == null) {
            currentState.setCurrPlayer(currentState.getCurrPlayer() == 1 ? 2 : 1);
        } else {
            currentState = State.applyAction(currentState, computerAction);
        }

        // check if computer wins
        if (currentState.isTerminal()) {
            int util = currentState.computeUtility(save.getPlayerColor());

            save.setCurrentState(currentState);
            repository.saveAndFlush(save); // TODO: delete game from DB

            return new OnlineActionResponse(id, currentState.getBoard(), currentState.getCurrPlayer(),
                    currentState.getNumberOfBlackTiles(), currentState.getNumberOfWhiteTiles(), true, true, util);
        }

        // print current state

        // save game to DB
        save.setCurrentState(currentState);
        repository.saveAndFlush(save);
        /*
        repository.findById(id).map(gameSave -> {
            gameSave.setCurrentState(currentState);
            return repository.save(gameSave);
        });
         */
        /*
        .orElseGet(() -> {
            gameSave.setId(id);
            return repository.save(gameSave);
        });
         */

        return new OnlineActionResponse(id, currentState.getBoard(), currentState.getCurrPlayer(),
                currentState.getNumberOfBlackTiles(), currentState.getNumberOfWhiteTiles(), true, false, 0);

    }

    public OnlineActionResponse createGame(NewGameInfo request) {
        State state = new State(request.getBoardSize());
        IAgent agent = null;
        int player_color = request.getPlayerColor();

        switch (request.getAgentType()) {
            case "random":
                agent = new RandomAgent();
                break;

            case "minimax":
                agent = new MinimaxAgent();
                break;

            case "ab_minimax":
                agent = new AB_MinimaxAgent();
                break;

            case "ab_h_minimax":
                agent = new AB_H_MinimaxAgent(request.getDepthLimit());
                break;
        }

        // make computer's move
        if (state.getCurrPlayer() != player_color) {
            Action computerAction = agent.makeAMove(state);
            if (computerAction == null) {
                state.setCurrPlayer(state.getCurrPlayer() == 1 ? 2 : 1);
            } else {
                state = State.applyAction(state, computerAction);
            }
        }

        GameSave save = new GameSave(state, agent, player_color);

        // save game to DB
        repository.saveAndFlush(save);

        return new OnlineActionResponse(save.getId(), state.getBoard(), state.getCurrPlayer(), state.getNumberOfBlackTiles(), state.getNumberOfWhiteTiles(), true, false, 0);
    }

}