CREATE TABLE IF NOT EXISTS user (
    id VARCHAR(20) PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password TEXT NOT NULL,
    name VARCHAR(100),
    description TEXT,
    visibility VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL
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

CREATE TABLE IF NOT EXISTS userStats (
    id VARCHAR(100) PRIMARY KEY,
    follower_count INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS followers (
    id VARCHAR(100),
    following_id VARCHAR(100),
    PRIMARY KEY (id, following_id)
);