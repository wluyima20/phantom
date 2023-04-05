--
-- Add Copyright
--

CREATE TABLE person (
    id int (11) NOT NULL AUTO_INCREMENT,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO person(id,first_name,last_name) VALUES(1, 'John', 'Doe');
