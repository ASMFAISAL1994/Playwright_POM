package pages;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartPage {
    static ExtentReports extent;
    static ExtentTest test;
    private Page page;


    public CartPage(Page page) {
        this.page = page;
//        ExtentSparkReporter spark = new ExtentSparkReporter("test-report.html");
//        extent = new ExtentReports();
//        extent.attachReporter(spark);
    }

    public void proceedToCheckout() {
        page.locator("[data-test=\"checkout\"]").click();
    }

    public void fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        page.locator("[data-test=\"firstName\"]").fill(firstName);
        page.locator("[data-test=\"lastName\"]").fill(lastName);
        page.locator("[data-test=\"postalCode\"]").fill(postalCode);
        page.locator("[data-test=\"continue\"]").click();


    }

    public void finishCheckout() {
        boolean test_finishbutton= page.locator("[data-test=\"finish\"]").isVisible();
        if (test_finishbutton==false){
            takeScreenshot("user failed cart login page");

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Open Menu")).click();
            page.locator("[data-test=\"logout-sidebar-link\"]").click();

        }
        else {
            page.locator("[data-test=\"finish\"]").click();
        }
    }
    void takeScreenshot(String fileName) {
        String path = "screenshots/" + fileName + ".png";
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
    }
}
