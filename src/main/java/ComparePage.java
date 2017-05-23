import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ComparePage {

    private static final String COMPARE_BY_DIFF_STR = "//span[text() = \"различающиеся\"]";
    private static final String COMPARE_BY_ALL_STR = "//span[text() = \"все характеристики\"]";

    @NotNull private final WebDriver driver;

    public ComparePage(@NotNull WebDriver driver) {
        this.driver = driver;
        waitToLoad();
    }

    private void waitToLoad() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.titleIs("Сравнение товаров — Яндекс.Маркет"));
    }

    public void compareByAll() {
        final WebElement btn = driver.findElement(By.xpath(COMPARE_BY_ALL_STR));
        btn.click();
    }

    public void compareByDifferent() {
        final WebElement btn = driver.findElement(By.xpath(COMPARE_BY_DIFF_STR));
        btn.click();
    }

    public ItemCompareColumn getItemColumn(int id) {
        return new ItemCompareColumn(driver, id);
    }

}
