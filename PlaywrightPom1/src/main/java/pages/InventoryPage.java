package pages;

import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class InventoryPage {
    private Page page;

    public InventoryPage(Page page) {
        this.page = page;
    }

    public boolean isInventoryPageDisplayed() {
        return page.locator("[data-test=\"inventory-container\"]").isVisible();
    }

    public void addItemToCart(String itemName) {
        page.locator("[data-test=\"add-to-cart-" + itemName + "\"]").click();
    }

    public void goToCart() {
        page.locator("[data-test=\"shopping-cart-link\"]").click();
    }
}
