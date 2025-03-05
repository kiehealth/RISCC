CREATE TABLE `tbl_setting`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `key_title`     VARCHAR(256) NOT NULL,
    `key_value`     VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;

INSERT INTO `tbl_setting` (created_date, modified_date, key_title, key_value)
VALUES (NOW(), NOW(), 'BULKSMS_TOKEN_ID', '6FCB3EDB5D1747128266590A612EBD5B-02-C'),
       (NOW(), NOW(), 'BULKSMS_TOKEN_SECRET', 'tw5wQkFnp2DCa6mAPqI4Y9oy1rEG8');