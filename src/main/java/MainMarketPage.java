import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Class representing main page of
 * market.yandex.ru
 */
public class MainMarketPage {

    @NotNull private final WebDriver driver;

    public MainMarketPage(@NotNull WebDriver driver) {
        this.driver = driver;
        driver.get("https://market.yandex.ru");
    }

    /**
     * Does search for request and returns SearchResultPage
     * representing search results
     * @param request items to search for on market.yandex.ru
     * @return page with search results represented by {@link SearchResultPage}
     */
    @NotNull
    public SearchResultPage searchFor(@NotNull String request) {
        final WebElement searchBox = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("header-search")));
        searchBox.sendKeys(request);
        searchBox.submit();
        return new SearchResultPage(driver);
    }

}
