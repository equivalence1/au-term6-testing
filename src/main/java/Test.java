import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.Assert.*;

public class Test {

    private static final String SEARCH_REQUEST = "Iphone";

    @org.junit.Test
    public void testMarketCompare() throws Exception {
        ChromeDriverManager.getInstance().setup();
        final WebDriver driver = new ChromeDriver();

        final MainMarketPage mainPage = new MainMarketPage(driver);
        final SearchResultPage searchResultPage = mainPage.searchFor(SEARCH_REQUEST);

        final List<SearchResultPage.ResultItem> items = searchResultPage.getResultItems();
        if (items.size() < 3) {
            throw new RuntimeException("Search returned less than 3 elements");
        }

        // just add first to for comparison
        for (int i = 0; i < 3; i++) {
            items.get(i).addToComparison();
        }

        final ComparePage comparePage = searchResultPage.compare();

        comparePage.compareByAll();
        Thread.sleep(1000); // just for us to see that content changed
        comparePage.compareByDifferent();
        Thread.sleep(1000);

        final ItemCompareColumn firstItem = comparePage.getItemColumn(0);
        final ItemCompareColumn secondItem = comparePage.getItemColumn(1);
        final ItemCompareColumn thirdItem = comparePage.getItemColumn(2);

        firstItem.likeItem(); // like it
        Thread.sleep(1000); // just for us to see this
        firstItem.likeItem(); // unlike it

        /*
        Я правда не знаю, как нормально проверить, что сравнение характеристик рабоает "адекватно".
        Так что просто проверяю, что то поле, которое очевидно должно совпадать, отображается во всех характеристиках,
        но не отображается в различающихся.
         */

        assertEquals("смартфон", firstItem.getAttribute("тип").toLowerCase().trim());
        assertEquals("ios", firstItem.getAttribute("операционная система").toLowerCase().trim());

        // at this point we should only display attributes which differ
        assertTrue(!firstItem.isAttributeDisplayed("тип")
                && !secondItem.isAttributeDisplayed("тип")
                && !thirdItem.isAttributeDisplayed("тип"));

        comparePage.compareByAll();

        // now we display all of them
        assertTrue(firstItem.isAttributeDisplayed("тип")
                && secondItem.isAttributeDisplayed("тип")
                && thirdItem.isAttributeDisplayed("тип"));

        firstItem.deleteItem();
        Thread.sleep(1000); // just so we can see it

        driver.quit();
    }

}
