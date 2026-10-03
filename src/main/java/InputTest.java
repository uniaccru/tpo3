import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.interactions.Actions;

public class InputTest {

    public static void main(String[] args) throws InterruptedException {

        WebDriver driver = new SafariDriver();

        try {
            driver.get("https://www.google.com");

            Thread.sleep(2000);

            new Actions(driver)
                    .sendKeys("hello selenium")
                    .perform();

            Thread.sleep(5000);

        } finally {
            driver.quit();
        }
    }
}
