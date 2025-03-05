import * as EndPoints from "./EndPoints.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as CommonUtil from "../util/CommonUtil.js";
import * as CallApi from "../util/CallApi.js";

export let listLink = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        ajax: {
            url: EndPoints.LINK,
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
                            emailAddress: item.emailAddress || "",
                            contactNumber: item.contactNumber || "",
                            url: item.url,
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
            {
                "data": "description",
                "render": function (data) {
                    if (data && data.length > 200) {
                        return data.substr(0, 200) + "...";
                    }
                    return data || "";
                }
            },
            {"data": "emailAddress"},
            {"data": "contactNumber"},
            {
                "data": "url",
                "width": "20%",
                "render": function (data) {
                    if (data) {
                        return "<a href='" + data + "'>" + data + "</a>";
                    }
                    return "";
                }
            },
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("Link (Update)")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "linkEdit btn color-orange");
                        spanEdit.setAttribute("data-link", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    if (CommonUtil.hasAuthority("Link (Delete)")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "linkDelete btn color-red");
                        spanDelete.setAttribute("data-link_id", data.id);
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

export let addLink = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.LINK, "POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let updateLink = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.LINK, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteLink = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.LINK + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback();
            }
        });
};