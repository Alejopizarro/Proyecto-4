package com.example.game_service.service.impl;

import com.example.game_service.commons.constants.TopicConstants;
import com.example.game_service.commons.entities.GameModel;
import com.example.game_service.repository.GameRepository;
import com.example.game_service.service.GameService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final StreamBridge streamBridge;

    public GameServiceImpl(GameRepository gameRepository, StreamBridge streamBridge) {
        this.gameRepository = gameRepository;
        this.streamBridge = streamBridge;
    }

    @Override
    public GameModel createGame(String userId, GameModel gameRequest) {
        return Optional.of(gameRequest)
                .map(request -> mapToEntity(request, userId))
                .map(gameRepository::save)
                .map(this::sendGameEvent)
                .orElseThrow(()-> new RuntimeException("Error creating game"));
    }

    private GameModel sendGameEvent(GameModel gameModel) {
        Optional.of(gameModel)
                .map(givenGame -> this.streamBridge.send(TopicConstants.GAME_CREATED_TOPIC, gameModel))
                .map(bool -> gameModel);

        return gameModel;
    }

    @Override
    public GameModel getGame(String userId, Long gameId) {
        return gameRepository.findById(gameId)
                .filter(game -> userId.equals(String.valueOf(game.getUserId())))
                .orElseThrow(()-> new RuntimeException("Error couldn't find game"));
    }

    @Override
    public GameModel updateGame(String userId, Long gameId, GameModel gameRequest) {
        return gameRepository.findById(gameId)
                .filter(game -> userId.equals(String.valueOf(game.getUserId())))
                .map(existingGame -> updateEntity(existingGame, gameRequest))
                .map(gameRepository::save)
                .orElseThrow(()-> new RuntimeException("Error couldn't update game"));
    }

    @Override
    public void deleteGame(String userId, Long gameId) {
        gameRepository.findById(gameId)
                .filter(game -> userId.equals(String.valueOf(game.getUserId())))
                .ifPresentOrElse(gameRepository::delete,
                        () -> { throw new RuntimeException("Error couldn't delete game"); });
    }

    private GameModel mapToEntity(GameModel request, String userId) {
        return GameModel.builder()
                .name(request.getName())
                .userId(Long.parseLong(userId))
                .build();
    }

    private GameModel updateEntity(GameModel updatedGame, GameModel gameRequest) {
        if (gameRequest.getName() != null) {
            updatedGame.setName(gameRequest.getName());
        }
        return updatedGame;
    }
}
