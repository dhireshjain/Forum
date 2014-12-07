# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  id                        bigint not null,
  body                      varchar(1000) not null,
  question_id               bigint,
  upvotes                   bigint,
  downvotes                 bigint,
  time                      date,
  user_usn                  varchar(255),
  constraint pk_answer primary key (id))
;

create table comment (
  id                        bigint auto_increment not null,
  body                      varchar(1000) not null,
  time                      date,
  answer_id                 bigint,
  user_usn                  varchar(255),
  constraint uq_comment_body unique (body),
  constraint pk_comment primary key (id))
;

create table question (
  id                        bigint auto_increment not null,
  title                     varchar(1000) not null,
  body                      varchar(1000) not null,
  time                      date,
  user_usn                  varchar(255),
  subject_name              varchar(255),
  constraint uq_question_body unique (body),
  constraint pk_question primary key (id))
;

create table subject (
  name                      varchar(255) not null,
  number                    bigint,
  constraint pk_subject primary key (name))
;

create table users (
  usn                       varchar(255) not null,
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  first_name                varchar(255) not null,
  last_name                 varchar(255) not null,
  email                     varchar(255) not null,
  constraint uq_users_username unique (username),
  constraint uq_users_email unique (email),
  constraint pk_users primary key (usn))
;

create sequence answer_seq;

create sequence subject_seq;

create sequence users_seq;

alter table answer add constraint fk_answer_question_1 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_answer_question_1 on answer (question_id);
alter table answer add constraint fk_answer_user_2 foreign key (user_usn) references users (usn) on delete restrict on update restrict;
create index ix_answer_user_2 on answer (user_usn);
alter table comment add constraint fk_comment_answer_3 foreign key (answer_id) references answer (id) on delete restrict on update restrict;
create index ix_comment_answer_3 on comment (answer_id);
alter table comment add constraint fk_comment_user_4 foreign key (user_usn) references users (usn) on delete restrict on update restrict;
create index ix_comment_user_4 on comment (user_usn);
alter table question add constraint fk_question_user_5 foreign key (user_usn) references users (usn) on delete restrict on update restrict;
create index ix_question_user_5 on question (user_usn);
alter table question add constraint fk_question_subject_6 foreign key (subject_name) references subject (name) on delete restrict on update restrict;
create index ix_question_subject_6 on question (subject_name);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists answer;

drop table if exists comment;

drop table if exists question;

drop table if exists subject;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists answer_seq;

drop sequence if exists subject_seq;

drop sequence if exists users_seq;

