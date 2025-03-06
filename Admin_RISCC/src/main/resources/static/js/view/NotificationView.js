'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as NotificationUI from "../ui/NotificationUI.js";
import * as NotificationController from "../controller/NotificationController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";
import * as GeneralUtil from "../util/GeneralUtil.js";
import * as UserController from "../controller/UserController.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    if (!CommonUtil.hasAuthority("Notification (Create)")) {
        $(NotificationUI.idBtnPopAddNotification).hide();
    } else {
        NotificationUI.idBtnPopAddNotification.addEventListener("click", function (event) {
            NotificationUI.idFormNotificationAdd.hidden = false;
            NotificationUI.userContainer.hidden = true;
            NotificationUI.roleContainer.hidden = true;
            NotificationUI.groupContainer.hidden = true;
            $(NotificationUI.modalNotificationTitle).textContent = "Send Notification";
            $(NotificationUI.idModalNotification).modal("show");
        });

        NotificationUI.notificationType.addEventListener("change", function (event) {
            if ($(NotificationUI.notificationType).val() === "ROLE") {
                SelectPickerUtil.populateSelectPickerNew(EndPoints.ROLE, "title", NotificationUI.roles)
                    .then(() => {
                        const selectElement = NotificationUI.roles;
                        if(!selectElement){
                            console.error("Dropdown not found");
                            return;
                        }

                        const choices = new Choices(selectElement, {
                            removeItemButton: true,
                            searchEnabled: false,
                            placeholderValue: "Select a ROLE",
                            noChoicesText: "No Role options available",
                        })

                        selectElement.addEventListener("change", () => {
                            const selectedValue = selectElement.value;
                            console.log("Selected value:", selectedValue);

                            if (!selectedValue){
                                NotificationController.listNotification(NotificationUI.idTableNotification);
                            } else {
                                null
                            }
                        });

                        console.log("Dropdown and event listener successfully initialized");
                    }).catch(error => console.error("Error initializing dropdown:", error));

                NotificationUI.userContainer.hidden = true;
                NotificationUI.groupContainer.hidden = true;
                NotificationUI.roleContainer.hidden = false;

            } else if ($(NotificationUI.notificationType).val() === "GROUP") {
                SelectPickerUtil.populateSelectPickerNew(EndPoints.GROUP, "title", NotificationUI.groups)
                    .then(() => {
                        const selectElement = NotificationUI.groups;

                        if (!selectElement) {
                            console.error("Dropdown not found");
                            return;
                        }

                        const choices = new Choices(selectElement, {
                            removeItemButton: true,
                            searchEnabled: false,
                            placeholderValue: "Select a Group",
                            noChoicesText: "No Group options available",
                        });

                        selectElement.addEventListener("change", () => {
                            const selectedValue = selectElement.value;
                            console.log("Selected value:", selectedValue);

                            if(!selectedValue){
                                NotificationController.listNotification(NotificationUI.idTableNotification);
                            } else {
                                null
                            }
                        });
                        console.log("Dropdown and event event listener successfully initialized");
                    }).catch(error => console.error("Error initializing dropdown:", error));

                NotificationUI.userContainer.hidden = true;
                NotificationUI.roleContainer.hidden = true;
                NotificationUI.groupContainer.hidden = false;

            } else if ($(NotificationUI.notificationType).val() === "INDIVIDUAL") {
                loadUserInSelectPicker(0, 100);
                NotificationUI.userContainer.hidden = false;
                NotificationUI.roleContainer.hidden = true;
                NotificationUI.groupContainer.hidden = true;

            } else {
                NotificationUI.userContainer.hidden = true;
                NotificationUI.roleContainer.hidden = true;
                NotificationUI.groupContainer.hidden = true;
            }
        });

        let loadUserInSelectPicker = function (pageNumber, pageSize) {
            let callback = function (data) {
                SelectPickerUtil.loadDataInSelectPicker(data.data.list, ['firstName', 'lastName', 'emailAddress'], NotificationUI.users);
                loadUserPagination(data.currentPage, data.totalPage);
            };
            UserController.getUserWithLimitedFieldData(NotificationUI.users, pageNumber, pageSize, callback);
        };

        let loadUserPagination = function (currentPageNumber, totalPages) {
            GeneralUtil.empty(NotificationUI.idUserSelectPagination);
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

                NotificationUI.idUserSelectPagination.appendChild(pageItem);
            }
        };

        $(NotificationUI.idUserSelectPagination).on("click", '.page-item', function (event) {
            if (event.target.text) {
                let clickedPageNumber = event.target.text;
                loadUserInSelectPicker(clickedPageNumber - 1, 100);
            }
        });

        $(NotificationUI.idFormNotificationAdd).validate({
            rules: {
                title: "required",
                description: "required",
                notificationType: "required"
            },
            messages: {
                title: "Title required.",
                description: "Description required.",
                notificationType: "Notification Type required."
            },
            submitHandler: function (form) {
                let requestBody = {
                    "title": form.elements["title"].value,
                    "description": form.elements["description"].value,
                    "notificationType": $(NotificationUI.notificationType).val()
                };
                if ($(NotificationUI.notificationType).val() === "ROLE") {
                    requestBody.roleIds = $(NotificationUI.roles).val();
                } else if ($(NotificationUI.notificationType).val() === "GROUP") {
                    requestBody.groupIds = $(NotificationUI.groups).val();
                } else if ($(NotificationUI.notificationType).val() === "INDIVIDUAL") {
                    requestBody.userIds = $(NotificationUI.users).val();
                }

                NotificationController.addNotification(form, requestBody, function () {
                    NotificationController.listNotification(NotificationUI.idTableNotification);
                });
            }
        });
    }

    NotificationController.listNotification(NotificationUI.idTableNotification);
    $(NotificationUI.idTableNotification).on('draw.dt', function () {
        // Delete Event Listener
        let deletes = NotificationUI.idTableNotification.querySelectorAll(".notificationDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                NotificationUI.idBtnNotificationDelete.dataset.notification_id = event.currentTarget.dataset.notification_id;
                $(NotificationUI.idModalNotificationDelete).modal("show");
            });
        });
    });

    //Delete
    NotificationUI.idBtnNotificationDelete.addEventListener("click", function (event) {
        NotificationController.deleteNotification(NotificationUI.idModalNotificationDelete, event.currentTarget.dataset.notification_id, function () {
            NotificationController.listNotification(NotificationUI.idTableNotification);
        });
    });

    $(NotificationUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(NotificationUI.idModalNotification).modal("hide");
        $(NotificationUI.idFormNotificationAdd).trigger("reset");
        $(NotificationUI.idModalNotificationDelete).modal("hide");
        $(NotificationUI.modalAlertBody).html("");
    });
});