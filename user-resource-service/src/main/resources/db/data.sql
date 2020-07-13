DELETE FROM users_role;
DELETE FROM users;
ALTER SEQUENCE users_roles_seq RESTART WITH 100;
ALTER SEQUENCE users_seq RESTART WITH 100;

INSERT INTO users (name, surname, email, password) VALUES
    ('Nikita', 'Ivchenko', 'munoongg@gmail.com', '{noop}pass');

INSERT INTO users_role (user_id, role) VALUES
    (100, 'ROLE_ADMIN'), (100, 'ROLE_USER');