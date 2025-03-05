'use strict';

import * as CommonUtil from "../util/CommonUtil.js";
import * as LinkUI from "../ui/LinkUI.js";
import * as LinkController from "../controller/LinkController.js";

$(document).ready(function () {
    CommonUtil.initialSetup();

    if (!CommonUtil.hasAuthority("Link (Create)")) {
        $(LinkUI.idBtnPopAddLink).hide();
    } else {
        LinkUI.idBtnPopAddLink.addEventListener("click", function (event) {
            $(LinkUI.idFormLinkUpdate).hide();
            $(LinkUI.idFormLinkAdd).show();
            $(LinkUI.modalLinkTitle).textContent = "Add Link";
            $(LinkUI.idModalLink).modal("show");
        });

        $(LinkUI.idFormLinkAdd).validate({
            rules: {
                title: "required"
            },
            messages: {
                title: "Title required."
            },
            submitHandler: function (form) {
                let requestBody = {
                    "title": form.querySelector("input[name='title']").value
                };
                if (form.elements["description"].value) {
                    requestBody.description = form.elements["description"].value;
                }
                if (form.elements["emailAddress"].value) {
                    requestBody.emailAddress = form.elements["emailAddress"].value;
                }
                if (form.elements["contactNumber"].value) {
                    requestBody.contactNumber = form.elements["contactNumber"].value;
                }
                if (form.elements["url"].value) {
                    requestBody.url = form.elements["url"].value;
                }

                LinkController.addLink(form, requestBody, function () {
                    LinkController.listLink(LinkUI.idTableLink);
                });
            }
        });
    }

    LinkController.listLink(LinkUI.idTableLink);
    $(LinkUI.idTableLink).on('draw.dt', function () {
        let linkEdit = LinkUI.idTableLink.querySelectorAll(".linkEdit");
        Array.from(linkEdit).forEach(function (element) {
            element.addEventListener('click', function (event) {
                let link = event.currentTarget.dataset.link;
                putValueInEditForm(JSON.parse(link));
                $(LinkUI.idFormLinkAdd).hide();
                $(LinkUI.idFormLinkUpdate).show();
                LinkUI.modalLinkTitle.textContent = "Edit Link";
                $(LinkUI.idModalLink).modal("show");
            });
        });

        // Delete Event Listener
        let deletes = LinkUI.idTableLink.querySelectorAll(".linkDelete");
        Array.from(deletes).forEach(function (element) {
            element.addEventListener('click', function (event) {
                LinkUI.idBtnLinkDelete.dataset.link_id = event.currentTarget.dataset.link_id;
                $(LinkUI.idModalLinkDelete).modal("show");
            });
        });
    });

    let putValueInEditForm = function (link) {
        LinkUI.id.value = link.id;
        LinkUI.titleUpdate.value = link.title;
        if (link.description) {
            LinkUI.descriptionUpdate.value = link.description;
        }
        if (link.emailAddress) {
            LinkUI.emailAddressUpdate.value = link.emailAddress;
        }
        if (link.contactNumber) {
            LinkUI.contactNumberUpdate.value = link.contactNumber;
        }
        if (link.url) {
            LinkUI.urlUpdate.value = link.url;
        }
    };

    $(LinkUI.idFormLinkUpdate).validate({
        rules: {
            title: "required"
        },
        messages: {
            title: "Title required."
        },
        submitHandler: function (form) {
            let requestBody = {
                "id": form.elements["id"].value,
                "title": form.querySelector("input[name='title']").value
            };
            if (form.elements["description"].value) {
                requestBody.description = form.elements["description"].value;
            }
            if (form.elements["emailAddress"].value) {
                requestBody.emailAddress = form.elements["emailAddress"].value;
            }
            if (form.elements["contactNumber"].value) {
                requestBody.contactNumber = form.elements["contactNumber"].value;
            }
            if (form.elements["url"].value) {
                requestBody.url = form.elements["url"].value;
            }

            LinkController.updateLink(form, requestBody, function () {
                LinkController.listLink(LinkUI.idTableLink);
            });
        }
    });

    //Delete
    LinkUI.idBtnLinkDelete.addEventListener("click", function (event) {
        LinkController.deleteLink(LinkUI.idModalLinkDelete, event.currentTarget.dataset.link_id, function () {
            LinkController.listLink(LinkUI.idTableLink);
        });
    });

    $(LinkUI.idModalAlert).on("hidden.bs.modal", function (e) {
        $(LinkUI.idModalLink).modal("hide");
        $(LinkUI.idFormLinkAdd).trigger("reset");
        $(LinkUI.idFormLinkUpdate).trigger("reset");
        $(LinkUI.idModalLinkDelete).modal("hide");
        $(LinkUI.modalAlertBody).html("");
    });
});