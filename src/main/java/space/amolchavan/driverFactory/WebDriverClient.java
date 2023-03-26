package space.amolchavan.driverFactory;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import space.amolchavan.core.BaseTest;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
public class WebDriverClient {
    public WebDriverWait webdriverWait;
    public String browser;
    public WebDriver driver;
    String currentWindowHandle;

    public WebDriverClient(WebDriver driver) {
        this.driver = driver;
        driver.manage().window().maximize();
        webdriverWait = new WebDriverWait(driver, Duration.ofSeconds(BaseTest.config.explicitWait()));
        webdriverWait.ignoring(StaleElementReferenceException.class);
//        setImplicitWait(Duration.ofSeconds(BaseTest.config.implicitWait()));
//        ((WebDriver) driver).setFileDetector(new LocalFileDetector());
    }

    public void scrollElementIntoCenterOfView(By by) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", by);
            waitForSpinnerToDisappear();
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-73)", "");
        } catch (Exception e) {

        }
    }


    public void scrollElementIntoCenterOfView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            waitForSpinnerToDisappear();
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-73)", "");
        } catch (Exception e) {

        }
    }

    public void setImplicitWait(Duration implicitWait) {
        driver.manage().timeouts().implicitlyWait(implicitWait);
    }

    public static String LOADER = "//div[contains(@class,'loader') or ]";

    public void waitForSpinnerToDisappear() throws Exception {
        waitForPageLoad();
        waitForInvisibilityOfElementLocatedBy(LOADER);
        waitForPageLoad();
    }

    /**
     * Clears the existing text if any, type the new text and hits 'Enter' key
     *
     * @param by
     * @param inputText
     * @return true if provided input text is set in the locator field, false otherwise
     * @throws Exception
     */
    public boolean clearTextTypeAndHitEnter(By by, String inputText) throws Exception {
        WebElement element = driver.findElement(by);
        if (isWebElementEnabled(element)) {
            element.clear();
            element.sendKeys(inputText);
            element.sendKeys(Keys.ENTER);
            return true;
        } else {
            throw new Exception("Locator does not exists or is disabled");
        }
    }


    /**
     * Clears the existing text if any, type the new text and hits 'Enter' key
     *
     * @param by
     * @param inputText
     * @return true if provided input text is set in the locator field, false otherwise
     * @throws Exception
     */
    public boolean clearTextAndType(By by, String inputText) throws Exception {
        WebElement element = driver.findElement(by);
        if (isWebElementEnabled(element) && isWebElementDisplayed(by)) {
            element.clear();
            element.sendKeys(inputText);
            element.sendKeys(Keys.TAB);
            return true;
        } else {
            throw new Exception("Locator does not exists or is disabled");
        }
    }

    /**
     * Clears the existing text if any, type the new text and hits 'Enter' key
     *
     * @param
     * @param inputText
     * @return true if provided input text is set in the locator field, false otherwise
     * @throws Exception
     */
    public boolean clearTextAndType(WebElement element, String inputText) throws Exception {
        if (isWebElementEnabled(element) && isWebElementDisplayed(element)) {
            element.clear();
            element.sendKeys(inputText);
            element.sendKeys(Keys.TAB);
            return true;
        } else {
            throw new Exception("Locator does not exists or is disabled");
        }
    }


    /**
     * Clears the existing text if any, type the new text and hits 'Enter' key
     *
     * @param by
     * @param inputText
     * @return true if provided input text is set in the locator field, false otherwise
     * @throws Exception
     */
    public boolean sendText(By by, String inputText) throws Exception {
        WebElement element = driver.findElement(by);
        if (isWebElementEnabled(element) && isWebElementDisplayed(by)) {
            element.sendKeys(inputText);
//            element.sendKeys(Keys.TAB);
            return true;
        } else {
            throw new Exception("Locator does not exists or is disabled");
        }
    }


    public void selectFromDropDown(By by, String dropDownValue) throws Exception {
        WebElement element = driver.findElement(by);
        isWebElementEnabled(element);
        refreshElement(element).click();
        WebElement dropDownElement = driver.findElement(By.xpath("//*[contains(@class,'dropdown-item active') and  contains(text(),'" + dropDownValue + "')]"));
        waitForVisibilityOfElement(dropDownElement);
        dropDownElement.click();
    }


    /**
     * First checks if element is displayed or not? If true then checks if it is enabled
     *
     * @param element
     * @return True if the element is enabled, false otherwise.
     * @throws Exception
     */
    public boolean isWebElementEnabled(WebElement element) throws Exception {
        try {
            if (isWebElementDisplayed(element)) {
                if (!element.getAttribute("class").contains("disabled"))
                    return element.isEnabled();
            }
        } catch (StaleElementReferenceException stexc) {
//            Reporter.log("find element again and check for enabled if stale element reference occur",true);
            return isWebElementEnabled(refreshElement(element));
        }
        return false;
    }

    /**
     * Is this element displayed or not? This method avoids the problem of having to parse an
     * element's "style" attribute.
     *
     * @param element
     * @return true if the element is displayed, false otherwise.
     * @see 'http://stackoverflow.com/questions/24946703/selenium-webdriver-using-isdisplayed-in-if-statement-is-not-working'
     */
    public boolean isWebElementDisplayed(WebElement element) {
        waitForPageLoad();
        try {
            if (element.isDisplayed())
                return true;
        } catch (Exception e) {

        }
        return false;
    }


    /**
     * Is this element displayed or not? This method avoids the problem of having to parse an
     * element's "style" attribute.
     *
     * @param by
     * @return true if the element is displayed, false otherwise.
     * @see 'http://stackoverflow.com/questions/24946703/selenium-webdriver-using-isdisplayed-in-if-statement-is-not-working'
     */
    public boolean isWebElementDisplayed(By by) {
        try {
            if (getDriver().findElement(by).isDisplayed())
                return true;
        } catch (Exception e) {
        }
        return false;
    }


    public WebElement refreshElement(WebElement element) throws Exception {
        try {
            return findElement(getByFromElement(element));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find the element based on provided locating technique
     *
     * @param locator
     * @return WebElement
     * @throws Exception
     */
    public WebElement findElement(By locator) throws Exception {
        isWebElementEnabled(driver.findElement(locator));
        return driver.findElement(locator);
    }

    /**
     * Find the element based on provided locating technique
     *
     * @param locator
     * @return list of  WebElements
     * @throws Exception
     */
    public List<WebElement> findElements(By locator) throws Exception {
        try {
            return driver.findElements(locator);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find the element based on provided locating technique
     *
     * @param locator
     * @return WebElement
     * @throws Exception
     */
    public WebElement findElement(WebElement element, By locator) throws Exception {
        return element.findElement(locator);
    }


    private By getByFromElement(WebElement element) throws Exception {
        By by = null;
        String[] pathVariables = (element.toString().split("->")[1].replaceFirst("(?s)(.*)\\]", "$1" + "")).split(":");

        String selector = pathVariables[0].trim();
        String value = pathVariables[1].trim().substring(0, pathVariables[1].length() - 2);


        switch (selector) {
            case "id":
                by = By.id(value);
                break;
            case "className":
                by = By.className(value);
                break;
            case "tagName":
                by = By.tagName(value);
                break;
            case "xpath":
                by = By.xpath(value);
                break;
            case "css selector":
                by = By.cssSelector(value);
                break;
            case "linkText":
                by = By.linkText(value);
                break;
            case "name":
                by = By.name(value);
                break;
            case "partialLinkText":
                by = By.partialLinkText(value);
                break;
            default:
                throw new Exception("locator : " + selector + " not found!!!");
        }
        return by;
    }


    public void click(String xpath) throws Exception {
        waitForSpinnerToDisappear();
        WebElement element = driver.findElement(By.xpath(xpath));
        isWebElementDisplayed(element);
        isWebElementEnabled(element);
        element.click();
    }


    public void click(By by) throws Exception {
        waitForSpinnerToDisappear();
        isWebElementDisplayed(by);
        isWebElementEnabled(driver.findElement(by));
        waitForSpinnerToDisappear();
        click(driver.findElement(by));
        waitForSpinnerToDisappear();
    }

    /**
     * Perform click action on element
     *
     * @param element
     * @return boolean
     * @throws Exception
     */
    public boolean click(WebElement element) throws Exception {
        try {
            waitForVisibilityOfElement(element);
            if (isWebElementEnabled(element)) {
                element.click();
                return true;
            } else {
                throw new Exception("Click failed as the element is disabled.");
            }
        } catch (StaleElementReferenceException stexc) {
            Reporter.log("find element again and click if stale element reference occur", true);
            refreshElement(element).click();
        } catch (WebDriverException wde) {
            if (isWebElementEnabled(element)) {
                element.click();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to click, as either unable to locate element OR met an exception.", e);
        }
        return false;
    }


    /**
     * Waits for the visibility of the element (WebElement) on DOM of a page
     *
     * @param element
     * @return WebElement
     * @throws Exception
     */
    public WebElement waitForVisibilityOfElement(WebElement element) throws Exception {
        try {
            webdriverWait.until(ExpectedConditions.visibilityOf(element));
            webdriverWait.until(ExpectedConditions.elementToBeClickable(element));
            return webdriverWait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (StaleElementReferenceException stexc) {
            Reporter.log("find element again and check visibility if stale element reference occur", true);
            return waitForVisibilityOfElement(refreshElement(element));
        } catch (Exception e) {
            if (!e.getMessage().contains("tried for 1 second(s)")) { // pdewan - not printing the stacktrace, when control comes here through inherent isAt() failure from pagefactory
                e.printStackTrace();
            }
            throw new Exception("Failed in waitForVisibilityOfElement", e);
        }
    }


    /**
     * Wait for locator to be invisible or not present on the DOM
     *
     * @param locator used to find the element
     * @return true if element is invisible, false otherwise
     * @throws Exception
     */
    public boolean waitForInvisibilityOfElementLocatedBy(String locator) throws Exception {
        try {
            waitForPageLoad();
            isWebElementDisplayed(By.xpath(locator));
            boolean isElementNotPresent = webdriverWait.until(ExpectedConditions
                    .invisibilityOfElementLocated(By.xpath(locator)));
            return isElementNotPresent;
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("can't access dead object")) {
                return true;
            } else {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * Wait for locator to be invisible or not present on the DOM
     *
     * @param by used to find the element
     * @return true if element is invisible, false otherwise
     * @throws Exception
     */
    public boolean waitForInvisibilityOfElementLocatedBy(By by) throws Exception {
        try {
            waitForPageLoad();
            isWebElementDisplayed(by);
            boolean isElementNotPresent = webdriverWait.until(ExpectedConditions
                    .invisibilityOfElementLocated(by));
            return isElementNotPresent;
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("can't access dead object")) {
                return true;
            } else {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Wait for locator to be invisible or not present on the DOM
     *
     * @param by used to find the element
     * @return true if element is invisible, false otherwise
     * @throws Exception
     */
    public boolean waitForVisibilityOfElement(By by) throws Exception {
        try {
            waitForPageLoad();
            webdriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
            webdriverWait.until(ExpectedConditions.elementToBeClickable(by));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("can't access dead object")) {
                return true;
            } else {
                e.printStackTrace();
            }
        }
        webdriverWait = new WebDriverWait(driver, Duration.ofSeconds(BaseTest.config.explicitWait()));
        return false;
    }


    /**
     * Select all options that display text matching the argument.
     *
     * @param by
     * @param optionToBeSelected
     * @return true if provided text is set as selected, false otherwise
     * @throws Exception
     */
    public boolean selectByVisibleText(By by, String optionToBeSelected) throws Exception {
        WebElement element = driver.findElement(by);
        if (isWebElementEnabled(element)) {
            Select selectItem = new Select(element);
            selectItem.selectByVisibleText(optionToBeSelected);
            return selectItem.getFirstSelectedOption().getText().equals(optionToBeSelected);
        } else {
            throw new Exception("Locator does not exists or is disabled");
        }
    }

    public String getAbsoluteXPath(WebElement element) {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "function absoluteXPath(element) {" +
                        "var comp, comps = [];" +
                        "var parent = null;" +
                        "var xpath = '';" +
                        "var getPos = function(element) {" +
                        "var position = 1, curNode;" +
                        "if (element.nodeType == Node.ATTRIBUTE_NODE) {" +
                        "return null;" +
                        "}" +
                        "for (curNode = element.previousSibling; curNode; curNode = curNode.previousSibling) {" +
                        "if (curNode.nodeName == element.nodeName) {" +
                        "++position;" +
                        "}" +
                        "}" +
                        "return position;" +
                        "};" +

                        "if (element instanceof Document) {" +
                        "return '/';" +
                        "}" +

                        "for (; element && !(element instanceof Document); element = element.nodeType == Node.ATTRIBUTE_NODE ? element.ownerElement : element.parentNode) {" +
                        "comp = comps[comps.length] = {};" +
                        "switch (element.nodeType) {" +
                        "case Node.TEXT_NODE:" +
                        "comp.name = 'text()';" +
                        "break;" +
                        "case Node.ATTRIBUTE_NODE:" +
                        "comp.name = '@' + element.nodeName;" +
                        "break;" +
                        "case Node.PROCESSING_INSTRUCTION_NODE:" +
                        "comp.name = 'processing-instruction()';" +
                        "break;" +
                        "case Node.COMMENT_NODE:" +
                        "comp.name = 'comment()';" +
                        "break;" +
                        "case Node.ELEMENT_NODE:" +
                        "comp.name = element.nodeName;" +
                        "break;" +
                        "}" +
                        "comp.position = getPos(element);" +
                        "}" +

                        "for (var i = comps.length - 1; i >= 0; i--) {" +
                        "comp = comps[i];" +
                        "xpath += '/' + comp.name.toLowerCase();" +
                        "if (comp.position !== null) {" +
                        "xpath += '[' + comp.position + ']';" +
                        "}" +
                        "}" +

                        "return xpath;" +

                        "} return absoluteXPath(arguments[0]);", element);
    }

    public void selectCheckBox(String xpath) {
        if (!getDriver().findElement(By.xpath(xpath)).isSelected()) {
            getDriver().findElement(By.xpath(xpath)).click();
        }
    }

    public void waitForPageLoad() {
        webdriverWait.until(ExpectedConditions.jsReturnsValue("return document.readyState")).equals("complete");
    }

    public void waitForAttributeToHaveValue(By by, String attribute, String value) {
        webdriverWait.until(ExpectedConditions.attributeContains(by, attribute, value));
    }

    public boolean switchToDefaultFrame() throws Exception {
        driver.switchTo().defaultContent();
        return true;
    }

    public boolean switchToIFrame(String frameId) throws Exception {
        driver.switchTo().defaultContent();
        driver.switchTo().frame(frameId);
        return true;
    }

    public void dragAndDropElement(WebElement source, WebElement target) {
        Actions act = new Actions(driver);
        act.clickAndHold(source);
        act.moveToElement(target);
        act.release(target).perform();
    }

    public boolean dragAndDropElement(By source, By target) {
        boolean isSuccessful = false;
        try {
            isSuccessful = isWebElementDisplayed(source);
            isSuccessful = isWebElementDisplayed(target);
            Actions actionProvider = new Actions(driver);
            actionProvider.clickAndHold(driver.findElement(source));
            actionProvider.moveToElement(driver.findElement(target));
            actionProvider.release(driver.findElement(target)).perform();
            return isSuccessful;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean selectFileThroughBrowseControl(String pdfName) {
        try {
            String pdfFile = System.getProperty("user.dir") + "/src/test/resources/test-data/files/" + pdfName;
            File file = new File(pdfFile);
            StringSelection pdfFilePath = new StringSelection(file.getAbsolutePath());
            Thread.sleep(2000);//please do not delete this , otherwise script inserts the file path very fast & execution fails
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pdfFilePath, null);


            Robot r = new Robot();
            //pressing enter
            r.keyPress(KeyEvent.VK_ENTER);
            //releasing enter
            r.keyRelease(KeyEvent.VK_ENTER);
            //pressing ctrl+v
            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyPress(KeyEvent.VK_V);
            //releasing ctrl+v
            r.keyRelease(KeyEvent.VK_CONTROL);
            r.keyRelease(KeyEvent.VK_V);
            //pressing enter
            r.keyPress(KeyEvent.VK_ENTER);
            //releasing enter
            r.keyRelease(KeyEvent.VK_ENTER);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public void clearLocalStorage() {
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        jse.executeScript(String.format("window.localStorage.clear();"));
    }

    public boolean setNewWindowAsDefault() {
        String currentWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String popUpWindowHandle : allWindowHandles) {
            if (!currentWindowHandle.equals(popUpWindowHandle)) {
                driver.switchTo().window(popUpWindowHandle);
                return true;
            }
        }
        return false;
    }

    public boolean closeCurrentBrowserTab() {
        try {
            String currentWindowHandle = driver.getWindowHandle();
            Set<String> allWindowHandles = driver.getWindowHandles();
            List<String> handlesList = new ArrayList<String>(allWindowHandles);
            driver.switchTo().window(handlesList.get(1));
            driver.close();
            driver.switchTo().window(handlesList.get(0));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setBrowserZoomLevel(int NoOfTimesToZoomOut) throws Exception {
        String jsCode="document.body.style.zoom='"+NoOfTimesToZoomOut+"%'";
        System.out.println(jsCode);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.body.style.zoom='"+NoOfTimesToZoomOut+"%'");
    }

}
