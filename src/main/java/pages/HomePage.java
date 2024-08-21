package pages;

import managers.PlaywrightFactory;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePage {
    private PlaywrightFactory pf;
    private String env;

    public HomePage(PlaywrightFactory pf) {
        this.pf = pf;
        this.env = pf.getProp().getProperty("env");
    }

    public void verifyHomePage(String title) {
        assertThat(pf.getLocator("pageHeader_xpath",true)).hasText(title);
    }
}
