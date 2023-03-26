package space.amolchavan.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ExtentReporter {
    private static ExtentReports extentReports;
    public static String reportFilePath, reportFileName, screenShotPath;
    public static Map<Long, String> threadToExtentTestMap = new HashMap<>();
    public static Map<String, ExtentTest> nameToTestMap = new HashMap<>();

    public synchronized static ExtentReports getExtentReport(String suiteName) {
        if (extentReports == null) {
            extentReports = createInstance(suiteName);
        }
        return extentReports;
    }

    public synchronized static ExtentReports getExtentReport() {
        return extentReports;
    }


    //Create an extent report instance
    public static ExtentReports createInstance(String suiteName) {
        String timeStamp=now("dd_MMM_yyyy_hh_mm_ss");
        reportFilePath = System.getProperty("user.dir")+ "/target/customReport/"+timeStamp ;
        reportFileName=reportFilePath+"/DetailedExecutionReport.html";
        screenShotPath=reportFilePath+"/screenshots/";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFileName);
        sparkReporter.config().setReportName(suiteName);
        sparkReporter.config().setDocumentTitle(suiteName);
        sparkReporter.config().setTheme(Theme.DARK);
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setReportUsesManualConfiguration(false);
        return extentReports;
    }

    public static void setSystemInfo(String key, String value){
        getExtentReport().setSystemInfo(key, value);
    }

    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }


    public synchronized static ExtentTest
    getTest(String testName, String testDescription) {
        if (!nameToTestMap.containsKey(testName)) {
            Long threadID = Thread.currentThread().getId();
            ExtentTest extentTest = getExtentReport().createTest(testName, testDescription);
            nameToTestMap.put(testName, extentTest);
            threadToExtentTestMap.put(threadID, testName);
        }
        return nameToTestMap.get(testName);
    }

    public synchronized static ExtentTest getTest(String testName) {
        return getTest(testName, "");
    }

    public synchronized static ExtentTest addChildTest(String testName) {
        return getTest(testName, "");
    }



    public synchronized static void closeReport() {
        if (getExtentReport() != null) {
            getExtentReport().flush();
        }
    }
}
