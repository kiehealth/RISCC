'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as AppAnalyticsReportController from "../controller/AppAnalyticsReportController.js";
import * as AppAnalyticsReportUI from "../ui/AppAnalyticsReportUI.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as UserController from "../controller/UserController.js";
import * as GeneralUtil from "../util/GeneralUtil.js";
import * as EndPoints from "../controller/EndPoints.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    //AppAnalyticsReportController.listAppAnalyticsReport(AppAnalyticsReportUI.idTableAppAnalyticsReport);

    let loadUserInSelectPicker = function (pageNumber, pageSize) {
        let callback = function (data) {
            SelectPickerUtil.loadDataInSelectPicker(data.data.list, ['firstName', 'lastName', 'emailAddress'], AppAnalyticsReportUI.users);
            loadUserPagination(data.currentPage, data.totalPage);
        };
        UserController.getUserWithLimitedFieldData(AppAnalyticsReportUI.users, pageNumber, pageSize, callback);
    };
    loadUserInSelectPicker(0, 100);

    let loadUserPagination = function (currentPageNumber, totalPages) {
        GeneralUtil.empty(AppAnalyticsReportUI.idUserSelectPagination);
        for (let i = 1; i <= totalPages; i++) {
            let pageItem = document.createElement("li");
            pageItem.setAttribute("class", "page-item");

            let pageLink = document.createElement("a");
            pageLink.setAttribute("class", "page-link");
            pageLink.setAttribute("href", "#");
            pageLink.textContent = i.toString();

            if (currentPageNumber === i) {
                pageItem.classList.add("disabled");
                pageItem.classList.add("active");
                pageLink.setAttribute("tabindex", "-1");
            }

            pageItem.appendChild(pageLink);

            AppAnalyticsReportUI.idUserSelectPagination.appendChild(pageItem);
        }
    };

    $(AppAnalyticsReportUI.idUserSelectPagination).on("click", '.page-item', function (event) {
        if (event.target.text) {
            let clickedPageNumber = event.target.text;
            loadUserInSelectPicker(clickedPageNumber - 1, 100);
        }
    });

    SelectPickerUtil.populateAppAnalyticsKeyPair(EndPoints.APP_ANALYTICS_KEY_PAIR, AppAnalyticsReportUI.appAnalyticsKeyPair);


    $(AppAnalyticsReportUI.idFormAppAnalyticsReportFilter).validate({
        rules: {},
        messages: {},
        submitHandler: function (form) {
            filterAppAnalyticsUtil();
        }
    });

    let filterAppAnalyticsUtil = function () {
        let api = EndPoints.APP_ANALYTICS_REPORT_FILTER + "?";
        let userIds = $(AppAnalyticsReportUI.users).val();
        if (userIds) {
            api += "userIds=" + userIds + "&";
        }

        let selectedPairs = $(AppAnalyticsReportUI.appAnalyticsKeyPair).val();

        if (selectedPairs && selectedPairs.length > 0) {
            selectedPairs.forEach(function (item) {
                api += "appAnalyticsKeyPairIds=" + item + "&";
            });
        }
        AppAnalyticsReportController.listAppAnalyticsReport(AppAnalyticsReportUI.idTableAppAnalyticsReport, api);
    };

    AppAnalyticsReportUI.idBtnExportSelectedCSV.addEventListener('click', function (event) {
        callExportApi(EndPoints.APP_ANALYTICS_REPORT_EXPORT_CSV, true);
    });

    AppAnalyticsReportUI.idBtnExportAllCSV.addEventListener('click', function (event) {
        callExportApi(EndPoints.APP_ANALYTICS_REPORT_EXPORT_CSV, false);
    });

    let callExportApi = function (api, isExportSelected) {
        if (isExportSelected) {
            let userIds = $(AppAnalyticsReportUI.users).val();
            if (userIds) {
                api += "userIds=" + userIds + "&";
            }

            let selectedPairs = $(AppAnalyticsReportUI.appAnalyticsKeyPair).val();
            if (selectedPairs && selectedPairs.length > 0) {
                selectedPairs.forEach(function (item) {
                    api += "appAnalyticsTypePairIds=" + item + "&";
                });
            }
        }
        AppAnalyticsReportController.exportAppAnalyticsReport(AppAnalyticsReportUI.idFormAppAnalyticsReportFilter, api);
    };

    $(AppAnalyticsReportUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(AppAnalyticsReportUI.modalAlertBody).html("");
    });
});