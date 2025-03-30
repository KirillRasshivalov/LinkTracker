-- Создание таблицы active_users
CREATE TABLE active_users (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL
);

-- Создание таблицы link_info
CREATE TABLE link_info (
                           id BIGSERIAL PRIMARY KEY,
                           link VARCHAR(255) NOT NULL,
                           filters VARCHAR(255),
                           tegs VARCHAR(255),
                           user_id BIGINT NOT NULL,
                           CONSTRAINT fk_link_info_user FOREIGN KEY (user_id) REFERENCES active_users(id)
);
