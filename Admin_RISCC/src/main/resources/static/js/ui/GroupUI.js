export let idModalAlert = document.querySelector("#idModalAlert");
export let modalAlertBody = idModalAlert.querySelector(".modal-body p");

export let idModalGroup = document.querySelector("#idModalGroup");
export let modalGroupTitle = idModalGroup.querySelector(".modal-title");

export let idBtnPopAddGroup = document.querySelector("#idBtnPopAddGroup");

export let idTableGroup = document.querySelector("#idTableGroup");

//Add
export let idFormGroupAdd = document.querySelector("#idFormGroupAdd");
export let usersAdd = idFormGroupAdd.querySelector("select[name='users']");
export let questionnaireAdd = idFormGroupAdd.querySelector("select[name='questionnaire']");
//For Questionnaire Add
export let idQuestionnaireContainerAdd = idFormGroupAdd.querySelector("#idQuestionnaireContainerAdd");
export let idQuestionnaireAdditionAdd = idFormGroupAdd.querySelector("#idQuestionnaireAdditionAdd");
export let idBtnAddQuestionnaireAdd = idFormGroupAdd.querySelector("#idBtnAddQuestionnaireAdd");
export let removeQuestionnaire = idFormGroupAdd.querySelector(".removeQuestionnaire");

//Update
export let idFormGroupUpdate = document.querySelector("#idFormGroupUpdate");
export let id = idFormGroupUpdate.querySelector("input[name='id']");
export let titleUpdate = idFormGroupUpdate.querySelector("input[name='title']");
export let descriptionUpdate = idFormGroupUpdate.querySelector("textarea[name='description']");
export let usersUpdate = idFormGroupUpdate.querySelector("select[name='users']");
//For Questionnaire Update
export let idQuestionnaireContainerUpdate = idFormGroupUpdate.querySelector("#idQuestionnaireContainerUpdate");
export let idQuestionnaireAdditionUpdate = idFormGroupUpdate.querySelector("#idQuestionnaireAdditionUpdate");
export let idBtnAddQuestionnaireUpdate = idFormGroupUpdate.querySelector("#idBtnAddQuestionnaireUpdate");
export let removeQuestionnaireUpdate = idFormGroupUpdate.querySelector(".removeQuestionnaireUpdate");

//Show
export let idGroupShow = document.querySelector("#idGroupShow");
export let idTitleShow = document.querySelector("#idTitleShow");
export let idDescriptionShow = document.querySelector("#idDescriptionShow");
export let idMembersShow = document.querySelector("#idMembersShow");
export let idQuestionnaireShow = document.querySelector("#idQuestionnaireShow");

//Delete
export let idModalGroupDelete = document.querySelector("#idModalGroupDelete");
export let idBtnGroupDelete = document.querySelector("#idBtnGroupDelete");