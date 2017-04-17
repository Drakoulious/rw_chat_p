DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS system_messages;

CREATE TABLE user
(
  login VARCHAR NOT NULL,
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