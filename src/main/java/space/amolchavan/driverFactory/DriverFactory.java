package space.amolchavan.driverFactory;

import space.amolchavan.core.BaseTest;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.testng.Reporter;
import space.amolchavan.reporting.WebDriverEventListener;

import java.net.URL;

public class DriverFactory {

    enum Target {
        LOCAL, REMOTE
    }

    public WebDriver createInstance(String browser) throws Exception {
        Target target = Target.valueOf(BaseTest.config.target().toUpperCase());
        WebDriver webdriver;

        switch (target) {
            case LOCAL:
                webdriver = BrowserFactory.valueOf(browser.toUpperCase()).createDriver();
                break;
            case REMOTE:
                webdriver = createRemoteInstance(BrowserFactory.valueOf(browser.toUpperCase()).getOptions());
                break;
            default:
                throw new Exception(target.toString());
        }
        if(BaseTest.config.screenShotStrategy().contains("eachStep") || BaseTest.config.screenShotStrategy().contains("passedOnly")){
            WebDriverEventListener eventListener = new WebDriverEventListener();
            WebDriver decorated = new EventFiringDecorator(eventListener).decorate(webdriver);
            return decorated;
        }else{
            return webdriver;
        }
    }

    private RemoteWebDriver createRemoteInstance(MutableCapabilities capability) {
        RemoteWebDriver remoteWebDriver = null;
        try {
            String gridURL = String.format("http://%s:%s", BaseTest.config.gridUrl(), BaseTest.config.gridPort());

            remoteWebDriver = new RemoteWebDriver(new URL(gridURL), capability);
        } catch (java.net.MalformedURLException e) {
            Reporter.log("Grid URL is invalid or Grid is not available",true);
            Reporter.log((String.format("Browser: %s", capability.getBrowserName(), e)),true);
        } catch (IllegalArgumentException e) {
            Reporter.log("Browser "+capability.getBrowserName()+" is not valid or recognized", true);
        }

        return remoteWebDriver;
    }
}
