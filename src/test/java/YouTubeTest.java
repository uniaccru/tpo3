import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class YouTubeTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new SafariDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Поиск по запросу")
    void search_opensResults() {
        driver.get("https://www.youtube.com");

        String searchXPath = "//input[@name='search_query']";
        String buttonXPath = "//button[@aria-label='Search']";

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(searchXPath)));
        driver.findElement(By.xpath(searchXPath)).click();
        driver.findElement(By.xpath(searchXPath)).sendKeys("selenium tutorial");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
        driver.findElement(By.xpath(buttonXPath)).click();

        wait.until(ExpectedConditions.urlContains("/results"));

        assertTrue(driver.getCurrentUrl().contains("/results"));
        assertTrue(driver.getTitle().contains("selenium tutorial"));
    }

    @Test
    @DisplayName("Открытие доступного видео")
    void openVideo_isPlaying() {
        driver.get("https://www.youtube.com/watch?v=T55Atwp9Yxs");

        String playingXPath = "//div[@id='movie_player' and contains(@class,'playing-mode')]";

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(playingXPath)));

        assertTrue(driver.getCurrentUrl().contains("/watch?v=T55Atwp9Yxs"));
        assertTrue(driver.findElement(By.xpath(playingXPath)).isDisplayed());
    }

    @Test
    @DisplayName("Открытие несуществующего видео")
    void openVideo_nonExistent() {
        driver.get("https://www.youtube.com/watch?v=Thfhfhfhfh55Atwp9Yxs");

        String errorXPath = "//div[contains(text(),'больше не доступно')]";
        String playingXPath = "//div[@id='movie_player' and contains(@class,'playing-mode')]";

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(errorXPath)));

        assertTrue(driver.findElement(By.xpath(errorXPath)).isDisplayed());
        assertTrue(driver.findElements(By.xpath(playingXPath)).isEmpty());
    }

    @Test
    @DisplayName("Открытие видео только для спонсоров")
    void openVideo_membersOnly() {
        driver.get("https://www.youtube.com/watch?v=CCngDQV8LG8");

        String paidContentXPath = "//div[contains(@class,'ypc-description')]";
        String playingXPath = "//div[@id='movie_player' and contains(@class,'playing-mode')]";

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(paidContentXPath)));

        assertTrue(driver.findElement(By.xpath(paidContentXPath)).isDisplayed());
        assertTrue(driver.findElements(By.xpath(playingXPath)).isEmpty());
    }

    @Test
    @DisplayName("Подписка без авторизации")
    void subscribe_notLoggedIn() {
        driver.get("https://www.youtube.com");

        String searchXPath = "//input[@name='search_query']";
        String buttonXPath = "//button[@aria-label='Search']";
        String channelXPath = "//a[contains(@class, 'channel-link')][1]";
        String subscribeXPath = "//button[@aria-label='Подписаться']";
        String modalTitleXPath = "//ytd-modal-with-title-and-button-renderer//yt-formatted-string[@id='title']";

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(searchXPath)));
        driver.findElement(By.xpath(searchXPath)).click();
        driver.findElement(By.xpath(searchXPath)).sendKeys("ITMO");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
        driver.findElement(By.xpath(buttonXPath)).click();

        wait.until(ExpectedConditions.urlContains("/results"));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(channelXPath)));
        driver.findElement(By.xpath(channelXPath)).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(subscribeXPath)));
        driver.findElement(By.xpath(subscribeXPath)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(modalTitleXPath)));

        assertTrue(driver.findElement(By.xpath(modalTitleXPath)).isDisplayed());
        assertTrue(driver.findElement(By.xpath(modalTitleXPath)).getText().contains("Хотите подписаться на этот канал?"));
    }

    @Test
    @DisplayName("Рекомендации после просмотра видео")
    void home_afterWatching_showsRecommendations() throws InterruptedException {
        driver.get("https://www.youtube.com");

        String searchXPath = "//input[@name='search_query']";
        String buttonXPath = "//button[@aria-label='Search']";
        String firstVideoXPath = "(//a[contains(@href,'/watch?v=')])[1]";
        String playingXPath = "//div[@id='movie_player' and contains(@class,'playing-mode')]";
        String recommendationXPath = "(//a[contains(@href,'/watch?v=')])[1]";

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(searchXPath)));
        driver.findElement(By.xpath(searchXPath)).click();
        driver.findElement(By.xpath(searchXPath)).sendKeys("ITMO");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
        driver.findElement(By.xpath(buttonXPath)).click();

        wait.until(ExpectedConditions.urlContains("/results"));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(firstVideoXPath)));
        driver.findElement(By.xpath(firstVideoXPath)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(playingXPath)));
        Thread.sleep(10000);

        driver.get("https://www.youtube.com");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(recommendationXPath)));

        assertTrue(driver.getCurrentUrl().equals("https://www.youtube.com/"));
        assertTrue(driver.findElement(By.xpath(recommendationXPath)).isDisplayed());
        Thread.sleep(3000);
    }
}