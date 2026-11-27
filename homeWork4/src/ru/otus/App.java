package ru.otus;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.otus.form.FormData;
import ru.otus.form.LargeFormPage;

import java.io.IOException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Использование: java ru.otus.App <пароль> [подтверждение пароля]");
            System.out.println("Пароль передается параметром запуска и не зашит в код.");
            System.exit(2);
        }
        String password = args[0];
        String passwordConfirm = args.length > 1 ? args[1] : password;

        setupLogging();

        log.info("=== Старт автотеста формы /large_form ===");

        FormData data = new FormData(
                "Иван",
                "Петров",
                "ivan.petrov@example.com",
                password,
                passwordConfirm,
                "vanya",
                "1990-05-10",
                "35",
                "Люблю автоматизировать тестирование",
                "phone",
                List.of("testing", "automation"),
                "10",
                "#00ff00"
        );

        ChromeOptions options = new ChromeOptions().addArguments("--window-size=1920,1080");
        WebDriver driver = new ChromeDriver(options);
        try {
            LargeFormPage page = new LargeFormPage(driver);
            page.open();
            page.fill(data);
            page.submit();

            assertEquals("Form Submitted", page.getHeader(), "Страница не подтвердила отправку формы");
            log.info("Форма отправлена, заголовок 'Form Submitted' найден");

            assertSubmitted(page, "first_name", data.getFirstName());
            assertSubmitted(page, "last_name", data.getLastName());
            assertSubmitted(page, "email", data.getEmail());
            assertSubmitted(page, "password", "*********");
            assertSubmitted(page, "nickname", data.getNickname());
            assertSubmitted(page, "birthday", data.getBirthday());
            assertSubmitted(page, "age", data.getAge());
            assertSubmitted(page, "bio", data.getBio());
            assertSubmitted(page, "contact", data.getContact());
            assertSubmitted(page, "interests", String.join(", ", data.getInterests()));
            assertSubmitted(page, "experience", data.getExperience());
            assertSubmitted(page, "color", data.getColor());
            assertSubmitted(page, "terms", "accepted");

            log.info("=== Тест ПРОЙДЕН: все данные формы корректно выведены на страницу ===");
        } catch (Exception e) {
            log.log(Level.SEVERE, "=== Тест ПРОВАЛЕН ===", e);
            System.exit(1);
        } finally {
            driver.quit();
        }
    }

    private static void assertSubmitted(LargeFormPage page, String key, String expected) {
        String actual = page.getSubmittedValue(key);
        assertTrue(actual.contains(expected),
                "В таблице результатов для '" + key + "' ожидается '" + expected + "', найдено '" + actual + "'");
        log.info("Проверено поле " + key + " = " + ("password".equals(key) ? "*********" : expected));
    }

    private static void setupLogging() throws IOException {
        Logger root = Logger.getLogger("ru.otus");
        root.setUseParentHandlers(false);
        root.setLevel(Level.ALL);

        ConsoleHandler console = new ConsoleHandler();
        console.setLevel(Level.INFO);
        console.setFormatter(new SimpleFormatter());
        root.addHandler(console);

        FileHandler file = new FileHandler("app.log", true);
        file.setLevel(Level.ALL);
        file.setFormatter(new SimpleFormatter());
        root.addHandler(file);
    }
}
