package ru.otus.scenario;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModalDialogScenario implements Scenario {

    @Override
    public String getName() {
        return "Modal dialog (kiosk): открытие модального окна";
    }

    @Override
    public ChromeOptions getOptions() {
        return new ChromeOptions()
                .addArguments("--kiosk");
    }

    @Override
    public void run(WebDriver driver) {
        driver.get("https://the-internet.herokuapp.com/dialog");
        driver.findElement(By.id("open-modal")).click();

        WebElement modal = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-dialog")));
        assertEquals("true", modal.getAttribute("open"), "Модальное окно не открылось");
        assertTrue(modal.isDisplayed(), "Модальное окно не отображается");
    }
}
