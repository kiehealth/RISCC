'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as AppAnalyticsKeyController from "../controller/AppAnalyticsKeyController.js";
import * as AppAnalyticsKeyUI from "../ui/AppAnalyticsKeyUI.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    if (!CommonUtil.hasAuthority("App Analytics")) {
        $(AppAnalyticsKeyUI.idBtnPopAddAppAnalyticsKey).hide();
    } else {
        AppAnalyticsKeyUI.idBtnPopAddAppAnalyticsKey.addEventListener("click", function (event) {
            $(AppAnalyticsKeyUI.idFormAppAnalyticsKeyUpdate).hide();
            $(AppAnalyticsKeyUI.idFormAppAnalyticsKeyAdd).show();
            AppAnalyticsKeyUI.modalAppAnalyticsKeyTitle.textContent = "Add App Analytics Key";
            $(AppAnalyticsKeyUI.idModalAppAnalyticsKey).modal("show");
        });

        //App Analytics Key add form submission
        $(AppAnalyticsKeyUI.idFormAppAnalyticsKeyAdd).validate({
            rules: {
                title: "required",
                appAnalyticsKeyDataType: "required"
            },
            messages: {
                title: "Title required.",
                appAnalyticsKeyDataType: "App Analytics Key Data Type required"
            },
            submitHandler: function (form) {
                let requestBody = {
                    "title": form.elements["title"].value,
                    "appAnalyticsKeyDataType": $(AppAnalyticsKeyUI.appAnalyticsKeyDataTypeAdd).val()
                };
                if (form.elements["description"].value) {
                    requestBody.description = form.elements["description"].value;
                }

                AppAnalyticsKeyController.addAppAnalyticsKey(form, requestBody, function () {
                    AppAnalyticsKeyController.listAppAnalyticsKey(AppAnalyticsKeyUI.idTableAppAnalyticsKey);
                    $(AppAnalyticsKeyUI.appAnalyticsKeyDataTypeAdd).val("DATETIME");
                    // $(AppAnalyticsKeyUI.appAnalyticsKeyDataTypeAdd).selectpicker("refresh");
                });
            }
        });
    }

    AppAnalyticsKeyController.listAppAnalyticsKey(AppAnalyticsKeyUI.idTableAppAnalyticsKey);

    $(AppAnalyticsKeyUI.idTableAppAnalyticsKey).on('draw.dt', function () {

        //Edit Event Listener
        let edits = AppAnalyticsKeyUI.idTableAppAnalyticsKey.querySelectorAll(".appAnalyticsKeyEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let appAnalyticsKey = event.currentTarget.dataset.app_analytics_key;
                putValueInEditForm(JSON.parse(appAnalyticsKey));
                $(AppAnalyticsKeyUI.idFormAppAnalyticsKeyAdd).hide();
                $(AppAnalyticsKeyUI.idFormAppAnalyticsKeyUpdate).show();
                AppAnalyticsKeyUI.modalAppAnalyticsKeyTitle.textContent = "Edit App Analytics Key";
                $(AppAnalyticsKeyUI.idModalAppAnalyticsKey).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = AppAnalyticsKeyUI.idTableAppAnalyticsKey.querySelectorAll(".appAnalyticsKeyDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                AppAnalyticsKeyUI.idBtnAppAnalyticsKeyDelete.dataset.app_analytics_key_id = event.currentTarget.dataset.app_analytics_key_id;
                $(AppAnalyticsKeyUI.idModalAppAnalyticsKeyDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (appAnalyticsKey) {
        AppAnalyticsKeyUI.id.value = appAnalyticsKey.id;
        AppAnalyticsKeyUI.titleUpdate.value = appAnalyticsKey.title;
        $(AppAnalyticsKeyUI.appAnalyticsKeyDataTypeUpdate).selectpicker("val", appAnalyticsKey.appAnalyticsKeyDataType);
        if (appAnalyticsKey.description) {
            AppAnalyticsKeyUI.descriptionUpdate.value = appAnalyticsKey.description;
        }
    };

    //App Analytics Key Update Form submission
    $(AppAnalyticsKeyUI.idFormAppAnalyticsKeyUpdate).validate({
        rules: {
            title: "required",
            appAnalyticsKeyDataType: "required"
        },
        messages: {
            title: "Title required.",
            appAnalyticsKeyDataType: "App Analytics Key Data Type required"
        },
        submitHandler: function (form) {

            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "title": form.elements["title"].value,
                "appAnalyticsKeyDataType": $(AppAnalyticsKeyUI.appAnalyticsKeyDataTypeUpdate).val()
            };
            if (form.elements["description"].value) {
                requestBody.description = form.elements["description"].value;
            }

            AppAnalyticsKeyController.updateAppAnalyticsKey(form, requestBody, function () {
                AppAnalyticsKeyController.listAppAnalyticsKey(AppAnalyticsKeyUI.idTableAppAnalyticsKey);
            });
        }
    });


    //Delete
    AppAnalyticsKeyUI.idBtnAppAnalyticsKeyDelete.addEventListener("click", function (event) {
        AppAnalyticsKeyController.deleteAppAnalyticsKey(AppAnalyticsKeyUI.idModalAppAnalyticsKeyDelete, event.currentTarget.dataset.app_analytics_key_id, function () {
            AppAnalyticsKeyController.listAppAnalyticsKey(AppAnalyticsKeyUI.idTableAppAnalyticsKey);
        });
    });

    $(AppAnalyticsKeyUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(AppAnalyticsKeyUI.idModalAppAnalyticsKey).modal("hide");
        $(AppAnalyticsKeyUI.idFormAppAnalyticsKeyAdd).trigger("reset");
        $(AppAnalyticsKeyUI.idFormAppAnalyticsKeyUpdate).trigger("reset");
        $(AppAnalyticsKeyUI.idModalAppAnalyticsKeyDelete).modal("hide");
        $(AppAnalyticsKeyUI.modalAlertBody).html("");
    });
});