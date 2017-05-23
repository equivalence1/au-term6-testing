import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ItemCompareColumn {

    /*
    Nice. ChromeDriver uses XPath 1.0, so functions like lower-case are not
    accessible for us. Thus this ugly `translate`
     */
    private static final String COMPARE_CONTENT_FORMAT =
            "//div[contains(@class, \"compare-content\")]" +
                    "/div[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ', " +
                    "'abcdefghijklmnopqrstuvwxyzйцукенгшщзхъфывапролджэячсмитьбю') = \"%s\"]/..";
    private static final String GET_ATTRIBUTE_FORMAT =
            "./div[%d]";

    @NotNull private final WebDriver driver;
    private final int id;

    private String itemName;
    private String startPrice;
    private WebElement likeBtn;
    private WebElement deleteBtn;

    public ItemCompareColumn(@NotNull WebDriver driver, int id) {
        if (id > 3 || id < 0) {
            throw new IllegalArgumentException("id should be between 0 and 2");
        }
        this.driver = driver;
        this.id = id;

        setupItemName();
        setupStartPrice();
        setupLikeBtn();
        setupDeleteBtn();
    }

    /**
     * Returns specified attribute.
     *
     * For example, if you are comparing phones and ask for
     * "материал корпуса", the answer can be "алюминий и стекло"
     * @return
     */
    public String getAttribute(@NotNull String attribute) {
        try {
            WebElement element = getContent(attribute);
            element = element.findElement(By.xpath(String.format(GET_ATTRIBUTE_FORMAT, id + 2)));
            return element.getAttribute("innerHTML");
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Check that specified attribute is hidden (i.e. if we only show
     * "различные характеристики" then equal fields should be hidden)
     * @param attribute field to check
     * @return true if this attribute is hidden, false otherwise
     */
    public boolean isAttributeDisplayed(@NotNull String attribute) {
        final WebElement element = getContent(attribute);
        return element.isDisplayed();
    }

    /**
     * @return name of the item
     */
    @NotNull
    public String getItemName() {
        return itemName;
    }

    /**
     * @return start prise of this item
     */
    @NotNull
    public String getStartPrise() {
        return startPrice;
    }

    /**
     * click on like button on this item
     */
    public void likeItem() {
        // scroll to top to click button
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, 0);");
        moveMouseToPicture(); // w/o this button is invisible
        waitUntilAppear(likeBtn);
        likeBtn.click();
    }

    /**
     * delete this item from comparison
     */
    public void deleteItem() {
        // scroll to top to click button
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, 0);");
        moveMouseToPicture(); // w/o this button is invisible
        waitUntilAppear(deleteBtn);
        deleteBtn.click();
    }

    private void setupItemName() {
        final String request = "//a[contains(@class, \"compare-head__name\")]";
        final WebElement header = driver.findElements(By.xpath(request)).get(id);
        itemName = header.getAttribute("innerHTML");
    }

    private void setupStartPrice() {
        final String request = "//div[contains(@class, \"price\")]";
        final WebElement header = driver.findElements(By.xpath(request)).get(id);
        startPrice = header.getAttribute("innerHTML");
    }

    private void setupLikeBtn() {
        final String request = "//a[contains(@class, \"compare-head__favorite\")]";
        likeBtn = driver.findElements(By.xpath(request)).get(id);
    }

    private void setupDeleteBtn() {
        final String request = "//span[contains(@class, \"compare-head__close\")]";
        deleteBtn = driver.findElements(By.xpath(request)).get(id);
    }

    private WebElement getContent(@NotNull String attribute) {
        final String request = String.format(COMPARE_CONTENT_FORMAT, attribute.toLowerCase());
        return driver.findElement(By.xpath(request));
    }

    private void moveMouseToPicture() {
        final String request = "//div[contains(@class, \"compare-cell_product\")]";
        final WebElement picture = driver.findElements(By.xpath(request)).get(id);
        Actions build = new Actions(driver);
        build.moveToElement(picture).build().perform();
    }

    private void waitUntilAppear(final WebElement element) {
        (new WebDriverWait(driver, 10))
                .until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return element.isDisplayed();
                    }
                });
    }

}
