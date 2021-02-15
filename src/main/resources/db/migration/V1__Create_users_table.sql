create table users
(
    id       BIGINT PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

-- Encypted password is 'admin'
INSERT INTO users(id, username, password) VALUES
  (1, 'admin', '$2a$10$oe6qoc.hUBBfqLHU.2gYT.weJRT8Jb.XqlsULjNe821bocUVWVbvy')
;