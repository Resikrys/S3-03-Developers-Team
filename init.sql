
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
('The Haunted Mansion', 100),
('The Secret Bunker', 50),
('The Abandoned Laboratory', 75);

CREATE TABLE Room (
    id INT PRIMARY KEY AUTO_INCREMENT,
    theme VARCHAR(255),
    difficulty_level INT NOT NULL,
    escape_room_id INT,
    FOREIGN KEY (escape_room_id) REFERENCES EscapeRoom(id)
);

INSERT INTO Room (theme, difficulty_level, escape_room_id) VALUES
('Classic Horror', 8, 1),
('Gothic Mistery', 7, 1),
('Post-Apocalyptic Survival', 9, 2),
('Retro SciFi', 6, 3),
('NULL Test Room', 5, NULL);

CREATE TABLE DecorationObject (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    price DECIMAL(10, 2),
    material VARCHAR(255),
    room_id INT,
    FOREIGN KEY (room_id) REFERENCES Room(id)
);
INSERT INTO DecorationObject (name, price, material, room_id) VALUES
('Antique Candlestick', 29.99, 'WOOD', 1),
('Wall Painting: Ghostly Manor', 49.50, 'WOOD', 1),
('Rusty Vault Door', 89.00, 'WOOD', 1),
('Holographic Map', 74.95, 'WOOD', 1),
('Plasma Lamp', 32.80, 'WOOD', 1);

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
INSERT INTO ClueObject (name, price, material, puzzle_description, solved, reward_id, room_id) VALUES
('Mysterious Skull', 19.99, 'WOOD', 'Solve the riddle carved on the jaw to unlock the secret compartment.', FALSE, NULL, 1),
('Encrypted Diary', 34.50, 'WOOD', 'Decode the diary entries to find the location of the hidden lever.', TRUE, NULL, 1),
('Laser-etched Keycard', 15.75, 'WOOD', 'Scan the pattern in the right orientation to open the lab door.', FALSE, NULL, 1),
('Rusty Timepiece', 22.10, 'WOOD', 'Adjust the hands to the correct time based on clues scattered around.', FALSE, NULL, 1),
('Ghost Painting', 41.00, 'WOOD', 'Find what’s wrong in the brushstrokes to reveal a hidden message.', TRUE, NULL, 1);

CREATE TABLE Player (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255),
    registered BOOLEAN,
    escape_room_id INT,
    FOREIGN KEY (escape_room_id) REFERENCES EscapeRoom(id)
);
INSERT INTO Player (name, email, registered, escape_room_id) VALUES
('Laura Sánchez', 'laura.sanchez@example.com', TRUE, 1),
('Carlos Romero', 'carlos.romero@example.com', FALSE, 1),
('Marta Gil', 'marta.gil@example.com', TRUE, 1),
('David Pérez', 'david.perez@example.com', TRUE, 1),
('Lucía Torres', 'lucia.torres@example.com', FALSE, 1),
('Andrés Molina', 'andres.molina@example.com', TRUE, 1),
('Eva Ríos', 'eva.rios@example.com', TRUE, 1);

CREATE TABLE Ticket (
    id INT PRIMARY KEY AUTO_INCREMENT,
    price DECIMAL(10, 2),
    player_id INT,
    escape_room_id INT,
    FOREIGN KEY (player_id) REFERENCES Player(id),
    FOREIGN KEY (escape_room_id) REFERENCES EscapeRoom(id)
);