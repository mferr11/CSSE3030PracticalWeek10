package jobsportal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Part 2 Selenium tests for the job search page.
 *
 * The app is started in-process by this class (on port 8080, with the random response delay
 * switched on), so do NOT run `./gradlew run` at the same time.
 */
class JobSearchTest {

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
        // No implicit wait: use explicit waits (WebDriverWait) where a test needs to wait.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.get(BASE_URL + "/");
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ---------------------------------------------------------------- Part 2 Question 1

    @Test
    void keywordSearchShowsMatchingJobs() {
        // TODO 1: type "Data" into the keyword field (id="keyword").
        // TODO 2: click the search button (data-testid="search-button").
        // TODO 3: wait explicitly until the results container (data-testid="results") is visible.
        // TODO 4: collect the job cards (data-testid="job-card") and assert there is at least one.
        // TODO 5: assert every job title (data-testid="job-title") contains "Data", ignoring case.
        fail("Not implemented yet");
    }

    // ---------------------------------------------------------------- Part 2 Question 3

    @Test
    void blankSearchShowsMessageAndNoResults() {
        // TODO (Part 2 Question 3): leave both fields blank, click Search, and check that the message
        // "Enter a keyword or location" is shown and the results container stays hidden.
    }

    @Test
    void keywordAndLocationReturnsOnlyThatLocation() {
        // TODO (Part 2 Question 3): search for keyword "Engineer" and location "Brisbane" and check that
        // every job card shows location "Brisbane".
    }
}
