package managers;


import pages.*;

public class PageObjectManager {

    private LoginPage loginPage;

    private HomePage homepage;

    private LandingPage landingPage;

    private PlaywrightFactory pf;

    public PageObjectManager() {
        this.pf = new PlaywrightFactory();
    }

    public PlaywrightFactory getPlaywrightFactory() {
        return pf;
    }

    public LoginPage getLoginPage() {
        if (loginPage == null)
            loginPage = new LoginPage(pf);
        return loginPage;
    }

    public HomePage getHomePage() {
        if (homepage == null)
            homepage = new HomePage(pf);
        return homepage;
    }

    public LandingPage getLandingPage() {
        if (landingPage == null)
            landingPage = new LandingPage(pf);
        return landingPage;
    }

}
