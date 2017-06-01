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

CREATE TABLE IF NOT EXISTS may_gifts
(
  line_num SERIAL PRIMARY KEY,
  new_gifts_quantity INTEGER NOT NULL,
  snapshot_time TIMESTAMP NOT NULL DEFAULT now(),
  last_gift_id INTEGER NOT NULL,
  id_delta INTEGER NOT NULL,
  horn_delta INTEGER NOT NULL,
  time_delta INTEGER NOT NULL,
  sum_delta INTEGER
);

CREATE TABLE IF NOT EXISTS horns
(
  line_num SERIAL PRIMARY KEY,
  player_id INTEGER,
  data_id INTEGER NOT NULL,
  time TIMESTAMP
);

DROP TRIGGER IF EXISTS set_sum on may_gifts;

CREATE OR REPLACE FUNCTION sum_horn_delta() RETURNS TRIGGER AS '
DECLARE
  horn_delta INTEGER;
  quantity INTEGER;
  previous_line_num INTEGER;
  previous_sum INTEGER;
BEGIN
  IF TG_OP = ''INSERT'' THEN
    horn_delta = NEW.horn_delta;
    previous_line_num = NEW.line_num - 1;
    quantity = NEW.new_gifts_quantity;
    IF previous_line_num = 0
    THEN
      UPDATE may_gifts SET sum_delta = quantity WHERE line_num = NEW.line_num;
    ELSE
      IF horn_delta > 1000
      THEN
        SELECT sum_delta INTO previous_sum FROM may_gifts WHERE line_num = previous_line_num LIMIT 1;
        UPDATE may_gifts SET sum_delta = quantity + previous_sum WHERE line_num = NEW.line_num;
      ELSE
        UPDATE may_gifts SET sum_delta = quantity WHERE line_num = NEW.line_num;
      END IF;
    END IF;
  END IF;
  RETURN NULL;
END;
' LANGUAGE plpgsql;

CREATE TRIGGER set_sum AFTER INSERT ON may_gifts
FOR EACH ROW EXECUTE PROCEDURE sum_horn_delta();

INSERT INTO user_one
(login, password)
  SELECT 'admin', '$2a$10$e0Q7VcxsdILygTuKl7wRHechi2msU8QFQpYXxhpN/c2bXvsNFjiWy'
  WHERE
    NOT EXISTS (
        SELECT login FROM user_one WHERE login = 'admin'
    );