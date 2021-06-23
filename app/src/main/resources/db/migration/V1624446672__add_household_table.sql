create table if not exists rentalprocessor.households
(
    id bigserial not null
        constraint households_pkey
        primary key,
    created_at        timestamp        not null,
    created_by        varchar(255),
    deleted           boolean          not null,
    updated_at        timestamp,
    updated_by        varchar(255),
    uuid_str          varchar(255)     not null,
    available         boolean          not null,
    available_from    timestamp        null,
    host_name         varchar(255)     not null,
    number_of_balcony smallint         null,
    number_of_bath    smallint         null,
    number_of_bed     smallint         null,
    property_type     varchar(255)     not null,
    rent_price        double precision not null,
    rent_type         varchar(255)     not null,
    size              bigint           not null,
    status            varchar(255)     not null,
    line_one          varchar(255)     null,
    line_two          varchar(255)     null,
    country           varchar(255)     null,
    city              varchar(255)     null,
    state             varchar(255)     null,
    zipcode           varchar(255)     null,
    floor_number      smallint         null,
    longitude         double precision not null,
    latitude          double precision not null,
    altitude          double precision not null,
    bbq_area          boolean          null,
    cctv              boolean          null,
    dining            boolean          null,
    drawing           boolean          null,
    electricity       boolean          null,
    elevator          boolean          null,
    furnished         boolean          null,
    gas               boolean          null,
    gym               boolean          null,
    hair_dryer        boolean          null,
    internet          boolean          null,
    kitchen           boolean          null,
    mosque            boolean          null,
    parking           boolean          null,
    roof_access       boolean          null,
    swimming_pool     boolean          null,
    water             boolean          null,
    tiled             boolean          null,

    constraint UK_1sipphspau8676wmck5mkk3kc
        unique (uuid_str)
);

create table if not exists rentalprocessor.household_images
(
    household_id bigint       not null,
    images       varchar(255) null
);

create index FK6lqf5g7t2huldw8c5ecgft7y1
    on rentalprocessor.household_images (household_id);
