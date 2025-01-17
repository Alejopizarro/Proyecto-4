package com.example.game_service.controller.impl;

import com.example.game_service.commons.entities.GameModel;
import com.example.game_service.controller.GameApi;
import com.example.game_service.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController implements GameApi {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public ResponseEntity<GameModel> createGame(String userId, GameModel gameRequest) {
        return ResponseEntity.ok(gameService.createGame(userId, gameRequest));
    }

    @Override
    public ResponseEntity<GameModel> getGame(String userId, Long gameId) {
        return ResponseEntity.ok(gameService.getGame(userId, gameId));
    }

    @Override
    public ResponseEntity<GameModel> updateGame(String userId, Long gameId, GameModel gameRequest) {
        return ResponseEntity.ok(gameService.updateGame(userId, gameId, gameRequest));
    }

    @Override
    public ResponseEntity<Void> deleteMapping(String userId, Long gameId) {
        gameService.deleteGame(userId, gameId);
        return ResponseEntity.noContent().build();
    }
}
