package ru.otus;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LargeFormTest {

    private static final Logger log = Logger.getLogger(LargeFormTest.class.getName());
    private static final String DEFAULT_SUT_URL = "https://the-internet.herokuapp.com/large_form";

    private static String sutUrl;
    private static String password;

    private WebDriver driver;

    @BeforeAll
    static void setUpClass() throws IOException {
        setupLogging();

        sutUrl = System.getProperty("sut.url", DEFAULT_SUT_URL);
        password = System.getProperty("password");
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Пароль не задан. Запуск: mvn test -Dpassword=<пароль> [-Dsut.url=<url стенда>]");
        }
        log.info("URL стенда из параметров запуска: " + sutUrl);
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver(new ChromeOptions().addArguments("--window-size=1920,1080"));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterAll
    static void tearDownClass() {
        log.info("=== Тесты завершены ===");
    }

    private FormData buildFormData(String password, String passwordConfirm) {
        return new FormData(
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
    }

    @Test
    void fillFormSubmitAndCheckDataDisplayed() {
        log.info("=== Тест: заполнение формы, отправка и проверка вывода данных ===");
        LargeFormPage page = new LargeFormPage(driver);
        page.open(sutUrl);

        FormData data = buildFormData(password, password);
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
    }

    @Test
    void passwordMismatchAbortsSubmission() {
        log.info("=== Тест: пароль и подтверждение не совпадают, отправка отменяется ===");
        LargeFormPage page = new LargeFormPage(driver);
        page.open(sutUrl);

        FormData data = buildFormData(password, password + "_не_совпадает");
        assertThrows(IllegalArgumentException.class, () -> page.fill(data),
                "Ожидалась ошибка при несовпадении пароля и подтверждения");
        log.info("=== Тест ПРОЙДЕН: отправка при несовпадении пароля отменена ===");
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
