'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as SettingController from "../controller/SettingController.js";
import * as SettingUI from "../ui/SettingUI.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";

$(document).ready(function () {
    CommonUtil.initialSetup();


    //App Version
    SettingController.listAppVersion(SettingUI.idTableAppVersion);
    $(SettingUI.idTableAppVersion).on('draw.dt', function () {
        let appVersionEdit = SettingUI.idTableAppVersion.querySelectorAll(".appVersionEdit");
        Array.from(appVersionEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let appVersion = event.currentTarget.dataset.app_version;
                putValueInAppVersionEditForm(JSON.parse(appVersion));
                $(SettingUI.idFormAppVersionUpdate).show();
                SettingUI.modalAppVersionTitle.textContent = "Edit App Version";
                $(SettingUI.idModalAppVersion).modal("show");
            });
        });
    });

    let putValueInAppVersionEditForm = function (appVersion) {
        SettingUI.idAppVersion.value = appVersion.id;
        SettingUI.familyUpdate.value = appVersion.family;
        SettingUI.versionUpdate.value = appVersion.version;
        if (appVersion.forceUpdate != null) {
            SettingUI.forceUpdateUpdate.forEach(function (item) {
                if ((item.value === "true") === appVersion.forceUpdate) {
                    item.checked = true;
                }
            });
        }
        if (appVersion.url) {
            SettingUI.urlUpdate.value = appVersion.url;
        }
    };

    $(SettingUI.idFormAppVersionUpdate).validate({
        rules: {
            family: "required",
            version: "required"
        },
        messages: {
            family: "Family required.",
            version: "Version required"
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.elements['id'].value,
                "family": form.elements['family'].value,
                "version": form.elements['version'].value,
                "forceUpdate": form.elements["forceUpdate"].value
            };
            if (form.elements["url"].value) {
                requestBody.url = form.elements["url"].value;
            }

            SettingController.updateAppVersion(form, requestBody, function () {
                SettingController.listAppVersion(SettingUI.idTableAppVersion);
            });
        }
    });


    //Setting
    SettingController.listSetting(SettingUI.idTableSetting);
    $(SettingUI.idTableSetting).on('draw.dt', function () {
        let settingEdit = SettingUI.idTableSetting.querySelectorAll(".settingEdit");
        Array.from(settingEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let setting = event.currentTarget.dataset.setting;
                putValueInSettingEditForm(JSON.parse(setting));
                $(SettingUI.idFormSettingUpdate).show();
                SettingUI.modalSettingTitle.textContent = "Edit Setting";
                $(SettingUI.idModalSetting).modal("show");
            });
        });
    });

    let putValueInSettingEditForm = function (setting) {
        SettingUI.idSetting.value = setting.id;
        SettingUI.keyTitleUpdate.value = setting.keyTitle;
        SettingUI.keyValueUpdate.value = setting.keyValue;
    };

    $(SettingUI.idFormSettingUpdate).validate({
        rules: {
            keyTitle: "required"
        },
        messages: {
            keyTitle: "Value required."
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.elements['id'].value,
                "keyValue": form.elements['keyValue'].value
            };

            SettingController.updateSetting(form, requestBody, function () {
                SettingController.listSetting(SettingUI.idTableSetting);
            });
        }
    });


    //About Us
    SettingController.listAboutUs(SettingUI.idTableAboutUs);
    $(SettingUI.idTableAboutUs).on('draw.dt', function () {
        let aboutUsEdit = SettingUI.idTableAboutUs.querySelectorAll(".aboutUsEdit");
        Array.from(aboutUsEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let aboutUs = event.currentTarget.dataset.about_us;
                putValueInAboutUsEditForm(JSON.parse(aboutUs));
                $(SettingUI.idFormAboutUsUpdate).show();
                SettingUI.modalAboutUsTitle.textContent = "Edit About Us";
                $(SettingUI.idModalAboutUs).modal("show");
            });
        });
    });

    let putValueInAboutUsEditForm = function (aboutUs) {
        SettingUI.idAboutUs.value = aboutUs.id;
        SettingUI.nameUpdate.value = aboutUs.name;
        SettingUI.phoneUpdate.value = aboutUs.phone;
        SettingUI.emailUpdate.value = aboutUs.email;
    };

    $(SettingUI.idFormAboutUsUpdate).validate({
        rules: {
            name: "required",
            phone: "required",
            email: "required"
        },
        messages: {
            name: "Name required.",
            phone: "Phone required.",
            email: "Email required."
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.elements['id'].value,
                "name": form.elements['name'].value,
                "phone": form.elements['phone'].value,
                "email": form.elements['email'].value
            };

            SettingController.updateAboutUs(form, requestBody, function () {
                SettingController.listAboutUs(SettingUI.idTableAboutUs);
            });
        }
    });


    //Welcome
    SettingController.listWelcome(SettingUI.idTableWelcome);
    $(SettingUI.idTableWelcome).on('draw.dt', function () {
        let welcomeEdit = SettingUI.idTableWelcome.querySelectorAll(".welcomeEdit");
        Array.from(welcomeEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let welcome = event.currentTarget.dataset.welcome;
                putValueInWelcomeEditForm(JSON.parse(welcome));
                $(SettingUI.idFormWelcomeUpdate).show();
                SettingUI.modalWelcomeTitle.textContent = "Edit Welcome";
                $(SettingUI.idModalWelcome).modal("show");
            });
        });
    });

    let putValueInWelcomeEditForm = function (welcome) {
        SettingUI.idWelcome.value = welcome.id;
        SettingUI.welcomeTextUpdate.value = welcome.welcomeText || "";
        SettingUI.welcomeTextSwedishUpdate.value = welcome.welcomeTextSwedish || "";
        SettingUI.welcomeTextSpanishUpdate.value = welcome.welcomeTextSpanish || "";
        SettingUI.thankYouTextUpdate.value = welcome.thankYouText || "";
        SettingUI.thankYouTextSwedishUpdate.value = welcome.thankYouTextSwedish || "";
        SettingUI.thankYouTextSpanishUpdate.value = welcome.thankYouTextSpanish || "";
    };

    $(SettingUI.idFormWelcomeUpdate).validate({
        rules: {},
        messages: {},
        submitHandler: function (form) {
            let requestBody = {
                "id": form.elements['id'].value,
                "welcomeText": form.elements['welcomeText'].value,
                "welcomeTextSwedish": form.elements['welcomeTextSwedish'].value,
                "welcomeTextSpanish": form.elements['welcomeTextSpanish'].value,
                "thankYouText": form.elements['thankYouText'].value,
                "thankYouTextSwedish": form.elements['thankYouTextSwedish'].value,
                "thankYouTextSpanish": form.elements['thankYouTextSpanish'].value
            };

            SettingController.updateWelcome(form, requestBody, function () {
                SettingController.listWelcome(SettingUI.idTableWelcome);
            });
        }
    });


    //Resource File
    if (!CommonUtil.hasAuthority("Setting (Update)")) {
        $(SettingUI.idBtnPopAddResourceFile).hide();
    } else {
        SettingUI.idBtnPopAddResourceFile.addEventListener("click", function (event) {
            $(SettingUI.idFormResourceFileUpdate).hide();
            $(SettingUI.idFormResourceFileAdd).show();
            $(SettingUI.modalResourceFileTitle).textContent = "Add Resource File";
            $(SettingUI.idModalResourceFile).modal("show");
        });

        $(SettingUI.idFormResourceFileAdd).validate({
            rules: {
                title: "required",
                resourceFile: "required"
            },
            messages: {
                title: "Title required.",
                resourceFile: "Resource File required."
            },
            submitHandler: function (form) {
                let data = new FormData();
                data.append("title", form.elements["title"].value);
                data.append("resourceFile", form.elements["resourceFile"].files[0]);

                if (form.elements["description"].value) {
                    data.append("description", form.elements["description"].value);
                }

                SettingController.addResourceFile(form, data, function () {
                    SettingController.listResourceFile(SettingUI.idTableResourceFile);
                });
            }
        });
    }


    SettingController.listResourceFile(SettingUI.idTableResourceFile);
    $(SettingUI.idTableResourceFile).on('draw.dt', function () {

        let resourceFileEdit = SettingUI.idTableResourceFile.querySelectorAll(".resourceFileEdit");
        Array.from(resourceFileEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let resourceFile = event.currentTarget.dataset.resource_file;
                putValueInResourceFileEditForm(JSON.parse(resourceFile));
                $(SettingUI.idFormResourceFileAdd).hide();
                $(SettingUI.idFormWelcomeUpdate).show();
                SettingUI.modalResourceFileTitle.textContent = "Edit Resource File";
                $(SettingUI.idModalResourceFile).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = SettingUI.idTableResourceFile.querySelectorAll(".resourceFileDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                SettingUI.idBtnResourceFileDelete.dataset.resource_file_id = event.currentTarget.dataset.resource_file_id;
                $(SettingUI.idModalResourceFileDelete).modal("show");
            });
        });
    });

    let putValueInResourceFileEditForm = function (resourceFile) {
        SettingUI.idResourceFile.value = resourceFile.id;
        SettingUI.resourceFileTitleUpdate.value = resourceFile.title;
        if (resourceFile.description) {
            SettingUI.resourceFileDescriptionUpdate.value = resourceFile.description;
        }
    };

    $(SettingUI.idFormResourceFileUpdate).validate({
        rules: {
            title: "required"
        },
        messages: {
            title: "Title required."
        },
        submitHandler: function (form) {
            let data = new FormData();
            data.append("id", form.elements["id"].value);
            data.append("title", form.elements["title"].value);

            if (form.elements["resourceFile"].files[0]) {
                data.append("resourceFile", form.elements["resourceFile"].files[0]);
            }
            if (form.elements["description"].value) {
                data.append("description", form.elements["description"].value);
            }

            SettingController.updateResourceFile(form, data, function () {
                SettingController.listResourceFile(SettingUI.idTableResourceFile);
            });
        }
    });

    //Delete
    SettingUI.idBtnResourceFileDelete.addEventListener("click", function (event) {
        SettingController.deleteResourceFile(SettingUI.idModalResourceFileDelete, event.currentTarget.dataset.resource_file_id, function () {
            SettingController.listResourceFile(SettingUI.idTableResourceFile);
        });
    });

    // RISCC Range
    if (!CommonUtil.hasAuthority("Setting (Update)")) {
        $(SettingUI.idBtnPopAddResourceFile).hide();
    } else {
        SettingUI.idBtnPopAddRisccRange.addEventListener("click", function (event) {
            $(SettingUI.idFormRisccRangeUpdate).hide();
            $(SettingUI.idFormRisccRangeAdd).show();
            SelectPickerUtil.populateSelectPicker(EndPoints.QUESTIONNAIRE_FIELDS, "title", SettingUI.questionnaireAdd);
            SettingUI.modalRisccRangeTitle.textContent = "Add RISCC Range";
            $(SettingUI.idModalRisccValueRange).modal("show");
        });

        $(SettingUI.idFormRisccRangeAdd).validate({
            rules: {
                // questionnaire: "requested",
                toValue: "required",
                fromValue: "required",
                message: "required"
            },
            messages: {
                // questionnaire: "Questionnaire value required",
                toValue: "toValue required.",
                fromValue: "fromValue required.",
                message: "message required."
            },
            submitHandler: function (form) {
                let requestBody ={
                    "questionaryId" : $(SettingUI.questionnaireAdd).val(),
                    "fromValue" : form.querySelector("input[name='fromValue']").value,
                    "toValue" : form.querySelector("input[name='toValue']").value,
                    "message" : form.querySelector("textarea[name='message']").value,
                    "moreInfo" : form.querySelector("textarea[name='moreInfo']").value
                };
                SettingController.addRisccRange(form, requestBody, function () {
                    SettingController.listRisccRange(SettingUI.idTableRisccValueRange);
                });
            }
        });
    }

    SettingController.listRisccRange(SettingUI.idTableRisccValueRange);
    $(SettingUI.idTableRisccValueRange).on('draw.dt', function () {

        let risccRangeEdit = SettingUI.idTableRisccValueRange.querySelectorAll(".risccRangeEdit");
        Array.from(risccRangeEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let risccRange = event.currentTarget.dataset.riscc_range;
                putValueInRisccRanceEditForm(JSON.parse(risccRange));
                $(SettingUI.idFormRisccRangeAdd).hide();
                $(SettingUI.idFormRisccRangeUpdate).show();
                SettingUI.modalRisccRangeTitle.textContent = "Edit Riscc Range";
                $(SettingUI.idModalRisccValueRange).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = SettingUI.idTableRisccValueRange.querySelectorAll(".risccRangeDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                SettingUI.idBtnRisccRangeDelete.dataset.riscc_range_id = event.currentTarget.dataset.riscc_range_id;
                $(SettingUI.idModalRisccRangeDelete).modal("show");
            });
        });
    });

    let putValueInRisccRanceEditForm = function (risccRange) {
        SelectPickerUtil.populateSelectPicker(EndPoints.QUESTIONNAIRE_FIELDS, "title", SettingUI.questionnaireUpdate,risccRange.questionnaireRes.id);
        SettingUI.idRisccRange.value = risccRange.id;
        SettingUI.risccFromValueUpdate.value = risccRange.fromValue;
        SettingUI.riscctoValueUpdate.value= risccRange.toValue;
        SettingUI.risccRangeMessageUpdate.value =risccRange.message;
        SettingUI.risccRangeMoreInfo.value=risccRange.moreInfo;
    };

    $(SettingUI.idFormRisccRangeUpdate).validate({
        rules: {
            toValue: "required",
            fromValue: "required",
            message: "required"
        },
        messages: {
            toValue: "toValue required.",
            fromValue: "fromValue required.",
            message: "message required."
        },

        submitHandler: function (form) {
            let requestBody ={
                   "id": form.querySelector("input[name='id']").value,
                   "questionaryId" : $(SettingUI.questionnaireUpdate).val(),
                   "fromValue" : form.querySelector("input[name='fromValue']").value,
                   "toValue" : form.querySelector("input[name='toValue']").value,
                   "message" : form.querySelector("textarea[name='message']").value,
                    "moreInfo": form.querySelector("textarea[name='moreInfo']").value
               };
                SettingController.updateRisccRange(form, requestBody, function () {
                SettingController.listRisccRange(SettingUI.idTableRisccValueRange);
            });
        }
    });

    //Delete
    SettingUI.idBtnRisccRangeDelete.addEventListener("click", function (event) {
        SettingController.deleteRisccRange(SettingUI.idModalRisccRangeDelete, event.currentTarget.dataset.riscc_range_id, function () {
            SettingController.listRisccRange(SettingUI.idTableRisccValueRange);
        });
    });

    $(SettingUI.idModalAlert).on("hidden.bs.modal", function (e) {

        $(SettingUI.idModalAppVersion).modal("hide");
        $(SettingUI.idFormAppVersionUpdate).trigger("reset");

        $(SettingUI.idModalSetting).modal("hide");
        $(SettingUI.idFormSettingUpdate).trigger("reset");

        $(SettingUI.idModalAboutUs).modal("hide");
        $(SettingUI.idFormAboutUsUpdate).trigger("reset");

        $(SettingUI.idModalWelcome).modal("hide");
        $(SettingUI.idFormWelcomeUpdate).trigger("reset");

        $(SettingUI.idModalResourceFile).modal("hide");
        $(SettingUI.idFormResourceFileUpdate).trigger("reset");

        $(SettingUI.idModalRisccValueRange).modal("hide");
        $(SettingUI.idModalRisccRangeDelete).modal("hide");


        $(SettingUI.modalAlertBody).html("");
    });
});