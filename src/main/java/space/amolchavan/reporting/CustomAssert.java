package space.amolchavan.reporting;


import space.amolchavan.utilities.DateTimeUtility;
import space.amolchavan.utilities.DriverUtils;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.Reporter;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.PASS;
import static space.amolchavan.reporting.TestListener.threadToExtentTestCases;

public class CustomAssert extends SoftAssert {

    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        String msg = assertCommand.getMessage() ;
        Reporter.log(DateTimeUtility.getTimeStamp()+ ": Assertion Passed... " +msg, true);
        threadToExtentTestCases.get(Thread.currentThread().getId()).log(PASS,msg, MediaEntityBuilder.createScreenCaptureFromPath(DriverUtils.takeScreenShot()).build());
    }

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        String suffix =
                String.format(
                        ". Expected [%s] but found [%s]",
                        assertCommand.getExpected().toString(), assertCommand.getActual().toString());
        String msg = assertCommand.getMessage() + suffix;
        Reporter.log(DateTimeUtility.getTimeStamp()+ ": Assertion Failed... " + msg, true);
        threadToExtentTestCases.get(Thread.currentThread().getId()).log(FAIL,msg);
    }
}
