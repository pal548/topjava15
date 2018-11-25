package ru.javawebinar.topjava;

import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.UserTestData.USER;

public class MealToTestData {

    public static final List<MealTo> MEALS_TO = MealsUtil.getWithExcess(MEALS, USER.getCaloriesPerDay());

    public static void assertMatch(Iterable<MealTo> actual, Iterable<MealTo> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
