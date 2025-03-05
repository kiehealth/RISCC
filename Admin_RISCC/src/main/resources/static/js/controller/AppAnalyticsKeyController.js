'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as CommonUtil from "../util/CommonUtil.js";

export let listAppAnalyticsKey = function (tableElem, api) {
    $(tableElem).DataTable({
        ajax: {
            url: api ? api : EndPoints.APP_ANALYTICS_KEY_PUBLIC,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(tableElem);
            },
            "data": function (d) {
                d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "";
                d.searchTerm = d.search.value;
                delete d.columns;
                delete d.order;
                delete d.search;
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(tableElem);
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
                            dataType: item.appAnalyticsKeyDataType,
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
                LoaderUtil.hideLoader(tableElem);
            }
        },

        columns: [
            {
                "data": "sn",
                "orderable": false,
                "searchable": false
            },
            {"data": "title"},
            {
                "data": "description",
                "render": function (data) {
                    if (data.length > 200) {
                        return data.substr(0, 200) + "...";
                    }
                    return data;
                }
            },
            {"data": "dataType"},
            {
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {

                    let returnValue = "";

                    if (CommonUtil.hasAuthority("App Analytics")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "appAnalyticsKeyEdit btn color-orange");
                        spanEdit.setAttribute("data-app_analytics_key", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    if (CommonUtil.hasAuthority("App Analytics")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "appAnalyticsKeyDelete btn color-red");
                        spanDelete.setAttribute("data-app_analytics_key_id", data.id);
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


export let updateAppAnalyticsKey = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.APP_ANALYTICS_KEY, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let addAppAnalyticsKey = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.APP_ANALYTICS_KEY, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};

export let deleteAppAnalyticsKey = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.APP_ANALYTICS_KEY + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};