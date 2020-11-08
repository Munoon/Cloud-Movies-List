DROP TABLE IF EXISTS users_role;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS users_authentications_jwt;
DROP TABLE IF EXISTS users_authentications;
DROP SEQUENCE IF EXISTS users_roles_seq;
DROP SEQUENCE IF EXISTS users_seq;
DROP SEQUENCE IF EXISTS users_authentications_jwt_seq;
DROP SEQUENCE IF EXISTS users_authentications_seq;

CREATE SEQUENCE users_seq AS INTEGER START WITH 100;
CREATE SEQUENCE users_roles_seq AS INTEGER START WITH 100;
CREATE SEQUENCE users_authentications_seq AS INTEGER START WITH 100;
CREATE SEQUENCE users_authentications_jwt_seq AS INTEGER START WITH 100;

CREATE TABLE users (
   id               INTEGER PRIMARY KEY DEFAULT nextval('users_seq'),
   name             VARCHAR                        NOT NULL,
   surname          VARCHAR                        NOT NULL,
   email            VARCHAR                        NOT NULL,
   password         VARCHAR                        NOT NULL,
   registered       TIMESTAMP DEFAULT now()        NOT NULL,
   UNIQUE (email)
);

CREATE TABLE users_role (
    id              INTEGER PRIMARY KEY DEFAULT nextval('users_roles_seq'),
    user_id         INTEGER                         NOT NULL,
    role            VARCHAR                         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE (user_id, role)
);

CREATE TABLE users_authentications (
    id              INTEGER PRIMARY KEY DEFAULT nextval('users_authentications_seq'),
    session_id      VARCHAR                         NOT NULL,
    UNIQUE (session_id)
);

CREATE TABLE users_authentications_jwt (
    id                  INTEGER PRIMARY KEY DEFAULT nextval('users_authentications_jwt_seq'),
    authentication_id   INTEGER                     NOT NULL,
    token               VARCHAR                     NOT NULL,
    expiration          TIMESTAMP                   NOT NULL,
    UNIQUE (token),
    FOREIGN KEY (authentication_id) REFERENCES users_authentications (id) ON DELETE CASCADE
);