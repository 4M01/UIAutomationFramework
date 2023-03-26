package space.amolchavan.driverFactory;

import space.amolchavan.core.BaseTest;
import space.amolchavan.reporting.WebDriverEventListener;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.net.MalformedURLException;
import java.net.URL;

public enum DeviceFactory {
    ANDROID_EMULATOR{
        @Override
        public WebDriver createDriver(){
            AppiumDriver driver = null;
            try {
                driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), getCapabilities());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return driver;
        }
        @Override
        public DesiredCapabilities getCapabilities(){
            String appPath  = System.getProperty("user.dir")+"/src/main/resources/ePro.apk";
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("appium:platformName", Platform.ANDROID);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12.0");
            capabilities.setCapability(MobileCapabilityType.APP, appPath);
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
            return capabilities;
        }
    };

    private static final String APPIUM_URL = "http://localhost:4723/wd/hub";

    public abstract WebDriver createDriver();

    public abstract DesiredCapabilities getCapabilities();


}
