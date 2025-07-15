-- Empty file (to be filled)
CREATE TABLE EscapeRoom (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    total_tickets INT
);

CREATE TABLE Room (
    id INT PRIMARY KEY AUTO_INCREMENT,
    theme VARCHAR(255),
    difficulty_level INT,
    escape_room_id INT,
    FOREIGN KEY (escape_room_id) REFERENCES EscapeRoom(id)
);

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
    FOREIGN KEY (reward_id) REFERENCES Reward(id),
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