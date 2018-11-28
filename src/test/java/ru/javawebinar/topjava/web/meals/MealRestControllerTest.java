package ru.javawebinar.topjava.web.meals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealToTestData.*;
import static ru.javawebinar.topjava.TestUtil.contentJson;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class MealRestControllerTest extends AbstractControllerTest {
    private static final String MEALS_URL = MealRestController.MEALS_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get(MEALS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEALS_TO));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(MEALS_URL + MEAL4.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL4));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(MEALS_URL + MEAL4.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(authUserId()), MEAL6, MEAL5, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void testCreate() throws Exception {
        var newMeal = new Meal(LocalDateTime.of(2018, 11, 21, 12, 0), "Новая еда", 777);
        ResultActions action = mockMvc.perform(
                post(MEALS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal returned = readFromJson(action, Meal.class);
        newMeal.setId(returned.getId());

        assertMatch(returned, newMeal);
        assertMatch(mealService.getAll(authUserId()), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void testUpdate() throws Exception {
        var meal = new Meal(MEAL3);
        meal.setDateTime(LocalDateTime.of(2018, 11, 11, 11, 11));
        meal.setDescription("Другая еда");
        meal.setCalories(778);
        mockMvc.perform(
                put(MEALS_URL + MEAL3.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(meal)))
                .andExpect(status().isNoContent());

        assertMatch(mealService.get(meal.getId(), authUserId()), meal);
    }

    @Test
    void testGetBetween() throws Exception {
        mockMvc.perform(
                get(MEALS_URL + "filter")
                        .param("startDate", "2015-05-30")
                        //.param("startTime", "12:00")
                        .param("endDate", "2015-05-31")
                        .param("endTime", "14:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL_TO_5, MEAL_TO_4, MEAL_TO_2, MEAL_TO_1));
    }
}
