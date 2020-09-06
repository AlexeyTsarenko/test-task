CREATE TABLE `requests`
(
    `id`     integer     NOT NULL AUTO_INCREMENT,
    `root`   integer     NOT NULL,
    `date`   TIMESTAMP   NOT NULL,
    `status` varchar(50) NOT NULL,
    `client` integer     NOT NULL,
    `ticket` integer     NOT NULL,


    PRIMARY KEY (`id`)
)

