package space.amolchavan.reporting;


import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import space.amolchavan.utilities.DriverUtils;
import org.testng.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.aventstack.extentreports.Status.*;


public class TestListener implements ITestListener, IReporter {

    public static Map<Long, ExtentTest> threadToExtentTestCases = new ConcurrentHashMap<Long, ExtentTest>();
    public static Map<Long, String> threadToClassName = new ConcurrentHashMap<Long, String>();


  /*  public void onStart(ITestContext context) {
        String testClassNameCompleteClassPath = ((TestRunner) context).getTest().getXmlClasses().get(0).getName();
        String testClassName = testClassNameCompleteClassPath.substring(testClassNameCompleteClassPath.lastIndexOf(".")+1,testClassNameCompleteClassPath.length());
        threadToClassName.put(Thread.currentThread().getId(), testClassName);
    }*/

    public void onTestStart(ITestResult result) {
        String testMethodName = result.getMethod().getMethodName();
        String testMethodDescription = result.getMethod().getDescription();
        ExtentTest extentTestMethod;
        if(testMethodName.contains("Login") || testMethodName.contains("Logout")){
            extentTestMethod = ExtentReporter.getTest(testMethodDescription + " from " + threadToClassName.get(Thread.currentThread().getId()),testMethodName + " from " + threadToClassName.get(Thread.currentThread().getId()));
        }else {
            extentTestMethod = ExtentReporter.getTest(testMethodDescription,testMethodName + " from " + threadToClassName.get(Thread.currentThread().getId()));
        }
        extentTestMethod.assignCategory(threadToClassName.get(Thread.currentThread().getId()));
        threadToExtentTestCases.put(Thread.currentThread().getId(), extentTestMethod);
        extentTestMethod.log(INFO,testMethodDescription +" Test started...");
    }

    public void onTestSuccess(ITestResult result) {
        String testMethodDescription = result.getMethod().getDescription();
        ExtentTest extentTestMethod = threadToExtentTestCases.get(Thread.currentThread().getId());
        extentTestMethod.log(PASS,"Test Passed -> " +testMethodDescription,MediaEntityBuilder.createScreenCaptureFromPath(DriverUtils.takeScreenShot()).build());
    }

    public void onTestFailure(ITestResult result) {
        String testMethodDescription = result.getMethod().getDescription();
        ExtentTest extentTestMethod = threadToExtentTestCases.get(Thread.currentThread().getId());
        extentTestMethod.log(FAIL,"Test Failed -> " +testMethodDescription,MediaEntityBuilder.createScreenCaptureFromPath(DriverUtils.takeScreenShot()).build());
    }

    public void onTestSkipped(ITestResult result) {
        String testMethodDescription = result.getMethod().getDescription();
        ExtentTest extentTestMethod = threadToExtentTestCases.get(Thread.currentThread().getId());
        extentTestMethod.log(WARNING,"Test Skipped -> " +testMethodDescription,MediaEntityBuilder.createScreenCaptureFromPath(DriverUtils.takeScreenShot()).build());
    }


    public void onFinish(ITestContext context) {
        threadToClassName.remove(Thread.currentThread().getId());
    }
}
