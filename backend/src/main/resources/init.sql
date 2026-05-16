-- Suppression dans le bon ordre (clés étrangères)
DROP TABLE IF EXISTS game_turn;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS card;
DROP TABLE IF EXISTS deck;
DROP TABLE IF EXISTS game_player;
DROP TABLE IF EXISTS ranking;
DROP TABLE IF EXISTS game;
DROP TABLE IF EXISTS player;

-- Table player
CREATE TABLE player (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

-- Table game
CREATE TABLE game (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(20) NOT NULL DEFAULT 'WAITING',
    direction VARCHAR(20) NOT NULL DEFAULT 'CLOCKWISE',
    current_color VARCHAR(10),
    current_player_id BIGINT,
    winner VARCHAR(50),
    FOREIGN KEY (current_player_id) REFERENCES player(id)
);

-- Table game_player
CREATE TABLE game_player (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    hand_size INT DEFAULT 0,
    FOREIGN KEY (game_id) REFERENCES game(id),
    FOREIGN KEY (player_id) REFERENCES player(id)
);

-- Table deck
CREATE TABLE deck (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_id BIGINT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES game(id)
);

-- Table card
CREATE TABLE card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    value VARCHAR(20) NOT NULL,
    color VARCHAR(10) NOT NULL,
    deck_id BIGINT,
    holder_id BIGINT,
    is_discard BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (deck_id) REFERENCES deck(id),
    FOREIGN KEY (holder_id) REFERENCES game_player(id)
);

-- Table game_turn
CREATE TABLE game_turn (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    card_id BIGINT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES game(id),
    FOREIGN KEY (player_id) REFERENCES player(id),
    FOREIGN KEY (card_id) REFERENCES card(id)
);

-- Table message
CREATE TABLE message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES game(id),
    FOREIGN KEY (player_id) REFERENCES player(id)
);

-- Table ranking
CREATE TABLE ranking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL UNIQUE,
    total_games INT DEFAULT 0,
    total_wins INT DEFAULT 0,
    total_losses INT DEFAULT 0,
    FOREIGN KEY (player_id) REFERENCES player(id)
);

-- ============================================================
-- DONNEES INITIALES — joueurs de test
-- ============================================================

INSERT INTO player (username, password_hash) VALUES
('amine', 'amine'),
('houssam', 'houssam');

INSERT INTO ranking (player_id, total_games, total_wins, total_losses) VALUES
(1, 5, 3, 2),
(2, 4, 1, 3);