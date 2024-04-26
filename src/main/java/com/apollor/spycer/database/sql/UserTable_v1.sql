CREATE TABLE Users (
    userid varchar(36) NOT NULL,
    display_name varchar(64),
    email_address varchar(255) NOT NULL,
    mfa_email bool,
    mfa_app bool,
    verified bool NOT NULL,
    created_date timestamp NOT NULL,
    hash_pw varchar(64) NOT NULL,
    hash_salt varchar(32) NOT NULL,
    PRIMARY KEY (userid)
);