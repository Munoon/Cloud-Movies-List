DELETE FROM users;
ALTER SEQUENCE users_seq RESTART WITH 100;

INSERT INTO users (name, surname, email, password) VALUES
    ('Nikita', 'Ivchenko', 'munoongg@gmail.com', '{noop}pass');