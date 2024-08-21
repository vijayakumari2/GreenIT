package managers;

import com.aventstack.extentreports.ExtentTest;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import org.apache.commons.io.FileUtils;
import org.testng.Reporter;
import reports.ExtentManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.testng.asserts.SoftAssert;


public class PlaywrightFactory {
    private Properties prop;
    private Properties envProp;
    private SoftAssert softAssert;

    private static ExtentTest test;

    private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();
    private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();

    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }

    public static Browser getBrowser() {
        return tlBrowser.get();
    }

    public static BrowserContext getBrowserContext() {
        return tlBrowserContext.get();
    }

    public static Page getPage() {
        return tlPage.get();
    }

    public static Page popupPage;

    public PlaywrightFactory() {
        // init the properties file
        if (prop == null) {
            prop = new Properties();
            envProp = new Properties();
            try {
                // Creating a File object for directory
                File directoryPath = new File(System.getProperty("user.dir") + "/src/test/resources/properties");
                // List of all files and directories
                File filesList[] = directoryPath.listFiles();
                for (File file : filesList) {
                    FileInputStream propertyFiles = new FileInputStream(
                            System.getProperty("user.dir") + "/src/test/resources/properties/" + file.getName());
                    prop.load(propertyFiles);
                }
                String env = prop.getProperty("env") + ".properties";
                FileInputStream envPropertyFile = new FileInputStream(
                        System.getProperty("user.dir") + "/src/test/resources/properties/" + env);
                envProp.load(envPropertyFile);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            softAssert = new SoftAssert();
        }
    }


    public Properties getEnvProp() {
        return envProp;
    }

    public Properties getProp() {
        return prop;
    }

    public Page openBrowser() {
//        playwright = Playwright.create();
//        for parallel execution need thread local
        tlPlaywright.set(Playwright.create());
        String browserName = prop.getProperty("browserName");
        switch (browserName.toLowerCase()) {
            case "chromium":
                tlBrowser.set(getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(Boolean.parseBoolean(prop.getProperty("headLess"))).setSlowMo(100)));

                break;
            case "firefox":
                tlBrowser.set(getPlaywright().firefox().launch(new BrowserType.LaunchOptions().setHeadless(Boolean.parseBoolean(prop.getProperty("headLess"))).setSlowMo(100)));

                break;
            case "webkit":
                tlBrowser.set(getPlaywright().webkit().launch(new BrowserType.LaunchOptions().setHeadless(Boolean.parseBoolean(prop.getProperty("headLess"))).setSlowMo(100)));
                break;
            case "chrome":
                tlBrowser.set(getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(Boolean.parseBoolean(prop.getProperty("headLess"))).setSlowMo(100)));
                break;
            default:
                System.out.println("Please provide proper browser ");
                break;
        }
        tlBrowserContext.set(getBrowser().newContext(new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos/")).setRecordVideoSize(900, 600)));
        getBrowserContext().tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        tlPage.set(getBrowserContext().newPage());
        return getPage();
    }

    public void init(ExtentTest test) {
        this.test = test;
    }

    public void navigateURL(String UrlKey) {
        getPage().navigate(envProp.getProperty(UrlKey));
    }

    public void navigateURL(String appURLkey, String subpageURL) {
        String url = envProp.getProperty(appURLkey) + prop.getProperty(subpageURL);
        getPage().navigate(url);
    }

    public void stopTracing() {
        getBrowserContext().tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }

    public static String takeScreenShot() {
        // fileName of the screenshot
        Date d = new Date();
        String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
        // Take Screen-shot
        String screenshotFilePath = ExtentManager.screenshotFolderPath + "/" + screenshotFile;
        byte[] screenshotBytes = getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotFilePath)).setFullPage(true));
        String base64Screenshot = null;
        base64Screenshot = Base64.getEncoder().encodeToString(screenshotBytes);
        test.log(Status.INFO, "Screenshot-> "
                + test.addScreenCaptureFromPath("screenshots" + "/" + screenshotFile));
        return base64Screenshot;
    }

    public Locator getLocator(String locatorKey, boolean assertType) {
        Locator l = null;
        if (locatorKey.endsWith("_xpath")) {
//           to locate an element by xpath or css
            l = getPage().locator(prop.getProperty(locatorKey));
        } else if (locatorKey.endsWith("_id")) {
//            to locate an element based on its data-testid attribute
            l = getPage().getByTestId(prop.getProperty(locatorKey));
        } else if (locatorKey.endsWith("_label")) {
//            to locate a form control by associated label's text.
            l = getPage().getByLabel(prop.getProperty(locatorKey));
        } else if (locatorKey.endsWith("_placeHolder")) {
//            to locate an input by placeholder.
            l = getPage().getByPlaceholder(prop.getProperty(locatorKey));
        } else if (locatorKey.endsWith("_text")) {
//            to locate by text content.
            l = getPage().getByText(prop.getProperty(locatorKey));
        } else if (locatorKey.endsWith("_altText")) {
//            to locate an element, usually image, by its text alternative.
            l = getPage().getByAltText(prop.getProperty(locatorKey));
        } else if (locatorKey.endsWith("_title")) {
//            to locate an element by its title attribute.
            l = getPage().getByTitle(prop.getProperty(locatorKey));
        } else if (locatorKey.endsWith("_link")) {
            l = getPage().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(prop.getProperty(locatorKey)));
        } else if (locatorKey.endsWith("_img")) {
            l = getPage().getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName(prop.getProperty(locatorKey)));
        } else {
            System.out.println("LocatorKey is not correct " + locatorKey);
            reportFailure("Locatorykey is not correct" + " " + locatorKey, assertType);

        }
        return l;
    }

    public void click(String locatorKey, boolean assertType) {
        getLocator(locatorKey, assertType).click();
    }

    public String getText(String locatorKey, boolean assertType) {
        return getLocator(locatorKey, assertType).innerText();
    }

    public void type(String locatorKey, String value, boolean assertType) {
        getLocator(locatorKey, assertType).fill(value);
    }

    public void quit() {
        if (getBrowser() != null) {
            getBrowser().close();
        }
    }

    public void waitForLocator(String locatorKey) {
        getPage().waitForSelector(locatorKey);
    }

    public void reportPass(String msg) {
        test.log(Status.PASS, MarkupHelper.createLabel(msg, ExtentColor.GREEN));
        takeScreenShot();
    }

    public void reportFailure(String failureMsg, boolean stopOnFailure) {
        test.log(Status.FAIL, MarkupHelper.createLabel(failureMsg, ExtentColor.RED));// report failure in extent report
        takeScreenShot();   // Put screen-shot in report

        softAssert.fail(failureMsg);// report failure in testng
        if (stopOnFailure == true) {
            Reporter.getCurrentTestResult().getTestContext().setAttribute("CriticalFailure", "Y");
            AssertAll();
        }
    }

    public void AssertAll() {
        softAssert.assertAll();
    }


    public void log(String msg) {
        test.log(Status.INFO, msg);
        System.out.println(msg);
    }

    public void getLocators(String locatorKey, String option, boolean assertType) {
        Locator buttons = getLocator(locatorKey, assertType);
        System.out.println(buttons.count());
        for (int i = 0; i <= buttons.count(); i++) {
            String text = buttons.nth(i).textContent();
            if (text.contains(option)) {
                buttons.nth(i).click();
                break;
            }
        }
    }

    /**
     * This Method is called to check whether text is present in the opened web page
     * or not
     *
     * @param locatorkey Is the web element(fetched from property file)
     * @param text       Is the text which needs to be verified
     * @param asserttype Is a boolean which tells what type of assertion should be
     *                   done in case of pass and failure whether hard assert or
     *                   sort assert should be done.
     * @return Boolen ,will return true if text is present or else will return false
     */
    public boolean verifyText(String locatorkey, String text, boolean assertType) {
        String ActualText = getLocator(locatorkey, assertType).textContent().trim().replaceAll("[\\t\\n\\r]+", " ");
        System.out.println(ActualText);
        // String ExpectedText = prop.getProperty(text);
        if (ActualText.equals(text)) {
            reportPass(text + " is present in the webpage based on the locatorkey");
            return true;
        } else {
            reportFailure(text + " is not present in the webpage based on the locatorkey", assertType);
            return false;
        }
    }

    public void clickRadioButton(String locatorKey) {
        getPage().check(prop.getProperty(locatorKey));
    }

    public void clickCheckBox(String locatorKey, boolean assertType) {
        getLocator(locatorKey, assertType).check();
    }

    public void saveLocalStorage(String user) {
        getBrowserContext().storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get(user + ".json")));
    }

    public Page restoreStorageState(String user) {
        tlBrowserContext.set(getBrowser().newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get(user + ".json"))));
        tlPage.set(getBrowserContext().newPage());
        return getPage();
    }

    public void download(String locatorKey, boolean assertType) {
        Download download = getPage().waitForDownload(() -> {
            getLocator(locatorKey, assertType).click();
        });
        download.saveAs(Paths.get("Downloads", download.suggestedFilename()));
        getPage().onDownload(downloads -> System.out.println(downloads.path()));
    }

    public void uploadFile(String locatorKey, String fileName, boolean assertType) {
        getLocator(locatorKey, assertType).setInputFiles(Paths.get(fileName));
    }

    public void uploadMultipleFiles(String locatorKey, String firstFileName, String secondFileName, boolean assertType) {
        getLocator(locatorKey, assertType).setInputFiles(new Path[]{Paths.get(firstFileName), Paths.get(secondFileName)});
    }

    public void removeAllSelectedFiles(String locatorKey, boolean assertType) {
        getLocator(locatorKey, assertType).setInputFiles(new Path[0]);
    }

    public void dragAndDrop(String draggingLocatorKey, String droppingLocatorKey, boolean assertType) {
        getLocator(draggingLocatorKey, assertType).dragTo(getLocator(droppingLocatorKey, assertType));
    }

    public void mouseHover(String locatorKey, boolean assertType) {
        getLocator(locatorKey, assertType).hover();
    }

    public void selectTextFromDropDown(String locatorKey, String option, boolean assertType) {
        getLocator(locatorKey, assertType).selectOption(new SelectOption().setLabel(option));
    }

    public void selectValueFromDropDown(String locatorKey, int value, boolean assertType) {
        getLocator(locatorKey, assertType).selectOption(new SelectOption().setIndex(value));
    }

    public void refreshPage() {
        getPage().reload();
    }

    public void waitForPageLoad() {
        getPage().waitForLoadState(LoadState.LOAD);
    }

    public void clearData(String locatorKey, boolean assertType) {
        getLocator(locatorKey, assertType).clear();
    }

    public void switchToTab(int tabIndex) {
        List<Page> pages = getBrowserContext().pages();
        if (pages.size() > tabIndex) {
            tlPage.set(pages.get(tabIndex));
            test.log(Status.PASS, "Switched to tab: " + tabIndex);
        } else {
            test.log(Status.FAIL, "Tab index out of bounds: " + tabIndex);
        }
    }

    public void waitForPopup(Runnable trigger) {
        popupPage = getPage().waitForPopup(trigger);
        popupPage.waitForLoadState();
    }

    public void pageInspector() {
        getPage().pause();
    }
}