package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-app-repo-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Test
    public void create() {
        Meal meal = new Meal(LocalDateTime.of(2018, 10, 18, 10, 0), "тестовый обед", 1000);
        Meal meal2 = mealService.create(meal, USER_ID);
        assertMatch(meal2, meal);
    }

    @Test
    public void get() {
        Meal meal = mealService.get(MEAL_1.getId(), USER_ID);
        assertMatch(meal, MEAL_1);
    }

    @Test
    public void delete() {
        mealService.delete(MEAL_1.getId(), USER_ID);
        assertMatch(mealService.getAll(USER_ID), MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> meals = mealService.getBetweenDates(LocalDate.of(2018, 10, 16), LocalDate.of(2018, 10, 16), USER_ID);
        assertMatch(meals, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                LocalDateTime.of(2018, 10, 16, 13, 0), LocalDateTime.of(2018, 10, 16, 15, 0), USER_ID);
        assertMatch(meals, MEAL_2);
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealService.getAll(USER_ID);
        assertMatch(meals, MEALS);
    }

    @Test
    public void update() {
        Meal meal = new Meal(MEAL_1);
        meal.setDescr("новое описание");
        meal.setDateTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        meal.setCalories(1);
        mealService.update(meal, USER_ID);
        assertMatch(mealService.get(meal.getId(), USER_ID), meal);
    }

    @Test(expected = NotFoundException.class)
    public void deleteOfOtherUser() {
        mealService.delete(MEAL_1.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getOfOtherUser() {
        mealService.get(MEAL_1.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateOfOtherUser() {
        mealService.update(MEAL_1, ADMIN_ID);
    }

    @Test(expected = DuplicateKeyException.class)
    public void sameDateTime() {
        Meal meal = new Meal(MEAL_1.getDateTime(), "test", 1);
        mealService.create(meal, USER_ID);
    }

}