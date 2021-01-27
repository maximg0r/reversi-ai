package com.maximgorshkov.reversi;

import org.springframework.data.jpa.repository.JpaRepository;

interface GameSaveRepository extends JpaRepository<GameSave, Long> {
    
}