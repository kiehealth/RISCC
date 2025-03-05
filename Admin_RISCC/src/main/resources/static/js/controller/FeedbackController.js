'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as DateTimeUtil from "../util/DateTimeUtil.js";

export let listFeedback = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        order: [[3, "desc"]],
        ajax: {
            url: EndPoints.FEEDBACK,
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
                            description: item.description,
                            createdDate: DateTimeUtil.prepareDateTimeForText(parseInt(item.date)),
                            feedbackBy: item.feedbackBy.firstName + " " + item.feedbackBy.lastName + " (" + item.feedbackBy.emailAddress + ")",
                            runningOS: item.runningOS || "",
                            phoneModel: item.phoneModel || ""
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
            {"data": "createdDate"},
            {
                "data": "feedbackBy",
                "orderable": false,
                "searchable": false
            },
            {
                "data": "runningOS",
                "orderable": false,
                "searchable": false
            },
            {
                "data": "phoneModel",
                "orderable": false,
                "searchable": false
            }
        ]
    });
};