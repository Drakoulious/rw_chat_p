DROP TABLE IF EXISTS user_one;
DROP TABLE IF EXISTS system_messages;

CREATE TABLE user_one
(
  login VARCHAR NOT NULL UNIQUE,
  password VARCHAR NOT NULL
);

CREATE TABLE system_messages
(
  id INTEGER PRIMARY KEY,
  player_id INTEGER NOT NULL,
  _date TIMESTAMP NOT NULL,
  rood_id INTEGER NOT NULL,
  type VARCHAR NOT NULL,
  channel INTEGER NOT NULL,
  text VARCHAR NOT NULL,
  linked_pid INTEGER NOT NULL,
  level INTEGER NOT NULL,
  clan_id INTEGER NOT NULL,
  flags INTEGER NOT NULL,
  nickname VARCHAR NOT NULL,
  fraction VARCHAR NOT NULL,
  clan_name VARCHAR NOT NULL,
  clan_status VARCHAR NOT NULL
);

INSERT INTO user_one VALUES ('admin', '$2a$10$e0Q7VcxsdILygTuKl7wRHechi2msU8QFQpYXxhpN/c2bXvsNFjiWy');