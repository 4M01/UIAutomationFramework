package space.amolchavan.core;

import space.amolchavan.driverFactory.WebDriverClient;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;

import java.util.HashMap;
import java.util.Map;

/**
 * This object can be used to create DB Object, initialization can happens once when we bootstrap this app*/
public class Provider {

    private static String environment, tenant;
    private WebDriverClient webDriverClient;
    private final Map<String, Object> dbClients = new HashMap<String, Object>();

    public void setEnvironmentAndTenant(String env, String tenant) {
        setEnvironment(env);
        Reporter.log("Environment used for execution [" + env + "]", true);
        setTenant(tenant);
        Reporter.log("Tenant used for execution [" + tenant + "]", true);
    }

    public void setEnvironment(String env) {
        environment = env;
    }

    public String getEnvironment(){
        return environment!=null?environment.toUpperCase():System.getProperty("environment").toUpperCase();
    }

    public String getTenant() {
        return tenant!=null?tenant.toUpperCase():System.getProperty("tenant").toUpperCase();
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public  WebDriverClient getWebDriverClient(String browser, ITestContext ctx, WebDriver driver) {
        if (webDriverClient == null 	|| !webDriverClient.getBrowser().equals(browser)) {
            webDriverClient = new WebDriverClient(driver);
            webDriverClient.setBrowser(browser);
        }
        return webDriverClient;
    }


}
