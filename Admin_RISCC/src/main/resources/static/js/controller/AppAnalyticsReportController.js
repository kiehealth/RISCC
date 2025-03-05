'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as DateTimeUtil from "../util/DateTimeUtil.js";

export let listAppAnalyticsReport = function (tableElem, api) {
    $(tableElem).DataTable({
        serverSide: false,
        searching: false,
        order: [[5, "asc"]],
        ajax: {
            url: api ? api : EndPoints.APP_ANALYTICS_REPORT,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(tableElem);
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(tableElem);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.list;

                let arr = [];
                if (reqData) {
                    reqData.forEach(function (item) {
                        arr.push({
                            title1: item.title1,
                            value1: item.value1 ? DateTimeUtil.prepareDateTimeForText(parseInt(item.value1)) : "",
                            title2: item.title2,
                            value2: item.value2 ? DateTimeUtil.prepareDateTimeForText(parseInt(item.value2)) : "",
                            difference: item.difference,
                            user: item.user ? item.user.firstName + " " + item.user.lastName : ""
                        });
                    });
                }
                let filteredData = {
                    "data": arr,
                    "recordsTotal": reqData.length,
                    "recordsFiltered": reqData.length
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
            {"data": "title1"},
            {"data": "value1"},
            {"data": "title2"},
            {"data": "value2"},
            {"data": "difference"},
            {"data": "user"}
        ]
    });
};

export let exportAppAnalyticsReport = function (elem, api) {
    CallApi.downloadFile(elem, api);
};