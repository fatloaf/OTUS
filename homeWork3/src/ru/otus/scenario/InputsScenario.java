package ru.otus.scenario;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1) Headless Chrome, страница /inputs: ввести 42 и проверить значение поля.
 */
public class InputsScenario implements Scenario {

    @Override
    public String getName() {
        return "Inputs (headless): ввод 42 и проверка значения";
    }

    @Override
    public ChromeOptions getOptions() {
        return new ChromeOptions()
                .addArguments("--headless=new")
                .addArguments("--window-size=1920,1080");
    }

    @Override
    public void run(WebDriver driver) {
        driver.get("https://the-internet.herokuapp.com/inputs");
        WebElement input = driver.findElement(By.tagName("input"));
        input.clear();
        input.sendKeys("42");
        assertEquals("42", input.getAttribute("value"), "Введенное значение не совпадает");
    }
}
