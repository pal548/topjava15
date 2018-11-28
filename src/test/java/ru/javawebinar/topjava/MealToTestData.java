package ru.javawebinar.topjava;

import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER;

public class MealToTestData {

    public static final MealTo MEAL_TO_1 = new MealTo(MEAL1, false);
    public static final MealTo MEAL_TO_2 = new MealTo(MEAL2, false);
    public static final MealTo MEAL_TO_3 = new MealTo(MEAL3, false);
    public static final MealTo MEAL_TO_4 = new MealTo(MEAL4, true);
    public static final MealTo MEAL_TO_5 = new MealTo(MEAL5, true);
    public static final MealTo MEAL_TO_6 = new MealTo(MEAL6, true);

    public static final List<MealTo> MEALS_TO = List.of(MEAL_TO_6, MEAL_TO_5, MEAL_TO_4, MEAL_TO_3, MEAL_TO_2, MEAL_TO_1);
}
