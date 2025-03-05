CREATE TABLE `tbl_welcome`
(
    `id`             BIGINT   NOT NULL AUTO_INCREMENT,
    `created_by`     BIGINT   NULL,
    `created_date`   DATETIME NOT NULL,
    `modified_by`    BIGINT   NULL,
    `modified_date`  DATETIME NOT NULL,
    `welcome_text`   TEXT     NOT NULL,
    `thank_you_text` TEXT     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;

INSERT INTO `tbl_welcome` (created_date, modified_date, welcome_text, thank_you_text)
VALUES (NOW(), NOW(),
        'Välkommen till STAV! Innan du börjar använda appen önskar vi att du besvarar ett antal frågor om din hälsa. Hela frågeformuläret tar mellan 10 - 15 minuter att besvara. Det är bra om du kan besvara hela formuläret vid ett och samma tillfälle. Skulle du emellertid behöva avbryta, kommer formuläret att vara öppet i en vecka så att du kan återuppta ditt svar där du slutade.',
        'Tack för att du tog tid att svara enkäten!');