
CREATE DATABASE IF NOT EXISTS escaperoom_db;
USE escaperoom_db;

DROP TABLE IF EXISTS Ticket;
DROP TABLE IF EXISTS Player;
DROP TABLE IF EXISTS ClueObject;
DROP TABLE IF EXISTS DecorationObject;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS EscapeRoom;

CREATE TABLE EscapeRoom (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    total_tickets INT
);

INSERT INTO EscapeRoom (name, total_tickets) VALUES
('La Mansión Embrujada', 100),
('El Búnker Secreto', 50),
('El Laboratorio Abandonado', 75);

CREATE TABLE Room (
    id INT PRIMARY KEY AUTO_INCREMENT,
    theme VARCHAR(255),
    difficulty_level INT NOT NULL,
    escape_room_id INT,
    FOREIGN KEY (escape_room_id) REFERENCES EscapeRoom(id)
);

INSERT INTO Room (theme, difficulty_level, escape_room_id) VALUES
('Terror Clásico', 8, 1),      -- De La Mansión Embrujada
('Misterio Gótico', 7, 1),     -- De La Mansión Embrujada
('Supervivencia Post-Apocalíptica', 9, 2), -- Del Búnker Secreto
('Ciencia Ficción Retro', 6, 3),   -- Del Laboratorio Abandonado
('Sala de Prueba NULL', 5, NULL);

CREATE TABLE DecorationObject (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    price DECIMAL(10, 2),
    material VARCHAR(255),
    room_id INT,
    FOREIGN KEY (room_id) REFERENCES Room(id)
);

CREATE TABLE ClueObject (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    price DECIMAL(10, 2),
    material VARCHAR(255),
    puzzle_description TEXT,
    solved BOOLEAN,
    reward_id INT,
    room_id INT,
    FOREIGN KEY (room_id) REFERENCES Room(id)
);

CREATE TABLE Player (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255),
    registered BOOLEAN,
    escape_room_id INT,
    FOREIGN KEY (escape_room_id) REFERENCES EscapeRoom(id)
);

CREATE TABLE Ticket (
    id INT PRIMARY KEY AUTO_INCREMENT,
    price DECIMAL(10, 2),
    player_id INT,
    escape_room_id INT,
    FOREIGN KEY (player_id) REFERENCES Player(id),
    FOREIGN KEY (escape_room_id) REFERENCES EscapeRoom(id)
);