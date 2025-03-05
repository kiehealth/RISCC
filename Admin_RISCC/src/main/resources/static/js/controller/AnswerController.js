'use strict';

import * as EndPoints from "./EndPoints.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as DateTimeUtil from "../util/DateTimeUtil.js";
import * as CommonUtil from "../util/CommonUtil.js";

export let listAnswer = function (tableElem, api) {
    $(tableElem).DataTable({
        dom: 'lBfrtip',
        buttons: [
            // {
            //     extend: 'csv',
            //     className: 'btn btn-primary',
            //     text: '<i class="fas fa-file-csv"></i> CSV',
            //     exportOptions: {
            //         columns: ':visible'
            //     }
            // },
            // {
            //     extend: 'print',
            //     text: '<i class="fas fa-print"></i> Print',
            //     exportOptions: {
            //         columns: ':visible'
            //     }
            // },
            // {
            //     extend: 'colvis',
            //     text: '<i class="fas fa-eye"></i> Column Visibility'
            // }
        ],
        searching: false,
        ajax: {
            url: api ? api : EndPoints.ANSWER,
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
                            dateTime: DateTimeUtil.prepareDateTimeForText(parseInt(item.dateTime)),
                            questionnaire: item.questionnaire.title,
                            user: item.user.firstName + " " + item.user.lastName + "(" + item.user.emailAddress + ")",
                            title: item.question.title,
                            body: item.question.body || "",
                            answer: item,
                            risccValue:item,
                            questionType: item.question.questionType.title,
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
            {"data": "dateTime"},
            {"data": "questionnaire"},
            {"data": "user"},
            {"data": "title"},
            {
                "data": "body",
                "orderable":false,
                "render": data => {
                    if (data.length > 200) {
                        return data.substr(0, 200) + "...";
                    }
                    return data;
                }
            },
            {
                "data": "answer",
                "orderable":false,
                "render": data => {
                    let answer = "";
                    if (data) {
                        if (data.answerOptions) {
                            data.answerOptions.forEach(function (item) {
                                answer += item.title + ", ";
                            });
                            answer = answer.substr(0, answer.length - 2);
                        } else {
                            answer = data.answer;
                        }
                        return answer;
                    }
                    return answer;
                }
            },
            {"data":"risccValue",
                "orderable":false,
              "render" : data => {
                let risccValue= 0;
                if (data){
                    if (data.answerOptions) {
                        data.answerOptions.forEach(function (item) {
                            risccValue += Number(item.risccValue);
                        });
                    } else{
                        risccValue = "0";
                    }
                }
                return risccValue;
              }
            },
            {"data": "questionType",
            "orderable":false},
            {
                "width": "10%",
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {

                    let returnValue = "";

                    // if (CommonUtil.hasAuthority("Answer (Delete)")) {
                    //     let spanDelete = document.createElement("span");
                    //     spanDelete.setAttribute("title", "Delete");
                    //     spanDelete.setAttribute("class", "answerDelete btn color-red");
                    //     spanDelete.setAttribute("data-answer_id", data.id);
                    //     let iDelete = document.createElement("i");
                    //     iDelete.setAttribute("class", "fas fa-trash");
                    //     spanDelete.appendChild(iDelete);
                    //     returnValue += spanDelete.outerHTML;
                    // }

                    return returnValue;
                }
            }
        ]
    });
};

export let exportAnswer = function (elem, api) {
    CallApi.downloadFile(elem, api);
};

export let deleteAnswer = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.ANSWER + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let risccCalculation = function (tableElem, api) {
    $(tableElem).DataTable({
        paging: false,
        info:false,
        ajax: {
            url: api,
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
                let reqData = parsedData.data.risccValue;
                let countr = parsedData.startPosition;

                 let arr = []
                if (reqData) {
                    reqData.forEach(function (item) {
                        arr.push({
                            risccValueType: item.calculationType,
                            risccValue: item.risccValue,
                            message: "Message : " + (item.message != null ? item.message : " "),
                        });
                     });
                }

                let filteredData = {
                    "data": arr,
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
              "data": "risccValueType"
            },
            {
               "data":"risccValue"
            },
            {
                "data": "message"
            }
        ]

    });
}
