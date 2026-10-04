import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YouTubePage {

    private static final String HOME_URL = "https://www.youtube.com";
    private static final By SEARCH_INPUT = By.xpath("//input[@name='search_query']");
    private static final By SEARCH_BUTTON = By.xpath("//button[@aria-label='Search']");
    private static final By FIRST_CHANNEL = By.xpath("//a[contains(@class, 'channel-link')][1]");
    private static final By SUBSCRIBE_BUTTON = By.xpath("//button[@aria-label='Подписаться']");
    private static final By MODAL_TITLE = By.xpath(
            "//ytd-modal-with-title-and-button-renderer//yt-formatted-string[@id='title']");
    private static final By FIRST_VIDEO = By.xpath("(//a[contains(@href,'/watch?v=')])[1]");
    private static final By PLAYING_PLAYER = By.xpath(
            "//div[@id='movie_player' and contains(@class,'playing-mode')]");
    private static final By UNAVAILABLE_VIDEO = By.xpath("//div[contains(text(),'больше не доступно')]");
    private static final By MEMBERS_ONLY_CONTENT = By.xpath("//div[contains(@class,'ypc-description')]");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public YouTubePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void openHomePage() {
        driver.get(HOME_URL);
    }

    public void search(String query) {
        wait.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT)).click();
        wait.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT)).sendKeys(query);

        wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BUTTON)).click();
        wait.until(ExpectedConditions.urlContains("/results"));
    }

    public void openVideo(String videoId) {
        driver.get(HOME_URL + "/watch?v=" + videoId);
    }

    public void openFirstVideo() {
        wait.until(ExpectedConditions.elementToBeClickable(FIRST_VIDEO)).click();
    }

    public void openFirstChannel() {
        wait.until(ExpectedConditions.elementToBeClickable(FIRST_CHANNEL)).click();
    }

    public void subscribe() {
        wait.until(ExpectedConditions.elementToBeClickable(SUBSCRIBE_BUTTON)).click();
    }

    public void waitForPlayingVideo() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PLAYING_PLAYER));
    }

    public void waitForUnavailableVideo() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(UNAVAILABLE_VIDEO));
    }

    public void waitForMembersOnlyContent() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(MEMBERS_ONLY_CONTENT));
    }

    public void waitForRecommendations() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_VIDEO));
    }

    public boolean isPlayingVideoDisplayed() {
        return driver.findElement(PLAYING_PLAYER).isDisplayed();
    }

    public boolean hasPlayingVideo() {
        return !driver.findElements(PLAYING_PLAYER).isEmpty();
    }

    public boolean isUnavailableVideoDisplayed() {
        return driver.findElement(UNAVAILABLE_VIDEO).isDisplayed();
    }

    public boolean isMembersOnlyContentDisplayed() {
        return driver.findElement(MEMBERS_ONLY_CONTENT).isDisplayed();
    }

    public boolean isRecommendationDisplayed() {
        return driver.findElement(FIRST_VIDEO).isDisplayed();
    }

    public boolean isSubscribeModalDisplayed() {
        return driver.findElement(MODAL_TITLE).isDisplayed();
    }

    public String getSubscribeModalTitle() {
        return driver.findElement(MODAL_TITLE).getText();
    }
}