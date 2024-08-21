package teststeps;

import context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.HomePage;
import pages.LandingPage;
import pages.LoginPage;

public class LoginSteps {
    private TestContext context;
    private HomePage homepage;
    private LoginPage loginpage;
    private LandingPage landingPage;

    public LoginSteps(TestContext context) {
        this.context = context;
        this.homepage = context.getPageObjectManager().getHomePage();
        this.loginpage = context.getPageObjectManager().getLoginPage();
        this.landingPage = context.getPageObjectManager().getLandingPage();
    }

    @Before
    public void before(Scenario scenario) {
        String[] tags = scenario.getSourceTagNames().toArray(new String[0]);
        context.createScenario(scenario.getName(), tags);
        context.log("Starting scenario " + scenario.getName());
    }

    @After
    public void after(Scenario scenario) {
        context.log("Ending scenario " + scenario.getName());
        if (scenario.getStatus() == Status.FAILED) {
            context.getPageObjectManager().getPlaywrightFactory().reportFailure("Scenario has not been finished correctly", true);
        } else if (scenario.getStatus() == Status.PASSED) {
            context.getPageObjectManager().getPlaywrightFactory().reportPass("Scenario has  been finished correctly");
        }
        context.endScenario();
        context.getPageObjectManager().getPlaywrightFactory().quit();
    }

    @Given("I open Browser")
    public void launchBrowser() {
        landingPage.load();
    }

    @Given("I go to URL")
    public void navigateURl() {
        landingPage.url();
    }

    @And("I click on Continue button")
    public void clickContinue() {
        loginpage.clickingOnContinue();
    }

    @Then("I verify the Login Page - {string}")
    public void iVerifyTheCCFApplicationPage(String actualText) {
        loginpage.iVerifyTheCCFApplicationPage(actualText);
    }

    @And("I click on Login button")
    public void clickLoginButton() {
        loginpage.clickLoginButton();
    }

    @And("I verify user is navigated to {string} page")
    public void verifySignInPage(String text) {
        loginpage.verifySignInPage(text);
    }

    @When("I enter {string} email address credentials in Sign in Page")
    public void iEnterEmailCredentialsInSignInPage(String emailAddress) {
        loginpage.iEnterEmailCredentialsInSignInPage(emailAddress);
    }

    @And("I click on Next Button in Sign in page")
    public void clickNextButton() {
        loginpage.clickNextButton();
    }

    @Then("I verify the username incorrect validation message: {string}")
    public void verifyUserNameIncorrectValidationMessage(String message) {
        loginpage.verifyUserNameIncorrectValidationMessage(message);
    }

    @When("I enter {string} password credentials in Sign in Page")
    public void iEnterPasswordCredentialsInSignInPage(String password) {
        loginpage.iEnterPasswordCredentialsInSignInPage(password);
    }

    @And("I click on Sign in button")
    public void clickSignInButton() {
        loginpage.clickSignInButton();
    }

    @Then("I verify the password incorrect validation message: {string}")
    public void verifyPasswordIncorrectValidationMessage(String message) {
        loginpage.verifyPasswordIncorrectValidationMessage(message);
    }

    @And("I verify the {string} message")
    public void verifyStaySignedInMessage(String message) {
        loginpage.verifyStaySignedInMessage(message);
    }

    @Then("I select the Don't show this again checkbox option")
    public void selectTheCheckBox() {
        loginpage.selectTheCheckBox();
    }

    @Then("I click on the No button")
    public void clickNoButton() {
        loginpage.clickNoButton();
    }
}