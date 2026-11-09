CREATE DATABASE IF NOT EXISTS `employee-schema`
  CHARACTER SET utf8 COLLATE utf8_bin;

USE `employee-schema`;

CREATE TABLE employee (
  emp_id     INT(11) NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(45) DEFAULT NULL,
  last_name  VARCHAR(45) DEFAULT NULL,
  email_id   VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (emp_id)
) ENGINE=InnoDB;