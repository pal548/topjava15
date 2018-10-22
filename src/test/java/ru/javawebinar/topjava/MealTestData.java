package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final Meal MEAL_1 = new Meal(START_SEQ + 2, LocalDateTime.of(2018, 10, 16, 10, 0), "завтрак", 600);
    public static final Meal MEAL_2 = new Meal(START_SEQ + 3, LocalDateTime.of(2018, 10, 16, 15, 0), "обед", 1000);
    public static final Meal MEAL_3 = new Meal(START_SEQ + 4, LocalDateTime.of(2018, 10, 16, 19, 0), "ужин", 500);
    public static final Meal MEAL_4 = new Meal(START_SEQ + 5, LocalDateTime.of(2018, 10, 17, 10, 0), "завтрак", 600);
    public static final Meal MEAL_5 = new Meal(START_SEQ + 6, LocalDateTime.of(2018, 10, 17, 15, 0), "обед", 1000);
    public static final Meal MEAL_6 = new Meal(START_SEQ + 7, LocalDateTime.of(2018, 10, 17, 19, 0), "ужин", 550);
    public static final List<Meal> MEALS = Arrays.asList(MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2, MEAL_1);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
