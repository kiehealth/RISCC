export let idModalAlert = document.querySelector("#idModalAlert");
export let modalAlertBody = idModalAlert.querySelector(".modal-body p");

export let idModalQuestion = document.querySelector("#idModalQuestion");
export let modalQuestionTitle = idModalQuestion.querySelector(".modal-title");

export let idBtnPopAddQuestion = document.querySelector("#idBtnPopAddQuestion");
export let idBtnPopImportQuestion = document.querySelector("#idBtnPopImportQuestion");

export let idTableQuestion = document.querySelector("#idTableQuestion");

//Add
export let idFormQuestionAdd = document.querySelector("#idFormQuestionAdd");
export let questionTypeAdd = idFormQuestionAdd.querySelector("select[name='questionType']");
export let questionnaireAdd = idFormQuestionAdd.querySelector("select[name='questionnaire']");
//For Question Option Add
export let idOptionContainerAdd = idFormQuestionAdd.querySelector("#idOptionContainerAdd");
export let idOptionAdditionAdd = idFormQuestionAdd.querySelector("#idOptionAdditionAdd");
export let idBtnAddOptionAdd = idFormQuestionAdd.querySelector("#idBtnAddOptionAdd");
export let removeOption = idFormQuestionAdd.querySelector(".removeOption");
//For Questionnaire Add
export let idQuestionnaireContainerAdd = idFormQuestionAdd.querySelector("#idQuestionnaireContainerAdd");
export let idQuestionnaireAdditionAdd = idFormQuestionAdd.querySelector("#idQuestionnaireAdditionAdd");
export let idBtnAddQuestionnaireAdd = idFormQuestionAdd.querySelector("#idBtnAddQuestionnaireAdd");
export let removeQuestionnaire = idFormQuestionAdd.querySelector(".removeQuestionnaire");

//Update
export let idFormQuestionUpdate = document.querySelector("#idFormQuestionUpdate");
export let id = idFormQuestionUpdate.querySelector("input[name='id']");
export let questionTypeUpdate = idFormQuestionUpdate.querySelector("select[name='questionType']");
export let questionnaireUpdate = idFormQuestionUpdate.querySelector("select[name='questionnaire']");
export let titleUpdate = idFormQuestionUpdate.querySelector("input[name='title']");
export let bodyUpdate = idFormQuestionUpdate.querySelector("textarea[name='body']");
//For Question Option Update
export let idOptionContainerUpdate = idFormQuestionUpdate.querySelector("#idOptionContainerUpdate");
export let idOptionAdditionUpdate = idFormQuestionUpdate.querySelector("#idOptionAdditionUpdate");
export let idBtnAddOptionUpdate = idFormQuestionUpdate.querySelector("#idBtnAddOptionUpdate");
export let removeOptionUpdate = idFormQuestionUpdate.querySelector(".removeOptionUpdate");
//For Questionnaire Update
export let idQuestionnaireContainerUpdate = idFormQuestionUpdate.querySelector("#idQuestionnaireContainerUpdate");
export let idQuestionnaireAdditionUpdate = idFormQuestionUpdate.querySelector("#idQuestionnaireAdditionUpdate");
export let idBtnAddQuestionnaireUpdate = idFormQuestionUpdate.querySelector("#idBtnAddQuestionnaireUpdate");
export let removeQuestionnaireUpdate = idFormQuestionAdd.querySelector(".removeQuestionnaireUpdate");

//Show
export let idQuestionShow = document.querySelector("#idQuestionShow");
export let idQuestionnaireShow = document.querySelector("#idQuestionnaireShow");
export let idTitleShow = document.querySelector("#idTitleShow");
export let idBodyShow = document.querySelector("#idBodyShow");
export let idQuestionTypeShow = document.querySelector("#idQuestionTypeShow");
export let idQuestionOptionShow = document.querySelector("#idQuestionOptionShow");

//Import
export let idFormQuestionImport = document.querySelector("#idFormQuestionImport");
export let idBtnQuestionTemplate = document.querySelector("#idBtnQuestionTemplate");

//Delete
export let idModalQuestionDelete = document.querySelector("#idModalQuestionDelete");
export let idBtnQuestionDelete = document.querySelector("#idBtnQuestionDelete");

//Question Filter
//export let idFormQuestionFilter = document.querySelector("#idFormQuestionFilter");
export let questionnaireFilter = document.querySelector("#questionnaireFilter");