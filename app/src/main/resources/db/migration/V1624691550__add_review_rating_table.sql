-- New Migration
create table if not exists rentalsearch.reviews
(
    id bigserial not null
        constraint reviews_pkey
        primary key,
    created_at     timestamp        not null,
    created_by     varchar(255),
    deleted        boolean          not null,
    updated_at     timestamp,
    updated_by     varchar(255),
    uuid_str       varchar(255)     not null
        constraint uk_egslkkjv6vf0r0quwn7fr3jmc
        unique,
    title          varchar(255)     not null,
    content        varchar(1200)    not null,
    average_rating double precision not null,
    household_id   bigint
        constraint fkpl51cejpw4gy5swfar8br9ngi
        references rentalprocessor.households,
    user_id        bigint
        constraint fk3py8udykil8gn82x5crrq7gbk
        references authenticator.m_users
);


create table if not exists rentalsearch.ratings
(
    id bigserial not null
        constraint ratings_pkey
        primary key,
    created_at timestamp    not null,
    created_by varchar(255),
    deleted    boolean      not null,
    updated_at timestamp,
    updated_by varchar(255),
    uuid_str   varchar(255) not null
        constraint uk_lpp1q361lb7ju50asn1y56q9n
        unique,
    value      smallint     not null,
    review_id  bigint
        constraint fkjq6xfubxj8cj6siie1vp7035b
        references rentalsearch.reviews
);
