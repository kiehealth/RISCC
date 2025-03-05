package com.chronelab.riscc.enums;

public enum ErrorMessage {
    //General Error
    ERR001("Something went wrong"),
    ERR002("Username and/or password is wrong."),
    ERR003("You are not Authorized."),
    ERR004("Invalid token."),
    ERR005("Super Admin cannot be deleted."),

    //Role
    ROL001("Role does not exist."),
    ROL002("Role already exists."),
    ROL003("Restricted Role."),
    ROL004("Role Id not provided."),

    //Verification
    VER001("Verification details are missing."),
    VER002("Verification code is wrong or expired."),
    VER003("Email address did not match."),
    VER004("Mobile number did not match."),
    VER005("Email Address or Mobile Number is missing."),
    VER006("Your Email Address has not been added to the allowed registration list. Please contact RISCC Support Team."),

    //User
    USR001("User does not exist."),
    USR002("User already exists."),
    USR003("You don't have permission to change the password."),
    USR004("Email Address is missing."),
    USR005("Email Address already exists."),
    USR006("Mobile Number already exists."),
    USR007("User with this email address already exists."),
    USR008("User with this mobile number already exists."),

    //Country
    COU001("Country does not exist."),
    COU002("Country already exists."),

    //State
    STA001("State already exists."),
    STA002("State does not exist."),

    //City
    CIT001("City already exists."),
    CIT002("City does not exist."),
    CIT003("There are addresses associated with this city."),

    //Authority
    AUT001("Authority already exists."),
    AUT002("Authority does not exist."),

    //About Us
    ABT001("About Use does not exist."),

    //App Analytics
    APA001("App Analytics Type does not exist."),

    //Group
    GRP001("Group already exists."),
    GRP002("Group does not exist."),

    //Questionnaire
    QUE001("Questionnaire does not exist.");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
