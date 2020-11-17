create sequence hibernate_sequence start with 1 increment by 1
create table pt_exercises (id bigint not null, uuid varchar(255), description blob, equipments blob, image varchar(255), last_update varchar(255), muscles blob, name varchar(255), video_urls blob, primary key (id))
alter table pt_exercises add constraint pt_exercises_index_uuid unique (uuid)
create sequence hibernate_sequence start with 1 increment by 1
create table pt_exercises (id bigint not null, uuid varchar(255), descriptions blob, equipments blob, image varchar(255), last_update varchar(255), muscles blob, name varchar(255), video_urls blob, primary key (id))
create table pt_muscles (id bigint not null, uuid varchar(255), primary key (id))
alter table pt_exercises add constraint pt_exercises_index_uuid unique (uuid)
alter table pt_muscles add constraint pt_muscles_index_uuid unique (uuid)
create sequence hibernate_sequence start with 1 increment by 1
create table exercise_muscles (exercise_id bigint not null, muscles integer)
create table pt_exercises (id bigint not null, uuid varchar(255), descriptions blob, equipments blob, image varchar(255), last_update varchar(255), name varchar(255), video_urls blob, primary key (id))
alter table pt_exercises add constraint pt_exercises_index_uuid unique (uuid)
alter table exercise_muscles add constraint FKi8nasvg193nf9ph2okwdkyeld foreign key (exercise_id) references pt_exercises
create sequence hibernate_sequence start with 1 increment by 1
create table exercise_muscles (exercise_id bigint not null, muscles integer)
create table pt_exercises (id bigint not null, uuid varchar(255), descriptions blob, equipments blob, image varchar(255), last_update varchar(255), name varchar(255), video_urls blob, primary key (id))
alter table pt_exercises add constraint pt_exercises_index_uuid unique (uuid)
alter table exercise_muscles add constraint FKi8nasvg193nf9ph2okwdkyeld foreign key (exercise_id) references pt_exercises
create sequence hibernate_sequence start with 1 increment by 1
create table exercise_muscles (exercise_id bigint not null, muscles integer)
create table pt_exercises (id bigint not null, uuid varchar(255), descriptions blob, equipments blob, image varchar(255), last_update varchar(255), name varchar(255), video_urls blob, primary key (id))
alter table pt_exercises add constraint pt_exercises_index_uuid unique (uuid)
alter table exercise_muscles add constraint FKi8nasvg193nf9ph2okwdkyeld foreign key (exercise_id) references pt_exercises
create sequence hibernate_sequence start with 1 increment by 1
create table exercise_muscles (exercise_id bigint not null, muscles integer)
create table pt_exercises (id bigint not null, uuid varchar(255), descriptions blob, equipments blob, image varchar(255), last_update varchar(255), name varchar(255), video_urls blob, primary key (id))
alter table pt_exercises add constraint pt_exercises_index_uuid unique (uuid)
alter table exercise_muscles add constraint FKi8nasvg193nf9ph2okwdkyeld foreign key (exercise_id) references pt_exercises
create sequence hibernate_sequence start with 1 increment by 1
create table exercise_muscles (exercise_id bigint not null, muscles integer)
create table pt_exercises (id bigint not null, uuid varchar(255), descriptions blob, equipments blob, image varchar(255), last_update varchar(255), name varchar(255), video_urls blob, primary key (id))
alter table pt_exercises add constraint pt_exercises_index_uuid unique (uuid)
alter table exercise_muscles add constraint FKi8nasvg193nf9ph2okwdkyeld foreign key (exercise_id) references pt_exercises
