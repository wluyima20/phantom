--
-- Add Copyright
--

CREATE TABLE location (
    id int (11) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO location(id,name) VALUES(1, 'Kampala');
