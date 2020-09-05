CREATE TABLE `requests`
(
    `id`     integer     NOT NULL AUTO_INCREMENT,
    `root`   integer     NOT NULL,
    `date`   TIMESTAMP   NOT NULL,
    `status` varchar(50) NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = latin1;
