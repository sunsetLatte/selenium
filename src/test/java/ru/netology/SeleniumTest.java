package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class SeleniumTest {

    private WebDriver driver;

    @BeforeAll

    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }


    @BeforeEach
    public void setUp() {
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--remote-allow-origins=*");
        option.addArguments("--disable-dev-shm-usage");
        option.addArguments("--no-sandbox");
        option.addArguments("--headless");
        driver = new ChromeDriver(option);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test                                  // Заполнение бланка, позитивный тест
    public void shouldTestBlankPositive() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пупкин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79169930331");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }
    @Test                                  // Заполнение бланка, позитивный тест, имя, граничное значение
    void shouldTestBlankPositiveNameBorderLow() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ф");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79169930331");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }


    @Test                                  // Заполнение бланка, негативный тест, имя, пустое поле
    void shouldTestBlankNegativeNameNull() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79169930331");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test                                  // Заполнение бланка, негативный тест, имя, невалидное значение, латинница
    void shouldTestBlankNegativeNameLatin() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Pupkin Vasiliy");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79169930331");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test                                  // Заполнение бланка, негативный тест, имя, невалидное значение, перепутаны поля
    void shouldTestBlankNegativeNameMistakeNamePhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("+79169930331");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("Пукин Василий");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test                                  // Заполнение бланка, негативный тест, телефон, пустое поле
    void shouldTestBlankNegativePhoneNull() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пупкин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test                                  // Заполнение бланка, негативный тест, телефон, невалидное значение
    void shouldTestBlankNegativePhoneInvalid() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пупкин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7 916 993 03 31");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test                                  // Заполнение бланка, негативный тест, телефон, невалидное значение, включает не только цифры
    void shouldTestBlankNegativePhoneInvalidOtherSymbols() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пупкин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7-(916)-993-03-31");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test                                  // Заполнение бланка, негативный тест, телефон, невалидное значение, без "+7"
    void shouldTestBlankNegativePhoneWithoutPlus7() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пупкин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("9169930331");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test                                  // Заполнение бланка, негативный тест, телефон, невалидное значение, без "+"
    void shouldTestBlankNegativePhoneWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пупкин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("79169930331");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test                                  // Заполнение бланка, негативный тест, телефон, невалидное значение, "8" вместо без "+7"
    void shouldTestBlankNegativePhoneWith8() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пупкин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("89169930331");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test                                  // Заполнение бланка, негативный тест, телефон, невалидное значение, короткий номер
    void shouldTestBlankNegativePhoneShorts() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пупкин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("02");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test                                  // Заполнение бланка, негативный тест, не нажато согласие
    public void shouldTestBlankNegativeWithoutAgreement() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пупкин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79169930331");
        driver.findElement(By.cssSelector("[data-test-id=agreement]"));
        driver.findElement(By.cssSelector("button.button")).click();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.className("checkbox__text")).getText().trim();
        assertEquals(expected, actual);
    }
}


