package pages;

import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {
    private Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void navigateToLogin() {
        page.navigate("https://www.saucedemo.com/");
        assertThat(page).hasTitle("Swag Labs");
    }

    public void login(String username, String password) {
        page.locator("[data-test=\"username\"]").fill(username);
        page.locator("[data-test=\"password\"]").fill(password);
        page.locator("[data-test=\"login-button\"]").click();
    }

    public boolean isLoginErrorDisplayed() {
        return page.locator("[data-test=\"error\"]").isVisible();
    }
}
