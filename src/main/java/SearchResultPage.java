import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a page containing search
 * results on market.yandex.com
 */
public class SearchResultPage {

    private static final String SEARCH_RESULTS_DIV_XPATH = "//div[contains(@class, \"filter-applied-results\")]";
    private static final String COMPARE_LINK_XPATH = "//a[contains(@class, \"header2-menu__item_type_compare\")]";

    @NotNull private final WebDriver driver;
    @NotNull private final List<ResultItem> resultItems;

    public SearchResultPage(@NotNull WebDriver driver) {
        this.driver = driver;
        this.resultItems = new ArrayList<ResultItem>();
        waitToLoad();
        getResultItemList();
    }

    /**
     * Get all items search returned
     * @return items returned by searching
     */
    @NotNull
    public List<ResultItem> getResultItems() {
        return resultItems;
    }

    /**
     * Click `compare` button
     */
    public ComparePage compare() {
        // scroll to the top so we can see compare button
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, 0);");
        final WebElement compareLink = driver.findElement(By.xpath(COMPARE_LINK_XPATH));
        compareLink.click();
        return new ComparePage(driver);
    }

    private void waitToLoad() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(SEARCH_RESULTS_DIV_XPATH)));
    }

    private void getResultItemList() {
        final List<WebElement> itemsFound = driver.findElements(By.xpath(ResultItem.ITEM_DIV_XPATH));
        for (WebElement element : itemsFound) {
            resultItems.add(new ResultItem(element));
        }
    }

    public class ResultItem {

        public static final String ITEM_DIV_XPATH = "//div[contains(@data-id, \"model-\")]";
        private static final String ITEM_ADD_COMPARE_XPATH = ".//div[contains(@class, \"product-action-compare\")]";
        private static final String ITEM_IN_COMPARE_LIST = "product-action-compare_in-list_yes";

        private final WebDriverWait wait;

        private final WebElement item;

        private ResultItem(@NotNull WebElement item) {
            this.item = item;
            wait = new WebDriverWait(driver, 10);
        }

        public void addToComparison() {
            final WebElement addButton = item.findElement(By.xpath(ITEM_ADD_COMPARE_XPATH));
            addButton.click();

            // wait until this item is actually added to list
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    final String divClass = addButton.getAttribute("class");
                    return divClass.contains(ITEM_IN_COMPARE_LIST);
                }
            });
        }

    }

}
