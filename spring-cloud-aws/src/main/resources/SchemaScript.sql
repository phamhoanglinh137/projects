DROP SCHEMA IF EXISTS rds_mysql_db;
CREATE SCHEMA IF NOT EXISTS rds_mysql_db DEFAULT CHARACTER SET utf8;
USE rds_mysql_db;

CREATE TABLE IF NOT EXISTS rds_mysql_db.app_address (
  id INT NOT NULL AUTO_INCREMENT,
  address VARCHAR(40) NOT NULL,
  postcode VARCHAR(8) NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS rds_mysql_db.app_customer_image (
  id INT NOT NULL AUTO_INCREMENT,
  s3_key VARCHAR(200) NOT NULL,
  url VARCHAR(1000) NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS rds_mysql_db.app_customer (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(30) NOT NULL,
  last_name VARCHAR(30) NOT NULL,
  date_of_birth DATE NOT NULL,
  phone VARCHAR(11) not null,
  email VARCHAR(100) not null, 
  customer_image_id INT NOT NULL,
  address_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_ADDRESS_ID
    FOREIGN KEY (address_id)
    REFERENCES rds_mysql_db.app_address (id),
  CONSTRAINT FK_CUSTOMER_IMAGE_ID
    FOREIGN KEY (customer_image_id)
    REFERENCES rds_mysql_db.app_customer_image (id));
