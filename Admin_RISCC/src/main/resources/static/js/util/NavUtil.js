import * as EndPoints from "../controller/EndPoints.js";

export let addNavMenu = function () {
    let navContainer = document.querySelector("#sidebar");
    navContainer.innerHTML =
        '<div class="sidebar-header">' +
        ' <h4><a href="' + EndPoints.BASE_URL + '#" id="linkDashboard"><img alt="RISCC Icon" height="100px" id="covid19Icon" src="/images/RISCC-icon.png"></a></h4>' +
        '</div>' +

        '<ul class="list-unstyled components">' +
        '            <p class="btn btn-link"><i class="fas fa-user fa-2x"></i> <a href="#" id="linkProfile"><span' +
        '                    id="loggedInUser"></span></a></p>' +

        '            <li id="linkQuestionContainer">' +
        '                <a href="#" id="linkQuestion">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-poll-h\'></i></div>' +
        '                        <div class="col-10">Question</div>' +
        '                    </div>' +
        '                </a>' +
        '            </li>' +

        '            <li id="linkAnswerContainer">' +
        '                <a href="#" id="linkAnswer">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-check\'></i></div>' +
        '                        <div class="col-10">Answer</div>' +
        '                    </div>' +
        '                </a>' +
        '            </li>' +

        '            <li id="linkLinkContainer">' +
        '                <a href="#" id="linkLink">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-info\'></i></div>' +
        '                        <div class="col-10">Link</div>' +
        '                    </div>' +
        '                </a>' +
        '            </li>' +

        '            <li id="linkUserContainer">' +
        '                <a href="#" id="linkUser">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-users-cog\'></i></div>' +
        '                        <div class="col-10">User</div>' +
        '                    </div>' +
        '                </a>' +
        '            </li>' +

        '            <li id="linkGroupContainer">' +
        '                <a href="#" id="linkGroup">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-users\'></i></div>' +
        '                        <div class="col-10">Group</div>' +
        '                    </div>' +
        '                </a>' +
        '            </li>' +

        '            <li id="linkNotificationContainer">' +
        '                <a href="#" id="linkNotification">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-flag\'></i></div>' +
        '                        <div class="col-10">Notification</div>' +
        '                    </div>' +
        '                </a>' +
        '            </li>' +

        '            <li id="linkFeedbackContainer">' +
        '                <a href="#" id="linkFeedback">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-comment-alt\'></i></div>' +
        '                        <div class="col-10">Feedback</div>' +
        '                    </div>' +
        '                </a>' +
        '            </li>' +

        //Utility Sub Menu dropdown
        '            <li id="utilitySubMenuContainer">' +
        '                <a aria-expanded="false" class="dropdown-toggle" data-toggle="collapse" href="#utilitySubMenu">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-tools\'></i></div>' +
        '                        <div class="col-10">Utility</div>' +
        '                    </div>' +
        '                </a>' +
        '               <ul class="collapse list-unstyled" id="utilitySubMenu">' +
        '                   <li id="linkQuestionTypeContainer">' +
        '                       <a href="#" id="linkQuestionType">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-layer-group\'></i></div>' +
        '                               <div class="col-10">Question Type</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '                   <li id="linkRoleContainer">' +
        '                       <a href="#" id="linkRole">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-user-lock\'></i></div>' +
        '                               <div class="col-10">Role</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '                   <li id="linkAuthorityContainer">' +
        '                       <a href="#" id="linkAuthority">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-lock\'></i></div>' +
        '                               <div class="col-10">Authority</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '                   <li id="linkAllowedRegistrationContainer">' +
        '                       <a href="#" id="linkAllowedRegistration">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-lock\'></i></div>' +
        '                               <div class="col-10">Allowed Registration</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '                   <li id="linkErrorMessageContainer">' +
        '                       <a href="#" id="linkErrorMessage">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-language\'></i></div>' +
        '                               <div class="col-10">Error Message</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '               </ul>' +
        '            </li>' +

        //App Analytics sub menu dropdown
        '            <li id="appAnalyticsSubMenuContainer">' +
        '                <a aria-expanded="false" class="dropdown-toggle" data-toggle="collapse" href="#appAnalyticsSubMenu">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-chart-bar\'></i></div>' +
        '                        <div class="col-10">App Analytics</div>' +
        '                    </div>' +
        '                </a>' +
        '               <ul class="collapse list-unstyled" id="appAnalyticsSubMenu">' +
        '                   <li id="linkAppAnalyticsKeyContainer">' +
        '                       <a href="#" id="linkAppAnalyticsKey">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-hammer\'></i></div>' +
        '                               <div class="col-10">Key</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '                   <li id="linkAppAnalyticsKeyPairContainer">' +
        '                       <a href="#" id="linkAppAnalyticsKeyPair">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-chart-bar\'></i></div>' +
        '                               <div class="col-10">Key Pair</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '                   <li id="linkAppAnalyticsReportContainer">' +
        '                       <a href="#" id="linkAppAnalyticsReport">' +
        '                            <div class="row">' +
        '                                <div class="col-2"><i class=\'fas fa-chart-bar\'></i></div>' +
        '                               <div class="col-10">Report</div>' +
        '                           </div>' +
        '                       </a>' +
        '                   </li>' +
        '               </ul>' +
        '            </li>' +

        '            <li id="linkSettingContainer">' +
        '                <a href="#" id="linkSetting">' +
        '                    <div class="row">' +
        '                        <div class="col-2"><i class=\'fas fa-cog\'></i></div>' +
        '                        <div class="col-10">Setting</div>' +
        '                    </div>' +
        '                </a>' +
        '            </li>' +
        '        </ul>';
};

export let addNavigationLink = function () {

    let questionContainer = document.querySelector("#linkQuestionContainer");
    let answerContainer = document.querySelector("#linkAnswerContainer");
    let linkContainer = document.querySelector("#linkLinkContainer");
    let userContainer = document.querySelector("#linkUserContainer");
    let groupContainer = document.querySelector("#linkGroupContainer");
    let notificationContainer = document.querySelector("#linkNotificationContainer");
    let feedbackContainer = document.querySelector("#linkFeedbackContainer");

    let questionTypeContainer = document.querySelector("#linkQuestionTypeContainer");
    let roleContainer = document.querySelector("#linkRoleContainer");
    let authorityContainer = document.querySelector("#linkAuthorityContainer");
    let allowedRegistrationContainer = document.querySelector("#linkAllowedRegistrationContainer");
    let errorMessageContainer = document.querySelector("#linkErrorMessageContainer");

    let appAnalyticsKeyContainer = document.querySelector("#linkAppAnalyticsKeyContainer");
    let appAnalyticsKeyPairContainer = document.querySelector("#linkAppAnalyticsKeyPairContainer");
    let appAnalyticsReportContainer = document.querySelector("#linkAppAnalyticsReportContainer");

    let settingContainer = document.querySelector("#linkSettingContainer");

    //Hide all the links
    questionContainer.hidden = true;
    answerContainer.hidden = true;
    linkContainer.hidden = true;
    userContainer.hidden = true;
    groupContainer.hidden = true;
    notificationContainer.hidden = true;
    feedbackContainer.hidden = true;

    questionTypeContainer.hidden = true;
    roleContainer.hidden = true;
    authorityContainer.hidden = true;
    allowedRegistrationContainer.hidden = true;
    errorMessageContainer.hidden = true;

    appAnalyticsKeyContainer.hidden = true;
    appAnalyticsKeyPairContainer.hidden = true;
    appAnalyticsReportContainer.hidden = true;

    settingContainer.hidden = true;

    //cityContainer.hide();

    let authorities = JSON.parse(localStorage.getItem("authorities"));
    if (authorities) {
        for (let item of authorities) {
            if (item.title === "Question") {
                questionContainer.querySelector("#linkQuestion").href = EndPoints.BASE_URL + "/question.html";
                questionContainer.hidden = false;

                answerContainer.querySelector("#linkAnswer").href = EndPoints.BASE_URL + "/answer.html";
                answerContainer.hidden = false;
            } else if (item.title === "Link") {
                linkContainer.querySelector("#linkLink").href = EndPoints.BASE_URL + "/link.html";
                linkContainer.hidden = false;
            } else if (item.title === "User (Read All)") {
                userContainer.querySelector("#linkUser").href = EndPoints.BASE_URL + "/user.html";
                userContainer.hidden = false;
            } else if (item.title === "Group (Read All)") {
                groupContainer.querySelector("#linkGroup").href = EndPoints.BASE_URL + "/group.html";
                groupContainer.hidden = false;
            } else if (item.title === "Notification") {
                notificationContainer.querySelector("#linkNotification").href = EndPoints.BASE_URL + "/notification.html";
                notificationContainer.hidden = false;
            } else if (item.title === "Feedback (Read All)") {
                feedbackContainer.querySelector("#linkFeedback").href = EndPoints.BASE_URL + "/feedback.html";
                feedbackContainer.hidden = false;
            } else if (item.title === "Question Type") {
                questionTypeContainer.querySelector("#linkQuestionType").href = EndPoints.BASE_URL + "/question_type.html";
                questionTypeContainer.hidden = false;
            } else if (item.title === "Role (Read All)") {
                roleContainer.querySelector("#linkRole").href = EndPoints.BASE_URL + "/role.html";
                roleContainer.hidden = false;
            } else if (item.title === "Authority") {
                authorityContainer.querySelector("#linkAuthority").href = EndPoints.BASE_URL + "/authority.html";
                authorityContainer.hidden = false;
            } else if (item.title === "Allowed Registration") {
                allowedRegistrationContainer.querySelector("#linkAllowedRegistration").href = EndPoints.BASE_URL + "/allowed_registration.html";
                allowedRegistrationContainer.hidden = false;
            } else if (item.title === "Multi Lang Message") {
                errorMessageContainer.querySelector("#linkErrorMessage").href = EndPoints.BASE_URL + "/error_message.html";
                errorMessageContainer.hidden = false;
            } else if (item.title === "App Analytics") {
                appAnalyticsKeyContainer.querySelector("#linkAppAnalyticsKey").href = EndPoints.BASE_URL + "/app_analytics_key.html";
                appAnalyticsKeyContainer.hidden = false;

                appAnalyticsKeyPairContainer.querySelector("#linkAppAnalyticsKeyPair").href = EndPoints.BASE_URL + "/app_analytics_key_pair.html";
                appAnalyticsKeyPairContainer.hidden = false;

                appAnalyticsReportContainer.querySelector("#linkAppAnalyticsReport").href = EndPoints.BASE_URL + "/app_analytics_report.html";
                appAnalyticsReportContainer.hidden = false;
            } else if (item.title === "Setting") {
                settingContainer.querySelector("#linkSetting").href = EndPoints.BASE_URL + "/setting.html";
                settingContainer.hidden = false;
            }
        }
    }

    document.querySelector("#linkProfile").href = EndPoints.BASE_URL + "/user_profile.html";

    let url = window.location;
    $('ul li a[href="' + url + '"]').parent().addClass('active');
    $('ul li a').filter(function () {
        return this.href === url.toString();
    }).parent().addClass('active');
};
export let addToggleFunctionality = function () {
    $("#sidebar").mCustomScrollbar({
        theme: "minimal"
    });

    $('#sidebarCollapse').on('click', function () {
        $('#sidebar, #content').toggleClass('active');
        $('.collapse.in').toggleClass('in');
        $('a[aria-expanded=true]').attr('aria-expanded', 'false');
    });
};

/*window.onload = function () {
    let accessToken = localStorage.getItem("token");
    let date = localStorage.getItem("token_date");
    let expireIn = localStorage.getItem("expire_in");//Minutes
    if (accessToken && date && expireIn) {
        let currentMillis = (new Date()).getTime();
        let expireMillis = new Date(date).getTime() + (expireIn * 60 * 1000);
        if (currentMillis > expireMillis) {
            localStorage.clear();
            window.location = EndPoints.BASE_URL;
        } else {
            let loggedInUser = document.querySelector("#loggedInUser");
            if (loggedInUser) {
                let user = JSON.parse(localStorage.getItem("user"));
                loggedInUser.textContent = user.firstName + " " + (user.middleName ? user.middleName + " " : "") + user.lastName;
            }
        }
    }
};*/

document.querySelector("#idButtonLogout").addEventListener("click", function (event) {
    localStorage.clear();
    window.location = EndPoints.BASE_URL;
});