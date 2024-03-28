CREATE TABLE IF NOT EXISTS user (
    id VARCHAR(20) PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password TEXT NOT NULL,
    name VARCHAR(100),
    description TEXT,
    visibility VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT 1 NOT NULL
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

CREATE TABLE IF NOT EXISTS unread (
    messageID VARCHAR(50),
    conversationID VARCHAR(30),
    userID VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS reviews (
    revID int NOT NULL AUTO_INCREMENT,
    userID VARCHAR(20) NOT NULL,
    review TEXT,
    stars INTEGER,
    timestamp DATETIME,
    edited BOOLEAN,
    placeID INTEGER,
    likes INTEGER,
    dislikes INTEGER,
    PRIMARY KEY (revID)
);

CREATE TABLE IF NOT EXISTS reports (
    repID int NOT NULL AUTO_INCREMENT,
    repUserID VARCHAR(20),
    conUserID VARCHAR(20),
    repText TEXT,
    appText TEXT,
    timestamp DATETIME,
    placeID INTEGER,
    revID INTEGER,
    comID INTEGER,
    banned BOOLEAN,
    PRIMARY KEY (repID)
);

CREATE TABLE IF NOT EXISTS comments (
    comID int NOT NULL AUTO_INCREMENT,
    userID VARCHAR(20) NOT NULL,
    comment TEXT,
    likes INTEGER,
    dislikes INTEGER,
    timestamp DATETIME,
    edited BOOLEAN,
    reviewID INTEGER,
    PRIMARY KEY (comID)
);

CREATE TABLE IF NOT EXISTS place (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    rating VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS reviewReaction (
    userID VARCHAR(20) NOT NULL,
    reviewID INTEGER NOT NULL,
    isLike BOOLEAN
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
