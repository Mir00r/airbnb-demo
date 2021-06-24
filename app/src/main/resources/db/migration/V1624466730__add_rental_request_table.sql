-- auto-generated definition
create table if not exists rentalprocessor.rental_requests
(
    id bigserial not null
        constraint rental_requests_pkey
        primary key,
    created_at      timestamp    not null,
    created_by      varchar(255),
    deleted         boolean      not null,
    updated_at      timestamp,
    updated_by      varchar(255),
    uuid_str        varchar(255) not null,
    check_in        timestamp    not null,
    check_out       timestamp    not null,
    note            text         null,
    status          varchar(255) not null,
    household_id    bigint       not null,
    requested_by_id bigint       not null,
    requested_to_id bigint       not null,
    assigned_to_id  bigint       null,
    cancelled_by_id bigint       null,

    constraint UK_h090yg08ugna2h8582qstnhsh
        unique (uuid_str),
    constraint FK4cx8p16ck37cx957a859c1lls
        foreign key (cancelled_by_id) references authenticator.m_users (id),
    constraint FKbo75rvy5cog9de7lsyio8dm1l
        foreign key (requested_by_id) references authenticator.m_users (id),
    constraint FKftb06ije3258ira298l3fp1ro
        foreign key (assigned_to_id) references authenticator.m_users (id),
    constraint FKftknfuogafga3ek2cxjacu7rf
        foreign key (household_id) references rentalprocessor.households (id),
    constraint FKmrma3qda9ibswk9ipixx3udw1
        foreign key (requested_to_id) references authenticator.m_users (id)
);

