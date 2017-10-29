
create schema if not exists sp_group_db default character set utf8;
use sp_group_db;
drop table if exists sp_user;
drop table if exists sp_user_relationship;

CREATE TABLE sp_user (
  user_id varchar(36) not null,
  email_address VARCHAR(100) NOT NULL,
  PRIMARY KEY (user_id)
);

CREATE TABLE sp_user_relationship (
  user_id varchar(36) not null,
  friend_id varchar(36) not null,
  type varchar(10) not null,
  block varchar(1) NOT NULL,
  PRIMARY KEY (user_id, friend_id)
);
