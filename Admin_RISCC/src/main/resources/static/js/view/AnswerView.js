'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as AnswerController from "../controller/AnswerController.js";
import * as AnswerUI from "../ui/AnswerUI.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as UserController from "../controller/UserController.js";
import * as GeneralUtil from "../util/GeneralUtil.js";
import * as EndPoints from "../controller/EndPoints.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    AnswerController.listAnswer(AnswerUI.idTableAnswer);

    $(".custom_date_time_picker").daterangepicker({
        singleDatePicker: true,
        startDate: moment(),
        locale: {
            format: 'YYYY-MM-DD'
        }
    });

    $(AnswerUI.idTableAnswer).on('draw.dt', function () {

        // Delete Event Listener
        let deletes = AnswerUI.idTableAnswer.querySelectorAll(".answerDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                AnswerUI.idBtnAnswerDelete.dataset.answer_id = event.currentTarget.dataset.answer_id;
                $(AnswerUI.idModalAnswerDelete).modal("show");
            });
        });
    });

    // let loadUserInSelectPicker = function (pageNumber, pageSize) {
    //     let callback = function (data) {
    //         SelectPickerUtil.loadDataInSelectPicker(data.data.list, ['firstName', 'lastName', 'emailAddress'], AnswerUI.selectUser);
    //         loadUserPagination(data.currentPage, data.totalPage);
    //     };
    //     UserController.getUserWithLimitedFieldData(AnswerUI.selectUser, pageNumber, pageSize, callback);
    // };
    // loadUserInSelectPicker(0, 100);
    //
    // let loadUserPagination = function (currentPageNumber, totalPages) {
    //     GeneralUtil.empty(AnswerUI.idUserSelectPagination);
    //     for (let i = 1; i <= totalPages; i++) {
    //         let pageItem = document.createElement("li");
    //         pageItem.setAttribute("class", "page-item");
    //
    //         let pageLink = document.createElement("a");
    //         pageLink.setAttribute("class", "page-link");
    //         pageLink.setAttribute("href", "#");
    //         pageLink.textContent = i.toString();
    //
    //         if (currentPageNumber === i) {
    //             pageItem.classList.add("disabled");
    //             pageItem.classList.add("active");
    //             pageLink.setAttribute("tabindex", "-1");
    //         }
    //
    //         pageItem.appendChild(pageLink);
    //
    //         AnswerUI.idUserSelectPagination.appendChild(pageItem);
    //     }
    // };

    // $(AnswerUI.idUserSelectPagination).on("click", '.page-item', function (event) {
    //     if (event.target.text) {
    //         let clickedPageNumber = event.target.text;
    //         loadUserInSelectPicker(clickedPageNumber - 1, 100);
    //     }
    // });


    // SelectPickerUtil.populateSelectPicker(EndPoints.QUESTIONNAIRE_FIELDS, "title", AnswerUI.selectQuestionnaire);

    SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTIONNAIRE_FIELDS, "title", AnswerUI.selectQuestionnaire).then(() => {
        const selectElement = AnswerUI.selectQuestionnaire;
        if (!selectElement) {
            console.error("Dropdown not found");
            return;
        }

        const choices = new Choices(selectElement, {
            removeItemButton: true,
            searchEnabled: false,
            placeholderValue: "Select a Questionnaire",
            noChoicesText: "No options available",
        });

        selectElement.addEventListener("change", () => {
            const selectedValue = selectElement.value;
            console.log("Selected value:", selectedValue);

            if (!selectedValue) {
                AnswerController.listAnswer(AnswerUI.idTableAnswer);
            } else {
                null
            }
        });

        console.log("Dropdown and event listener successfully initialized");
    }).catch(error => console.error("Error initializing dropdown:", error));

    SelectPickerUtil.populateSelectPickerNew(EndPoints.USER, ['firstName', 'lastName', 'emailAddress'], AnswerUI.selectUser).then(() => {
        const selectElement = AnswerUI.selectUser;
        if (!selectElement) {
            console.error("Dropdown not found");
            return;
        }

        const choices = new Choices(selectElement, {
            removeItemButton: true,
            searchEnabled: false,
            placeholderValue: "Select a User",
            noChoicesText: "No options available",
        });

        selectElement.addEventListener("change", () => {
            const selectedValue = selectElement.value;
            console.log("Selected value:", selectedValue);

            if (!selectedValue) {
                AnswerController.listAnswer(AnswerUI.idTableAnswer);
            } else {
                null
            }
        });

        console.log("Dropdown and event listener successfully initialized");
    }).catch(error => console.error("Error initializing dropdown:", error));

    $(AnswerUI.idFormAnswerFilter).validate({
        rules: {},
        messages: {},
        submitHandler: function (form) {
            let api = EndPoints.ANSWER + "?";
            let risccCalculationApi = EndPoints.RISCC_CALCULATION + "?";

            let questionnaireId = $(AnswerUI.selectQuestionnaire).val();
            let userId = $(AnswerUI.selectUser).val();

            if (questionnaireId && questionnaireId !== "All") {
                api += "questionnaireIds=" + questionnaireId;
                risccCalculationApi += "questionnaireIds=" + questionnaireId;
            }
            if (userId && userId !== "All") {
                api += "&userIds=" + userId;
                risccCalculationApi  += "&userIds=" + userId;
            }

            AnswerController.listAnswer(AnswerUI.idTableAnswer, api);
            AnswerController.risccCalculation(AnswerUI.idTableRisccCalculation, risccCalculationApi);

        }
    });

    $(AnswerUI.idFormAnswerExport).validate({
        rules: {
            startDate: "required",
            endDate: "required"
        },
        message: {
            startDate: "Start Date required.",
            endDate: "End Date required."
        },
        submitHandler: function (form) {
            let api = EndPoints.ANSWER_EXPORT_DATE + "/" +
                new Date(form.elements["startDate"].value).valueOf() + "/" +
                new Date(form.elements["endDate"].value).valueOf();

            AnswerController.exportAnswer(AnswerUI.idFormAnswerFilter, api);
        }
    });


    //Delete
    AnswerUI.idBtnAnswerDelete.addEventListener("click", function (event) {
        AnswerController.deleteAnswer(AnswerUI.idModalAnswerDelete, event.currentTarget.dataset.answer_id, function () {
            AnswerController.listAnswer(AnswerUI.idTableAnswer);
        });
    });

    $(AnswerUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(AnswerUI.idModalAnswerDelete).modal("hide");
        $(AnswerUI.modalAlertBody).html("");
    });
});