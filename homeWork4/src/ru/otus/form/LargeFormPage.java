package ru.otus.form;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class LargeFormPage {

    private static final String URL = "https://the-internet.herokuapp.com/large_form";
    private static final Logger log = Logger.getLogger(LargeFormPage.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LargeFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        log.info("Открываю страницу " + URL);
        driver.get(URL);
    }

    public void fill(FormData data) {
        log.info("Заполняю поля формы");
        type("first-name", data.getFirstName());
        type(By.name("last_name"), data.getLastName());
        type(By.name("email"), data.getEmail());
        typePassword(data);
        type(By.name("nickname"), data.getNickname());
        setJsValue(By.id("birthday"), data.getBirthday());
        type(By.name("age"), data.getAge());
        type(By.id("bio"), data.getBio());

        selectContact(data.getContact());
        selectInterests(data.getInterests());
        setRangeValue(By.id("experience"), data.getExperience());
        setColorValue(By.name("color"), data.getColor());
        checkTerms();
    }

    private void type(By locator, String value) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(value);
        log.fine("Поле " + locator + " заполнено");
    }

    private void type(String id, String value) {
        type(By.id(id), value);
    }

    private void typePassword(FormData data) {
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        passwordField.sendKeys(data.getPassword());
        String entered = passwordField.getAttribute("value");
        if (!entered.equals(data.getPasswordConfirm())) {
            log.severe("Пароль и подтверждение не совпадают, отправка отменена");
            throw new IllegalArgumentException("Пароль и подтверждение не совпадают");
        }
        log.info("Проверка пароля пройдена: пароль и подтверждение совпадают (значение скрыто)");
    }

    private String toUsDateInput(String isoDate) {
        String[] parts = isoDate.split("-");
        return parts[1] + parts[2] + parts[0];
    }

    private void selectContact(String contact) {
        driver.findElement(By.cssSelector("input[name='contact'][value='" + contact + "']")).click();
        log.fine("Выбран способ связи: " + contact);
    }

    private void selectInterests(java.util.List<String> interests) {
        for (String interest : interests) {
            driver.findElement(By.cssSelector("input.interest[value='" + interest + "']")).click();
            log.fine("Отмечен интерес: " + interest);
        }
    }

    private void setRangeValue(By locator, String value) {
        setJsValue(locator, value);
        log.fine("Поле " + locator + " установлено в " + value);
    }

    private void setColorValue(By locator, String value) {
        setJsValue(locator, value);
        log.fine("Поле " + locator + " установлено в " + value);
    }

    private void setJsValue(By locator, String value) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];"
                        + "arguments[0].dispatchEvent(new Event('input'));"
                        + "arguments[0].dispatchEvent(new Event('change'));",
                element, value);
    }

    private void checkTerms() {
        WebElement terms = driver.findElement(By.id("terms"));
        if (!terms.isSelected()) {
            terms.click();
        }
        log.fine("Отмечено согласие с условиями");
    }

    public void submit() {
        log.info("Нажимаю кнопку Submit");
        driver.findElement(By.id("submit")).click();
    }

    public String getHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3"))).getText();
    }

    public String getSubmittedValue(String key) {
        WebElement cell = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@id='submitted-data']//td[text()='" + key + "']/following-sibling::td")));
        return cell.getText();
    }
}
