use rectledb;

CREATE TABLE user
(
    `id` INT NOT NULL AUTO_INCREMENT,
    `login` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    PRIMARY KEY ( id )
);
