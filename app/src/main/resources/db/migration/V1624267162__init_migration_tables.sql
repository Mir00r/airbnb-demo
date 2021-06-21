create table if not exists authenticator.m_users
(
    id                      bigserial    not null
        constraint m_users_pkey
        primary key,
    created_at              timestamp    not null,
    created_by              varchar(255),
    deleted                 boolean      not null,
    updated_at              timestamp,
    updated_by              varchar(255),
    uuid_str                varchar(255) not null
        constraint uk_a2ao1y942v9whup5j7dhw4qve
        unique,
    account_non_expired     boolean,
    account_non_locked      boolean,
    credentials_non_expired boolean,
    email                   varchar(255)
        constraint uk_5tyf96frxy09iqb90fag74jl5
        unique,
    enabled                 boolean      not null,
    gender                  varchar(255) not null,
    name                    varchar(255) not null,
    password                varchar(512) not null,
    phone                   varchar(255)
        constraint uk_7aoads8ubmh10m9uo1kxwpvud
        unique,
    username                varchar(255) not null
        constraint uk_lxevwjnma30s6actvjsblbp6a
        unique
);
