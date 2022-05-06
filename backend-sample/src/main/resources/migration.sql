--liquibase formatted sql

--changeset author:1
create table owner
(
    id   bigserial primary key,
    name varchar not null
);
--rollback drop table owner

--changeset demo:2
create table pet
(
    id       bigserial,
    name     varchar not null,
    owner_id bigint,
    foreign key (owner_id) references owner (id)
);
--rollback drop table pet
