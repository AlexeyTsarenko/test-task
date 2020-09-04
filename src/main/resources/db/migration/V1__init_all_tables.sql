
CREATE TABLE `requests`
(
    `id`                     integer      NOT NULL AUTO_INCREMENT,
    `root_id`                integer      NOT NULL,
    `creation_date`          TIMESTAMP    NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = latin1;
