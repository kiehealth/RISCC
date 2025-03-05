'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";
import * as GeneralUtil from "../util/GeneralUtil.js";

export let listQuestion = function (elemTable, api) {
    $(elemTable).DataTable({
        searching: false,
        order: [[4, "asc"]],
        ajax: {
            url: api || EndPoints.QUESTION,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(elemTable);
            },
            "data": function (d) {
                d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "";
                delete d.columns;
                delete d.order;
                delete d.search;
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(elemTable);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.list;
                let counter = parsedData.startPosition;

                let arr = [];
                if (reqData) {
                    reqData.forEach(function (item) {
                        arr.push({
                            sn: (counter++),
                            title: item.title,
                            body: item.body,
                            questionType: item.questionType.title,
                            displayOrder: item.questionQuestionnaires,
                            action: item
                        });
                    });
                }
                let filteredData = {
                    "data": arr,
                    "recordsTotal": parsedData.totalRecord,
                    "recordsFiltered": parsedData.totalRecord
                };
                return JSON.stringify(filteredData);
            },
            error: function (xhr, error, code) {
                AlertMessageUtil.alertMessage(JSON.parse(xhr.responseText));
            },
            complete: function () {
                LoaderUtil.hideLoader(elemTable);
            }
        },

        columns: [
            {
                "data": "sn",
                "orderable": false,
                "searchable": false
            },
            {
                "width": "20%",
                "data": "title"
            },
            {
                "width": "35%",
                "data": "body",
                "render": function (data) {
                    if (data.length > 200) {
                        return data.substr(0, 200) + "...";
                    }
                    return data;
                }
            },
            {
                "width": "5%",
                "data": "questionType",
                "orderable" :false,
                "render": function (data) {
                    let splitted = data.split("_");
                    if (splitted.length > 1) {
                        let temp = GeneralUtil.capitalizeFirst(splitted[0]);
                        for (let i = 1; i < splitted.length; i++) {
                            temp += " " + GeneralUtil.capitalizeFirst(splitted[i]);
                        }
                        return temp;
                    } else {
                        return splitted;
                    }
                }
            },
            {
                "data": "displayOrder",
                "render": function (data) {
                    let questionnaires = "";
                    if (data) {
                        data.forEach(function (item) {
                            questionnaires += item.questionnaire.title + " [" + (item.displayOrder || "") + "], ";
                        });
                        questionnaires = questionnaires.substr(0, questionnaires.length - 2);
                    }
                    return questionnaires;
                }
            },
            {
                "width": "10%",
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {

                    let returnValue = "";

                    if (CommonUtil.hasAuthority("Question (Update)")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "questionEdit btn color-orange");
                        spanEdit.setAttribute("data-question", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "More Info");
                    spanShow.setAttribute("class", "questionShow btn color-green");
                    spanShow.setAttribute("data-question", JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue = returnValue + spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("Question (Delete)")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "questionDelete btn color-red");
                        spanDelete.setAttribute("data-question_id", data.id);
                        let iDelete = document.createElement("i");
                        iDelete.setAttribute("class", "fas fa-trash");
                        spanDelete.appendChild(iDelete);
                        returnValue = returnValue + spanDelete.outerHTML;
                    }

                    return returnValue;
                }
            }
        ]
    });
};

export let filterByQuestionnaire = function (tableElem, questionnaireId) {
    listQuestion(tableElem, EndPoints.QUESTION_BY_QUESTIONNAIRE + questionnaireId);
};

export let updateQuestion = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.QUESTION, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let addQuestion = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.QUESTION, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let importQuestion = async function (elem, requestBody = {}, callback) {
    CallApi.uploadFile(elem, EndPoints.QUESTION_IMPORT, requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteQuestion = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.QUESTION + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let exportQuestionTemplate = function (elem, api) {
    CallApi.downloadFile(elem, api);
};