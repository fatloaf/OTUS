package ru.otus.scenario;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Один тестовый сценарий: режим браузера + шаги проверки.
 */
public interface Scenario {

    String getName();

    ChromeOptions getOptions();

    void run(WebDriver driver) throws Exception;
}
