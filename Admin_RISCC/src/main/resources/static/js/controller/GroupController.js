'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";
import * as DateTimeUtil from "../util/DateTimeUtil.js";

export let listGroup = function (elemTable, api) {
    $(elemTable).DataTable({
        searching: false,
        ajax: {
            url: api || EndPoints.GROUP,
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
                            description: item.description || "",
                            users: item.users ? item.users.length : "0",
                            questionnaire: item.groupQuestionnaires || "",
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
            {"data": "title"},
            {"data": "description"},
            {"data": "users",
            "orderable":false
            },

            {
                "data": "questionnaire",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (data) {
                        data.forEach(function (item) {
                            returnValue += "["
                                + item.questionnaire.title + "<br>"
                                + " (" + DateTimeUtil.prepareDateTimeForText(parseInt(item.startDateTime)) + " to "
                                + DateTimeUtil.prepareDateTimeForText(parseInt(item.endDateTime)) + ") <br>"
                                + item.reminderMessage
                                + " - "
                                + Math.trunc(item.reminderTimeInterval / 60) + ":" + (item.reminderTimeInterval % 60) + "]<br>";
                        });
                    }

                    return returnValue;
                }
            },
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue;

                    let spanShow = document.createElement("span");
                    spanShow.setAttribute("title", "Show");
                    spanShow.setAttribute("class", "groupShow btn color-green");
                    spanShow.setAttribute("data-group", JSON.stringify(data));
                    let iShow = document.createElement("i");
                    iShow.setAttribute("class", "fas fa-eye");
                    spanShow.appendChild(iShow);
                    returnValue = spanShow.outerHTML;

                    if (CommonUtil.hasAuthority("Group (Update)")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "groupEdit btn color-orange");
                        spanEdit.setAttribute("data-group", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue += spanEdit.outerHTML;
                    }

                    if (CommonUtil.hasAuthority("Group (Delete)")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "groupDelete btn color-red");
                        spanDelete.setAttribute("data-group_id", data.id);
                        let iDelete = document.createElement("i");
                        iDelete.setAttribute("class", "fas fa-trash");
                        spanDelete.appendChild(iDelete);
                        returnValue += spanDelete.outerHTML;
                    }

                    return returnValue;
                }
            }
        ]
    });
};

export let updateGroup = function (elem, requestBody = {}, callback) {
    console.log('UPDATE: ', requestBody);

    CallApi.callBackend(elem, EndPoints.GROUP, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let addGroup = async function (elem, requestBody = {}, callback) {
    console.log('Error: ', requestBody);

    CallApi.callBackend(elem, EndPoints.GROUP, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteGroup = async function (elem, groupId, callback) {
    CallApi.callBackend(elem, EndPoints.GROUP + "/" + groupId, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};