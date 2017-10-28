DROP SCHEMA IF EXISTS rds_mysql_db;

CREATE SCHEMA IF NOT EXISTS rds_mysql_db DEFAULT CHARACTER SET utf8;
USE rds_mysql_db;

CREATE TABLE IF NOT EXISTS rds_mysql_db.sp_user (
  user_id varchar(36) not null,
  email_address VARCHAR(100) NOT NULL,
  PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS rds_mysql_db.sp_user_relationship (
  user_id varchar(36) not null,
  friend_id varchar(36) not null,
  type varchar(10) not null,
  block varchar(1) NOT NULL,
  PRIMARY KEY (user_id, friend_id)
);
