--liquibase formatted sql

--changeset author:1
create table owner(
    id bigserial primary key,
    name varchar not null
) partition by hash(id);
CREATE TABLE owner_0 PARTITION OF owner FOR VALUES WITH (MODULUS 3, REMAINDER 0);
CREATE TABLE owner_1 PARTITION OF owner FOR VALUES WITH (MODULUS 3, REMAINDER 1);
CREATE TABLE owner_2 PARTITION OF owner FOR VALUES WITH (MODULUS 3, REMAINDER 2);
--rollback drop table owner

--changeset demo:2
create table pet(
    id bigserial,
    name varchar not null,
    birthday date not null,
    owner_id bigint,
    primary key (id, birthday),
    foreign key (owner_id) references owner(id)
) partition by range(birthday);
CREATE TABLE pet_2008 PARTITION OF pet FOR VALUES FROM ('2008-01-01') TO ('2009-01-01');
CREATE TABLE pet_2009 PARTITION OF pet FOR VALUES FROM ('2009-01-01') TO ('2010-01-01');
CREATE TABLE pet_after2009 PARTITION OF pet FOR VALUES FROM ('2010-01-01') TO ('2110-01-01');
--rollback drop table pet

--changeset a.v.golikov:add_pet_full_name
create table pet_full_name(
    pet_id bigint primary key,
    full_name varchar not null
);
--rollback drop table full_name

--changeset author:populate_owner
insert into owner(id, name)
select generate_series(1,1000000), 'name' || generate_series(1,1000000);
--rollback DELETE FROM owner where id in (1,2,20)

--changeset author:populate_pet
insert into pet(id, name, owner_id, birthday)
select generate_series(1,10000000), 'name' || generate_series(1,10000000), null, DATE '2008-01-01' + (interval '1' day * (generate_series(1,10000000) / 10000));
--rollback DELETE FROM pet
