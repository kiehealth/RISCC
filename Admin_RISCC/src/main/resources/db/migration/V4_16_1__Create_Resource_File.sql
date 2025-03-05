-- Create tbl_resource_file
CREATE TABLE `tbl_resource_file`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `title`         VARCHAR(256) NOT NULL,
    `description`   TEXT         NULL,
    `url`           VARCHAR(500) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;