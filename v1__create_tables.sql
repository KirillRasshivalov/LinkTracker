CREATE TABLE active_users (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL
);

CREATE TABLE link_info (
                           id BIGSERIAL PRIMARY KEY,
                           link VARCHAR(255) NOT NULL,
                           filters VARCHAR(255),
                           tegs VARCHAR(255)
);

CREATE TABLE user_links (
                            user_id BIGINT NOT NULL,
                            link_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, link_id),
                            CONSTRAINT fk_user_links_user FOREIGN KEY (user_id) REFERENCES active_users(id),
                            CONSTRAINT fk_user_links_link FOREIGN KEY (link_id) REFERENCES link_info(id)
);
