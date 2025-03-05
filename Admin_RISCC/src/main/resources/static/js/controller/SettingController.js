'use strict';

import * as EndPoints from "./EndPoints.js";
import * as LoaderUtil from "../util/LoaderUtil.js";
import * as CallApi from "../util/CallApi.js";
import * as AlertMessageUtil from "../util/AlertMessageUtil.js";
import * as CommonUtil from "../util/CommonUtil.js";

//App Version
export let listAppVersion = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        serverSide: false,
        ajax: {
            url: EndPoints.APP_VERSION,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(elemTable);
            },
            "data": function (d) {
                /*d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "";
                delete d.columns;
                delete d.order;
                delete d.search;*/
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(elemTable);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.appVersion;

                let arr = [];
                if (reqData) {
                    reqData.forEach(function (item) {
                        arr.push({
                            family: item.family,
                            version: item.version,
                            forceUpdate: item.forceUpdate,
                            url: item.url || "",
                            action: item
                        });
                    });
                }
                let filteredData = {
                    "data": arr
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
            {"data": "family"},
            {"data": "version"},
            {"data": "forceUpdate"},
            {"data": "url"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("App Version (Update)")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "appVersionEdit btn color-orange");
                        spanEdit.setAttribute("data-app_version", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    return returnValue;
                }
            }
        ]
    });
};

export let updateAppVersion = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.APP_VERSION, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

//Setting
export let listSetting = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        serverSide: false,
        ajax: {
            url: EndPoints.SETTING,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(elemTable);
            },
            "data": function (d) {
                /*d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "";
                delete d.columns;
                delete d.order;
                delete d.search;*/
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(elemTable);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.settings;

                let arr = [];
                if (reqData) {
                    reqData.forEach(function (item) {
                        arr.push({
                            title: item.keyTitle,
                            value: item.keyValue,
                            action: item
                        });
                    });
                }
                let filteredData = {
                    "data": arr
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
            {"data": "title"},
            {"data": "value"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("Setting (Update)")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "settingEdit btn color-orange");
                        spanEdit.setAttribute("data-setting", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    return returnValue;
                }
            }
        ]
    });
};

export let updateSetting = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.SETTING, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

//About Us
export let listAboutUs = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        serverSide: false,
        ajax: {
            url: EndPoints.ABOUT_US,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(elemTable);
            },
            "data": function (d) {
                /*d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "";
                delete d.columns;
                delete d.order;
                delete d.search;*/
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(elemTable);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.aboutUs;

                let arr = [];
                if (reqData) {
                    arr.push({
                        name: reqData.name,
                        phone: reqData.phone,
                        email: reqData.email,
                        action: reqData
                    });
                }
                let filteredData = {
                    "data": arr
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
            {"data": "name"},
            {"data": "phone"},
            {"data": "email"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("Setting (Update)")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "aboutUsEdit btn color-orange");
                        spanEdit.setAttribute("data-about_us", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    return returnValue;
                }
            }
        ]
    });
};

export let updateAboutUs = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.ABOUT_US, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

//Welcome
export let listWelcome = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        serverSide: false,
        ajax: {
            url: EndPoints.WELCOME,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(elemTable);
            },
            "data": function (d) {
                /*d.pageNumber = d.start / d.length;
                d.pageSize = d.length;
                d.sortOrder = d.order && d.columns ? d.order[0].dir : "";
                d.sortBy = d.order && d.columns ? d.columns[d.order[0].column].data : "";
                delete d.columns;
                delete d.order;
                delete d.search;*/
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(elemTable);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.welcome;

                let arr = [];
                if (reqData) {
                    arr.push({
                        language: "Swedish",
                        welcomeText: reqData.welcomeTextSwedish || "",
                        thankYouText: reqData.thankYouTextSwedish || "",
                        action: reqData
                    });
                    arr.push({
                        language: "English",
                        welcomeText: reqData.welcomeText || "",
                        thankYouText: reqData.thankYouText || "",
                        action: reqData
                    });
                    arr.push({
                        language: "Spanish",
                        welcomeText: reqData.welcomeTextSpanish || "",
                        thankYouText: reqData.thankYouTextSpanish || "",
                        action: reqData
                    });
                }
                let filteredData = {
                    "data": arr
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
            {"data": "language"},
            {"data": "welcomeText"},
            {"data": "thankYouText"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {
                    let returnValue = "";

                    if (CommonUtil.hasAuthority("Setting (Update)")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "welcomeEdit btn color-orange");
                        spanEdit.setAttribute("data-welcome", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    return returnValue;
                }
            }
        ]
    });
};

export let updateWelcome = function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.WELCOME, "PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};


//Resource File
export let listResourceFile = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        serverSide: false,
        ajax: {
            url: EndPoints.RESOURCE_FILE_PUBLIC,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                LoaderUtil.showLoader(elemTable);
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(elemTable);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.resourceFiles;

                let arr = [];
                if (reqData) {
                    reqData.forEach(function (item) {
                        arr.push({
                            title: item.title,
                            description: item.description || "",
                            file: item.url,
                            action: item
                        });
                    });
                }
                let filteredData = {
                    "data": arr
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
            {"data": "title"},
            {"data": "description"},
            {
                "data": "file",
                "render": function (data) {
                    return "<a target='_blank' href='" + EndPoints.BASE_URL + data + "'>See File</a>";
                }
            },
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {

                    let returnValue = "";

                    if (CommonUtil.hasAuthority("Setting (Update)")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "resourceFileEdit btn color-orange");
                        spanEdit.setAttribute("data-resource_file", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    if (CommonUtil.hasAuthority("Setting (Update)")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "resourceFileDelete btn color-red");
                        spanDelete.setAttribute("data-resource_file_id", data.id);
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

export let updateResourceFile = function (elem, requestBody = {}, callback) {
    CallApi.uploadFile(elem, EndPoints.RESOURCE_FILE, requestBody, "PUT")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let addResourceFile = async function (elem, requestBody = {}, callback) {
    CallApi.uploadFile(elem, EndPoints.RESOURCE_FILE, requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

export let deleteResourceFile = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.RESOURCE_FILE + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

// Riscc Range
export let listRisccRange = function (elemTable) {
    $(elemTable).DataTable({
        searching: false,
        serverSide: false,
        ajax: {
            url: EndPoints.RISCC_RANGE_VALUE,
            type: "GET",
            "beforeSend": function (xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

                LoaderUtil.showLoader(elemTable);
            },
            dataFilter: function (data, type) {
                let parsedData = JSON.parse(data);
                if (!parsedData.status) {
                    LoaderUtil.hideLoader(elemTable);
                    AlertMessageUtil.alertMessage(data);
                    return;
                }
                let reqData = parsedData.data.risccRange;
                let arr = [];
                if (reqData) {
                    reqData.forEach(function (item) {
                        arr.push({
                            toValue: item.toValue,
                            fromValue: item.fromValue,
                            message: item.message || "",
                            questionnaire:item.questionnaireRes.title,
                            action: item
                        });
                    });
                }
                let filteredData = {
                    "data": arr
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
            {"data": "fromValue"},
            {"data": "toValue"},
            {"data": "message"},
            {"data" : "questionnaire"},
            {
                "targets": -1,
                "data": "action",
                "orderable": false,
                "searchable": false,
                "render": function (data) {

                    let returnValue = "";

                    if (CommonUtil.hasAuthority("Setting (Update)")) {
                        let spanEdit = document.createElement("span");
                        spanEdit.setAttribute("title", "Edit");
                        spanEdit.setAttribute("class", "risccRangeEdit btn color-orange");
                        spanEdit.setAttribute("data-riscc_range", JSON.stringify(data));
                        let iEdit = document.createElement("i");
                        iEdit.setAttribute("class", "fas fa-edit");
                        spanEdit.appendChild(iEdit);
                        returnValue = spanEdit.outerHTML;
                    }

                    if (CommonUtil.hasAuthority("Setting (Update)")) {
                        let spanDelete = document.createElement("span");
                        spanDelete.setAttribute("title", "Delete");
                        spanDelete.setAttribute("class", "risccRangeDelete btn color-red");
                        spanDelete.setAttribute("data-riscc_range_id", data.id);
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

// add Riscc Range
export let addRisccRange = async function (elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.RISCC_RANGE_VALUE,"POST", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};

// update riscc range
export function updateRisccRange(elem, requestBody = {}, callback) {
    CallApi.callBackend(elem, EndPoints.RISCC_RANGE_VALUE,"PUT", requestBody)
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};



export let deleteRisccRange = async function (elem, id = {}, callback) {
    CallApi.callBackend(elem, EndPoints.RISCC_RANGE_VALUE + "/" + id, "DELETE")
        .then(response => {
            AlertMessageUtil.alertMessage(response);
            if (callback && typeof callback === 'function') {
                callback(response);
            }
        });
};
