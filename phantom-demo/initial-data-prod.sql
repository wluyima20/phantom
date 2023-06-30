--
-- Copyright (C) Amiyul LLC - All Rights Reserved
--
-- This source code is protected under international copyright law. All rights
-- reserved and protected by the copyright holder.
--
-- This file is confidential and only available to authorized individuals with the
-- permission of the copyright holder. If you encounter this file and do not have
-- permission, please contact the copyright holder and delete this file.
--

CREATE TABLE location (
    id int (11) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

INSERT INTO location(id,name) VALUES(1, 'Kampala');
