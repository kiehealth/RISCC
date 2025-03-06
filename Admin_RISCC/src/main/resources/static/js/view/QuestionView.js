'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as QuestionController from "../controller/QuestionController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";
import * as QuestionUI from "../ui/QuestionUI.js";

$(document).ready(function () {
    CommonUtil.initialSetup();
    $(QuestionUI.idOptionContainerAdd).hide();
    SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTIONNAIRE_FIELDS, "title", QuestionUI.questionnaireFilter)
        .then(() => {
            const selectElement = QuestionUI.idTableQuestion;
            if(!selectElement) {
                console.error("Dropdown not found");
                return;
            }

            const choices = new Choices(selectElement, {
                removeItemButton: true,
                searchEnabled: false,
                placeholderValue: "Select a TITLE",
                noChoicesText: "No TITLE options available",
            })

            selectElement.addEventListener("change", () => {
                const selectedValue = selectElement.value;
                console.log("Selected value:", selectedValue);

                if (!selectedValue){
                    QuestionController.listQuestion(QuestionUI.idTableQuestion);
                } else {
                    null
                }
            });

            console.log("Dropdown and event listener successfully initialized");
        }).catch(error => console.error("Error initializing dropdown:", error));

    if (!CommonUtil.hasAuthority("Question (Create)")) {
        $(QuestionUI.idBtnPopAddQuestion).hide();
    } else {
        QuestionUI.idBtnPopAddQuestion.addEventListener("click", function (event) {
            $(QuestionUI.idQuestionShow).hide();
            $(QuestionUI.idFormQuestionUpdate).hide();
            $(QuestionUI.idFormQuestionImport).hide();
            $(QuestionUI.idFormQuestionAdd).show();
            SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTION_TYPE, "title", QuestionUI.questionTypeAdd)
                .then(() => {
                    const selectElement = QuestionUI.idBtnPopAddQuestion;

                    if(!selectElement) {
                        console.error("Dropdown not found");
                        return;
                    }

                    if (selectElement){
                        const choices = new Choices(selectElement, {
                            removeItemButton: true,
                            searchEnabled: false,
                            placeholderValue: "Select a QUESTION TYPE",
                            noChoicesText: "NO QUESTION options available",
                        })
                    } else {
                        console.error("SelectElement not found in DOM.")
                    }


                    selectElement.addEventListener("change", () => {
                        const selectedValue = selectElement.value;
                        console.log("Selected value:", selectedValue);

                        if (!selectedValue){
                            QuestionController.listQuestion(QuestionUI.idTableQuestion);
                        } else {
                            null
                        }
                    });

                    console.log("Dropdown and event listener successfully initialized");
                }).catch(error => console.error("Error initializing dropdown:", error));


            SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTIONNAIRE_FIELDS,
                "title", QuestionUI.questionnaireAdd)
                .then(() => {
                    const selectElement = QuestionUI.idTableQuestion;

                    if(!selectElement){
                        console.error("Dropdown not found");
                        return;
                    }

                    const choices = new Choices(selectElement, {
                        removeItemButton: true,
                        searchEnabled: false,
                        placeholderValue: "Select a QUESTIONNAIRE",
                        noChoicesText: "No QUESTIONNAIRE options available",
                    })

                    selectElement.addEventListener("change", () => {
                        const selectedValue = selectElement.value;
                        console.log("Selected value:", selectedValue);

                        if(!selectedValue){
                            QuestionController.listQuestion(QuestionUI.idTableQuestion);
                        } else {
                            null
                        }
                    });
                    console.log("Dropdown and event listener successfully initialized");
                }).catch(error => console.error("Error initializing dropdown:", error));

            QuestionUI.modalQuestionTitle.textContent = "Add Question";
            $(QuestionUI.idModalQuestion).modal("show");
        })

        QuestionUI.questionTypeAdd.addEventListener("change", function (event) {
            let selectedOption = event.currentTarget.options[event.currentTarget.options.selectedIndex];
            let selectedOptionText = selectedOption.text;
            if (selectedOptionText === "SELECT_MORE_THAN_ONE" || selectedOptionText === "SELECT_ONE" || selectedOptionText === "RATING") {
                $(QuestionUI.idOptionContainerAdd).show("fast");
            } else {
                $(QuestionUI.idOptionContainerAdd).hide("fast");
            }
        });

        //Add Option
        QuestionUI.idBtnAddOptionAdd.addEventListener("click", function (event) {
            event.preventDefault();
            let div = document.createElement("div");
            div.setAttribute("class", "questionOption border border-dark rounded container m-2");
            div.innerHTML = "<div class='row p-1'><input class='form-control col-sm-4' type='text' name='optionTitle' placeholder='Title'>" +
                "<input class='form-control col-sm-2' type='number' name='optionValue' placeholder='Value'>" +
                "<textarea class='form-control col-sm-5' name='optionMessage' placeholder='Email body message'></textarea>" +
                "<button class='btn btn-link removeOption'><i class='fas fa-times-circle color-red'></i></button></div>";

            /* --------------------- RISCC Value ------------------------------ */
             let risccValueDiv = document.createElement("div");
             risccValueDiv.classList.add("row");

            // RISCC Value label
            let risccLabel = document.createElement("label");
            risccLabel.classList.add("col-form-label","col-sm-3");
            risccLabel.textContent = "RISCC Value:";
            risccValueDiv.appendChild(risccLabel);

            //
            let risccValue = document.createElement("input");
            risccValue.classList.add("form-control", "col-sm-8","ml-3");
            risccValue.setAttribute("name","risccValue");
            risccValue.setAttribute("placeholder", "riscc value");
            risccValue.setAttribute("type","number");
            risccValueDiv.appendChild(risccValue);
            div.appendChild(risccValueDiv);

            /* -------------------------- Start Notify User Div ----------------------------------------- */
            let notifyUserDiv = document.createElement("div");
            notifyUserDiv.classList.add("row");

            //Label
            let notifyUserLabel = document.createElement("label");
            notifyUserLabel.classList.add("col-form-label", "col-sm-3");
            notifyUserLabel.textContent = "Notify User:";
            notifyUserDiv.appendChild(notifyUserLabel);

            //SelectPicker
            let notifyUserSelectPicker = document.createElement("select");
            notifyUserSelectPicker.classList.add("col-sm-8");
            notifyUserSelectPicker.setAttribute("title", "Notify User");
            notifyUserSelectPicker.setAttribute("name", "notifyUser");
            notifyUserDiv.appendChild(notifyUserSelectPicker);

            let option1 = document.createElement("option");
            option1.setAttribute("value", "YES");
            option1.textContent = "Yes";
            notifyUserSelectPicker.appendChild(option1);

            let option2 = document.createElement("option");
            option2.setAttribute("value", "NO");
            option2.textContent = "No";

            notifyUserSelectPicker.appendChild(option2);

            div.appendChild(notifyUserDiv);
            /* -------------------------- End Notify User Div ----------------------------------------- */


            /* -------------------------- Start Notify Other Div ----------------------------------------- */
            let notifyOtherDiv = document.createElement("div");
            notifyOtherDiv.classList.add("row");

            //Other Email label
            let otherEmailLabel = document.createElement("label");
            otherEmailLabel.classList.add("col-form-label", "col-sm-3");
            otherEmailLabel.textContent = "Other Email:";
            notifyOtherDiv.appendChild(otherEmailLabel);

            //Other Email input
            let otherEmail = document.createElement("input");
            otherEmail.classList.add("form-control", "col-sm-8", "ml-3");
            otherEmail.setAttribute("name", "otherEmail");
            otherEmail.setAttribute("placeholder", "Other Email");
            otherEmail.setAttribute("type", "email");
            notifyOtherDiv.appendChild(otherEmail);

            //Notify Other label
            let notifyOtherLabel = document.createElement("label");
            notifyOtherLabel.classList.add("col-form-label", "col-sm-3");
            notifyOtherLabel.textContent = "Notify Other:";
            notifyOtherDiv.appendChild(notifyOtherLabel);

            //Notify Other SelectPicker
            let notifyOtherSelectPicker = document.createElement("select");
            notifyOtherSelectPicker.classList.add("col-sm-8");
            notifyOtherSelectPicker.setAttribute("title", "Notify Other");
            notifyOtherSelectPicker.setAttribute("name", "notifyOther");
            notifyOtherDiv.appendChild(notifyOtherSelectPicker);

            let optionNotifyOther1 = document.createElement("option");
            optionNotifyOther1.setAttribute("value", "YES");
            optionNotifyOther1.textContent = "Yes";
            notifyOtherSelectPicker.appendChild(optionNotifyOther1);

            let optionNotifyOther2 = document.createElement("option");
            optionNotifyOther2.setAttribute("value", "NO");
            optionNotifyOther2.textContent = "No";

            notifyOtherSelectPicker.appendChild(optionNotifyOther2);
            div.appendChild(notifyOtherDiv);
            /* -------------------------- End Notify Other Div ----------------------------------------- */

            QuestionUI.idOptionAdditionAdd.appendChild(div);
        });

        //Remove Option
        $(QuestionUI.idOptionAdditionAdd).on("click", $(QuestionUI.removeOption), function (e) {
            e.preventDefault();
            if (e.target.getAttribute("class") === "fas fa-times-circle color-red") {
                let toRemove = e.target.parentNode.parentNode.parentNode;
                toRemove.parentNode.removeChild(toRemove);
            }
        });

        //Add Questionnaire for Question
        $(QuestionUI.idBtnAddQuestionnaireAdd).on("click", function (e) {
            e.preventDefault();

            let div = document.createElement("div");
            div.setAttribute("class", "row m-1 questionQuestionnaire");

            let inputDisplayOrder = document.createElement("input");
            inputDisplayOrder.setAttribute("type", "text");
            inputDisplayOrder.setAttribute("class", "form-control col-sm-4");
            inputDisplayOrder.setAttribute("name", "questionnaireDisplayOrder");
            inputDisplayOrder.setAttribute("placeholder", "Display Order");

            let buttonRemove = document.createElement("button");
            buttonRemove.setAttribute("class", "btn btn-link removeQuestionnaire");

            let iButtonRemove = document.createElement("i");
            iButtonRemove.setAttribute("class", "fas fa-times-circle color-red");
            buttonRemove.appendChild(iButtonRemove);

            let questionnaireSelect = document.createElement("select");
            questionnaireSelect.setAttribute("class", " mr-1");
            questionnaireSelect.setAttribute("name", "questionnaire");
            questionnaireSelect.setAttribute("title", "Select");

            div.appendChild(questionnaireSelect);
            div.appendChild(inputDisplayOrder);
            div.appendChild(buttonRemove);

            SelectPickerUtil.populateSelectPicker(EndPoints.QUESTIONNAIRE, "title", questionnaireSelect);

            QuestionUI.idQuestionnaireAdditionAdd.appendChild(div);
        });

        //Remove Questionnaire
        $(QuestionUI.idQuestionnaireAdditionAdd).on("click", $(QuestionUI.removeQuestionnaire), function (e) {
            e.preventDefault();
            if (e.target.getAttribute("class") === "fas fa-times-circle color-red") {
                let toRemove = e.target.parentNode.parentNode;
                toRemove.parentNode.removeChild(toRemove);
            }
        });

        //Question add form submission
        $(QuestionUI.idFormQuestionAdd).validate({
            rules: {
                questionType: "required",
                title: "required",
                body: "required"
            },
            messages: {
                questionType: "Question Type required.",
                title: "Title required",
                body: "Body required."
            },
            submitHandler: function (form) {
                let requestBody = {
                    "questionTypeId": $(QuestionUI.questionTypeAdd).val(),
                    "title": form.querySelector("input[name='title']").value,
                    "body": form.elements["body"].value
                };

                //Questionnaire
                let questionQuestionnaires = [];

                let questionnaires = document.querySelectorAll(".questionQuestionnaire");
                questionnaires.forEach(function (item) {
                    let questionQuestionnaire = {};

                    let questionnaireElem = item.getElementsByTagName("select")[0];
                    if (questionnaireElem && questionnaireElem.value) {
                        questionQuestionnaire.questionnaireId = questionnaireElem.value;
                    }

                    let displayOrderElem = item.getElementsByTagName("input")[0];
                    if (displayOrderElem && displayOrderElem.value) {
                        questionQuestionnaire.displayOrder = displayOrderElem.value;
                    }

                    if (questionQuestionnaire && questionQuestionnaire.questionnaireId) {
                        questionQuestionnaires.push(questionQuestionnaire);
                    }
                });
                requestBody.questionQuestionnaires = questionQuestionnaires;

                let selectOption = form.elements["questionType"];
                let selectedIndex = form.elements["questionType"].selectedIndex;
                let selectedText = selectOption.options[selectedIndex].text;
                if (selectedText === "SELECT_ONE"
                    || selectedText === "SELECT_MORE_THAN_ONE"
                    || selectedText === "RATING") {

                    let options = [];

                    let optionInputs = document.querySelectorAll(".questionOption");
                    optionInputs.forEach(function (item) {
                        let inputs = item.getElementsByTagName("input");
                        if (inputs && inputs.length > 0) {
                            let option;

                            if (inputs[0].value) {
                                if (!option) {
                                    option = {};
                                }
                                option.title = inputs[0].value;
                            }
                            if (inputs[1].value) {
                                option.value = inputs[1].value;
                            }

                            let optionMessage = item.getElementsByTagName("textarea")[0];
                            if (optionMessage && optionMessage.value) {
                                option.optionMessage = optionMessage.value;
                            }

                            let risccValue = item.querySelector("input[name='risccValue']");
                            let notifyUser = item.querySelector("select[name='notifyUser']");
                            let otherEmail = item.querySelector("input[name='otherEmail']");
                            let notifyOther = item.querySelector("select[name='notifyOther']");
                            if ($(notifyUser).val()) {
                                option.notifyUser = $(notifyUser).val() === "YES";
                            }
                            if ($(notifyOther).val()) {
                                option.notifyOther = $(notifyOther).val() === "YES";
                            }
                            if (otherEmail.value) {
                                option.otherEmail = otherEmail.value;
                            }
                            if (risccValue.value) {
                                option.risccValue = risccValue.value;
                            }

                            if (option) {
                                options.push(option);
                            }
                        }
                    });
                    requestBody.questionOptions = options;
                    if (options.length < 2) {
                        alert("Please select at least 2 options.");
                        return;
                    }
                }

                QuestionController.addQuestion(form, requestBody, function (data) {
                    QuestionController.listQuestion(QuestionUI.idTableQuestion);
                    if (typeof data.status === 'boolean' && data.status) {
                        $(QuestionUI.idOptionAdditionAdd).empty();
                    }
                });
            }
        });
    }

    QuestionController.listQuestion(QuestionUI.idTableQuestion);

    $(QuestionUI.idTableQuestion).on('draw.dt', function () {

        //Edit Event Listener
        let edits = QuestionUI.idTableQuestion.querySelectorAll(".questionEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let question = event.currentTarget.dataset.question;
                putValueInEditForm(JSON.parse(question));
                $(QuestionUI.idQuestionShow).hide();
                $(QuestionUI.idFormQuestionAdd).hide();
                $(QuestionUI.idFormQuestionImport).hide();
                $(QuestionUI.idFormQuestionUpdate).show();
                QuestionUI.modalQuestionTitle.textContent = "Edit Question";
                $(QuestionUI.idModalQuestion).modal("show");
            });
        });

        //Show Event Listener
        let shows = QuestionUI.idTableQuestion.querySelectorAll(".questionShow");
        Array.from(shows).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let question = event.currentTarget.dataset.question;
                putValueInShow(JSON.parse(question));
                $(QuestionUI.idFormQuestionAdd).hide();
                $(QuestionUI.idFormQuestionUpdate).hide();
                $(QuestionUI.idFormQuestionImport).hide();
                $(QuestionUI.idQuestionShow).show();
                QuestionUI.modalQuestionTitle.textContent = "Question Detail";
                $(QuestionUI.idModalQuestion).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = QuestionUI.idTableQuestion.querySelectorAll(".questionDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                QuestionUI.idBtnQuestionDelete.dataset.question_id = event.currentTarget.dataset.question_id;
                $(QuestionUI.idModalQuestionDelete).modal("show");
            });
        });
    });

    let putValueInShow = function (question) {
        if (!question) {
            return;
        }
        if (question.questionQuestionnaires) {
            let questionnaires = "";
            question.questionQuestionnaires.forEach(function (item) {
                questionnaires += item.questionnaire.title + " [" + item.displayOrder + "], ";
            });
            QuestionUI.idQuestionnaireShow.textContent = questionnaires.substr(0, questionnaires.length - 2);
        }

        QuestionUI.idTitleShow.textContent = question.title;
        QuestionUI.idBodyShow.textContent = question.body;
        QuestionUI.idQuestionTypeShow.textContent = question.questionType.title;

        $(QuestionUI.idQuestionOptionShow).empty();
        if (question.questionOptions && question.questionOptions.length > 0) {
            let pLabel = document.createElement("p");
            pLabel.setAttribute("class", "col-sm-3 font-weight-bold");
            pLabel.textContent = "Options:";

            let pContent = document.createElement("span");
            pContent.setAttribute("class", "col-sm-9");
            let options = "";
            question.questionOptions.forEach(function (item) {
                options += item.title + (item.value ? (" (" + item.value + ") ") : "") + (item.optionMessage ? (" [" + item.optionMessage + "] ") : "") + ", ";
            });
            pContent.textContent = options.substr(0, options.length - 2);

            QuestionUI.idQuestionOptionShow.appendChild(pLabel);
            QuestionUI.idQuestionOptionShow.appendChild(pContent);
        }
    };

    let putValueInEditForm = function (question) {
        QuestionUI.id.value = question.id;
        QuestionUI.titleUpdate.value = question.title;
        QuestionUI.bodyUpdate.value = question.body;
        SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTION_TYPE, "title",
            QuestionUI.questionTypeUpdate, question.questionType.id)
            .then(() => {
                const selectElement = QuestionUI.idTableQuestion;

                if (!selectElement){
                    console.error("Dropdown  not found");
                    return;
                }

                const choices = new Choices(selectElement, {
                    removeItemButton : true,
                    searchEnabled: false,
                    placeholderValue: "Select a QUESTION TYPE",
                    noChoicesText: "No Question Type available",
                })

                selectElement.addEventListener("change", () => {
                    const selectedValue= selectElement.value;
                    console.log("Selected value:", selectedValue);

                    if (!selectedValue){
                        QuestionController.listQuestion(QuestionUI.idTableQuestion);
                    } else {
                        null
                    }
                });
                console.log("Dropdown and event listener successfully initialized");
            }).catch(error => console.error("Error initializing dropdown:", error));

        if (question.questionQuestionnaires && question.questionQuestionnaires.length > 0) {
            $(QuestionUI.idQuestionnaireAdditionUpdate).empty();

            function compareQuestionnaire(a, b) {
                if (a.displayOrder < b.displayOrder) {
                    return -1;
                }
                if (a.displayOrder > b.displayOrder) {
                    return 1;
                }
                return 0;
            }

            question.questionQuestionnaires.sort(compareQuestionnaire);

            question.questionQuestionnaires.forEach(function (item) {
                let div = document.createElement("div");
                div.setAttribute("class", "row m-1 questionQuestionnaireUpdate");
                div.setAttribute("data-id", item.id);

                let inputDisplayOrder = document.createElement("input");
                inputDisplayOrder.setAttribute("type", "text");
                inputDisplayOrder.setAttribute("class", "form-control col-sm-4");
                inputDisplayOrder.setAttribute("name", "questionnaireDisplayOrder");
                inputDisplayOrder.setAttribute("placeholder", "Display Order");
                inputDisplayOrder.setAttribute("value", item.displayOrder || "");

                let buttonRemove = document.createElement("button");
                buttonRemove.setAttribute("class", "btn btn-link removeQuestionnaireUpdate");

                let iButtonRemove = document.createElement("i");
                iButtonRemove.setAttribute("class", "fas fa-times-circle color-red");
                buttonRemove.appendChild(iButtonRemove);

                let questionnaireSelect = document.createElement("select");
                questionnaireSelect.setAttribute("class", "selectpicker show-tick mr-1");
                questionnaireSelect.setAttribute("name", "questionnaire");
                questionnaireSelect.setAttribute("title", "Select");

                div.appendChild(questionnaireSelect);
                div.appendChild(inputDisplayOrder);
                div.appendChild(buttonRemove);

                SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTIONNAIRE_FIELDS,
                    "title", questionnaireSelect, item.questionnaire.id)
                    .then(() => {
                       const selectElement = QuestionUI.idTableQuestion;

                       if (!selectElement){
                           console.error("Dropdown not found");
                           return;
                       }

                       const choices = new Choices(selectElement, {
                           removeItemButton: true,
                           searchEnabled: false,
                           placeholderValue: "Select a QUESTIONNAIRE",
                           noChoicesText: "No QUESTIONNAIRE options available",
                       })

                        selectElement.addEventListener("change", () => {
                            const selectedValue = selectElement.value;
                            console.log("Selected value:", selectedValue);

                            if(!selectedValue){
                                QuestionController.listQuestion(QuestionUI.idTableQuestion);
                            } else {
                                null
                            }
                        });
                       console.log("Dropdown and event listener successfully initialized");
                }).catch(error => console.error("Error initializing dropdown", error));

                QuestionUI.idQuestionnaireAdditionUpdate.appendChild(div);
            });
        }


        if (question.questionOptions && question.questionOptions.length > 0) {

            $(QuestionUI.idOptionAdditionUpdate).empty();

            function compare(a, b) {
                if (a.value < b.value) {
                    return -1;
                }
                if (a.value > b.value) {
                    return 1;
                }
                return 0;
            }

            question.questionOptions.sort(compare);

            question.questionOptions.forEach(function (item) {
                let div = document.createElement("div");
                div.setAttribute("class", "questionOptionUpdate border border-dark rounded container m-2");
                div.setAttribute("data-id", item.id);
                div.innerHTML = "<div class='row p-1'><input class='form-control col-sm-4' type='text' name='optionTitle' placeholder='Title' value='"
                    + item.title + "'>" +
                    "<input class='form-control col-sm-2' type='number' name='optionValue' placeholder='Value' value='"
                    + (item.value || "") + "'>" +
                    "<textarea class='form-control col-sm-5' name='optionMessage' placeholder='Email body message'>" + (item.optionMessage || "") + "</textarea>" +
                    "<button class='btn btn-link removeOptionUpdate'><i class='fas fa-times-circle color-red'></i></button></div>";

                //riscc value
                          let risccValueDiv = document.createElement("div");
                          risccValueDiv.classList.add("row","p-1");

                          let risccValueLabel= document.createElement("label");
                          risccValueLabel.classList.add("col-form-label","col-sm-3");
                          risccValueLabel.textContent="RISCC Value";
                          risccValueDiv.appendChild(risccValueLabel);
                          let risccValue = document.createElement("input");
                          risccValue.classList.add("form-control","col-sm-8","ml-3");
                          risccValue.setAttribute("name","risccValue");
                          risccValue.setAttribute("placeholder","riscc value");
                          risccValue.setAttribute("type","number");
                          risccValue.value = item.risccValue|| "";
                          risccValueDiv.appendChild(risccValue);
                div.appendChild(risccValueDiv);
                //Notify User
                let notifyUserDiv = document.createElement("div");
                notifyUserDiv.classList.add("row");

                let notifyUserLabel = document.createElement("label");
                notifyUserLabel.classList.add("col-form-label", "col-sm-3");
                notifyUserLabel.textContent = "Notify User:";
                notifyUserDiv.appendChild(notifyUserLabel);

                let notifyUserSelectPicker = document.createElement("select");
                notifyUserSelectPicker.classList.add("form-control", "show-tick", "col-sm-8");
                notifyUserSelectPicker.setAttribute("title", "Notify User");
                notifyUserSelectPicker.setAttribute("name", "notifyUser");
                notifyUserDiv.appendChild(notifyUserSelectPicker);

                let option1 = document.createElement("option");
                option1.setAttribute("value", "YES");
                option1.textContent = "Yes";
                notifyUserSelectPicker.appendChild(option1);

                let option2 = document.createElement("option");
                option2.setAttribute("value", "NO");
                option2.textContent = "No";

                notifyUserSelectPicker.appendChild(option2);
                if (item.notifyUser !== undefined) {
                    notifyUserSelectPicker.value = item.notifyUser ? "YES" : "NO";
                    // $(notifyUserSelectPicker).selectpicker("val", item.notifyUser ? "YES" : "NO");
                }
                div.appendChild(notifyUserDiv);

                //Notify Other
                let notifyOtherDiv = document.createElement("div");
                notifyOtherDiv.classList.add("row", "p-1");

                let otherEmailLabel = document.createElement("label");
                otherEmailLabel.classList.add("col-form-label", "col-sm-3");
                otherEmailLabel.textContent = "Other Email:";
                notifyOtherDiv.appendChild(otherEmailLabel);

                let otherEmail = document.createElement("input");
                otherEmail.classList.add("form-control", "col-sm-8", "ml-3");
                otherEmail.setAttribute("name", "otherEmail");
                otherEmail.setAttribute("placeholder", "Other Email");
                otherEmail.setAttribute("type", "email");
                otherEmail.value = item.otherEmail || "";
                notifyOtherDiv.appendChild(otherEmail);

                let notifyOtherLabel = document.createElement("label");
                notifyOtherLabel.classList.add("col-form-label", "col-sm-3");
                notifyOtherLabel.textContent = "Notify Other:";
                notifyOtherDiv.appendChild(notifyOtherLabel);

                let notifyOtherSelectPicker = document.createElement("select");
                notifyOtherSelectPicker.classList.add("show-tick", "col-sm-8");
                notifyOtherSelectPicker.setAttribute("title", "Notify Other");
                notifyOtherSelectPicker.setAttribute("name", "notifyOther");
                notifyOtherDiv.appendChild(notifyOtherSelectPicker);

                let optionNotifyOther1 = document.createElement("option");
                optionNotifyOther1.setAttribute("value", "YES");
                optionNotifyOther1.textContent = "Yes";
                notifyOtherSelectPicker.appendChild(optionNotifyOther1);

                let optionNotifyOther2 = document.createElement("option");
                optionNotifyOther2.setAttribute("value", "NO");
                optionNotifyOther2.textContent = "No";

                notifyOtherSelectPicker.appendChild(optionNotifyOther2);
                if (item.notifyOther !== undefined) {
                    notifyOtherSelectPicker.value = item.notifyOther ? "YES" : "NO";
                }
                div.appendChild(notifyOtherDiv);

                QuestionUI.idOptionAdditionUpdate.appendChild(div);
            });
            $(QuestionUI.idOptionContainerUpdate).show();
        } else {
            $(QuestionUI.idOptionContainerUpdate).hide();
        }
    };

    QuestionUI.idBtnAddOptionUpdate.addEventListener("click", function (event) {
        event.preventDefault();
        let div = document.createElement("div");
        div.setAttribute("class", "questionOptionUpdate border border-dark rounded container m-2");
        div.innerHTML = "<div class='row'><input class='form-control col-sm-4' type='text' name='optionTitle' placeholder='Title'>" +
            "<input class='form-control col-sm-2' type='number' name='optionValue' placeholder='Value'>" +
            "<textarea class='form-control col-sm-5' name='optionMessage' placeholder='Email body message'></textarea>" +
            "<button class='btn btn-link removeOptionUpdate'><i class='fas fa-times-circle color-red'></i></button></div>";

        /* ------------------------------------ riscc value ------------------ */
        let risccValueDiv = document.createElement("div");
           risccValueDiv.classList.add("row");

        // RISCC Value label
        let risccLabel = document.createElement("label");
        risccLabel.classList.add("col-form-label","col-sm-3");
        risccLabel.textContent = "RISCC Value:";
        risccValueDiv.appendChild(risccLabel);

        // RISCC value
        let risccValue = document.createElement("input");
        risccValue.classList.add("form-control", "col-sm-8","ml-3");
        risccValue.setAttribute("name","risccValue");
        risccValue.setAttribute("placeholder", "riscc value");
        risccValue.setAttribute("type","number");
        risccValueDiv.appendChild(risccValue);
        div.appendChild(risccValueDiv);
        /* -------------------------- Start Notify User Div ----------------------------------------- */
        let notifyUserDiv = document.createElement("div");
        notifyUserDiv.classList.add("row");

        //Label
        let notifyUserLabel = document.createElement("label");
        notifyUserLabel.classList.add("col-form-label", "col-sm-3");
        notifyUserLabel.textContent = "Notify User:";
        notifyUserDiv.appendChild(notifyUserLabel);

        //SelectPicker
        let notifyUserSelectPicker = document.createElement("select");
        notifyUserSelectPicker.classList.add("selectpicker", "show-tick", "col-sm-8");
        notifyUserSelectPicker.setAttribute("title", "Notify User");
        notifyUserSelectPicker.setAttribute("name", "notifyUser");
        notifyUserDiv.appendChild(notifyUserSelectPicker);

        let option1 = document.createElement("option");
        option1.setAttribute("value", "YES");
        option1.textContent = "Yes";
        notifyUserSelectPicker.appendChild(option1);

        let option2 = document.createElement("option");
        option2.setAttribute("value", "NO");
        option2.textContent = "No";

        notifyUserSelectPicker.appendChild(option2);

        div.appendChild(notifyUserDiv);
        /* -------------------------- End Notify User Div ----------------------------------------- */


        /* -------------------------- Start Notify Other Div ----------------------------------------- */
        let notifyOtherDiv = document.createElement("div");
        notifyOtherDiv.classList.add("row");

        //Other Email label
        let otherEmailLabel = document.createElement("label");
        otherEmailLabel.classList.add("col-form-label", "col-sm-3");
        otherEmailLabel.textContent = "Other Email:";
        notifyOtherDiv.appendChild(otherEmailLabel);

        //Other Email input
        let otherEmail = document.createElement("input");
        otherEmail.classList.add("form-control", "col-sm-8", "ml-3");
        otherEmail.setAttribute("name", "otherEmail");
        otherEmail.setAttribute("placeholder", "Other Email");
        otherEmail.setAttribute("type", "email");
        notifyOtherDiv.appendChild(otherEmail);

        //Notify Other Label
        let notifyOtherLabel = document.createElement("label");
        notifyOtherLabel.classList.add("col-form-label", "col-sm-3");
        notifyOtherLabel.textContent = "Notify Other:";
        notifyOtherDiv.appendChild(notifyOtherLabel);

        //Notify Other SelectPicker
        let notifyOtherSelectPicker = document.createElement("select");
        notifyOtherSelectPicker.classList.add("col-sm-8");
        notifyOtherSelectPicker.setAttribute("title", "Notify Other");
        notifyOtherSelectPicker.setAttribute("name", "notifyOther");
        notifyOtherDiv.appendChild(notifyOtherSelectPicker);

        let optionNotifyOther1 = document.createElement("option");
        optionNotifyOther1.setAttribute("value", "YES");
        optionNotifyOther1.textContent = "Yes";
        notifyOtherSelectPicker.appendChild(optionNotifyOther1);

        let optionNotifyOther2 = document.createElement("option");
        optionNotifyOther2.setAttribute("value", "NO");
        optionNotifyOther2.textContent = "No";

        notifyOtherSelectPicker.appendChild(optionNotifyOther2);
        div.appendChild(notifyOtherDiv);
        /* -------------------------- End Notify Other Div ----------------------------------------- */

        QuestionUI.idOptionAdditionUpdate.appendChild(div);
    });

    $(QuestionUI.idOptionAdditionUpdate).on("click", $(QuestionUI.removeOptionUpdate), function (e) {
        e.preventDefault();
        if (e.target.getAttribute("class") === "fas fa-times-circle color-red") {
            let toRemove = e.target.parentNode.parentNode.parentNode;
            toRemove.parentNode.removeChild(toRemove);
        }
    });

    QuestionUI.idBtnAddQuestionnaireUpdate.addEventListener("click", function (event) {
        event.preventDefault();

        let div = document.createElement("div");
        div.setAttribute("class", "row m-1 questionQuestionnaireUpdate");

        let questionnaireSelect = document.createElement("select");
        questionnaireSelect.setAttribute("class", "mr-1");
        questionnaireSelect.setAttribute("name", "questionnaireUpdate");
        questionnaireSelect.setAttribute("title", "Select");
        SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTIONNAIRE_FIELDS, "title", questionnaireSelect)
            .then(() => {
                const selectElement=  QuestionUI.idQuestionnaireContainerUpdate;

                if(!selectElement){
                    console.error("No dropdown found");
                    return;
                }

                const choices = new Choices (selectElement, {
                    removeItemButton: true,
                    searchEnabled: false,
                    placeholderValue: "Select a QUESTIONNAIRE fIELD",
                    NoChoicesText: "No QUESTIONNAIRE options available",
                })

                selectElement.addEventListener("change", () => {
                    const selectedValue = selectElement.value;
                    console.log("Selected value:", selectedValue);

                    if (!selectedValue) {
                        QuestionController.listQuestion(QuestionUI.idTableQuestion);
                    } else {
                        null
                    }
                });
                console.log("Dropdown and event listener successfully initialized");
            }).catch(error => console.error("error initializing dropdown:", error));

        div.appendChild(questionnaireSelect);

        let inputDisplayOrder = document.createElement("input");
        inputDisplayOrder.setAttribute("type", "text");
        inputDisplayOrder.setAttribute("class", "form-control col-sm-4");
        inputDisplayOrder.setAttribute("name", "questionnaireDisplayOrder");
        inputDisplayOrder.setAttribute("placeholder", "Display Order");
        div.appendChild(inputDisplayOrder);

        let buttonRemove = document.createElement("button");
        buttonRemove.setAttribute("class", "btn btn-link removeQuestionnaireUpdate");

        let iButtonRemove = document.createElement("i");
        iButtonRemove.setAttribute("class", "fas fa-times-circle color-red");
        buttonRemove.appendChild(iButtonRemove);

        div.appendChild(buttonRemove);

        QuestionUI.idQuestionnaireAdditionUpdate.appendChild(div);
    });

    $(QuestionUI.idQuestionnaireAdditionUpdate).on("click", $(QuestionUI.removeQuestionnaireUpdate), function (e) {
        e.preventDefault();
        if (e.target.getAttribute("class") === "fas fa-times-circle color-red") {
            let toRemove = e.target.parentNode.parentNode;
            toRemove.parentNode.removeChild(toRemove);
        }
    });

    //Question Update Form submission-
    $(QuestionUI.idFormQuestionUpdate).validate({
        rules: {
            questionType: "required",
            title: "required",
            body: "required"
        },
        messages: {
            questionType: "Question Type required.",
            title: "Title required",
            body: "Body required."
        },
        submitHandler: function (form) {

            let requestBody = {
                "id": form.querySelector("input[name='id']").value,
                "questionTypeId": $(QuestionUI.questionTypeUpdate).val(),
                "title": form.querySelector("input[name='title']").value,
                "body": form.elements["body"].value
            };

            //Questionnaire
            let questionQuestionnaires = [];

            let questionnaires = document.querySelectorAll(".questionQuestionnaireUpdate");
            questionnaires.forEach(function (item) {
                let questionQuestionnaire = {};

                if (item.dataset.id) {
                    questionQuestionnaire.id = item.dataset.id;
                }

                let questionnaireElem = item.getElementsByTagName("select")[0];
                if (questionnaireElem && questionnaireElem.value) {
                    questionQuestionnaire.questionnaireId = questionnaireElem.value;
                }

                let displayOrderElem = item.getElementsByTagName("input")[0];
                if (displayOrderElem && displayOrderElem.value) {
                    questionQuestionnaire.displayOrder = displayOrderElem.value;
                }

                if (questionQuestionnaire) {
                    questionQuestionnaires.push(questionQuestionnaire);
                }
            });
            requestBody.questionQuestionnaires = questionQuestionnaires;

            let selectOption = form.elements["questionType"];
            let selectedIndex = form.elements["questionType"].selectedIndex;
            let selectedText = selectOption.options[selectedIndex].text;
            if (selectedText === "SELECT_ONE"
                || selectedText === "SELECT_MORE_THAN_ONE"
                || selectedText === "RATING") {
                let options = [];

                let optionInputs = document.querySelectorAll(".questionOptionUpdate");
                optionInputs.forEach(function (item) {
                    let inputs = item.getElementsByTagName("input");
                    if (inputs && inputs.length > 0) {
                        let option;

                        if (item.dataset.id) {
                            if (!option) {
                                option = {};
                            }
                            option.id = item.dataset.id;
                        }

                        if (inputs[0].value) {
                            if (!option) {
                                option = {};
                            }
                            option.title = inputs[0].value;
                        }
                        if (inputs[1].value) {
                            option.value = inputs[1].value;
                        }

                        let optionMessage = item.getElementsByTagName("textarea")[0];
                        if (optionMessage && optionMessage.value) {
                            option.optionMessage = optionMessage.value;
                        }

                        let risccValue = item.querySelector("input[name='risccValue']")
                        let notifyUser = item.querySelector("select[name='notifyUser']");
                        let otherEmail = item.querySelector("input[name='otherEmail']");
                        let notifyOther = item.querySelector("select[name='notifyOther']");
                        if ($(notifyUser).val()) {
                            option.notifyUser = $(notifyUser).val() === "YES";
                        }
                        if ($(notifyOther).val()) {
                            option.notifyOther = $(notifyOther).val() === "YES";
                        }
                        if (otherEmail.value) {
                            option.otherEmail = otherEmail.value;
                        }
                        if (risccValue.value) {
                            option.risccValue = risccValue.value;
                        }

                        if (option) {
                            options.push(option);
                        }
                    }
                });
                requestBody.questionOptions = options;
                if (options.length < 2) {
                    alert("Please select at least 2 options.");
                    return;
                }
            }

            QuestionController.updateQuestion(form, requestBody, function () {
                QuestionController.listQuestion(QuestionUI.idTableQuestion);
            });
        }
    });


    //Question filter
    QuestionUI.questionnaireFilter.addEventListener('change', function (event) {
        QuestionController.filterByQuestionnaire(QuestionUI.idTableQuestion, $(QuestionUI.questionnaireFilter).val());
    });

    //Import Question
    QuestionUI.idBtnPopImportQuestion.addEventListener('click', function (event) {
        $(QuestionUI.idQuestionShow).hide();
        $(QuestionUI.idFormQuestionAdd).hide();
        $(QuestionUI.idFormQuestionUpdate).hide();
        $(QuestionUI.idFormQuestionImport).show();
        QuestionUI.modalQuestionTitle.textContent = "Import Question";
        $(QuestionUI.idModalQuestion).modal("show");
    });

    $(QuestionUI.idFormQuestionImport).validate({
        rules: {
            excelFile: "required"
        },
        messages: {
            excelFile: "Please select an excel file to import data."
        },
        submitHandler: function (form) {

            let requestBody = new FormData();
            let fileField = form.querySelector("input[type='file']");
            requestBody.append('excelFile', fileField.files[0]);

            QuestionController.importQuestion(form, requestBody, function () {
                QuestionController.listQuestion(QuestionUI.idTableQuestion);
            });
        }
    });


    //Delete
    QuestionUI.idBtnQuestionDelete.addEventListener("click", function (event) {
        QuestionController.deleteQuestion(QuestionUI.idModalQuestionDelete, event.currentTarget.dataset.question_id, function () {
            QuestionController.listQuestion(QuestionUI.idTableQuestion);
        });
    });

    $(QuestionUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(QuestionUI.idModalQuestion).modal("hide");
        $(QuestionUI.idFormQuestionAdd).trigger("reset");
        $(QuestionUI.idFormQuestionUpdate).trigger("reset");
        $(QuestionUI.idModalQuestionDelete).modal("hide");
        $(QuestionUI.modalAlertBody).html("");
    });

    QuestionUI.idBtnQuestionTemplate.addEventListener('click', function (event) {
        QuestionController.exportQuestionTemplate(QuestionUI.idBtnQuestionTemplate, EndPoints.QUESTION_TEMPLATE);
    });
});