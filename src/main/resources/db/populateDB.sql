DELETE FROM user_roles;
DELETE FROM users; -- meals удалятся констреинтом
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

insert into meals (user_id, date_time, descr, calories) VALUES
  (100000, '2018-10-16T10:00:00', 'завтрак', 600),
  (100000, '2018-10-16T15:00:00', 'обед', 1000),
  (100000, '2018-10-16T19:00:00', 'ужин', 500),
  (100000, '2018-10-17T10:00:00', 'завтрак', 600),
  (100000, '2018-10-17T15:00:00', 'обед', 1000),
  (100000, '2018-10-17T19:00:00', 'ужин', 550);

