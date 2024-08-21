package pages;

import managers.PlaywrightFactory;

public class LoginPage {
    private PlaywrightFactory pf;
    private String env;

    public LoginPage(PlaywrightFactory pf) {
        this.pf = pf;
        this.env = pf.getProp().getProperty("env");
    }

    public void clickingOnContinue() {
        pf.log("clicking on continue button");
        pf.click("continueButton_xpath", true);
    }

    public void iVerifyTheCCFApplicationPage(String actualText) {
        pf.log("Verifying whether the user is navigated to Cloud Carbon Footprint page");
        pf.verifyText("ccfApplicationText_xpath", actualText, true);
    }

    public void clickLoginButton() {
        pf.log("clicking on login button");
        pf.waitForPopup(() -> pf.click("loginButton_xpath", true));
        pf.switchToTab(1);
    }

    public void verifySignInPage(String text) {
        pf.log("Verifying Sign in page");
        pf.verifyText("signInText_xpath", text, true);
    }

    public void iEnterEmailCredentialsInSignInPage(String emailAddress) {
        pf.log("Entering" + emailAddress + "credentials");
        pf.type("emailAddress_xpath", emailAddress, true);
    }

    public void clickNextButton() {
        pf.log("Clicking on Next button on Sign in page");
        pf.click("nextButton_xpath", true);
    }

    public void verifyUserNameIncorrectValidationMessage(String message) {
        pf.log("Verifying the username incorrect validation message");
        pf.verifyText("incorrectUserNameValidationText_xpath", message, true);
    }

    public void iEnterPasswordCredentialsInSignInPage(String password) {
        pf.log("Entering" + password + "credentials");
        pf.type("passwordField_xpath", password, true);
    }

    public void clickSignInButton() {
        pf.log("Click on Sign in button");
        pf.click("signInButton_xpath", true);
    }

    public void verifyPasswordIncorrectValidationMessage(String message) {
        pf.log("Verifying the Password incorrect validation message");
        pf.verifyText("passwordIncorrectValidationText_xpath", message, true);
    }

    public void verifyStaySignedInMessage(String message) {
        pf.log("Verifying the Stay signed in? message");
        pf.verifyText("staySignedInAlertText_xpath", message, true);
    }

    public void selectTheCheckBox() {
        pf.log("Don't show this again checkbox");
        pf.click("don'tShowCheckBox_xpath", true);
    }

    public void clickNoButton() {
        pf.log("Click on the No button");
        pf.click("noButton_xpath", true);
        pf.switchToTab(0);
    }
}