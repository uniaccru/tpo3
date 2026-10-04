import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class YouTubeTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private YouTubePage youTubePage;

    @BeforeEach
    void setUp() {
        driver = new SafariDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        youTubePage = new YouTubePage(driver, wait);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Поиск по запросу")
    void search_opensResults() {
        youTubePage.openHomePage();
        youTubePage.search("selenium tutorial");

        assertTrue(driver.getCurrentUrl().contains("/results"));
        assertTrue(driver.getTitle().contains("selenium tutorial"));
    }

    @Test
    @DisplayName("Открытие доступного видео")
    void openVideo_isPlaying() {
        youTubePage.openVideo("T55Atwp9Yxs");
        youTubePage.waitForPlayingVideo();

        assertTrue(driver.getCurrentUrl().contains("/watch?v=T55Atwp9Yxs"));
        assertTrue(youTubePage.isPlayingVideoDisplayed());
    }

    @Test
    @DisplayName("Открытие несуществующего видео")
    void openVideo_nonExistent() {
        youTubePage.openVideo("Thfhfhfhfh55Atwp9Yxs");
        youTubePage.waitForUnavailableVideo();

        assertTrue(youTubePage.isUnavailableVideoDisplayed());
        assertTrue(!youTubePage.hasPlayingVideo());
    }

    @Test
    @DisplayName("Открытие видео только для спонсоров")
    void openVideo_membersOnly() {
        youTubePage.openVideo("CCngDQV8LG8");
        youTubePage.waitForMembersOnlyContent();

        assertTrue(youTubePage.isMembersOnlyContentDisplayed());
        assertTrue(!youTubePage.hasPlayingVideo());
    }

    @Test
    @DisplayName("Подписка без авторизации")
    void subscribe_notLoggedIn() {
        youTubePage.openHomePage();
        youTubePage.search("ITMO");
        youTubePage.openFirstChannel();
        youTubePage.subscribe();

        assertTrue(youTubePage.isSubscribeModalDisplayed());
        assertTrue(youTubePage.getSubscribeModalTitle().contains("Хотите подписаться на этот канал?"));
    }

    @Test
    @DisplayName("Рекомендации после просмотра видео")
    void home_afterWatching_showsRecommendations() throws InterruptedException {
        youTubePage.openHomePage();
        youTubePage.search("ITMO");
        youTubePage.openFirstVideo();
        youTubePage.waitForPlayingVideo();
        Thread.sleep(10000);

        youTubePage.openHomePage();
        youTubePage.waitForRecommendations();

        assertTrue(driver.getCurrentUrl().equals("https://www.youtube.com/"));
        assertTrue(youTubePage.isRecommendationDisplayed());
        Thread.sleep(3000);
    }
}