CREATE TABLE IF NOT EXISTS user_one
(
  login VARCHAR NOT NULL UNIQUE,
  password VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS system_messages
(
  id INTEGER PRIMARY KEY,
  player_id INTEGER NOT NULL,
  date TIMESTAMP NOT NULL,
  room_id INTEGER NOT NULL,
  type VARCHAR NOT NULL,
  channel INTEGER NOT NULL,
  text VARCHAR NOT NULL,
  linked_pid INTEGER NOT NULL,
  level INTEGER NOT NULL,
  clan_id INTEGER NOT NULL,
  nickname VARCHAR NOT NULL,
  fraction VARCHAR NOT NULL,
  clan_name VARCHAR NOT NULL,
  item_name VARCHAR NOT NULL,
  item_count INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS checked_profiles
(
  message_id INTEGER PRIMARY KEY,
  player_id INTEGER NOT NULL,
  gift_data_id INTEGER NOT NULL,
  date TIMESTAMP NOT NULL
);

INSERT INTO user_one
(login, password)
  SELECT 'admin', '$2a$10$e0Q7VcxsdILygTuKl7wRHechi2msU8QFQpYXxhpN/c2bXvsNFjiWy'
  WHERE
    NOT EXISTS (
        SELECT login FROM user_one WHERE login = 'admin'
    );