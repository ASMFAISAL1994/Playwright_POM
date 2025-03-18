package test;

import com.microsoft.playwright.*;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import pages.InventoryPage;
import pages.CartPage;

import java.nio.file.Paths;

public class TestCases {
    static Playwright pw;
    static Browser browser;
    static Page page;
    static LoginPage loginPage;
    static InventoryPage inventoryPage;
    static CartPage cartPage;
    static ExtentReports extent;
    static ExtentTest test;

    @BeforeAll
    static void setUp() {
        // Initialize Playwright and Browser
        pw = Playwright.create();
        browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
        page = browser.newPage();

        // Initialize Page Objects
        loginPage = new LoginPage(page);
        inventoryPage = new InventoryPage(page);
        cartPage = new CartPage(page);

        // Initialize Extent Reports
        ExtentSparkReporter spark = new ExtentSparkReporter("test-report.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeEach
    void navigateToLoginPage() {
        page.navigate("https://www.saucedemo.com/");
    }

    @Test
    @DisplayName("Login and checkout as standard_user")
    void testStandardUserCheckout() {
        test = extent.createTest("Standard User Checkout");

        loginPage.login("standard_user", "secret_sauce");
        Assertions.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Inventory page should be displayed");
        test.pass("Successfully logged in");

        inventoryPage.addItemToCart("sauce-labs-backpack");
        test.pass("Item added to cart");

        inventoryPage.goToCart();
        cartPage.proceedToCheckout();
        cartPage.fillCheckoutInfo("Asm", "Faisal", "1207");
        cartPage.finishCheckout();

        takeScreenshot("standard_user_checkout");
        test.pass("Checkout completed").addScreenCaptureFromPath("screenshots/standard_user_checkout.png");
    }

    @Test
    @DisplayName("Locked_out_user should fail to login")
    void testLockedOutUser() {
        test = extent.createTest("Locked Out User Login");

        loginPage.login("locked_out_user", "secret_sauce");
        Assertions.assertTrue(loginPage.isLoginErrorDisplayed(), "Error message should be displayed");

        takeScreenshot("locked_out_user_login");
        test.fail("Login failed as expected").addScreenCaptureFromPath("screenshots/locked_out_user_login.png");
    }

    @Test
    @DisplayName("Login and checkout as problem_user")
    void testProblemUserCheckout() {
        test = extent.createTest("Problem User Checkout");

        loginPage.login("problem_user", "secret_sauce");
        Assertions.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Inventory page should be displayed");

        inventoryPage.addItemToCart("sauce-labs-bike-light");
        inventoryPage.goToCart();
        cartPage.proceedToCheckout();
        cartPage.fillCheckoutInfo("Asm", "K", "1207");
        cartPage.finishCheckout();

//        takeScreenshot("problem_user_checkout");
//        test.pass("Checkout completed").addScreenCaptureFromPath("screenshots/problem_user_checkout.png");
    }

    @AfterEach
    void tearDownTest() {
        extent.flush();
    }

    @AfterAll
    static void tearDown() {
        pw.close();
    }

    // Method to take a screenshot
    void takeScreenshot(String fileName) {
        String path = "screenshots/" + fileName + ".png";
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
    }
}
