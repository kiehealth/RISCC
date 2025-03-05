'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as AppAnalyticsKeyPairController from "../controller/AppAnalyticsKeyPairController.js";
import * as AppAnalyticsKeyPairUI from "../ui/AppAnalyticsKeyPairUI.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoint from "../controller/EndPoints.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    /*if (!CommonUtil.hasAuthority("App Analytics")) {
        $(AppAnalyticsKeyPairUI.idBtnPopAddAppAnalyticsKeyPair).hide();
    } else {
        AppAnalyticsKeyPairUI.idBtnPopAddAppAnalyticsKeyPair.addEventListener("click", function (event) {
            $(AppAnalyticsKeyPairUI.idFormAppAnalyticsKeyPairUpdate).hide();
            $(AppAnalyticsKeyPairUI.idFormAppAnalyticsKeyPairAdd).show();

            SelectPickerUtil.populateSelectPicker(EndPoint.APP_ANALYTICS_KEY_PUBLIC, "title", AppAnalyticsKeyPairUI.appAnalyticsKey1Add);
            SelectPickerUtil.populateSelectPicker(EndPoint.APP_ANALYTICS_KEY_PUBLIC, "title", AppAnalyticsKeyPairUI.appAnalyticsKey2Add);

            AppAnalyticsKeyPairUI.modalAppAnalyticsKeyPairTitle.textContent = "Add App Analytics Key Pair";
            $(AppAnalyticsKeyPairUI.idModalAppAnalyticsKeyPair).modal("show");
        });

        //App Analytics Key Pair add form submission
        $(AppAnalyticsKeyPairUI.idFormAppAnalyticsKeyPairAdd).validate({
            rules: {
                appAnalyticsKey1: "required",
                appAnalyticsKey2: "required"
            },
            messages: {
                appAnalyticsKey1: "Title 1 required.",
                appAnalyticsKey2: "Title 2 required"
            },
            submitHandler: function (form) {
                let requestBody = {
                    "appAnalyticsKeyOneId": $(AppAnalyticsKeyPairUI.appAnalyticsKey1Add).val(),
                    "appAnalyticsKeyTwoId": $(AppAnalyticsKeyPairUI.appAnalyticsKey2Add).val()
                };

                AppAnalyticsKeyPairController.addAppAnalyticsKeyPair(form, requestBody, function () {
                    AppAnalyticsKeyPairController.listAppAnalyticsKeyPair(AppAnalyticsKeyPairUI.idTableAppAnalyticsKeyPair);
                });
            }
        });
    }*/

    AppAnalyticsKeyPairController.listAppAnalyticsKeyPair(AppAnalyticsKeyPairUI.idTableAppAnalyticsKeyPair);

    $(AppAnalyticsKeyPairUI.idTableAppAnalyticsKeyPair).on('draw.dt', function () {

        //Edit Event Listener
        let edits = AppAnalyticsKeyPairUI.idTableAppAnalyticsKeyPair.querySelectorAll(".appAnalyticsKeyPairEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let appAnalyticsKeyPair = event.currentTarget.dataset.app_analytics_key_pair;
                putValueInEditForm(JSON.parse(appAnalyticsKeyPair));
                $(AppAnalyticsKeyPairUI.idFormAppAnalyticsKeyPairAdd).hide();
                $(AppAnalyticsKeyPairUI.idFormAppAnalyticsKeyPairUpdate).show();
                AppAnalyticsKeyPairUI.modalAppAnalyticsKeyPairTitle.textContent = "Edit App Analytics Key Pair";
                $(AppAnalyticsKeyPairUI.idModalAppAnalyticsKeyPair).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = AppAnalyticsKeyPairUI.idTableAppAnalyticsKeyPair.querySelectorAll(".appAnalyticsKeyPairDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                AppAnalyticsKeyPairUI.idBtnAppAnalyticsKeyPairDelete.dataset.app_analytics_key_pair_id = event.currentTarget.dataset.app_analytics_key_pair_id;
                $(AppAnalyticsKeyPairUI.idModalAppAnalyticsKeyPairDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (appAnalyticsKeyPair) {
        AppAnalyticsKeyPairUI.id.value = appAnalyticsKeyPair.id;
        SelectPickerUtil.populateSelectPicker(EndPoint.APP_ANALYTICS_KEY_PUBLIC, "title", AppAnalyticsKeyPairUI.appAnalyticsKey1Update, appAnalyticsKeyPair.appAnalyticsKeyOne.id);
        if (appAnalyticsKeyPair.appAnalyticsKeyTwo) {
            SelectPickerUtil.populateSelectPicker(EndPoint.APP_ANALYTICS_KEY_PUBLIC_FILTER + "titlesLike=OUT", "title", AppAnalyticsKeyPairUI.appAnalyticsKey2Update, appAnalyticsKeyPair.appAnalyticsKeyTwo.id);
        } else {
            SelectPickerUtil.populateSelectPicker(EndPoint.APP_ANALYTICS_KEY_PUBLIC_FILTER + "titlesLike=OUT", "title", AppAnalyticsKeyPairUI.appAnalyticsKey2Update);
        }
    };

    //App Analytics Key Update Form submission
    $(AppAnalyticsKeyPairUI.idFormAppAnalyticsKeyPairUpdate).validate({
        rules: {
            appAnalyticsKey1: "required",
            appAnalyticsKey2: "required"
        },
        messages: {
            appAnalyticsKey1: "Title 1 required.",
            appAnalyticsKey2: "Title 2 required"
        },
        submitHandler: function (form) {

            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "appAnalyticsKeyOneId": $(AppAnalyticsKeyPairUI.appAnalyticsKey1Update).val(),
                "appAnalyticsKeyTwoId": $(AppAnalyticsKeyPairUI.appAnalyticsKey2Update).val()
            };

            AppAnalyticsKeyPairController.updateAppAnalyticsKeyPair(form, requestBody, function () {
                AppAnalyticsKeyPairController.listAppAnalyticsKeyPair(AppAnalyticsKeyPairUI.idTableAppAnalyticsKeyPair);
            });
        }
    });


    //Delete
    AppAnalyticsKeyPairUI.idBtnAppAnalyticsKeyPairDelete.addEventListener("click", function (event) {
        AppAnalyticsKeyPairController.deleteAppAnalyticsKeyPair(AppAnalyticsKeyPairUI.idModalAppAnalyticsKeyPairDelete, event.currentTarget.dataset.app_analytics_key_pair_id, function () {
            AppAnalyticsKeyPairController.listAppAnalyticsKeyPair(AppAnalyticsKeyPairUI.idTableAppAnalyticsKeyPair);
        });
    });

    $(AppAnalyticsKeyPairUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(AppAnalyticsKeyPairUI.idModalAppAnalyticsKeyPair).modal("hide");
        $(AppAnalyticsKeyPairUI.idFormAppAnalyticsKeyPairAdd).trigger("reset");
        $(AppAnalyticsKeyPairUI.idFormAppAnalyticsKeyPairUpdate).trigger("reset");
        $(AppAnalyticsKeyPairUI.idModalAppAnalyticsKeyPairDelete).modal("hide");
        $(AppAnalyticsKeyPairUI.modalAlertBody).html("");
    });
});