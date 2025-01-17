package com.example.game_service.repository;

import com.example.game_service.commons.entities.GameModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<GameModel, Long> {
    @Override
    List<GameModel> findAllById(Iterable<Long> longs);
}
