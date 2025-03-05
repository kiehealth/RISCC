ALTER TABLE `tbl_group_questionnaire_answer_finished`
    ADD COLUMN `start_date_time` DATETIME NOT NULL,
    ADD COLUMN `end_date_time`   DATETIME NOT NULL;