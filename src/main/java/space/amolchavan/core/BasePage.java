package space.amolchavan.core;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import space.amolchavan.driverFactory.WebDriverClient;
import java.io.File;
import java.util.List;

/**
 * BasePage class in parent to all space.amolchavan.pages which represent page object model, Purpose of this class to have common
 * behaviour of space.amolchavan.pages. Please note that is is not for common behaviour of users. Example can be waitForAllSpinnersToDisappear
 * This method likely to be part of every method.
 * By Virtue of adding in BasePage we are making it available to all space.amolchavan.pages*/

@Getter @Setter
public class BasePage {
    public WebDriverClient webDriverClient= null;
    public static String LOADER = "//*[contains(@class,'loader') or contains(@id,'loaderId') or contains(@class,'spiner')]";
    private final By breadcrumbList=By.xpath("//div[contains(@class,'breadcrumb') and @id]");
    private final String breadcrumbItem="//div[contains(@class,'breadcrumb') and text()='%s']";


    public void waitForSpinnerToDisappear() throws Exception {
        getWebDriverClient().waitForPageLoad();
        getWebDriverClient().waitForInvisibilityOfElementLocatedBy(LOADER);
        getWebDriverClient().waitForPageLoad();
    }



    public boolean isAt() {
        return false;
    }

    public boolean isElementDisplayed(String xpath){
        boolean isDisplayed=false;
        try{
            isDisplayed=getWebDriverClient().getDriver().findElement(By.xpath(xpath)).isDisplayed();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isElementDisplayed(By by){
        boolean isDisplayed=false;
        try{
            isDisplayed=getWebDriverClient().getDriver().findElement(by).isDisplayed();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isElementDisplayed(WebElement element,By by){
        boolean isDisplayed=false;
        try{
            isDisplayed=element.findElement(by).isDisplayed();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isElementNotDisplayed(String xpath){
        try{
            return !getWebDriverClient().getDriver().findElement(By.xpath(xpath)).isDisplayed();
        }catch(Exception e){
            e.printStackTrace();
            return true;
        }
    }

    public boolean isElementNotDisplayed(By by){
        boolean isDisplayed=false;
        try{
            isDisplayed=getWebDriverClient().getDriver().findElement(by).isDisplayed();
            return false;
        }catch(Exception e){
            return true;
        }
    }

    public boolean isElementNotDisplayed(WebElement element){
        try{
            return !element.isDisplayed();
        }catch(Exception e){
            e.printStackTrace();
            return true;
        }
    }

    public boolean isElementNotDisplayed(WebElement element,By by){
        boolean isDisplayed=false;
        try{
            isDisplayed=element.findElement(by).isDisplayed();
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return true;
        }
    }

    public WebElement getWebElement(By by){
        try{
            return webDriverClient.getDriver().findElement(by);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<WebElement> getWebElements(By by){
        try{
            return webDriverClient.getDriver().findElements(by);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public void playVideo() {
        JavascriptExecutor js = (JavascriptExecutor) webDriverClient.getDriver();
        String javaScript = "var elem=document.getElementsByTagName('video'); return elem[0].play()";
        js.executeScript(javaScript, "");
    }

    public int getCurrentDurationOfVideo() {
        int dur=0;
        try {
            JavascriptExecutor js = (JavascriptExecutor) webDriverClient.getDriver();
            String javaScript = "var elem=document.getElementsByTagName('video'); return elem[0].currentTime";
            Double duration = (Double) js.executeScript(javaScript, "");
            return (int)Math.ceil(duration);
        }catch (Exception e){
            return dur;
        }
    }

    public void pauseVideo() {
        JavascriptExecutor js = (JavascriptExecutor) webDriverClient.getDriver();
        String pauseScript = "var elem=document.getElementsByTagName('video'); return elem[0].pause();";
        js.executeScript(pauseScript, "");
    }

    public void hoverMouseOver(WebElement element){
        try {
            Actions actions = new Actions(webDriverClient.getDriver());
            actions.moveToElement(element).build().perform();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dropFile(File filePath, WebElement target, int offsetX, int offsetY) {
        JavascriptExecutor jse = (JavascriptExecutor) webDriverClient.getDriver();
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

    public boolean verifyBreadcrumb(String expectedBreadcrumb) {
        try {
            waitForSpinnerToDisappear();
            List<WebElement> breadCrumbFromUI=webDriverClient.findElements(breadcrumbList);
            String actualBreadcrumb="";
            for(WebElement breadcrumb:breadCrumbFromUI){
                actualBreadcrumb=actualBreadcrumb+breadcrumb.getText()+" ";
            }
            return (expectedBreadcrumb+" ").equals(actualBreadcrumb);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void selectBreadcrumb(String breadcrumbToSelect) {
        try {
            webDriverClient.findElement(By.xpath(breadcrumbItem.formatted(breadcrumbToSelect))).click();
            waitForSpinnerToDisappear();
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
