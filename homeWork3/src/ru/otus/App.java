package ru.otus;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.otus.scenario.InputsScenario;
import ru.otus.scenario.LoginScenario;
import ru.otus.scenario.ModalDialogScenario;
import ru.otus.scenario.Scenario;

import java.util.List;

public class App {

    public static void main(String[] args) {
        List<Scenario> scenarios = List.of(
                new InputsScenario(),
                new ModalDialogScenario(),
                new LoginScenario()
        );

        int passed = 0;
        for (Scenario scenario : scenarios) {
            System.out.println("=== " + scenario.getName() + " ===");
            WebDriver driver = new ChromeDriver(scenario.getOptions());
            try {
                scenario.run(driver);
                System.out.println(">>> ПРОЙДЕНО");
                passed++;
            } catch (Exception e) {
                System.out.println(">>> ПРОВАЛЕНО: " + e.getMessage());
            } finally {
                driver.quit();
            }
        }
        System.out.println("Итог: " + passed + " из " + scenarios.size() + " сценариев пройдено");
        if (passed != scenarios.size()) {
            System.exit(1);
        }
    }
}
