DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS users_seq;

CREATE SEQUENCE users_seq AS INTEGER START WITH 100;

CREATE TABLE users (
   id               INTEGER PRIMARY KEY DEFAULT nextval('users_seq'),
   name             VARCHAR                        NOT NULL,
   surname          VARCHAR                        NOT NULL,
   email            VARCHAR                        NOT NULL,
   password         VARCHAR                        NOT NULL,
   registered       TIMESTAMP DEFAULT now()        NOT NULL
);