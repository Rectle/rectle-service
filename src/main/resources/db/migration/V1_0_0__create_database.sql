CREATE TABLE IF NOT EXISTS `team`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `create_date` TIMESTAMP NOT NULL,
    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS `user`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `provider` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(100),
    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS `membership`
(
    `user_id` INT NOT NULL,
    `team_id` INT NOT NULL,
    PRIMARY KEY ( user_id, team_id ),
    FOREIGN KEY (team_id) REFERENCES team (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS `project`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `create_date` TIMESTAMP NOT NULL,
    `team_id` INT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES team (id),
    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS `model`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `create_date` TIMESTAMP NOT NULL,
    `user_id` INT NOT NULL,
    `project_id` INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (project_id) REFERENCES project (id),
    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS `compilation`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `create_date` TIMESTAMP NOT NULL ,
    `score` VARCHAR(100) NOT NULL,
    `model_id` INT NOT NULL,
    FOREIGN KEY (model_id) REFERENCES model (id),
    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS `log`
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `create_date` TIMESTAMP NOT NULL ,
    `text` VARCHAR(1000) NOT NULL,
    `compilation_id` INT NOT NULL,
    FOREIGN KEY (compilation_id) REFERENCES compilation (id),
    PRIMARY KEY ( id )
);
