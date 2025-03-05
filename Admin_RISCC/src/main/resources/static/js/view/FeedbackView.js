'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as FeedbackController from "../controller/FeedbackController.js";
import * as FeedbackUI from "../ui/FeedbackUI.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    //Listing
    FeedbackController.listFeedback(FeedbackUI.idTableFeedback);

    $(FeedbackUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(FeedbackUI.modalAlertBody).html("");
    });
});