'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as RoleController from "../controller/RoleController.js";
import * as SelectPickerUtil from "../util/SelectPickerUtil.js";
import * as EndPoints from "../controller/EndPoints.js";
import {RoleModel} from "../model/RoleModel.js";
import * as RoleUI from "../ui/RoleUI.js";


$(document).ready(function () {
    CommonUtil.initialSetup();

    if (!CommonUtil.hasAuthority("Role (Create)")) {
        $(RoleUI.idBtnPopAddRole).hide();
    } else {
        RoleUI.idBtnPopAddRole.addEventListener("click", function (event) {
            $(RoleUI.idFormRoleUpdate).hide();
            $(RoleUI.idFormRoleAdd).show();
            // SelectPickerUtil.populateSelectPicker(EndPoints.AUTHORITY, "title", RoleUI.authoritiesAdd);

            SelectPickerUtil.populateSelectPickerNew(EndPoints.AUTHORITY,"title", RoleUI.authoritiesAdd).then(() => {
                const selectElement = RoleUI.authoritiesAdd;
                if (!selectElement) {
                    console.error("Dropdown not found");
                    return;
                }

                const choices = new Choices(selectElement, {
                    removeItemButton: true,
                    searchEnabled: false,
                    placeholderValue: "Select a AUTHORITY",
                    noChoicesText: "No AUTHORITY options available",
                });

                selectElement.addEventListener("change", () => {
                    const selectedValue = selectElement.value;
                    console.log("Selected value:", selectedValue);

                    if (!selectedValue) {
                        RoleController.listRole(RoleUI.idTableRole);
                    } else {
                        null
                    }
                });

                console.log("Dropdown and event listener successfully initialized");
            }).catch(error => console.error("Error initializing dropdown:", error));

            RoleUI.modalRoleTitle.textContent = "Add Role";
            $(RoleUI.idModalRole).modal("show");
        });

        $(RoleUI.idFormRoleAdd).validate({
            rules: {
                title: "required"
            },
            messages: {
                title: "Title required."
            },
            submitHandler: function (form) {
                RoleController.addRole(form,
                    new RoleModel(null, form.querySelector("input[name='title']").value, $(RoleUI.authoritiesAdd).val()),
                    function () {
                        RoleController.listRole(RoleUI.idTableRole);
                    }
                );
            }
        });
    }

    RoleController.listRole(RoleUI.idTableRole);
    $(RoleUI.idTableRole).on('draw.dt', function () {
        let roleEdit = RoleUI.idTableRole.querySelectorAll(".roleEdit");
        Array.from(roleEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let role = event.currentTarget.dataset.role;
                putValueInEditForm(JSON.parse(role));
                $(RoleUI.idFormRoleAdd).hide();
                $(RoleUI.idFormRoleUpdate).show();
                RoleUI.modalRoleTitle.textContent = "Edit Role";
                $(RoleUI.idModalRole).modal("show");
            });
        });
    });

    let putValueInEditForm = function (role) {
        RoleUI.id.value = role.id;
        RoleUI.titleUpdate.value = role.title;
        let selectedAuthorityIds = [];
        Array.from(role.authorities).forEach(function (value, index, array) {
            selectedAuthorityIds.push(value.id);
        });
        // SelectPickerUtil.populateSelectPicker(EndPoints.AUTHORITY, "title", RoleUI.authoritiesUpdate, selectedAuthorityIds);

        SelectPickerUtil.populateSelectPickerNew(EndPoints.AUTHORITY,"title", RoleUI.authoritiesUpdate, selectedAuthorityIds).then(() => {
            const selectElement = RoleUI.authoritiesUpdate;
            if (!selectElement) {
                console.error("Dropdown not found");
                return;
            }

            const choices = new Choices(selectElement, {
                removeItemButton: true,
                searchEnabled: false,
                placeholderValue: "Select a AUTHORITY",
                noChoicesText: "No AUTHORITY options available",
            });

            selectElement.addEventListener("change", () => {
                const selectedValue = selectElement.value;
                console.log("Selected value:", selectedValue);

                if (!selectedValue) {
                    RoleController.listRole(RoleUI.idTableRole);
                } else {
                    null
                }
            });

            console.log("Dropdown and event listener successfully initialized");
        }).catch(error => console.error("Error initializing dropdown:", error));
    };

    $(RoleUI.idFormRoleUpdate).validate({
        rules: {
            title: "required"
        },
        messages: {
            title: "Title required."
        },
        submitHandler: function (form) {
            RoleController.updateRole(form,
                new RoleModel(form.querySelector("input[name='id']").value, RoleUI.titleUpdate.value, $(RoleUI.authoritiesUpdate).val()),
                function () {
                    RoleController.listRole(RoleUI.idTableRole);
                }
            );
        }
    });

    $(RoleUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(RoleUI.idModalRole).modal("hide");
        $(RoleUI.idFormRoleAdd).trigger("reset");
        $(RoleUI.idFormRoleUpdate).trigger("reset");
        $(RoleUI.modalAlertBody).html("");
    });
});