CREATE TABLE IF NOT EXISTS user (
    id VARCHAR(20),
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password TEXT NOT NULL,
    name VARCHAR(100),
    description TEXT,
    visibility VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT 0 NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS verification (
    userID VARCHAR(20),
    link VARCHAR(50),
    created TIMESTAMP,
    register BOOLEAN
);

CREATE TABLE IF NOT EXISTS conversation (
    id VARCHAR(30),
    name VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS conversationParticipants(
    conversationID VARCHAR(30),
    userID VARCHAR(20),
    FOREIGN KEY (conversationID) REFERENCES conversation(id),
    FOREIGN KEY (userID) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS message (
    id VARCHAR(50),
    sender VARCHAR(20),
    content TEXT,
    timestamp DATETIME,
    edited BOOLEAN,
    conversation VARCHAR(30),
    PRIMARY KEY (id),
    FOREIGN KEY (conversation) REFERENCES conversation(id),
    FOREIGN KEY (sender) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS unread (
    messageID VARCHAR(50),
    conversationID VARCHAR(30),
    userID VARCHAR(20),
    FOREIGN KEY (messageID) REFERENCES message(id),
    FOREIGN KEY (conversationID) REFERENCES conversation(id),
    FOREIGN KEY (userID) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS reviews (
    revID int NOT NULL AUTO_INCREMENT,
    userID VARCHAR(20) NOT NULL,
    review TEXT,
    stars INTEGER,
    timestamp DATETIME,
    edited BOOLEAN,
    placeID VARCHAR(20),
    likes INTEGER,
    dislikes INTEGER,
    PRIMARY KEY (revID),
    FOREIGN KEY (userID) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS comments (
    comID int NOT NULL AUTO_INCREMENT,
    userID VARCHAR(20) NOT NULL,
    comment TEXT,
    likes INTEGER,
    timestamp DATETIME,
    edited BOOLEAN,
    reviewID INTEGER NOT NULL,
    PRIMARY KEY (comID),
    FOREIGN KEY (userID) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS place (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    rating INTEGER NOT NULL,
    description TEXT,
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

CREATE TABLE IF NOT EXISTS travelGroup (
    id VARCHAR(30),
    name TEXT,
    owner VARCHAR(20),
    created DATETIME,
    description TEXT,
    conversationID VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS travelGroupMembers(
    travelGroupID VARCHAR(30),
    userID VARCHAR(20),
    inviteStatus INTEGER
);

CREATE TABLE IF NOT EXISTS trip (
    id VARCHAR(30),
    name TEXT,
    owner VARCHAR(20),
    created DATETIME,
    description TEXT,
    travelGroupID VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS tripMembers(
    tripID VARCHAR(30),
    userID VARCHAR(20),
    inviteStatus INTEGER
);

CREATE TABLE IF NOT EXISTS event (
    id VARCHAR(50),
    name TEXT,
    creator VARCHAR(20),
    place INTEGER NOT NULL,
    start DATETIME,
    end DATETIME,
    description TEXT,
    tripID VARCHAR(30)
);
