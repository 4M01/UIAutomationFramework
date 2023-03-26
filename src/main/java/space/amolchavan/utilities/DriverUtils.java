package space.amolchavan.utilities;

import space.amolchavan.core.BaseTest;
import space.amolchavan.reporting.ExtentReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;


import javax.imageio.ImageIO;
import java.io.File;

import static space.amolchavan.reporting.ExtentReporter.now;

public class DriverUtils {
/*

    public static  void takeScreenShot(ITestResult result, WebDriver driver) throws IOException {
        try {
            driver.switchTo().defaultContent();
            WebDriver augmentedDriver = new Augmenter().augment(driver);
            ITestNGMethod testMethod = result.getMethod();
            File screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
            String nameScreenshot = ((RemoteWebDriver) driver).getCapabilities().getBrowserName() + "_" + testMethod.getTestClass().getRealClass().getSimpleName() + "_" + testMethod.getMethodName();
            String path = getFullPath(nameScreenshot);
            Reporter.log("Path in take screenshot" +path);
            FileUtils.copyFile(screenshot, new File(path));
            Reporter.log ("\n failure screenshot:");
            Reporter.log ("\n getFileName:" +getFileName(nameScreenshot));

            Reporter.log("<img src='../../screenShots/" + getFileName(nameScreenshot) +"'"+ " target='_blank' >" + "<br>" + getFileName(nameScreenshot) );

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        public static  String getFullPath(String nameTest) throws IOException {
            File directory = new File(".");
            System.out.println("Path in getpath" +directory.getCanonicalPath());
            String newFileNamePath = directory.getCanonicalPath() + "//screenShots//" + getFileName(nameTest);
            return newFileNamePath;
        }

        private static String getFileName(String nameTest) throws IOException {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_hh-mm-ss");
            return dateFormat.format(date) + "_" + nameTest + ".png";
        }

    }

*/
   /*     public static String takeScreenShot() {
            TakesScreenshot newScreen = (TakesScreenshot) BaseTest.threadDriver.get();
            String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
            return "data:image/jpg;base64, " + scnShot;
        }*/


    public static String takeScreenShot() {
        String imageName = now("dd_MMM_yyyy_hh_mm_ss_SSS") + ".jpg";
        String imagePath = ExtentReporter.screenShotPath + imageName;
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) BaseTest.threadDriverMap.get(Thread.currentThread().getId());
            File tempFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File objDestination = new File(imagePath);
            FileUtils.copyFile(tempFile, objDestination);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "screenshots/" + imageName;
    }

    public static String takeScreenShotOFWebElement(WebDriver driver, By by) {
        String imageName = now("dd_MMM_yyyy_hh_mm_ss_SSS") + ".jpg";
        String imagePath = ExtentReporter.screenShotPath + imageName;
        try {
            File tempFile = driver.findElement(by).getScreenshotAs(OutputType.FILE);
            File objDestination = new File(imagePath);
            FileUtils.copyFile(tempFile, objDestination);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return imagePath;
    }



    public static String takeFullPageScreenShot(WebDriver driver) {
        String imageName = now("dd_MMM_yyyy_hh_mm_ss_SSS") + ".jpg";
        String imagePath = ExtentReporter.screenShotPath + imageName;
        try {
            Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(),"PNG",new File(imagePath));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return imagePath;
    }


}
