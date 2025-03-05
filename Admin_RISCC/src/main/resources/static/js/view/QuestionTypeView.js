'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as QuestionTypeController from "../controller/QuestionTypeController.js";
import * as QuestionTypeUI from "../ui/QuestionTypeUI.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    //Listing
    QuestionTypeController.listQuestionType(QuestionTypeUI.idTableQuestionType);

    $(QuestionTypeUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(QuestionTypeUI.modalAlertBody).html("");
    });
});