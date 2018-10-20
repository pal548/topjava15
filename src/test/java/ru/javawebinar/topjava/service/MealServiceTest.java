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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Test
    public void createAndGet() {
        Meal meal = mealService.create(new Meal(LocalDateTime.of(2018, 10, 18, 10, 0), "тестовый обед", 1000), USER_ID);
        Meal meal2 = mealService.get(meal.getId(), USER_ID);
        assertEquals(meal, meal2);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        mealService.delete(MEAL_1.getId(), USER_ID);
        assertNull(mealService.get(MEAL_1.getId(), USER_ID));
    }

    @Test
    public void getBetweenDates() {
        List<Meal> meals = mealService.getBetweenDates(LocalDate.of(2018, 10, 16), LocalDate.of(2018, 10, 16), USER_ID);
        assertEquals(Arrays.asList(MEAL_3, MEAL_2, MEAL_1), meals);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                LocalDateTime.of(2018, 10, 16, 13, 0), LocalDateTime.of(2018, 10, 16, 15, 0), USER_ID);
        assertEquals(MEAL_2, meals.get(0));
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealService.getAll(USER_ID);
        List<Meal> meals_orig = new ArrayList<>(MEALS);
        meals_orig.sort(Comparator.comparing(Meal::getDateTime).reversed());
        assertEquals(meals_orig, meals);
    }

    @Test
    public void update() {
        updateOfUser(USER_ID);
    }

    private void updateOfUser(int userId) {
        Meal meal = new Meal(MEAL_1);
        meal.setDescr("новое описание");
        meal.setDateTime(LocalDateTime.of(2000,1,1,1,1));
        meal.setCalories(1);
        mealService.update(meal, userId);
        assertEquals(meal, mealService.get(meal.getId(), userId));
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
        updateOfUser(ADMIN_ID);
    }

    @Test(expected = DuplicateKeyException.class)
    public void sameDateTime() {
        Meal meal = new Meal(MEAL_1.getDateTime(), "test", 1);
        mealService.create(meal, USER_ID);
    }

}