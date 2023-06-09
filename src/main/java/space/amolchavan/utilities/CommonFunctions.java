package space.amolchavan.utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;


public class CommonFunctions {
    static WebDriver driver;

/*    CommonUtilities (WebDriver driver){
    this.driver=driver;
    }*/

    public static void dragDropPDFFile(String filePath, WebElement pdfUpload) {
        //WebElement dragArea = getWebElement(objectMap.getLocator("DragDropContainer"));
        dropFile(new File(filePath), pdfUpload, 0, 0);
    }

    public static void dropFile(File filePath, WebElement target, int offsetX, int offsetY) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
       // WebDriverWait wait = new WebDriverWait(driver, 30);
        String JS_DROP_FILE =
                "var target = arguments[0]," +
                        "    offsetX = arguments[1]," +
                        "    offsetY = arguments[2]," +
                        "    document = target.ownerDocument || document," +
                        "    window = document.defaultView || window;" +
                        "" +
                        "var input = document.createElement('INPUT');" +
                        "input.type = 'file';" +
                        "input.style.display = 'none';" +
                        "input.onchange = function () {" +
                        "  var rect = target.getBoundingClientRect()," +
                        "      x = rect.left + (offsetX || (rect.width >> 1))," +
                        "      y = rect.top + (offsetY || (rect.height >> 1))," +
                        "      dataTransfer = { files: this.files };" +
                        "" +
                        "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {" +
                        "    var evt = document.createEvent('MouseEvent');" +
                        "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);" +
                        "    evt.dataTransfer = dataTransfer;" +
                        "    target.dispatchEvent(evt);" +
                        "  });" +
                        "" +
                        "  setTimeout(function () { document.body.removeChild(input); }, 25);" +
                        "};" +
                        "document.body.appendChild(input);" +
                        "return input;";
        WebElement input = (WebElement) jse.executeScript(JS_DROP_FILE, target, offsetX, offsetY);
        input.sendKeys(filePath.getAbsoluteFile().toString());
        //wait.until(ExpectedConditions.stalenessOf(input));
    }
}
