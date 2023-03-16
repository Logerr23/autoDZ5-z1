package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        Configuration.holdBrowserOpen = true;
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $x("//*[@data-test-id = 'city']//input").setValue(validUser.getCity());
        $x("//*[@data-test-id = 'date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//*[@data-test-id = 'date']//input").setValue(firstMeetingDate);
        $x("//*[@data-test-id = 'name']//input").setValue(validUser.getName());
        $x("//*[@data-test-id = 'phone']//input").setValue(validUser.getPhone());
        $x("//*[@data-test-id = 'agreement']").click();
        $x("//*[text() = 'Запланировать']").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);

        $x("//*[@data-test-id = 'date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//*[@data-test-id = 'date']//input").setValue(secondMeetingDate);
        $x("//*[text() = 'Запланировать']").click();
        $x("//*[text() = 'У вас уже запланирована встреча на другую дату. Перепланировать?']").shouldBe(Condition.visible);
        $x("//*[text() = 'Перепланировать']").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }
}
