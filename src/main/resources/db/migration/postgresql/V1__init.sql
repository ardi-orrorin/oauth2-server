CREATE TABLE users (
    id             SERIAL4         PRIMARY KEY,
    user_id        VARCHAR(255)    NOT NULL UNIQUE,
    pwd            VARCHAR(255)    NOT NULL,
    email          VARCHAR(255)    NOT NULL UNIQUE,
    name           VARCHAR(255)    NOT NULL,
    phone          VARCHAR(20)     UNIQUE,
    address        VARCHAR(255),
    detail_address VARCHAR(255),
    postcode       VARCHAR(255),
    birthday       VARCHAR(12),
    profile_image  VARCHAR(255),
    created_at     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX "IDX_USERS_USER_ID" ON "users" ("user_id");
