CREATE TABLE IF NOT EXISTS user (
    id VARCHAR(20) PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    description TEXT,
    visibility TEXT
);

CREATE TABLE IF NOT EXISTS conversation (
    id VARCHAR(30) PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS conversationParticipants(
    conversationID VARCHAR(30),
    userID VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS message (
    id VARCHAR(50) PRIMARY KEY,
    sender VARCHAR(10),
    content TEXT,
    timestamp DATETIME,
    edited BOOLEAN,
    conversation VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS reviews (
    revID int NOT NULL AUTO_INCREMENT,
    userID VARCHAR(20) NOT NULL,
    review TEXT,
    stars INTEGER,
    timestamp DATETIME,
    edited BOOLEAN,
    placeID VARCHAR(20),
    PRIMARY KEY (revID)
);

CREATE TABLE IF NOT EXISTS place (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    rating VARCHAR(255) NOT NULL
);