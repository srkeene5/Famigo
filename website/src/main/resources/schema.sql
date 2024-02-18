DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id VARCHAR(10) NOT NULL,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    description TEXT,
    visibility TEXT
);

CREATE TABLE IF NOT EXISTS place (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    rating VARCHAR(255) NOT NULL
);