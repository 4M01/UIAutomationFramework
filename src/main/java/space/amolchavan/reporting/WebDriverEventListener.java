package space.amolchavan.reporting;

import space.amolchavan.core.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import space.amolchavan.utilities.DriverUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import static com.aventstack.extentreports.Status.INFO;

public class WebDriverEventListener implements WebDriverListener {


    @Override
    public void afterGet(WebDriver driver, String url) {
        ExtentTest extentTestCase = TestListener.threadToExtentTestCases.get(Thread.currentThread().getId());
        extentTestCase.log(INFO,"Navigated to the URL - " + url, MediaEntityBuilder.createScreenCaptureFromPath(DriverUtils.takeScreenShot()).build());
    }

   /* @Override
    public void afterSendKeys(WebElement element,CharSequence... keysToSend){
        ExtentTest extentTestCase = TestListener.threadToExtentTestCases.get(Thread.currentThread().getId());
        String[] pathVariables = (element.toString().split("->")[1].replaceFirst("(?s)(.*)\\]", "$1" + "")).split(":");
        String selector = pathVariables[0].trim();
        String value = pathVariables[1].trim().substring(0, pathVariables[1].length() - 1);
        extentTestCase.log(INFO,"Text entered in '"+keysToSend.toString()+"' in the WebElement with selector - " + selector + " and value - "+value , MediaEntityBuilder.createScreenCaptureFromPath(DriverUtils.takeScreenShot()).build());
    }


    @Override
    public void beforeClick(WebElement element){
        if(BaseTest.config.screenShotStrategy().contains("eachStep") || BaseTest.config.screenShotStrategy().contains("passedOnly")){
            ExtentTest extentTestCase = TestListener.threadToExtentTestCases.get(Thread.currentThread().getId());
            String[] pathVariables = (element.toString().split("->")[1].replaceFirst("(?s)(.*)\\]", "$1" + "")).split(":");
            String selector = pathVariables[0].trim();
            String value = pathVariables[1].substring(0, pathVariables[1].length() - 1).trim();
            extentTestCase.log(INFO,"Before Clicking  WebElement with selector - " + selector + " and value - "+value , MediaEntityBuilder.createScreenCaptureFromPath(DriverUtils.takeScreenShot()).build());
        }
    }
*/
    @Override
    public void afterClick(WebElement element){
        if(BaseTest.config.screenShotStrategy().contains("eachStep") || BaseTest.config.screenShotStrategy().contains("passedOnly")){
        ExtentTest extentTestCase = TestListener.threadToExtentTestCases.get(Thread.currentThread().getId());
        String[] pathVariables = (element.toString().split("->")[1].replaceFirst("(?s)(.*)\\]", "$1" + "")).split(":");
        String selector = pathVariables[0].trim();
        String value = pathVariables[1].substring(0, pathVariables[1].length() - 1).trim();
        extentTestCase.log(INFO,"After Clicking  WebElement with selector - " + selector + " and value - "+value , MediaEntityBuilder.createScreenCaptureFromPath(DriverUtils.takeScreenShot()).build());
        }
    }


}
