package ru.otus.scenario;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginScenario implements Scenario {

    @Override
    public String getName() {
        return "Login (fullscreen): вход tomsmith и проверка статуса";
    }

    @Override
    public ChromeOptions getOptions() {
        return new ChromeOptions()
                .addArguments("--start-maximized")
                .addArguments("--start-fullscreen");
    }

    @Override
    public void run(WebDriver driver) {
        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String flash = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")))
                .getText();
        assertTrue(flash.contains("You logged into a secure area!"),
                "Сообщение об успешном входе не найдено: " + flash);
        assertTrue(driver.getCurrentUrl().contains("/secure"),
                "После входа ожидается переход на /secure");
    }
}
