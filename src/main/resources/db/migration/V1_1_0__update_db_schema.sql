CREATE TABLE IF NOT EXISTS `project`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `create_date` TIMESTAMP NOT NULL ,
    `name` VARCHAR(100) NOT NULL,
    `user_id` INT,
    FOREIGN KEY (user_id) REFERENCES user (id),
    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS `model`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `user_id` INT,
    `project_id` INT,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (project_id) REFERENCES project (id),
    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS `score`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `score` VARCHAR(100) NOT NULL,
    `score_date` TIMESTAMP NOT NULL ,
    `user_id` INT,
    `project_id` INT,
    `model_id` INT,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (project_id) REFERENCES project (id),
    FOREIGN KEY (model_id) REFERENCES model (id),
    PRIMARY KEY ( id )
);
