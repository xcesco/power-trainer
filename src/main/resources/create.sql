create sequence hibernate_sequence start with 1 increment by 1
create table pt_exercises (id bigint not null, uuid varchar(255), description blob, equipments blob, image varchar(255), last_update varchar(255), muscles blob, name varchar(255), video_urls blob, primary key (id))
alter table pt_exercises add constraint pt_exercises_index_uuid unique (uuid)
