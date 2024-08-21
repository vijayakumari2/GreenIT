package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        // features = "@rerun/failed_scenarios.txt",
        features = "src/test/resources/features/login.feature", glue = {"teststeps"},
        tags = "@RegressionWorking")

public class loginRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}