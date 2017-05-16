CREATE TABLE IF NOT EXISTS user_one
(
  login VARCHAR NOT NULL UNIQUE,
  password VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS system_messages
(
  line_num SERIAL PRIMARY KEY,
  id INTEGER UNIQUE NOT NULL,
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
  line_num SERIAL PRIMARY KEY,
  message_id INTEGER UNIQUE NOT NULL,
  message_date TIMESTAMP,
  player_id INTEGER NOT NULL,
  gift_data_id INTEGER NOT NULL,
  date TIMESTAMP NOT NULL
);

/* use only once */
ALTER TABLE system_messages DROP CONSTRAINT IF EXISTS system_messages_pkey;
ALTER TABLE system_messages DROP CONSTRAINT IF EXISTS system_messages_id_key;
ALTER TABLE system_messages ADD CONSTRAINT system_messages_id_key UNIQUE (id);
ALTER TABLE system_messages ADD COLUMN line_num SERIAL PRIMARY KEY;

ALTER TABLE checked_profiles DROP CONSTRAINT IF EXISTS checked_profiles_pkey;
ALTER TABLE checked_profiles DROP CONSTRAINT IF EXISTS checked_profiles_message_id_key;
ALTER TABLE checked_profiles ADD CONSTRAINT checked_profiles_message_id_key UNIQUE (message_id);
ALTER TABLE checked_profiles ADD COLUMN line_num SERIAL PRIMARY KEY;
ALTER TABLE checked_profiles ADD COLUMN message_date TIMESTAMP;

INSERT INTO user_one
(login, password)
  SELECT 'admin', '$2a$10$e0Q7VcxsdILygTuKl7wRHechi2msU8QFQpYXxhpN/c2bXvsNFjiWy'
  WHERE
    NOT EXISTS (
        SELECT login FROM user_one WHERE login = 'admin'
    );