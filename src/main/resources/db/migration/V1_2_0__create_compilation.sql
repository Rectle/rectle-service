CREATE TABLE IF NOT EXISTS `compilation`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `create_date` TIMESTAMP NOT NULL ,
    `user_id` INT,
    `project_id` INT,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (project_id) REFERENCES project (id),
    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS `log`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `create_date` TIMESTAMP NOT NULL ,
    `text` VARCHAR(1000) NOT NULL,
    `compilation_id` INT,
    FOREIGN KEY (compilation_id) REFERENCES compilation (id),
    PRIMARY KEY ( id )
);
