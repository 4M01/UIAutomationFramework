package space.amolchavan.core;


import space.amolchavan.driverFactory.DriverFactory;
import space.amolchavan.entities.DTO;
import space.amolchavan.entities.TestConfig;
import space.amolchavan.reporting.CustomAssert;
import space.amolchavan.reporting.ExtentReporter;
import space.amolchavan.reporting.TestListener;
import space.amolchavan.reporting.WebDriverEventListener;
import space.amolchavan.utilities.DateTimeUtility;
import space.amolchavan.utilities.EmailReport;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.annotations.Optional;
import org.testng.xml.XmlTest;
import org.threeten.bp.Duration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


public class BaseTest {
    //Objects that will get reference throughout the execution for data, for browser & for reporting
    static public DTO dto = new DTO();
    public static Map<Long, WebDriver> threadDriverMap = new ConcurrentHashMap<Long, WebDriver>();
    static ExtentReporter extentReporter;
    public static TestConfig config ;

    //Bootstrapping details need for execution and post-processing
    protected Provider provider = new Provider();
    protected String env, productName, appName, moduleName, buildVersion, tenant, browser  = null;
    protected String testCycle, uploadToBucket, pushResultToDB, screenshotStrategy, publishToJira, publishToTestRail = null;
    protected static List<String> testsToRun = new ArrayList<String>();
    protected String testScriptName =  null;

    protected WebDriver driver;

    //Declare the controller class as attribute, Controller class will initialize the page objects

    public CustomAssert customAssert;

    @Parameters({"xml", "env","productName","appName", "moduleName","buildVersion", "tenant", "browser",
            "testCycle", "uploadToBucket", "publishResultsToDB", "screenshotStrategy", "publishToJira", "publishToTestRail"})
    @BeforeSuite(alwaysRun = true)
    public void setup(@Optional String xml, @Optional String env,@Optional String productName,@Optional String appName,
                      @Optional String moduleName, @Optional String buildVersion, @Optional String tenant, @Optional String browser,
                      @Optional String testCycle, @Optional String uploadToBucket, @Optional String publishResultsToDB, @Optional String screenshotStrategy,
                      @Optional String publishToJira, @Optional String publishToTestRail, ITestContext context) throws IOException {

        int threadCount = System.getProperty("threadCount") != null ?  Integer.valueOf(System.getProperty("threadCount")): context.getSuite().getXmlSuite().getThreadCount();
        context.getSuite().getXmlSuite().setThreadCount(threadCount);
        Reporter.log("Thread count for execution [" + threadCount + "]", true);
        this.env = System.getProperty("env") != null ? System.getProperty("env") : (env!= null ? env : "test");
        this.productName = System.getProperty("productName") != null ? System.getProperty("productName") : (productName != null? productName : "ABCT");
        this.appName = System.getProperty("appName") != null ? System.getProperty("appName") :(appName !=null? appName:"EDC");
        this.moduleName = System.getProperty("moduleName") != null ? System.getProperty("moduleName"):(moduleName!=null?moduleName:"eic");
        this.buildVersion = System.getProperty("buildVersion") != null ? System.getProperty("buildVersion") :(buildVersion!=null?buildVersion:"Not Provided");
        this.tenant = System.getProperty("tenant") != null ?System.getProperty("tenant"):(tenant!=null?tenant:"ABCL");
        this.browser = System.getProperty("browser") != null ?System.getProperty("browser"):(browser!=null?browser:"chrome");
        this.uploadToBucket = System.getProperty("uploadToBucket") != null ?System.getProperty("uploadToBucket"):(uploadToBucket!=null?uploadToBucket:"NO");

        extentReporter.getExtentReport(context.getSuite().getXmlSuite().getName());
        extentReporter.setSystemInfo("Environment ",this.env);
        ExtentReporter.setSystemInfo("Product Name",this.productName);
        extentReporter.setSystemInfo("App Name",this.appName);
        ExtentReporter.setSystemInfo("Module Name",this.moduleName);
        ExtentReporter.setSystemInfo("Build Version",this.buildVersion);
        ExtentReporter.setSystemInfo("Browser",this.browser);
        ExtentReporter.setSystemInfo("Tenant",this.tenant);

        provider.setEnvironmentAndTenant(this.env,this.tenant);
        dto.createTestData(this.env,this.moduleName,this.tenant);

        Properties props = new Properties();
        props.load(new FileReader(dto.getConfigPath()+this.appName+"-"+env+"-"+tenant+".properties"));
        config = ConfigFactory.create(TestConfig.class,props);

        if (isNotEmpty(System.getProperty("testsToRun"))) {
            testsToRun = Arrays.asList(System.getProperty("testsToRun").split(","));
            Reporter.log("Selective test(s) to Run : [" + testsToRun.size() + "]", true);
        } else {
            List<XmlTest> testToExecute = context.getSuite().getXmlSuite().getTests();
            testsToRun = testToExecute.stream().map(test -> test.getName()).collect(Collectors.toList());
            Reporter.log("Test(s) to run from the XML suite file : " + Arrays.toString(new List[]{testsToRun}) + "", true);
        }
    }


    @Parameters({"xml", "env", "tenant", "study", "browser"})
    @BeforeClass(alwaysRun = true)
    public void setupScriptClass(@Optional String xml, @Optional String env, @Optional String tenant,
                                 @Optional String study, @Optional String browser, ITestContext context) throws Exception {
        this.customAssert = new CustomAssert();
        testScriptName=this.getClass().getSimpleName();

        Reporter.log(DateTimeUtility.getTimeStamp()+ ": Started with Test Script... " + testScriptName, true);
        driver = new DriverFactory().createInstance(browser);
        WebDriverListener listener = new WebDriverEventListener();
        WebDriver decorated = new EventFiringDecorator(listener).decorate(driver);
        threadDriverMap.put(Thread.currentThread().getId(), decorated);

        // Initialize the object of controller class, it will accept env, browser, context, driver

         TestListener.threadToClassName.put(Thread.currentThread().getId(), testScriptName);
    }


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method m) {
        Reporter.log(DateTimeUtility.getTimeStamp()+ ": Test Started... " + m.getDeclaringClass().getSimpleName() + "." + m.getName() + "()", true);
    }

    @AfterMethod
    public void afterMethod(Method m){
        ExtentReporter.closeReport();
        Reporter.log(DateTimeUtility.getTimeStamp()+ ": Test completed... " + m.getDeclaringClass().getSimpleName() + "." + m.getName() + "()", true);
    }


    @AfterClass(alwaysRun = true)
    public void tearDown() {
//        TestListener.threadToExtentTestClass.remove(Thread.currentThread().getId());
        Reporter.log(DateTimeUtility.getTimeStamp()+ ": Exiting Test Script... " + testScriptName, true);
        driver.quit();
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTearDown() throws IOException {
        ExtentReporter.closeReport();
        if(!this.uploadToBucket.contains("NO")){
            uploadReportToGoogleStorage();
        }
    }

    private void uploadReportToGoogleStorage() throws IOException {
        String jsonPath  = System.getProperty("user.dir")+"/src/main/resources/kloojj-2-0-dev-1729c558ab70.json";
        String[] path = ExtentReporter.reportFilePath.split("/");
        String folder = path[path.length - 1];
        folder = this.env+"/"+this.productName+"/"+this.appName+"/"+this.moduleName+"/"+this.buildVersion+ "/"+this.tenant.toUpperCase()+"/"+folder ;

        File[] files = new File(ExtentReporter.reportFilePath).listFiles();
        String bucket = "abcl-automation-reports";
        String projectId = "Kloojj-2-0-Dev";

        RetrySettings retrySettings =
                StorageOptions.getDefaultRetrySettings()
                        .toBuilder()
                        // Set the max number of attempts to 10 (initial attempt plus 9 retries)
                        .setMaxAttempts(10)
                        // Set the backoff multiplier to 3.0
                        .setRetryDelayMultiplier(3.0)
                        // Set the max duration of all attempts to 5 minutes
                        .setTotalTimeout(Duration.ofMinutes(10))
                        .build();

        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setRetrySettings(retrySettings)
                .setCredentials(GoogleCredentials.fromStream(new
                        FileInputStream(jsonPath))).build();
        Storage storage = storageOptions.getService();
        System.out.println("Uploading folder: " + folder);
        String resultPath = uploadFolder(files, folder, bucket, storage);
        EmailReport report  = new EmailReport(resultPath);
    }

    String uploadFolder(File[] files, String folder, String bucket, com.google.cloud.storage.Storage storage) throws IOException {
    String resultPath = "";
        for (File file : files) {
            if (!file.isHidden()) {
                // if it is a directory read the files within the subdirectory
                if (file.isDirectory()) {
                    String[] lpath = file.getAbsolutePath().split("/");
                    String lfolder = lpath[lpath.length - 1];
                    String xfolder = folder + "/" + lfolder;
                    xfolder = folder+"/"+xfolder.substring(xfolder.lastIndexOf("\\")+1,xfolder.length());

                    uploadFolder(file.listFiles(), xfolder, bucket, storage); // Calls same method again.

                } else {
                    // add directory/subdirectory to the file name to create the file structure
                    BlobId blobId = BlobId.of(bucket, folder + "/" + file.getName());

                    //prepare object
                    BlobInfo blobInfo;
                    if(file.getName().contains("html")){
                         blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/html").build();
                        resultPath= "https://storage.googleapis.com/" + bucket + "/" + folder + "/" + file.getName();
                         System.out.println("Will be uploading results to-"+resultPath);
                    }else{
                        blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
                    }

                    // upload object
                    storage.create(blobInfo, Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                    // setup Permission to set Object public
                    storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
//                    System.out.println("Uploaded: gs://" + bucket + "/" + folder + "/" + file.getName());
                }

            }

        }
        return resultPath;
    }

    protected void testStart(String description) {
        Reporter.log(testScriptName, true);
        Reporter.log("Running " + testScriptName, true);
    }


    public WebDriver getDriver() {
//       return  threadDriver.get();
        return threadDriverMap.get(Thread.currentThread().getId());
    }

    /**
     * Returns host name of selenium grid node, which is executing current test
     *
     * @author amol chavan
     * @param hubUrl
     *
     * @return String (grid node hostname OR null, if error)
     */
   /* public String getGridExecutionNode(String hubUrl){
        String node = null;
        try{
            String hub = hubUrl.split("//")[1].split(":")[0]; // Grid Hub hostname
            if(hub.toUpperCase().contains("LOCALHOST")) return hubUrl;
            int port = Integer.parseInt(hubUrl.split("//")[1].split(":")[1].split("/")[0]); // Grid Hub port number
            HttpHost host = new HttpHost(hub,port);
            HttpClient client = HttpClientBuilder.create().build();
            String url = host + "/grid/space.amolchavan.api/testsession?session=" + threadDriver.get().getSessionId();
            HttpGet request = new HttpGet(url);
            HttpResponse response  = client.execute(host, request);
            JSONObject object = new JSONObject(EntityUtils.toString(response.getEntity()));
            String proxyID = (String) object.get("proxyId");
            node = proxyID.split("//")[1].split(":")[0];
        }
        catch(Exception e){
            e.printStackTrace();
            Reporter.log("Error in figuring out grid node running this test", true);
        }
        return node;
    }
*/
}
