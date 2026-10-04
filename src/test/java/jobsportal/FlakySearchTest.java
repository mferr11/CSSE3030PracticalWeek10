package jobsportal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Question 2: this test is flaky. Run it several times, explain why, and fix it.
 */
class FlakySearchTest {

    static final String BASE_URL = "http://localhost:8080";

    static JobsPortalApp app;
    WebDriver driver;

    @BeforeAll
    static void startApp() {
        System.setProperty("demo.delay", "true");
        app = JobsPortalApp.fromSystemProperties().start(8080);
    }

    @AfterAll
    static void stopApp() {
        app.stop();
    }

    @BeforeEach
    void openBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,900");
        driver = new ChromeDriver(options);
        driver.get(BASE_URL + "/");
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void searchShowsResults() throws InterruptedException {
        driver.findElement(By.id("keyword")).sendKeys("Data");
        driver.findElement(By.cssSelector("[data-testid='search-button']")).click();
        Thread.sleep(500);
        List<WebElement> cards = driver.findElements(By.cssSelector("[data-testid='job-card']"));
        assertFalse(cards.isEmpty());
    }
}
