CREATE TABLE client_infos (
    id                              SERIAL8                     PRIMARY KEY NOT NULL,
    user_id                         VARCHAR(255)                NOT NULL,
    registered_client_id            VARCHAR(255)                NOT NULL,
    client_id                       VARCHAR(255)                NOT NULL,
    client_secret                   VARCHAR(255)    DEFAULT     NULL,
    created_at                      TIMESTAMP       DEFAULT     CURRENT_TIMESTAMP
);
