'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as QuestionnaireController from "../controller/QuestionnaireController.js";
import * as QuestionnaireUI from "../ui/QuestionnaireUI.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    if (!CommonUtil.hasAuthority("Questionnaire (Create)")) {
        $(QuestionnaireUI.idBtnPopAddQuestionnaire).hide();
    } else {
        QuestionnaireUI.idBtnPopAddQuestionnaire.addEventListener("click", function (event) {
            $(QuestionnaireUI.idFormQuestionnaireUpdate).hide();
            $(QuestionnaireUI.idFormQuestionnaireAdd).show();
            QuestionnaireUI.modalQuestionnaireTitle.textContent = "Add Questionnaire";
            $(QuestionnaireUI.idModalQuestionnaire).modal("show");
        });

        //Questionnaire add form submission
        $(QuestionnaireUI.idFormQuestionnaireAdd).validate({
            rules: {
                title: "required"
            },
            messages: {
                title: "Title required"
            },
            submitHandler: function (form) {
                let requestBody = {
                    "title": form.elements['title'].value
                };

                QuestionnaireController.addQuestionnaire(form, requestBody, function () {
                    QuestionnaireController.listQuestionnaire(QuestionnaireUI.idTableQuestionnaire);
                });
            }
        });
    }

    QuestionnaireController.listQuestionnaire(QuestionnaireUI.idTableQuestionnaire);

    $(QuestionnaireUI.idTableQuestionnaire).on('draw.dt', function () {

        //Edit Event Listener
        let edits = QuestionnaireUI.idTableQuestionnaire.querySelectorAll(".questionnaireEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let questionnaire = event.currentTarget.dataset.questionnaire;
                putValueInEditForm(JSON.parse(questionnaire));
                $(QuestionnaireUI.idFormQuestionnaireAdd).hide();
                $(QuestionnaireUI.idFormQuestionnaireUpdate).show();
                QuestionnaireUI.modalQuestionnaireTitle.textContent = "Edit Questionnaire";
                $(QuestionnaireUI.idModalQuestionnaire).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = QuestionnaireUI.idTableQuestionnaire.querySelectorAll(".questionnaireDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                QuestionnaireUI.idBtnQuestionnaireDelete.dataset.questionnaire_id = event.currentTarget.dataset.questionnaire_id;
                $(QuestionnaireUI.idModalQuestionnaireDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (questionnaire) {
        QuestionnaireUI.id.value = questionnaire.id;
        QuestionnaireUI.titleUpdate.value = questionnaire.title;
    };

    //Questionnaire Update Form submission
    $(QuestionnaireUI.idFormQuestionnaireUpdate).validate({
        rules: {
            title: "required"
        },
        messages: {
            title: "Title required"
        },
        submitHandler: function (form) {

            let requestBody = {
                "id": form.elements['id'].value,
                "title": form.elements["title"].value
            };

            QuestionnaireController.updateQuestionnaire(form, requestBody, function () {
                QuestionnaireController.listQuestionnaire(QuestionnaireUI.idTableQuestionnaire);
            });
        }
    });

    //Delete
    QuestionnaireUI.idBtnQuestionnaireDelete.addEventListener("click", function (event) {
        QuestionnaireController.deleteQuestionnaire(QuestionnaireUI.idModalQuestionnaireDelete, event.currentTarget.dataset.questionnaire_id, function () {
            QuestionnaireController.listQuestionnaire(QuestionnaireUI.idTableQuestionnaire);
        });
    });

    $(QuestionnaireUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(QuestionnaireUI.idModalQuestionnaire).modal("hide");
        $(QuestionnaireUI.idFormQuestionnaireAdd).trigger("reset");
        $(QuestionnaireUI.idFormQuestionnaireUpdate).trigger("reset");
        $(QuestionnaireUI.idModalQuestionnaireDelete).modal("hide");
        $(QuestionnaireUI.modalAlertBody).html("");
    });
});