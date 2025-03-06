'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as GroupController from "../controller/GroupController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";
import * as GroupUI from "../ui/GroupUI.js";
import * as DateTimeUtil from "../util/DateTimeUtil.js";


$(document).ready(function () {
    CommonUtil.initialSetup();

    $(".custom_date_time_picker").daterangepicker({
        singleDatePicker: true,
        timePicker: true,
        startDate: moment().startOf('hour'),
        locale: {
            format: 'YYYY-MM-DD hh:mm A'
        }
    });

    if (!CommonUtil.hasAuthority("Group (Create)")) {
        $(GroupUI.idBtnPopAddGroup).hide();
    } else {
        GroupUI.idBtnPopAddGroup.addEventListener("click", function (event) {
            $(GroupUI.idFormGroupUpdate).hide();
            $(GroupUI.idGroupShow).hide();
            $(GroupUI.idFormGroupAdd).show();

            SelectPickerUtil.populateSelectPickerNew(EndPoints.USER_FIELDS,["firstName", "lastName", "emailAddress"], GroupUI.usersAdd).then(() => {
                const selectElement = GroupUI.usersAdd;
                if (!selectElement) {
                    console.error("Dropdown not found");
                    return;
                }

                const choices = new Choices(selectElement, {
                    removeItemButton: true,
                    searchEnabled: false,
                    placeholderValue: "Select a User",
                    noChoicesText: "No User options available",
                });

                selectElement.addEventListener("change", () => {
                    const selectedValue = selectElement.value;
                    console.log("Selected value:", selectedValue);

                    if (!selectedValue) {
                        GroupController.listGroup(GroupUI.idTableGroup);
                    } else {
                        null
                    }
                });

                console.log("Dropdown and event listener successfully initialized");
            }).catch(error => console.error("Error initializing dropdown:", error));

            SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTIONNAIRE_FIELDS, "title", GroupUI.questionnaireAdd).then(() => {
                const selectElement = GroupUI.questionnaireAdd;
                if (!selectElement) {
                    console.error("Dropdown not found");
                    return;
                }

                const choices = new Choices(selectElement, {
                    removeItemButton: true,
                    searchEnabled: false,
                    placeholderValue: "Select a questionnaire",
                    noChoicesText: "No questionnaire options available",
                });

                selectElement.addEventListener("change", () => {
                    const selectedValue = selectElement.value;
                    console.log("Selected value:", selectedValue);

                    if (!selectedValue) {
                        GroupController.listGroup(GroupUI.idTableGroup);
                    } else {
                        null
                    }
                });

                console.log("Dropdown and event listener successfully initialized");
            }).catch(error => console.error("Error initializing dropdown:", error));

            GroupUI.modalGroupTitle.textContent = "Add Group";
            $(GroupUI.idModalGroup).modal("show");
        });


        //Add Questionnaire
        $(GroupUI.idBtnAddQuestionnaireAdd).on("click", function (e) {
            e.preventDefault();

            let div = document.createElement("div");
            div.setAttribute("class", "m-1 p-1 groupQuestionnaire border rounded");

            //Start Questionnaire
            let divQuestionnaire = document.createElement("div");
            divQuestionnaire.classList.add("row", "mb-1");

            let labelQuestionnaire = document.createElement("label");
            labelQuestionnaire.classList.add("col-sm-3", "col-form-label", "text-right");
            labelQuestionnaire.textContent = "Questionnaire:";
            divQuestionnaire.appendChild(labelQuestionnaire);

            let selectQuestionnaire = document.createElement("select");
            selectQuestionnaire.classList.add("selectpicker", "show-tick");
            selectQuestionnaire.setAttribute("name", "questionnaire");
            selectQuestionnaire.setAttribute("title", "Select");
            selectQuestionnaire.setAttribute("required", "true");
            divQuestionnaire.appendChild(selectQuestionnaire);

            SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTIONNAIRE_FIELDS, "title", selectQuestionnaire).then(() => {
                const selectElement = selectQuestionnaire;
                if (!selectElement) {
                    console.error("Dropdown not found");
                    return;
                }

                const choices = new Choices(selectElement, {
                    removeItemButton: true,
                    searchEnabled: false,
                    placeholderValue: "Select a ROLE",
                    noChoicesText: "No options available",
                });

                selectElement.addEventListener("change", () => {
                    const selectedValue = selectElement.value;
                    console.log("Selected value:", selectedValue);

                    if (!selectedValue) {
                        GroupController.listGroup(GroupUI.idTableGroup);
                    } else {
                        null
                    }
                });

                console.log("Dropdown and event listener successfully initialized");
            }).catch(error => console.error("Error initializing dropdown:", error));

            div.appendChild(divQuestionnaire);
            //End Questionnaire

            //Start Start Date Time
            let divActiveDateTime = document.createElement("div");
            divActiveDateTime.classList.add("row", "mb-1");

            let labelActiveDateTime = document.createElement("label");
            labelActiveDateTime.classList.add("col-sm-3", "col-form-label", "text-right");
            labelActiveDateTime.textContent = "Start Date Time:";
            divActiveDateTime.appendChild(labelActiveDateTime);

            let inputStartDateTime = document.createElement("input");
            inputStartDateTime.classList.add("form-control", "col-sm-8", "custom_date_time_picker");
            inputStartDateTime.setAttribute("name", "startDateTime");
            inputStartDateTime.setAttribute("type", "text");
            inputStartDateTime.setAttribute("required", "true");
            divActiveDateTime.appendChild(inputStartDateTime);
            DateTimeUtil.attachDateTimePicker(inputStartDateTime);
            div.appendChild(divActiveDateTime);
            //End Start Date Time

            //Start End Date Time
            let divEndDateTime = document.createElement("div");
            divEndDateTime.classList.add("row", "mb-1");

            let labelEndDateTime = document.createElement("label");
            labelEndDateTime.classList.add("col-sm-3", "col-form-label", "text-right");
            labelEndDateTime.textContent = "End Date Time:";
            divEndDateTime.appendChild(labelEndDateTime);

            let inputEndDateTime = document.createElement("input");
            inputEndDateTime.classList.add("form-control", "col-sm-8", "custom_date_time_picker");
            inputEndDateTime.setAttribute("name", "endDateTime");
            inputEndDateTime.setAttribute("type", "text");
            inputEndDateTime.setAttribute("required", "true");
            divEndDateTime.appendChild(inputEndDateTime);
            DateTimeUtil.attachDateTimePicker(inputEndDateTime);
            div.appendChild(divEndDateTime);
            //End End Date Time

            //Start Reminder Message
            let divReminderMessage = document.createElement("div");
            divReminderMessage.classList.add("row", "mb-1");

            let labelReminderMessage = document.createElement("label");
            labelReminderMessage.classList.add("col-sm-3", "col-form-label", "text-right");
            labelReminderMessage.textContent = "Reminder Message:";
            divReminderMessage.appendChild(labelReminderMessage);

            let textareaReminderMessage = document.createElement("textarea");
            textareaReminderMessage.classList.add("form-control", "col-sm-8");
            textareaReminderMessage.setAttribute("name", "reminderMessage");
            divReminderMessage.appendChild(textareaReminderMessage);

            div.appendChild(divReminderMessage);
            //End Reminder Message

            //Start Reminder Time Interval
            let divReminderTimeInterval = document.createElement("div");
            divReminderTimeInterval.classList.add("row", "mb-1");

            let labelReminderTimeInterval = document.createElement("label");
            labelReminderTimeInterval.classList.add("col-sm-3", "col-form-label", "text-right");
            labelReminderTimeInterval.textContent = "Reminder Time Interval (Minute):";
            divReminderTimeInterval.appendChild(labelReminderTimeInterval);

            let inputReminderTimeIntervalHour = document.createElement("input");
            inputReminderTimeIntervalHour.classList.add("form-control", "col-sm-3");
            inputReminderTimeIntervalHour.setAttribute("name", "reminderTimeIntervalHour");
            inputReminderTimeIntervalHour.setAttribute("type", "number");
            inputReminderTimeIntervalHour.setAttribute("step", "1");
            inputReminderTimeIntervalHour.setAttribute("min", "0");
            inputReminderTimeIntervalHour.setAttribute("max", "24");
            inputReminderTimeIntervalHour.setAttribute("placeholder", "hh");
            divReminderTimeInterval.appendChild(inputReminderTimeIntervalHour);

            let inputReminderTimeIntervalMinute = document.createElement("input");
            inputReminderTimeIntervalMinute.classList.add("form-control", "col-sm-3");
            inputReminderTimeIntervalMinute.setAttribute("name", "reminderTimeIntervalMinute");
            inputReminderTimeIntervalMinute.setAttribute("type", "number");
            inputReminderTimeIntervalMinute.setAttribute("step", "1");
            inputReminderTimeIntervalMinute.setAttribute("min", "0");
            inputReminderTimeIntervalMinute.setAttribute("max", "60");
            inputReminderTimeIntervalMinute.setAttribute("placeholder", "mm");
            divReminderTimeInterval.appendChild(inputReminderTimeIntervalMinute);

            div.appendChild(divReminderTimeInterval);
            //End Reminder Time Interval


            //Start Remove
            let buttonRemove = document.createElement("button");
            buttonRemove.setAttribute("class", "btn btn-link removeQuestionnaire");
            buttonRemove.textContent = "Remove ";
            div.appendChild(buttonRemove);
            //End Remove

            GroupUI.idQuestionnaireAdditionAdd.appendChild(div);
        });

        //Remove Questionnaire
        $(GroupUI.idQuestionnaireAdditionAdd).on("click", $(GroupUI.removeQuestionnaire), function (e) {
            e.preventDefault();
            if (e.target.getAttribute("class") === "btn btn-link removeQuestionnaire") {
                let toRemove = e.target.parentNode;
                toRemove.parentNode.removeChild(toRemove);
            }
        });


        $(GroupUI.idFormGroupAdd).validate({
            rules: {
                title: "required"
            },
            messages: {
                title: "Title required."
            },
            submitHandler: function (form) {
                let requestBody = {
                    "title": form.elements["title"].value
                };
                if (form.elements["description"].value) {
                    requestBody.description = form.elements["description"].value;
                }
                if ($(GroupUI.usersAdd).val()) {
                    requestBody.userIds = $(GroupUI.usersAdd).val();
                }

                //Questionnaire
                let groupQuestionnaires = [];
                let questionnaires = document.querySelectorAll(".groupQuestionnaire");
                questionnaires.forEach(function (item) {
                    let groupQuestionnaire = {};

                    let questionnaireElem = item.getElementsByTagName("select")[0];
                    if (questionnaireElem && questionnaireElem.value) {
                        groupQuestionnaire.questionnaireId = questionnaireElem.value;
                    }

                    let startDateTimeElem = item.querySelector("input[name='startDateTime']");

                    if (startDateTimeElem && startDateTimeElem.value) {
                        const dateString = startDateTimeElem.value;
                        const format = 'YYYY-MM-DD hh:mm A';
                        const parsedDate = moment(dateString, format);
                        if (parsedDate.isValid()) {
                            groupQuestionnaire.startDateTime = parsedDate.valueOf();
                        } else {
                            console.error('Invalid date format:', dateString);
                        }

                        // groupQuestionnaire.startDateTime = new Date(startDateTimeElem.value).valueOf();
                    }
                    let endDateTimeElem = item.querySelector("input[name='endDateTime']");
                    if (endDateTimeElem && endDateTimeElem.value) {
                        // groupQuestionnaire.endDateTime = new Date(endDateTimeElem.value).valueOf();
                        const dateString = endDateTimeElem.value;
                        const format = 'YYYY-MM-DD hh:mm A';
                        const parsedDate = moment(dateString, format);

                        // Check if the parsed date is valid before converting to a timestamp
                        if (parsedDate.isValid()) {
                            groupQuestionnaire.endDateTime = parsedDate.valueOf();
                        } else {
                            console.error('Invalid date format:', dateString);
                        }



                    }

                    let reminderMessageElem = item.querySelector("textarea[name='reminderMessage']");
                    if (reminderMessageElem && reminderMessageElem.value) {
                        groupQuestionnaire.reminderMessage = reminderMessageElem.value;
                    }

                    let reminderTimeIntervalElemHour = item.querySelector("input[name='reminderTimeIntervalHour']");
                    let reminderTimeIntervalElemMinute = item.querySelector("input[name='reminderTimeIntervalMinute']");
                    let reminderTime;//In minute
                    if (reminderTimeIntervalElemHour && reminderTimeIntervalElemHour.value) {
                        reminderTime = reminderTimeIntervalElemHour.value * 60;
                        groupQuestionnaire.reminderTimeInterval = reminderTime;
                    }
                    if (reminderTimeIntervalElemMinute && reminderTimeIntervalElemMinute.value) {
                        if (reminderTime) {
                            reminderTime += parseInt(reminderTimeIntervalElemMinute.value);
                        } else {
                            reminderTime = 0;
                            reminderTime += parseInt(reminderTimeIntervalElemMinute.value);
                        }
                        groupQuestionnaire.reminderTimeInterval = reminderTime;
                    }

                    if (groupQuestionnaire.questionnaireId) {
                        groupQuestionnaires.push(groupQuestionnaire);
                    }
                });
                requestBody.groupQuestionnaires = groupQuestionnaires;

                GroupController.addGroup(form, requestBody, function () {
                        GroupController.listGroup(GroupUI.idTableGroup);
                    }
                );
            }
        });
    }

    GroupController.listGroup(GroupUI.idTableGroup);
    $(GroupUI.idTableGroup).on('draw.dt', function () {

        //Edit Event Listeners
        let edits = GroupUI.idTableGroup.querySelectorAll(".groupEdit");
        Array.from(edits).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let group = event.currentTarget.dataset.group;
                putValueInEditForm(JSON.parse(group));
                $(GroupUI.idFormGroupAdd).hide();
                $(GroupUI.idGroupShow).hide();
                $(GroupUI.idFormGroupUpdate).show();
                GroupUI.modalGroupTitle.textContent = "Edit Group";
                $(GroupUI.idModalGroup).modal("show");
            });
        });

        //Show Event Listeners
        let show = GroupUI.idTableGroup.querySelectorAll(".groupShow");
        Array.from(show).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let group = event.currentTarget.dataset.group;
                putValueInShow(JSON.parse(group));
                $(GroupUI.idFormGroupAdd).hide();
                $(GroupUI.idFormGroupUpdate).hide();
                $(GroupUI.idGroupShow).show();
                GroupUI.modalGroupTitle.textContent = "Show Group";
                $(GroupUI.idModalGroup).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = GroupUI.idTableGroup.querySelectorAll(".groupDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                GroupUI.idBtnGroupDelete.dataset.group_id = event.currentTarget.dataset.group_id;
                $(GroupUI.idModalGroupDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (group) {
        GroupUI.id.value = group.id;
        GroupUI.titleUpdate.value = group.title;
        if (group.description) {
            GroupUI.descriptionUpdate.value = group.description;
        }
        if (group.users) {
            let selectedUserIds = [];
            Array.from(group.users).forEach(function (value, index, array) {
                selectedUserIds.push(value.id);
            });

            SelectPickerUtil.populateSelectPickerNew(EndPoints.USER_FIELDS, ["firstName", "lastName", "emailAddress"], GroupUI.usersUpdate, selectedUserIds).then(() => {
                const selectElement = GroupUI.usersUpdate;
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
                        GroupController.listGroup(GroupUI.idTableGroup);
                    } else {
                        null
                    }
                });

                console.log("Dropdown and event listener successfully initialized");
            }).catch(error => console.error("Error initializing dropdown:", error));

        } else {

            SelectPickerUtil.populateSelectPickerNew(EndPoints.USER_FIELDS, ["firstName", "lastName", "emailAddress"], GroupUI.usersUpdate).then(() => {
                const selectElement = GroupUI.usersUpdate;
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
                        GroupController.listGroup(GroupUI.idTableGroup);
                    } else {
                        null
                    }
                });

                console.log("Dropdown and event listener successfully initialized");
            }).catch(error => console.error("Error initializing dropdown:", error));
        }

        if (group.groupQuestionnaires && group.groupQuestionnaires.length > 0) {
            $(GroupUI.idQuestionnaireAdditionUpdate).empty();

            group.groupQuestionnaires.forEach(function (item) {
                let div = document.createElement("div");
                div.setAttribute("class", "m-1 p-1 groupQuestionnaire border rounded");

                //hidden input
                let hiddenInput = document.createElement("input");
                hiddenInput.setAttribute("type", "hidden");
                hiddenInput.value = item.id;
                //hiddenInput.dataset.group_questionnaire_id = item.id;
                div.appendChild(hiddenInput);

                //Start Questionnaire
                let divQuestionnaire = document.createElement("div");
                divQuestionnaire.classList.add("row", "mb-1");

                let labelQuestionnaire = document.createElement("label");
                labelQuestionnaire.classList.add("col-sm-3", "col-form-label", "text-right");
                labelQuestionnaire.textContent = "Questionnaire:";
                divQuestionnaire.appendChild(labelQuestionnaire);

                let selectQuestionnaire = document.createElement("select");
                selectQuestionnaire.classList.add("selectpicker", "show-tick");
                selectQuestionnaire.setAttribute("name", "questionnaire");
                selectQuestionnaire.setAttribute("title", "Select");
                divQuestionnaire.appendChild(selectQuestionnaire);


                SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTIONNAIRE_FIELDS, "title", selectQuestionnaire, item.questionnaire.id).then(() => {
                    const selectElement = selectQuestionnaire;
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
                            GroupController.listGroup(GroupUI.idTableGroup);
                        } else {
                            null
                        }
                    });

                    console.log("Dropdown and event listener successfully initialized");
                }).catch(error => console.error("Error initializing dropdown:", error));

                div.appendChild(divQuestionnaire);
                //End Questionnaire

                //Start Start Date Time
                let divActiveDateTime = document.createElement("div");
                divActiveDateTime.classList.add("row", "mb-1");

                let labelActiveDateTime = document.createElement("label");
                labelActiveDateTime.classList.add("col-sm-3", "col-form-label", "text-right");
                labelActiveDateTime.textContent = "Start Date Time:";
                divActiveDateTime.appendChild(labelActiveDateTime);

                let inputStartDateTime = document.createElement("input");
                inputStartDateTime.classList.add("form-control", "col-sm-8");
                inputStartDateTime.setAttribute("name", "startDateTime");
                inputStartDateTime.setAttribute("type", "text");
                DateTimeUtil.attachDateTimePicker(inputStartDateTime);
                $(inputStartDateTime).data("daterangepicker").setStartDate(DateTimeUtil.prepareDateTimeForInput(parseInt(item.startDateTime)));
                divActiveDateTime.appendChild(inputStartDateTime);

                div.appendChild(divActiveDateTime);
                //End Start Date Time

                //Start End Date Time
                let divEndDateTime = document.createElement("div");
                divEndDateTime.classList.add("row", "mb-1");

                let labelEndDateTime = document.createElement("label");
                labelEndDateTime.classList.add("col-sm-3", "col-form-label", "text-right");
                labelEndDateTime.textContent = "End Date Time:";
                divEndDateTime.appendChild(labelEndDateTime);

                let inputEndDateTime = document.createElement("input");
                inputEndDateTime.classList.add("form-control", "col-sm-8");
                inputEndDateTime.setAttribute("name", "endDateTime");
                inputEndDateTime.setAttribute("type", "text");
                DateTimeUtil.attachDateTimePicker(inputEndDateTime);
                $(inputEndDateTime).data("daterangepicker").setStartDate(DateTimeUtil.prepareDateTimeForInput(parseInt(item.endDateTime)));
                divEndDateTime.appendChild(inputEndDateTime);

                div.appendChild(divEndDateTime);
                //End End Date Time

                //Start Reminder Message
                let divReminderMessage = document.createElement("div");
                divReminderMessage.classList.add("row", "mb-1");

                let labelReminderMessage = document.createElement("label");
                labelReminderMessage.classList.add("col-sm-3", "col-form-label", "text-right");
                labelReminderMessage.textContent = "Reminder Message:";
                divReminderMessage.appendChild(labelReminderMessage);

                let textareaReminderMessage = document.createElement("textarea");
                textareaReminderMessage.classList.add("form-control", "col-sm-8");
                textareaReminderMessage.setAttribute("name", "reminderMessage");
                textareaReminderMessage.value = item.reminderMessage;
                divReminderMessage.appendChild(textareaReminderMessage);

                div.appendChild(divReminderMessage);
                //End Reminder Message

                //Start Reminder Time Interval
                let divReminderTimeInterval = document.createElement("div");
                divReminderTimeInterval.classList.add("row", "mb-1");

                let labelReminderTimeInterval = document.createElement("label");
                labelReminderTimeInterval.classList.add("col-sm-3", "col-form-label", "text-right");
                labelReminderTimeInterval.textContent = "Reminder Time Interval (hh:mm):";
                divReminderTimeInterval.appendChild(labelReminderTimeInterval);

                let reminderHours = Math.trunc(item.reminderTimeInterval / 60);
                let reminderMinutes = item.reminderTimeInterval % 60;
                let inputReminderTimeIntervalHour = document.createElement("input");
                inputReminderTimeIntervalHour.classList.add("form-control", "col-sm-3");
                inputReminderTimeIntervalHour.setAttribute("name", "reminderTimeIntervalHour");
                inputReminderTimeIntervalHour.setAttribute("type", "number");
                inputReminderTimeIntervalHour.setAttribute("step", "1");
                inputReminderTimeIntervalHour.setAttribute("min", "0");
                inputReminderTimeIntervalHour.setAttribute("max", "24");
                inputReminderTimeIntervalHour.setAttribute("placeholder", "hh");
                //inputReminderTimeIntervalHour.value = item.reminderTimeInterval / 60;
                inputReminderTimeIntervalHour.value = reminderHours;
                divReminderTimeInterval.appendChild(inputReminderTimeIntervalHour);

                let inputReminderTimeIntervalMinute = document.createElement("input");
                inputReminderTimeIntervalMinute.classList.add("form-control", "col-sm-3");
                inputReminderTimeIntervalMinute.setAttribute("name", "reminderTimeIntervalMinute");
                inputReminderTimeIntervalMinute.setAttribute("type", "number");
                inputReminderTimeIntervalMinute.setAttribute("step", "1");
                inputReminderTimeIntervalMinute.setAttribute("min", "0");
                inputReminderTimeIntervalMinute.setAttribute("max", "60");
                inputReminderTimeIntervalMinute.setAttribute("placeholder", "mm");
                inputReminderTimeIntervalMinute.value = reminderMinutes;
                divReminderTimeInterval.appendChild(inputReminderTimeIntervalMinute);

                div.appendChild(divReminderTimeInterval);
                //End Reminder Time Interval


                //Start Remove
                let buttonRemove = document.createElement("button");
                buttonRemove.setAttribute("class", "btn btn-link removeQuestionnaire");
                buttonRemove.textContent = "Remove ";
                div.appendChild(buttonRemove);
                //End Remove

                GroupUI.idQuestionnaireAdditionUpdate.appendChild(div);
            });

            //Remove Questionnaire
            $(GroupUI.idQuestionnaireAdditionUpdate).on("click", $(GroupUI.removeQuestionnaire), function (e) {
                e.preventDefault();
                if (e.target.getAttribute("class") === "btn btn-link removeQuestionnaire") {
                    let toRemove = e.target.parentNode;
                    toRemove.parentNode.removeChild(toRemove);
                }
            });
        }
    };

    let putValueInShow = function (group) {
        GroupUI.idTitleShow.textContent = group.title;
        GroupUI.idDescriptionShow.textContent = group.description || "";

        let members = "";
        if (group.users) {
            group.users.forEach(function (item) {
                members += item.firstName + " " + item.lastName + " (" + item.emailAddress + "), ";
            });
        }
        GroupUI.idMembersShow.textContent = members;

        let questionnaires = "";
        if (group.groupQuestionnaires) {
            group.groupQuestionnaires.forEach(function (item) {
                questionnaires += "["
                    + item.questionnaire.title
                    + " (" + DateTimeUtil.prepareDateTimeForText(parseInt(item.startDateTime)) + " to "
                    + DateTimeUtil.prepareDateTimeForText(parseInt(item.endDateTime)) + ") "
                    + item.reminderMessage
                    + " - "
                    + Math.trunc(item.reminderTimeInterval / 60) + ":" + item.reminderTimeInterval % 60 + "], ";
            });
        }
        GroupUI.idQuestionnaireShow.textContent = questionnaires;
    };

    //Add Questionnaire
    $(GroupUI.idBtnAddQuestionnaireUpdate).on("click", function (e) {
        e.preventDefault();

        let div = document.createElement("div");
        div.setAttribute("class", "m-1 p-1 groupQuestionnaire border rounded");

        //Start Questionnaire
        let divQuestionnaire = document.createElement("div");
        divQuestionnaire.classList.add("row", "mb-1");

        let labelQuestionnaire = document.createElement("label");
        labelQuestionnaire.classList.add("col-sm-3", "col-form-label", "text-right");
        labelQuestionnaire.textContent = "Questionnaire:";
        divQuestionnaire.appendChild(labelQuestionnaire);

        let selectQuestionnaire = document.createElement("select");
        selectQuestionnaire.classList.add("selectpicker", "show-tick");
        selectQuestionnaire.setAttribute("name", "questionnaire");
        selectQuestionnaire.setAttribute("title", "Select");
        selectQuestionnaire.setAttribute("required", "true");
        divQuestionnaire.appendChild(selectQuestionnaire);


        SelectPickerUtil.populateSelectPickerNew(EndPoints.QUESTIONNAIRE_FIELDS, "title", selectQuestionnaire).then(() => {
            const selectElement = selectQuestionnaire;
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
                    GroupController.listGroup(GroupUI.idTableGroup);
                } else {
                    null
                }
            });

            console.log("Dropdown and event listener successfully initialized");
        }).catch(error => console.error("Error initializing dropdown:", error));

        div.appendChild(divQuestionnaire);
        //End Questionnaire

        //Start Start Date Time
        let divActiveDateTime = document.createElement("div");
        divActiveDateTime.classList.add("row", "mb-1");

        let labelActiveDateTime = document.createElement("label");
        labelActiveDateTime.classList.add("col-sm-3", "col-form-label", "text-right");
        labelActiveDateTime.textContent = "Start Date Time:";
        divActiveDateTime.appendChild(labelActiveDateTime);

        let inputStartDateTime = document.createElement("input");
        inputStartDateTime.classList.add("form-control", "col-sm-8");
        inputStartDateTime.setAttribute("name", "startDateTime");
        inputStartDateTime.setAttribute("type", "text");
        inputStartDateTime.setAttribute("required", "true");
        divActiveDateTime.appendChild(inputStartDateTime);
        DateTimeUtil.attachDateTimePicker(inputStartDateTime);
        div.appendChild(divActiveDateTime);
        //End Start Date Time

        //Start End Date Time
        let divEndDateTime = document.createElement("div");
        divEndDateTime.classList.add("row", "mb-1");

        let labelEndDateTime = document.createElement("label");
        labelEndDateTime.classList.add("col-sm-3", "col-form-label", "text-right");
        labelEndDateTime.textContent = "End Date Time:";
        divEndDateTime.appendChild(labelEndDateTime);

        let inputEndDateTime = document.createElement("input");
        inputEndDateTime.classList.add("form-control", "col-sm-8");
        inputEndDateTime.setAttribute("name", "endDateTime");
        inputEndDateTime.setAttribute("type", "text");
        inputEndDateTime.setAttribute("required", "true");
        divEndDateTime.appendChild(inputEndDateTime);
        DateTimeUtil.attachDateTimePicker(inputEndDateTime);
        div.appendChild(divEndDateTime);
        //End End Date Time

        //Start Reminder Message
        let divReminderMessage = document.createElement("div");
        divReminderMessage.classList.add("row", "mb-1");

        let labelReminderMessage = document.createElement("label");
        labelReminderMessage.classList.add("col-sm-3", "col-form-label", "text-right");
        labelReminderMessage.textContent = "Reminder Message:";
        divReminderMessage.appendChild(labelReminderMessage);

        let textareaReminderMessage = document.createElement("textarea");
        textareaReminderMessage.classList.add("form-control", "col-sm-8");
        textareaReminderMessage.setAttribute("name", "reminderMessage");
        divReminderMessage.appendChild(textareaReminderMessage);

        div.appendChild(divReminderMessage);
        //End Reminder Message

        //Start Reminder Time Interval
        let divReminderTimeInterval = document.createElement("div");
        divReminderTimeInterval.classList.add("row", "mb-1");

        let labelReminderTimeInterval = document.createElement("label");
        labelReminderTimeInterval.classList.add("col-sm-3", "col-form-label", "text-right");
        labelReminderTimeInterval.textContent = "Reminder Time Interval (hh:mm):";
        divReminderTimeInterval.appendChild(labelReminderTimeInterval);

        let inputReminderTimeIntervalHour = document.createElement("input");
        inputReminderTimeIntervalHour.classList.add("form-control", "col-sm-3");
        inputReminderTimeIntervalHour.setAttribute("name", "reminderTimeIntervalHour");
        inputReminderTimeIntervalHour.setAttribute("type", "number");
        inputReminderTimeIntervalHour.setAttribute("step", "1");
        inputReminderTimeIntervalHour.setAttribute("min", "0");
        inputReminderTimeIntervalHour.setAttribute("max", "24");
        inputReminderTimeIntervalHour.setAttribute("placeholder", "hh");
        divReminderTimeInterval.appendChild(inputReminderTimeIntervalHour);

        let inputReminderTimeIntervalMinute = document.createElement("input");
        inputReminderTimeIntervalMinute.classList.add("form-control", "col-sm-3");
        inputReminderTimeIntervalMinute.setAttribute("name", "reminderTimeIntervalMinute");
        inputReminderTimeIntervalMinute.setAttribute("type", "number");
        inputReminderTimeIntervalMinute.setAttribute("step", "1");
        inputReminderTimeIntervalMinute.setAttribute("min", "0");
        inputReminderTimeIntervalMinute.setAttribute("max", "60");
        inputReminderTimeIntervalMinute.setAttribute("placeholder", "mm");
        divReminderTimeInterval.appendChild(inputReminderTimeIntervalMinute);

        div.appendChild(divReminderTimeInterval);
        //End Reminder Time Interval


        //Start Remove
        let buttonRemove = document.createElement("button");
        buttonRemove.setAttribute("class", "btn btn-link removeQuestionnaire");
        buttonRemove.textContent = "Remove ";
        div.appendChild(buttonRemove);
        //End Remove

        GroupUI.idQuestionnaireAdditionUpdate.appendChild(div);
    });

    $(GroupUI.idFormGroupUpdate).validate({
        rules: {
            title: "required"
        },
        messages: {
            title: "Title required."
        },
        submitHandler: function (form) {

            let requestBody = {
                "id": form.elements["id"].value,
                "title": form.elements["title"].value
            };
            if (form.elements["description"].value) {
                requestBody.description = form.elements["description"].value;
            }
            if ($(GroupUI.usersUpdate).val()) {
                requestBody.userIds = $(GroupUI.usersUpdate).val();
            }

            //Questionnaire
            let groupQuestionnaires = [];
            let questionnaires = document.querySelectorAll(".groupQuestionnaire");
            questionnaires.forEach(function (item) {
                let groupQuestionnaire = {};

                if (item.querySelector("input[type='hidden']") && item.querySelector("input[type='hidden']").value) {
                    groupQuestionnaire.id = item.querySelector("input[type='hidden']").value;
                }

                let questionnaireElem = item.getElementsByTagName("select")[0];
                if (questionnaireElem && questionnaireElem.value) {
                    groupQuestionnaire.questionnaireId = questionnaireElem.value;
                }

                let startDateTimeElem = item.querySelector("input[name='startDateTime']");
                if (startDateTimeElem && startDateTimeElem.value) {
                    const dateString = startDateTimeElem.value;
                    const format = 'YYYY-MM-DD hh:mm A';
                    const parsedDate = moment(dateString, format);

                    // Check if the parsed date is valid before converting to a timestamp
                    if (parsedDate.isValid()) {
                        groupQuestionnaire.startDateTime = parsedDate.valueOf();
                    } else {
                        console.error('Invalid date format:', dateString);
                    }
                    // groupQuestionnaire.startDateTime = new Date(startDateTimeElem.value).valueOf();
                }
                let endDateTimeElem = item.querySelector("input[name='endDateTime']");
                if (endDateTimeElem && endDateTimeElem.value) {
                    const dateString = endDateTimeElem.value;
                    const format = 'YYYY-MM-DD hh:mm A';
                    const parsedDate = moment(dateString, format);

                    // Check if the parsed date is valid before converting to a timestamp
                    if (parsedDate.isValid()) {
                        groupQuestionnaire.endDateTime = parsedDate.valueOf();
                    } else {
                        console.error('Invalid date format:', dateString);
                    }
                    // groupQuestionnaire.endDateTime = new Date(endDateTimeElem.value).valueOf();
                }

                let reminderMessageElem = item.querySelector("textarea[name='reminderMessage']");
                if (reminderMessageElem && reminderMessageElem.value) {
                    groupQuestionnaire.reminderMessage = reminderMessageElem.value;
                }

                let reminderTimeIntervalElemHour = item.querySelector("input[name='reminderTimeIntervalHour']");
                let reminderTimeIntervalElemMinute = item.querySelector("input[name='reminderTimeIntervalMinute']");
                let reminderTime;//In minute
                if (reminderTimeIntervalElemHour && reminderTimeIntervalElemHour.value) {
                    reminderTime = reminderTimeIntervalElemHour.value * 60;
                    groupQuestionnaire.reminderTimeInterval = reminderTime;
                }
                if (reminderTimeIntervalElemMinute && reminderTimeIntervalElemMinute.value) {
                    if (reminderTime) {
                        reminderTime += parseInt(reminderTimeIntervalElemMinute.value);
                    } else {
                        reminderTime = 0;
                        reminderTime += parseInt(reminderTimeIntervalElemMinute.value);
                    }
                    groupQuestionnaire.reminderTimeInterval = reminderTime;
                }

                if (groupQuestionnaire.questionnaireId) {
                    groupQuestionnaires.push(groupQuestionnaire);
                }
            });
            requestBody.groupQuestionnaires = groupQuestionnaires;

            GroupController.updateGroup(form, requestBody, function () {
                GroupController.listGroup(GroupUI.idTableGroup);
            });
        }
    });

    //Delete
    GroupUI.idBtnGroupDelete.addEventListener("click", function (event) {
        GroupController.deleteGroup(GroupUI.idModalGroupDelete, event.currentTarget.dataset.group_id, function () {
            GroupController.listGroup(GroupUI.idTableGroup);
        });
    });

    $(GroupUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(GroupUI.idModalGroup).modal("hide");
        $(GroupUI.idFormGroupAdd).trigger("reset");
        $(GroupUI.idFormGroupUpdate).trigger("reset");
        $(GroupUI.idModalGroupDelete).modal("hide");
        $(GroupUI.modalAlertBody).html("");
    });
});