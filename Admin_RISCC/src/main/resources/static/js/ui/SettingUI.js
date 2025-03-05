export let idModalAlert = document.querySelector("#idModalAlert");
export let modalAlertBody = idModalAlert.querySelector(".modal-body p");

//App Version
export let idTableAppVersion = document.querySelector("#idTableAppVersion");

export let idModalAppVersion = document.querySelector("#idModalAppVersion");
export let modalAppVersionTitle = idModalAppVersion.querySelector(".modal-title");

export let idFormAppVersionUpdate = document.querySelector("#idFormAppVersionUpdate");
export let idAppVersion = idFormAppVersionUpdate.querySelector("input[name='id']");
export let familyUpdate = idFormAppVersionUpdate.querySelector("input[name='family']");
export let versionUpdate = idFormAppVersionUpdate.querySelector("input[name='version']");
export let forceUpdateUpdate = idFormAppVersionUpdate.querySelectorAll("input[name='forceUpdate']");
export let urlUpdate = idFormAppVersionUpdate.querySelector("input[name='url']");

//Setting
export let idTableSetting = document.querySelector("#idTableSetting");

export let idModalSetting = document.querySelector("#idModalSetting");
export let modalSettingTitle = idModalSetting.querySelector(".modal-title");

export let idFormSettingUpdate = document.querySelector("#idFormSettingUpdate");
export let idSetting = idFormSettingUpdate.querySelector("input[name='id']");
export let keyTitleUpdate = idFormSettingUpdate.querySelector("input[name='keyTitle']");
export let keyValueUpdate = idFormSettingUpdate.querySelector("input[name='keyValue']");

//About Us
export let idTableAboutUs = document.querySelector("#idTableAboutUs");

export let idModalAboutUs = document.querySelector("#idModalAboutUs");
export let modalAboutUsTitle = idModalAboutUs.querySelector(".modal-title");

export let idFormAboutUsUpdate = document.querySelector("#idFormAboutUsUpdate");
export let idAboutUs = idFormAboutUsUpdate.querySelector("input[name='id']");
export let nameUpdate = idFormAboutUsUpdate.querySelector("input[name='name']");
export let phoneUpdate = idFormAboutUsUpdate.querySelector("input[name='phone']");
export let emailUpdate = idFormAboutUsUpdate.querySelector("input[name='email']");

//Welcome
export let idTableWelcome = document.querySelector("#idTableWelcome");

export let idModalWelcome = document.querySelector("#idModalWelcome");
export let modalWelcomeTitle = idModalWelcome.querySelector(".modal-title");

export let idFormWelcomeUpdate = document.querySelector("#idFormWelcomeUpdate");
export let idWelcome = idFormWelcomeUpdate.querySelector("input[name='id']");
export let welcomeTextUpdate = idFormWelcomeUpdate.querySelector("textarea[name='welcomeText']");
export let welcomeTextSwedishUpdate = idFormWelcomeUpdate.querySelector("textarea[name='welcomeTextSwedish']");
export let welcomeTextSpanishUpdate = idFormWelcomeUpdate.querySelector("textarea[name='welcomeTextSpanish']");
export let thankYouTextUpdate = idFormWelcomeUpdate.querySelector("input[name='thankYouText']");
export let thankYouTextSwedishUpdate = idFormWelcomeUpdate.querySelector("input[name='thankYouTextSwedish']");
export let thankYouTextSpanishUpdate = idFormWelcomeUpdate.querySelector("input[name='thankYouTextSpanish']");


//Resource File
export let idBtnPopAddResourceFile = document.querySelector("#idBtnPopAddResourceFile");
export let idTableResourceFile = document.querySelector("#idTableResourceFile");
export let idModalResourceFile = document.querySelector("#idModalResourceFile");
export let modalResourceFileTitle = idModalResourceFile.querySelector(".modal-title");
//Add
export let idFormResourceFileAdd = document.querySelector("#idFormResourceFileAdd");
//Update
export let idFormResourceFileUpdate = document.querySelector("#idFormResourceFileUpdate");
export let idResourceFile = idFormResourceFileUpdate.querySelector("input[name='id']");
export let resourceFileTitleUpdate = idFormResourceFileUpdate.querySelector(" input[name='title']");
export let resourceFileDescriptionUpdate = idFormResourceFileUpdate.querySelector("textarea[name='description']");
//Delete
export let idModalResourceFileDelete = document.querySelector("#idModalResourceFileDelete");
export let idBtnResourceFileDelete = document.querySelector("#idBtnResourceFileDelete");

// RISCC Range
export let idBtnPopAddRisccRange = document.querySelector("#idBtnPopAddRisccRange");
export let idTableRisccValueRange = document.querySelector("#idTableRisccValueRange");
export let idModalRisccValueRange = document.querySelector("#idModalRisccValueRange");
export let modalRisccRangeTitle = idModalRisccValueRange.querySelector(".modal-title");
// add
export let idFormRisccRangeAdd = document.querySelector("#idFormRisccRangeAdd");
export let questionnaireAdd = idFormRisccRangeAdd.querySelector("select[name='questionnaire']");

// update
export let idFormRisccRangeUpdate = document.querySelector("#idFormRisccRangeUpdate");
export let questionnaireUpdate = idFormRisccRangeUpdate.querySelector("select[name='questionnaire']");
export let idRisccRange = idFormRisccRangeUpdate.querySelector("input[name='id']");
export let risccFromValueUpdate = idFormRisccRangeUpdate.querySelector("input[name='fromValue']");
export let riscctoValueUpdate = idFormRisccRangeUpdate.querySelector("input[name='toValue']");
export let risccRangeMessageUpdate = idFormRisccRangeUpdate.querySelector("textarea[name='message']");
export let risccRangeMoreInfo = idFormRisccRangeUpdate.querySelector("textarea[name='moreInfo']");

// Delete
export let idModalRisccRangeDelete = document.querySelector("#idModalRisccRangeDelete");
export let idBtnRisccRangeDelete = document.querySelector("#idBtnRisccRangeDelete");