import * as EndPoints from "./EndPoints.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as CommonUtil from "../util/CommonUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as DateTimeUtil from "../util/DateTimeUtil.js";
import * as GeneralUtil from "../util/GeneralUtil.js";

export let listNotification = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        ajax: {
            url: EndPoints.NOTIFICATION,
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
                            notificationType: GeneralUtil.capitalizeFirst(item.notificationType),
                            sentTo: item,
                            title: item.title,
                            description: item.description,
                            dateTime: DateTimeUtil.prepareDateTimeForText(parseInt(item.dateTime)),
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
            {"data": "notificationType"},
            {
                "data": "sentTo",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    if (data.roles) {
                        let roles = "";
                        data.roles.forEach(function (item) {
                            roles += item.title + ", ";
                        });
                        return GeneralUtil.trim(roles);

                    } else if (data.groups) {
                        let groups = "";
                        data.groups.forEach(function (item) {
                            groups += item.title + ", ";
                        });
                        return GeneralUtil.trim(groups);

                    } else if (data.users) {
                        let users = "";
                        data.users.forEach(function (item) {
                            users += item.firstName + " " + item.lastName + ", ";
                        });
                        return users.substr(0, users.length - 2);
                    } else {
                        return "";
                    }
                }
            },
            {"data": "title"},
            {"data": "description",
                "orderable":false
            },
            {"data": "dateTime"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("Notification (Delete)")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "notificationDelete btn color-red");
                        spanDelete.setAttribute("data-notification_id", data.id);
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

export let addNotification = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.NOTIFICATION, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteNotification = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.NOTIFICATION + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};