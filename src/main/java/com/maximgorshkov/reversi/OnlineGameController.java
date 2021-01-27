package com.maximgorshkov.reversi;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class OnlineGameController {
    private final GameSaveRepository repository;
    ReversiAPI api;

    OnlineGameController(GameSaveRepository repository) {
        this.repository = repository;
        api = new ReversiAPI(repository);
    }

    @PostMapping("/new-game")
    OnlineActionResponse newGame(@RequestBody NewGameInfo info) {
        return api.createGame(info);
    }

    @PostMapping("/make-move")
    OnlineActionResponse makeMove(@RequestBody OnlineActionRequest request) {
        return api.makeMove(request);
    }

    @GetMapping("/get-saves")
    List<GameSave> all() {
        return repository.findAll();
    }
}