DROP TABLE IF EXISTS routes CASCADE;
DROP TABLE IF EXISTS routes_lanes CASCADE;
DROP TABLE IF EXISTS lanes CASCADE;
DROP TABLE IF EXISTS crossings CASCADE;
DROP SEQUENCE IF EXISTS crossings_id_seq;
DROP SEQUENCE IF EXISTS lanes_id_seq;
DROP SEQUENCE IF EXISTS routes_id_seq;


CREATE TABLE crossings (
  id INTEGER PRIMARY KEY
);

CREATE TABLE lanes (
  id                INTEGER PRIMARY KEY,
  nr                INTEGER                           NOT NULL,
  start_id          INTEGER REFERENCES crossings (id) NOT NULL,
  end_id            INTEGER REFERENCES crossings (id) NOT NULL,
  min_speed         INTEGER,
  max_speed         INTEGER,
  length            DECIMAL                           NOT NULL,
  participant_count INTEGER                           NOT NULL,
  blocked           BOOLEAN                           NOT NULL,
  created_at        TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
  modified_at       TIMESTAMP WITHOUT TIME ZONE       NOT NULL
);

CREATE TABLE routes (
  id          INTEGER PRIMARY KEY,
  start_id    INTEGER REFERENCES crossings (id) NOT NULL,
  end_id      INTEGER REFERENCES crossings (id) NOT NULL,
  created_at  TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
  modified_at TIMESTAMP WITHOUT TIME ZONE       NOT NULL
);

CREATE TABLE routes_lanes (
  route_id INTEGER REFERENCES routes (id)  NOT NULL,
  lane_id  INTEGER REFERENCES lanes (id)   NOT NULL,
  PRIMARY KEY (route_id, lane_id)
);

CREATE SEQUENCE crossings_id_seq;

CREATE SEQUENCE lanes_id_seq;

CREATE SEQUENCE routes_id_seq;
