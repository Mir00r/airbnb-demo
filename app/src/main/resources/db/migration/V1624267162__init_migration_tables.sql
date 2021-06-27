create table if not exists authenticator.m_users
(
    id bigserial not null
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


-- Roles
create table if not exists authenticator.roles
(
    id bigserial not null
        constraint roles_pkey
        primary key,
    created_at  timestamp    not null,
    created_by  varchar(255),
    deleted     boolean      not null,
    updated_at  timestamp,
    updated_by  varchar(255),
    uuid_str    varchar(255) not null,
    description varchar(255) null,
    name        varchar(255) not null,
    restricted  boolean      not null
);

create index FK95jx57baw7c39ybblooejidye
    on authenticator.roles (updated_by);

create index FKolo7v6bxrho3bgjfh10i4y4lb
    on authenticator.roles (created_by);

-- users role
create table if not exists authenticator.m_users_roles
(
    user_id  bigint not null,
    roles_id bigint not null
);

create index FK143dfv8wd8b2w93gbvd84ps5k
    on authenticator.m_users_roles (user_id);

create index FK5hwrg9mw7hmdq8cn63mh7sx0j
    on authenticator.m_users_roles (roles_id);

-- Privileges
create table if not exists authenticator.privileges
(
    id bigserial not null
        constraint privileges_pkey
        primary key,
    created_at  timestamp    not null,
    created_by  varchar(255),
    deleted     boolean      not null,
    updated_at  timestamp,
    updated_by  varchar(255),
    uuid_str    varchar(255) not null,
    label       varchar(255) not null,
    name        varchar(255) not null,
    description varchar(255) null
);

create index FKewlbnm1g24vnjeuxlsk681k7j
    on authenticator.privileges (created_by);

create index FKj8t6kkmks6qe3dluky108cku
    on authenticator.privileges (updated_by);


-- Roles Privileges
create table if not exists authenticator.roles_privileges
(
    role_id      bigint not null,
    privilege_id bigint not null
);

create index FK5duhoc7rwt8h06avv41o41cfy
    on authenticator.roles_privileges (privilege_id);

create index FK629oqwrudgp5u7tewl07ayugj
    on authenticator.roles_privileges (role_id);


-- Ac validation token

create table if not exists authenticator.ac_validation_tokens
(
    id bigserial not null
        constraint ac_validation_tokens_pkey
        primary key,
    created_at        timestamp    not null,
    created_by        varchar(255),
    deleted           boolean      not null,
    updated_at        timestamp,
    updated_by        varchar(255),
    uuid_str          varchar(255) not null,
    token             varchar(255) null,
    token_valid       boolean      not null default false,
    token_valid_until timestamp    null,
    reason            varchar(255) null,
    username          varchar(255) null,
    user_id           bigint       null
);

create index FK5c0f4fivjiyy19j4f0fxayyc3
    on authenticator.ac_validation_tokens (updated_by);

create index FKi746ajdn1vcbojvapsq6jsc9u
    on authenticator.ac_validation_tokens (user_id);

create index FKqamdf34eo911js0e19a36p6v2
    on authenticator.ac_validation_tokens (created_by);
