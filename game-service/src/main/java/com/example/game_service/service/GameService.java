package com.example.game_service.service;

import com.example.game_service.commons.entities.GameModel;

public interface GameService {
    GameModel createGame(String userId, GameModel gameRequest);
    GameModel getGame(String userId, Long gameId);
    GameModel updateGame(String userId, Long gameId, GameModel gameRequest);
    void deleteGame(String userId, Long gameId);
}
