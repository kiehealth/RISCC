'use strict';
import * as LoaderUtil from "./LoaderUtil.js";
import * as AlertMessageUtil from "./AlertMessageUtil.js";


export async function populateSelectPickerNew(api, titleKey, elem, selectedItem, extraOption) {
    try {
        if (elem.choicesInstance) {
            elem.choicesInstance.destroy();
            elem.choicesInstance = null;
        }

        elem.innerHTML = '';
        LoaderUtil.showLoader(elem);

        const response = await fetch(api, {
            method: 'GET',
            headers: { "Authorization": localStorage.getItem("token") }
        });
        const json = await response.json();

        if (!json.status) {
            AlertMessageUtil.alertMessage(json);
            return;
        }

        const options = [];
        if (extraOption) {
            options.push(new Option(extraOption, ''));
        }

        const dataList = json.data.list;
        dataList.forEach(item => {
            const content = Array.isArray(titleKey)
                ? titleKey.map(key => item[key] || '').join(' ').trim()
                : item[titleKey];
            const option = new Option(content, item.id);

            if (selectedItem) {
                if (Array.isArray(selectedItem) && selectedItem.includes(item.id)) {
                    option.selected = true;
                } else if (selectedItem === item.id) {
                    option.selected = true;
                }
            }
            options.push(option);
        });

        elem.options.length = 0;
        options.forEach(opt => elem.add(opt));

        if (elem.classList.contains('form-select')) {
            elem.dispatchEvent(new Event('change'));
        }
    } catch (error) {
        console.error("Error: ", error);
        AlertMessageUtil.alertMessage(error.message || 'Error occurred.');
    } finally {
        LoaderUtil.hideLoader(elem);

        selectedItem = null;
    }
}


export let populateSelectPicker = function (api, titleKey, elem, selectedItem, extraOption) {
    empty(elem);

    LoaderUtil.showLoader(elem);

    fetch(api, {
        method: 'GET',
        headers: {
            "Authorization": localStorage.getItem("token")
        }
    }).then(function (response) {
        return response.json();
    }).then(json => {
        if (json.status) {
            let response = json.data.list;
            let options = [];

            if (extraOption) {
                let option = $("<option />").text(extraOption);
                options.push(option);
            }

            response.forEach(function (item) {
                let option = $("<option />").val(item.id).text(item[titleKey]);
                if (Array.isArray(titleKey)) {
                    let content = "";
                    titleKey.forEach(function (key) {
                        content += item[key] + " ";
                    });
                    option.text(content);
                } else {
                    option.text(item[titleKey]);
                }

                if (selectedItem) {
                    if (Array.isArray(selectedItem)) {
                        selectedItem.forEach(function (itm) {
                            if (itm === item.id) {
                                option.attr("selected", "selected");
                            }
                        });
                    } else {
                        if (selectedItem === item.id) {
                            option.attr('selected', 'selected');
                        }
                    }
                }

                options.push(option);
            });
            if (options.length > 0) {
                $(elem).append(options);
                /*if (selectedItem) {
                    $(elem).val(selectedItem);
                }*/
                $(elem).selectpicker("refresh");
            }
        } else {
            AlertMessageUtil.alertMessage(json);
        }
    }).catch(error => {
        console.log("Error: ", error);
        AlertMessageUtil.alertMessage(error);
    }).finally(function () {
        LoaderUtil.hideLoader(elem);
    })
};

export let empty = function (elem) {
    if ($(elem).find('option:first').hasClass('bs-title-option')) {
        $(elem).find('option:not(:first)').remove();
    } else {
        $(elem).find('option').remove();
    }

    /*$(elem).find('option:not(:first)').remove();
    $(elem).selectpicker("refresh");*/
};

export let loadDataInSelectPicker = function (data, titleKey, elem, selectedItem, extraOption) {
    empty(elem);
    LoaderUtil.showLoader(elem);

    if (data) {
        let options = [];

        if (extraOption) {
            let option = document.createElement("option");
            option.textContent = extraOption;
            options.push(option);
        }

        data.forEach(function (item) {
            let option = document.createElement("option");
            option.value = item.id;
            if (Array.isArray(titleKey)) {
                let content = "";
                titleKey.forEach(function (key) {
                    content += item[key] + " ";
                });
                option.textContent = content;
            } else {
                option.textContent = item[titleKey];
            }

            if (selectedItem) {
                if (Array.isArray(selectedItem)) {
                    selectedItem.forEach(function (itm) {
                        if (itm === item.id) {
                            option.attr("selected", "selected");
                        }
                    });
                } else {
                    if (selectedItem === item.id) {
                        option.attr('selected', 'selected');
                    }
                }
            }

            options.push(option);
        });
        if (options.length > 0) {
            $(elem).append(options);
            /*if (selectedItem) {
                $(elem).val(selectedItem);
            }*/
            $(elem).selectpicker("refresh");
        }
    }
    LoaderUtil.hideLoader(elem);
};

export let populateAppAnalyticsKeyPair = function (api, elem, selectedItem, extraOption) {
    empty(elem);
    LoaderUtil.showLoader(elem);

    fetch(api, {
        method: 'GET',
        headers: {
            "Authorization": localStorage.getItem("token")
        }
    }).then(function (response) {
        return response.json();
    }).then(json => {
        if (json.status) {
            let response = json.data.appAnalyticsKeyPairs;
            let options = [];

            if (extraOption) {
                let option = $("<option />").text(extraOption);
                options.push(option);
            }

            response.forEach(function (item) {
                let option = $("<option />").val(item.id);
                option.attr('data-key1_id', item.appAnalyticsKeyOne.id);
                option.attr('data-key2_id', item.appAnalyticsKeyTwo.id);
                option.text(item.appAnalyticsKeyOne.title + " - " + item.appAnalyticsKeyTwo.title);


                if (selectedItem) {
                    if (Array.isArray(selectedItem)) {
                        selectedItem.forEach(function (itm) {
                            if (itm === item.id) {
                                option.attr("selected", "selected");
                            }
                        });
                    } else {
                        if (selectedItem === item.id) {
                            option.attr('selected', 'selected');
                        }
                    }
                }

                options.push(option);

                let optionDivider = $("<option />");
                optionDivider.attr("data-divider", true);
                options.push(optionDivider);
            });
            if (options.length > 0) {
                $(elem).append(options);
                /*if (selectedItem) {
                    $(elem).val(selectedItem);
                }*/
                $(elem).selectpicker("refresh");
            }
        } else {
            AlertMessageUtil.alertMessage(json);
        }
    }).catch(error => {
        console.log("Error: ", error);
        AlertMessageUtil.alertMessage(error);
    }).finally(function () {
        LoaderUtil.hideLoader(elem);
    });
};