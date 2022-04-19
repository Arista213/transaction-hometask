--liquibase formatted sql

--changeset author:1
create table owner(
    id bigint auto_increment primary key,
    name varchar not null
);
--rollback drop table owner

--changeset demo:2
create table pet(
    id bigint auto_increment primary key,
    name varchar not null,
    owner_id bigint,
    foreign key (owner_id) references owner(id)
);
--rollback drop table pet

--changeset author:populate_owner
insert into owner(id, name) values
(20, 'Mehmed Vahideddin'),
(2, 'Andrey Golikov'),
(1, 'Mr Fyodor');
--rollback DELETE FROM owner

--changeset author:populate_pet
insert into pet(id, name, owner_id) values
(35, 'tapir', null),
(21, 'yak', 20),
(20, 'elephant', 20),
(1, 'cat', 1),
(2, 'dog', 1);
--rollback DELETE FROM pet