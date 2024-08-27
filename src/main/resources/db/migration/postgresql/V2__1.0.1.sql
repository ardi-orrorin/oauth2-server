CREATE TABLE client (
    id                              VARCHAR(255)    PRIMARY KEY NOT NULL,
    client_id                       VARCHAR(255)    UNIQUE      NOT NULL,
    client_id_issued_at             TIMESTAMP       DEFAULT     CURRENT_TIMESTAMP NOT NULL,
    client_secret                   VARCHAR(255)    DEFAULT     NULL,
    client_secret_expires_at        TIMESTAMP       DEFAULT     NULL,
    client_name                     VARCHAR(255)                NOT NULL,
    client_authentication_methods   VARCHAR(1000)               NOT NULL,
    authorization_grant_types       VARCHAR(1000)               NOT NULL,
    redirect_uris                   VARCHAR(1000)   DEFAULT     NULL,
    post_logout_redirect_uris       VARCHAR(1000)   DEFAULT     NULL,
    scopes                          VARCHAR(1000)               NOT NULL,
    client_settings                 VARCHAR(2000)               NOT NULL,
    token_settings                  VARCHAR(2000)               NOT NULL
);

CREATE TABLE authorizations (
    id                              VARCHAR(255)    PRIMARY KEY NOT NULL,
    registered_client_id            VARCHAR(255)            NOT NULL,
    principal_name                  VARCHAR(255)            NOT NULL,
    authorization_grant_type        VARCHAR(255)            NOT NULL,
    authorized_scopes               VARCHAR(1000)   DEFAULT NULL,
    attributes                      VARCHAR(4000)   DEFAULT NULL,
    state                           VARCHAR(500)    DEFAULT NULL,
    authorization_code_value        VARCHAR(4000)   DEFAULT NULL,
    authorization_code_issued_at    TIMESTAMP       DEFAULT NULL,
    authorization_code_expires_at   TIMESTAMP       DEFAULT NULL,
    authorization_code_metadata     VARCHAR(2000)   DEFAULT NULL,
    access_token_value              VARCHAR(4000)   DEFAULT NULL,
    access_token_issued_at          TIMESTAMP       DEFAULT NULL,
    access_token_expires_at         TIMESTAMP       DEFAULT NULL,
    access_token_metadata           VARCHAR(2000)   DEFAULT NULL,
    access_token_type               VARCHAR(255)    DEFAULT NULL,
    access_token_scopes             VARCHAR(1000)   DEFAULT NULL,
    refresh_token_value             VARCHAR(4000)   DEFAULT NULL,
    refresh_token_issued_at         TIMESTAMP       DEFAULT NULL,
    refresh_token_expires_at        TIMESTAMP       DEFAULT NULL,
    refresh_token_metadata          VARCHAR(2000)   DEFAULT NULL,
    oidc_id_token_value             VARCHAR(4000)   DEFAULT NULL,
    oidc_id_token_issued_at         TIMESTAMP       DEFAULT NULL,
    oidc_id_token_expires_at        TIMESTAMP       DEFAULT NULL,
    oidc_id_token_metadata          VARCHAR(2000)   DEFAULT NULL,
    oidc_id_token_claims            VARCHAR(2000)   DEFAULT NULL,
    user_code_value                 VARCHAR(4000)   DEFAULT NULL,
    user_code_issued_at             TIMESTAMP       DEFAULT NULL,
    user_code_expires_at            TIMESTAMP       DEFAULT NULL,
    user_code_metadata              VARCHAR(2000)   DEFAULT NULL,
    device_code_value               VARCHAR(4000)   DEFAULT NULL,
    device_code_issued_at           TIMESTAMP       DEFAULT NULL,
    device_code_expires_at          TIMESTAMP       DEFAULT NULL,
    device_code_metadata            VARCHAR(2000)   DEFAULT NULL
);

CREATE TABLE authorization_consent (
    registered_client_id            VARCHAR(255) NOT NULL,
    principal_name                  VARCHAR(255) NOT NULL,
    authorities                     VARCHAR(1000) NOT NULL,
    PRIMARY KEY (registered_client_id, principal_name)
);

CREATE TABLE user_clients (
    user_id                         VARCHAR(255) NOT NULL,
    registered_client_id            VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, registered_client_id)
);

CREATE TABLE user_roles (
    user_id                         int8         NOT NULL,
    role                            VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role)
);
